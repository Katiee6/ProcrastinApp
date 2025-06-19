package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ForbiddenOperationException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.DefiProcrastinationRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.ParticipationDefiRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test unitaire pour le service ParticipationDefiService.
 */
class ParticipationDefiServiceTest {

    private ParticipationDefiRepository participationDefiRepository;

    private DefiProcrastinationRepository defiProcrastinationRepository;

    private UtilisateurRepository utilisateurRepository;

    private ParticipationDefiService participationDefiService;

    @BeforeEach
    void setUp() {
        participationDefiRepository = mock(ParticipationDefiRepository.class);
        defiProcrastinationRepository = mock(DefiProcrastinationRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        participationDefiService = new ParticipationDefiService(participationDefiRepository, defiProcrastinationRepository, utilisateurRepository);
    }

    @Test
    void getAllParticipationsDefis() {
        List<ParticipationDefi> participations = List.of(new ParticipationDefi(), new ParticipationDefi());
        when(participationDefiRepository.findAll()).thenReturn(participations);
        Iterable<ParticipationDefi> result = participationDefiService.getAllParticipationsDefis();
        assertNotNull(result);
        assertEquals(2, ((Collection<?>) result).size());
    }

    @Test
    void getAllParticipationsDefisUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        when(participationDefiRepository.findParticipationDefiByUtilisateur(utilisateur))
                .thenReturn(List.of(new ParticipationDefi()));
        Iterable<ParticipationDefi> result = participationDefiService.getAllParticipationsDefisUtilisateur(utilisateur);
        assertNotNull(result);
        assertEquals(1, ((Collection<?>) result).size());
    }

    @Test
    void getAllParticipationsAuDefi() {
        Long defiId = 1L;
        when(participationDefiRepository.findParticipationDefiByDefi_Id(defiId))
                .thenReturn(List.of(new ParticipationDefi(), new ParticipationDefi()));
        Iterable<ParticipationDefi> result = participationDefiService.getAllParticipationsAuDefi(defiId);
        assertEquals(2, ((Collection<?>) result).size());
    }

    @Test
    void getParticipationDefiById_success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        ParticipationDefi participation = new ParticipationDefi();
        participation.setId(1L);
        participation.setUtilisateur(utilisateur);
        when(participationDefiRepository.findById(1L)).thenReturn(Optional.of(participation));
        ParticipationDefi result = participationDefiService.getParticipationDefiById(1L, utilisateur);
        assertEquals(1L, result.getId());
    }

    @Test
    void getParticipationDefiById_forbiddenException() {
        Utilisateur otherUser = new Utilisateur();
        otherUser.setId(2L);
        otherUser.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        Utilisateur owner = new Utilisateur();
        owner.setId(1L);
        ParticipationDefi participation = new ParticipationDefi();
        participation.setId(1L);
        participation.setUtilisateur(owner);
        when(participationDefiRepository.findById(1L)).thenReturn(Optional.of(participation));
        assertThrows(ForbiddenOperationException.class, () ->
                participationDefiService.getParticipationDefiById(1L, otherUser));
    }

    @Test
    void creerParticipationDefi() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setPointAccumules(0);
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        DefiProcrastination defi = new DefiProcrastination();
        defi.setId(1L);
        defi.setActif(true);
        defi.setDateDebut(LocalDate.now().plusDays(1));
        defi.setPointsAGagner(100);
        ParticipationDefi input = new ParticipationDefi();
        input.setDefi(defi);
        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(defi));
        when(participationDefiRepository.existsParticipationDefiByDefi_IdAndStatutNot(eq(1L), any())).thenReturn(false);
        when(participationDefiRepository.countParticipationDefisByUtilisateurAndStatutNot(eq(utilisateur), any())).thenReturn(0);
        when(participationDefiRepository.countParticipationDefisByDefi(defi)).thenReturn(2);
        when(participationDefiRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        ParticipationDefi result = participationDefiService.creerParticipationDefi(input, utilisateur);
        assertNotNull(result);
        assertEquals(defi, result.getDefi());
        assertEquals(utilisateur, result.getUtilisateur());
        assertEquals(StatutParticipationDefi.INSCRIT, result.getStatut());
    }

    @Test
    void commencerLesParticipationsDefis() {
        DefiProcrastination defi = new DefiProcrastination();
        defi.setDateDebut(LocalDate.now().minusDays(1));
        ParticipationDefi participation = new ParticipationDefi();
        participation.setDefi(defi);
        participation.setStatut(StatutParticipationDefi.INSCRIT);
        when(participationDefiRepository
                .findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual(eq(StatutParticipationDefi.INSCRIT), any()))
                .thenReturn(List.of(participation));
        when(participationDefiRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        participationDefiService.commencerLesParticipationsDefis();
        assertEquals(StatutParticipationDefi.EN_COURS, participation.getStatut());
        verify(participationDefiRepository).save(participation);
    }

    @Test
    void terminerParticipationDefi() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setPointAccumules(10);
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        DefiProcrastination defi = new DefiProcrastination();
        defi.setPointsAGagner(50);
        ParticipationDefi existing = new ParticipationDefi();
        existing.setId(1L);
        existing.setUtilisateur(utilisateur);
        existing.setDefi(defi);
        existing.setStatut(StatutParticipationDefi.EN_COURS);
        ParticipationDefi input = new ParticipationDefi();
        input.setId(1L);
        input.setPointsGagnes(40);
        when(participationDefiRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(participationDefiRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(utilisateurRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        ParticipationDefi result = participationDefiService.terminerParticipationDefi(input, utilisateur);
        assertEquals(StatutParticipationDefi.TERMINE, result.getStatut());
        assertEquals(40, result.getPointsGagnes());
        assertEquals(50, utilisateur.getPointAccumules());
        verify(participationDefiRepository).save(existing);
        verify(utilisateurRepository).save(utilisateur);
    }
}