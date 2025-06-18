package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ExcuseCreative;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.CategorieExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.ExcuseCreativeRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitaire pour le service ExcuseCreativeService.
 */
class ExcuseCreativeServiceTest {

    private ExcuseCreativeRepository excuseCreativeRepository;
    private UtilisateurRepository utilisateurRepository;
    private TacheAEviterService tacheAEviterService;
    private ExcuseCreativeService excuseCreativeService;

    @BeforeEach
    void setUp() {
        excuseCreativeRepository = mock(ExcuseCreativeRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        tacheAEviterService = mock(TacheAEviterService.class);
        excuseCreativeService = new ExcuseCreativeService(excuseCreativeRepository, tacheAEviterService, utilisateurRepository);
    }

    @Test
    void getAllExcusesCreatives() {
        when(excuseCreativeRepository.findAll()).thenReturn(Collections.emptyList());
        Iterable<ExcuseCreative> result = excuseCreativeService.getAllExcusesCreatives();
        assertNotNull(result);
        verify(excuseCreativeRepository, times(1)).findAll();
    }

    @Test
    void getExcuseCreativeById_Success() {
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setId(1L);
        when(excuseCreativeRepository.findById(1L)).thenReturn(Optional.of(excuse));

        ExcuseCreative result = excuseCreativeService.getExcuseCreativeById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getExcuseCreativeById_NotFound() {
        when(excuseCreativeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> excuseCreativeService.getExcuseCreativeById(1L));
    }

    @Test
    void creerExcuseCreative_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexteExcuse("texte excuse");
        excuse.setCategorie(CategorieExcuse.ETUDES);
        excuse.setSituationApp("En plein partiel");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(excuseCreativeRepository.existsByTexteExcuse(excuse.getTexteExcuse())).thenReturn(false);
        when(excuseCreativeRepository.save(any())).thenReturn(excuse);

        ExcuseCreative result = excuseCreativeService.creerExcuseCreative(excuse, utilisateur);
        assertNotNull(result);
        verify(excuseCreativeRepository, times(1)).save(excuse);
    }

    @Test
    void creerExcuseCreative_EmptyTexte() {
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexteExcuse("");
        excuse.setCategorie(CategorieExcuse.VIE_SOCIALE);
        excuse.setSituationApp("Test");

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        assertThrows(InvalidRequestException.class, () -> excuseCreativeService.creerExcuseCreative(excuse, utilisateur));
    }

    @Test
    void voterExcuseCreative_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setId(1L);
        excuse.setAuteur(utilisateur);
        excuse.setVotesRecus(0);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(excuseCreativeRepository.findByIdAndAuteur(1L, utilisateur)).thenReturn(excuse);
        when(excuseCreativeRepository.save(any())).thenReturn(excuse);

        ExcuseCreative result = excuseCreativeService.voterExcuseCreative(1L, 1L, 10);
        assertEquals(10, result.getVotesRecus());
    }

    @Test
    void voterExcuseCreative_UserNotFound() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> excuseCreativeService.voterExcuseCreative(1L, 1L, 5));
    }

    @Test
    void voterExcuseCreative_ExcuseNotFound() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(excuseCreativeRepository.findByIdAndAuteur(1L, utilisateur)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> excuseCreativeService.voterExcuseCreative(1L, 1L, 5));
    }

    @Test
    void voterExcuseCreative_InvalidVote() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setId(1L);
        excuse.setAuteur(utilisateur);
        excuse.setVotesRecus(0);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(excuseCreativeRepository.findByIdAndAuteur(1L, utilisateur)).thenReturn(excuse);

        assertThrows(IllegalArgumentException.class, () -> excuseCreativeService.voterExcuseCreative(1L, 1L, -1));
    }

    @Test
    void modererExcuseCreative_Success_Approuvee() {
        Utilisateur auteur = new Utilisateur();
        auteur.setId(1L);
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setId(1L);
        excuse.setAuteur(auteur);
        excuse.setStatut(StatutExcuse.EN_ATTENTE);

        when(excuseCreativeRepository.findByIdAndStatut(1L, StatutExcuse.EN_ATTENTE)).thenReturn(excuse);
        when(tacheAEviterService.verifierUtilisateurEligibiliteModerer(1L)).thenReturn(true);
        when(excuseCreativeRepository.save(any())).thenReturn(excuse);

        ExcuseCreative result = excuseCreativeService.modererExcuseCreative(1L);
        assertEquals(StatutExcuse.APPROUVEE, result.getStatut());
    }

    @Test
    void modererExcuseCreative_NotFound() {
        when(excuseCreativeRepository.findByIdAndStatut(1L, StatutExcuse.EN_ATTENTE)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> excuseCreativeService.modererExcuseCreative(1L));
    }

    @Test
    void modifierExcuseCreative_Success() {
        ExcuseCreative existing = new ExcuseCreative();
        existing.setId(1L);
        existing.setTexteExcuse("Ancien texte");

        ExcuseCreative update = new ExcuseCreative();
        update.setTexteExcuse("New texte");
        update.setSituationApp("Newsituation");
        update.setCategorie(CategorieExcuse.TRAVAIL);

        when(excuseCreativeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(excuseCreativeRepository.save(any())).thenReturn(existing);

        ExcuseCreative result = excuseCreativeService.modifierExcuseCreative(1L, update);
        assertEquals("New texte", result.getTexteExcuse());
        assertEquals("Newsituation", result.getSituationApp());
        assertEquals(CategorieExcuse.TRAVAIL, result.getCategorie());
        verify(excuseCreativeRepository, times(1)).save(existing);
    }

    @Test
    void supprimerExcuseCreative_Success() {
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setId(1L);

        when(excuseCreativeRepository.findById(1L)).thenReturn(Optional.of(excuse));
        doNothing().when(excuseCreativeRepository).delete(excuse);

        excuseCreativeService.supprimerExcuseCreative(1L);
        verify(excuseCreativeRepository, times(1)).findById(1L);
        verify(excuseCreativeRepository, times(1)).delete(excuse);
    }

    @Test
    void supprimerExcuseCreative_NotFound() {
        when(excuseCreativeRepository.findById(1L)).thenReturn(Optional.empty());
        // Vérifie que l'exception est declanchee lorsque l'excuse n'est pas trouvée
        assertThrows(ResourceNotFoundException.class, () -> excuseCreativeService.supprimerExcuseCreative(1L));
    }
}