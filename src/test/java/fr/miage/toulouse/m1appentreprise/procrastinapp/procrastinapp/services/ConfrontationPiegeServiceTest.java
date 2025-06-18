package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ForbiddenOperationException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfrontationPiegeServiceTest {

    private ConfrontationPiegeRepository confrontationPiegeRepository;

    private PiegeProductiviteRepository piegeProductiviteRepository;

    private RecompenseRepository recompenseRepository;

    private AttributionRecompenseRepository attributionRecompenseRepository;

    private UtilisateurRepository utilisateurRepository;

    private ConfrontationPiegeService confrontationPiegeService;

    @BeforeEach
    void setUp() {
        confrontationPiegeRepository = mock(ConfrontationPiegeRepository.class);
        piegeProductiviteRepository = mock(PiegeProductiviteRepository.class);
        recompenseRepository = mock(RecompenseRepository.class);
        attributionRecompenseRepository = mock(AttributionRecompenseRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        confrontationPiegeService = new ConfrontationPiegeService(confrontationPiegeRepository, piegeProductiviteRepository, recompenseRepository, attributionRecompenseRepository, utilisateurRepository);
    }

    @Test
    void getAllConfrontationPieges() {
        when(confrontationPiegeRepository.findAll()).thenReturn(List.of(new ConfrontationPiege(), new ConfrontationPiege()));
        Iterable<ConfrontationPiege> result = confrontationPiegeService.getAllConfrontationPieges();
        assertNotNull(result);
        assertEquals(2, ((List<?>) result).size());
    }

    @Test
    void getConfrontationPiegeById_success() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        ConfrontationPiege confrontation = new ConfrontationPiege();
        confrontation.setId(1L);
        confrontation.setUtilisateur(user);
        when(confrontationPiegeRepository.findById(1L)).thenReturn(Optional.of(confrontation));
        ConfrontationPiege result = confrontationPiegeService.getConfrontationPiegeById(1L, user);
        assertEquals(1L, result.getId());
    }

    @Test
    void getConfrontationPiegeById_forbidden() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        Utilisateur otherUser = new Utilisateur();
        otherUser.setId(2L);
        ConfrontationPiege confrontation = new ConfrontationPiege();
        confrontation.setId(1L);
        confrontation.setUtilisateur(otherUser);
        when(confrontationPiegeRepository.findById(1L)).thenReturn(Optional.of(confrontation));
        assertThrows(ForbiddenOperationException.class, () -> confrontationPiegeService.getConfrontationPiegeById(1L, user));
    }

    @Test
    void getAllConfrontationsAuPiege() {
        when(confrontationPiegeRepository.findConfrontationPiegesByPiege_Id(1L))
                .thenReturn(List.of(new ConfrontationPiege()));
        var result = confrontationPiegeService.getAllConfrontationsAuPiege(1L);
        assertEquals(1, ((List<?>) result).size());
    }

    @Test
    void getAllConfrontationPiegesUtilisateur() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        when(confrontationPiegeRepository.findConfrontationPiegesByUtilisateur(user))
                .thenReturn(List.of(new ConfrontationPiege()));
        var result = confrontationPiegeService.getAllConfrontationPiegesUtilisateur(user);
        assertEquals(1, ((List<?>) result).size());
    }

    @Test
    void creerConfrontationPiegeSucces() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setPointAccumules(100);
        PiegeProductivite piege = new PiegeProductivite();
        piege.setId(1L);
        ConfrontationPiege input = new ConfrontationPiege();
        input.setSucces(true);
        input.setPiege(piege);
        input.setDateConfrontation(LocalDate.now());
        when(piegeProductiviteRepository.findById(1L)).thenReturn(Optional.of(piege));
        when(confrontationPiegeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(utilisateurRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        ConfrontationPiege result = confrontationPiegeService.creerConfrontationPiege(input, user);
        assertTrue(result.isSucces());
        assertEquals(150, user.getPointAccumules());
        assertEquals(50, result.getPoints());
        assertEquals(user, result.getUtilisateur());
    }

    @Test
    void creerConfrontationPiegeEchec() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setPointAccumules(100);
        PiegeProductivite piege = new PiegeProductivite();
        piege.setId(1L);
        Recompense recompense = new Recompense();
        recompense.setTitre("Procrastinateur en danger");
        recompense.setType(TypeRecompense.BADGE);
        ConfrontationPiege input = new ConfrontationPiege();
        input.setSucces(false);
        input.setPiege(piege);
        input.setDateConfrontation(LocalDate.now());
        when(piegeProductiviteRepository.findById(1L)).thenReturn(Optional.of(piege));
        when(recompenseRepository.findRecompenseByTitreAndType("Procrastinateur en danger", TypeRecompense.BADGE))
                .thenReturn(List.of(recompense));
        when(attributionRecompenseRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(utilisateurRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(confrontationPiegeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        ConfrontationPiege result = confrontationPiegeService.creerConfrontationPiege(input, user);
        assertFalse(result.isSucces());
        assertEquals(50, user.getPointAccumules());
        assertEquals(-50, result.getPoints());
    }

    @Test
    void supprimerConfrontationPiegeSucces() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setPointAccumules(100);
        PiegeProductivite piege = new PiegeProductivite();
        piege.setId(1L);
        ConfrontationPiege confrontation = new ConfrontationPiege();
        confrontation.setId(1L);
        confrontation.setSucces(true);
        confrontation.setPoints(50);
        confrontation.setUtilisateur(user);
        confrontation.setPiege(piege);
        when(confrontationPiegeRepository.findById(1L)).thenReturn(Optional.of(confrontation));
        when(utilisateurRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        confrontationPiegeService.supprimerConfrontationPiege(1L, user);
        verify(confrontationPiegeRepository).delete(confrontation);
        assertEquals(50, user.getPointAccumules());
    }

    @Test
    void supprimerConfrontationPiege_succesFalse() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setPointAccumules(50);
        PiegeProductivite piege = new PiegeProductivite();
        piege.setId(1L);
        ConfrontationPiege confrontation = new ConfrontationPiege();
        confrontation.setId(1L);
        confrontation.setSucces(false);
        confrontation.setPoints(-50);
        confrontation.setUtilisateur(user);
        confrontation.setPiege(piege);
        AttributionRecompense attribution = new AttributionRecompense();
        when(confrontationPiegeRepository.findById(1L)).thenReturn(Optional.of(confrontation));
        when(attributionRecompenseRepository
                .findAttributionRecompenseByContexteAttribution("Echec confrontation piège 1"))
                .thenReturn(List.of(attribution));
        confrontationPiegeService.supprimerConfrontationPiege(1L, user);
        verify(attributionRecompenseRepository).delete(attribution);
        verify(confrontationPiegeRepository).delete(confrontation);
        verify(utilisateurRepository).save(user);
        assertEquals(100, user.getPointAccumules());
    }
}