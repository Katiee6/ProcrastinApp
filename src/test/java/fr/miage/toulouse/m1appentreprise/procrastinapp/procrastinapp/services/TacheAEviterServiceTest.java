package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.TacheAEviterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TacheAEviterServiceTest {

    TacheAEviterRepository tacheAEviterRepository;
    TacheAEviterService tacheAEviterService;

    @BeforeEach
    void setUp() {
        tacheAEviterRepository = mock(TacheAEviterRepository.class);
        tacheAEviterService = new TacheAEviterService(tacheAEviterRepository);
    }

    @Test
    void findAllByUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        List<TacheAEviter> taches = new ArrayList<>();
        when(tacheAEviterRepository.findByUtilisateur(utilisateur)).thenReturn(taches);

        List<TacheAEviter> result = tacheAEviterService.findAllByUtilisateur(utilisateur);
        assertEquals(taches, result);
        verify(tacheAEviterRepository, times(1)).findByUtilisateur(utilisateur);
    }

    @Test
    void creerTacheAEviterValide() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        TacheAEviter tache = new TacheAEviter();
        tache.setDescription("desc");
        tache.setDegreUrgence(1);
        tache.setDateLimite(LocalDateTime.now().plusDays(4));
        tache.setConsequencesPotentielles("conséquences");

        when(tacheAEviterRepository.save(any())).thenReturn(tache);

        TacheAEviter res = tacheAEviterService.creerTacheAEviter(tache, utilisateur);

        assertNotNull(res);
        assertEquals(tache, res);
        verify(tacheAEviterRepository, times(1)).save(any());
    }

    @Test
    void creerTacheAEviterUtilisateurNull() {
        TacheAEviter tache = new TacheAEviter();
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> tacheAEviterService.creerTacheAEviter(tache, null));
        assertEquals("L'utilisateur ne peut pas être nul", exception.getMessage());
    }

    @Test
    void validerTacheAEviterAvecErreurs() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        TacheAEviter tache = new TacheAEviter();
        tache.setDescription("");
        tache.setDegreUrgence(3);
        tache.setDateLimite(LocalDateTime.now().plusDays(1));
        tache.setConsequencesPotentielles("Risques divers");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> tacheAEviterService.creerTacheAEviter(tache, utilisateur));
        assertEquals("La description de la tâche ne peut pas être vide", exception.getMessage());
    }

    @Test
    void getTacheByIdExistant() {
        TacheAEviter tache = new TacheAEviter();
        tache.setId(2L);
        when(tacheAEviterRepository.findById(2L)).thenReturn(Optional.of(tache));

        TacheAEviter res = tacheAEviterService.getTacheById(2L);
        assertEquals(tache, res);
        verify(tacheAEviterRepository).findById(2L);
    }

    @Test
    void getTacheByIdNonExistante() {
        when(tacheAEviterRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> tacheAEviterService.getTacheById(1L));
        assertTrue(exception.getMessage().contains("Tâche non trouvée avec l'identifiant"));
    }

    @Test
    void modifierTacheAEviter() {
        TacheAEviter ancienne = new TacheAEviter();
        ancienne.setId(1L);
        ancienne.setDescription("Ancienne");
        ancienne.setDegreUrgence(3);
        ancienne.setDateLimite(LocalDateTime.now().plusDays(2));
        ancienne.setConsequencesPotentielles("Conséquences");

        TacheAEviter nouvelle = new TacheAEviter();
        nouvelle.setDescription("Nouvelle");
        nouvelle.setDegreUrgence(4);
        nouvelle.setDateLimite(LocalDateTime.now().plusDays(5));
        nouvelle.setStatut(StatutTacheAEviter.EN_ATTENTE);
        nouvelle.setConsequencesPotentielles("Nouvelles conséquences");

        when(tacheAEviterRepository.findById(1L)).thenReturn(Optional.of(ancienne));
        when(tacheAEviterRepository.save(any())).thenReturn(ancienne);

        TacheAEviter modifiee = tacheAEviterService.modifierTacheAEviter(1L, nouvelle);
        assertEquals("Nouvelle", modifiee.getDescription());
    }

    @Test
    void supprimerTacheAEviter() {
        TacheAEviter tache = new TacheAEviter();
        tache.setId(4L);
        when(tacheAEviterRepository.findById(4L)).thenReturn(Optional.of(tache));

        tacheAEviterService.supprimerTacheAEviter(4L);
        verify(tacheAEviterRepository).delete(tache);
    }

    @Test
    void calculerPointsValide() {
        TacheAEviter tache = new TacheAEviter();
        tache.setStatut(StatutTacheAEviter.EVITEE_AVEC_SUCCES);
        tache.setDegreUrgence(4);
        tache.setDateLimite(LocalDateTime.now().minusDays(2));
        tache.setDateFin(LocalDateTime.now());

        int points = tacheAEviterService.calculerPoints(tache);
        assertEquals(4 * 10 + 2 * 5, points);
    }

    @Test
    void calculerPointsStatutNonValide() {
        TacheAEviter tache = new TacheAEviter();
        tache.setStatut(StatutTacheAEviter.EN_ATTENTE);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> tacheAEviterService.calculerPoints(tache));
        assertEquals("La tâche doit être évitée avec succès pour calculer les points.", exception.getMessage());
    }

    @Test
    void calculerTotalPoints() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        TacheAEviter tache1 = new TacheAEviter();
        tache1.setStatut(StatutTacheAEviter.EVITEE_AVEC_SUCCES);
        tache1.setDegreUrgence(3);
        tache1.setDateLimite(LocalDateTime.now().minusDays(1));
        tache1.setDateFin(LocalDateTime.now());

        TacheAEviter tache2 = new TacheAEviter();
        tache2.setStatut(StatutTacheAEviter.EN_ATTENTE);

        when(tacheAEviterRepository.findByUtilisateur(utilisateur)).thenReturn(List.of(tache1, tache2));

        TacheAEviterService spyService = spy(tacheAEviterService);
        doReturn(35).when(spyService).calculerPoints(tache1);

        int total = spyService.calculerTotalPoints(utilisateur);
        assertEquals(35, total);
    }

    @Test
    void verifierUtilisateurEligibiliteModerer() {
        when(tacheAEviterRepository.existsByUtilisateurIdAndStatutIn(eq(1L), anyList())).thenReturn(true);
        boolean eligible = tacheAEviterService.verifierUtilisateurEligibiliteModerer(1L);
        assertTrue(eligible);

        when(tacheAEviterRepository.existsByUtilisateurIdAndStatutIn(eq(2L), anyList())).thenReturn(false);
        boolean nonEligible = tacheAEviterService.verifierUtilisateurEligibiliteModerer(2L);
        assertFalse(nonEligible);
    }

    @Test
    void verifierUtilisateurEligibiliteModererAvecIdNull() {
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> tacheAEviterService.verifierUtilisateurEligibiliteModerer(null));
        assertEquals("L'identifiant de l'utilisateur ne peut pas être nul", exception.getMessage());
    }
}