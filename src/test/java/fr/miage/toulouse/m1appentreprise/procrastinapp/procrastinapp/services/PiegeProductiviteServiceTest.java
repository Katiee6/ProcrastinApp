package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypePiege;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.PiegeProductiviteRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PiegeProductiviteServiceTest {

    @Mock
    private PiegeProductiviteRepository piegeRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private PiegeProductiviteService piegeService;

    private Utilisateur createur;
    private PiegeProductivite piege;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createur = new Utilisateur();
        createur.setId(1L);
        createur.setPseudo("Jérémy");

        piege = new PiegeProductivite();
        piege.setId(1L);
        piege.setTitre("TikTok");
        piege.setDescription("Perte de temps totale");
        piege.setType(TypePiege.JEU);
        piege.setNiveauDifficulte(3);
        piege.setRecompenseResistance(5);
        piege.setConsequenceEchec(10);
        piege.setDateCreation(LocalDateTime.now());
        piege.setCreateur(createur);
    }

    @Test
    void getAllPiegeProductivite() {
        when(piegeRepository.findAll()).thenReturn(List.of(piege));
        Iterable<PiegeProductivite> result = piegeService.getAllPiegeProductivite();
        assertEquals(1, result.spliterator().getExactSizeIfKnown());
        verify(piegeRepository).findAll();
    }

    @Test
    void getAllPiegeProductiviteUtilisateur() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(createur));
        when(piegeRepository.findAllByCreateur(createur)).thenReturn(List.of(piege));

        Iterable<PiegeProductivite> result = piegeService.getAllPiegeProductiviteUtilisateur(createur);
        assertEquals(1, result.spliterator().getExactSizeIfKnown());
        verify(piegeRepository).findAllByCreateur(createur);
    }

    @Test
    void getPiegeProductiviteById() {
        when(piegeRepository.findById(1L)).thenReturn(Optional.of(piege));
        PiegeProductivite found = piegeService.getPiegeProductiviteById(1L);
        assertEquals("TikTok", found.getTitre());
    }

    @Test
    void getPiegeProductiviteById_notFound() {
        when(piegeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> piegeService.getPiegeProductiviteById(999L));
    }

    @Test
    void creerPiegeProductivite() {
        when(piegeRepository.save(any(PiegeProductivite.class))).thenReturn(piege);
        PiegeProductivite created = piegeService.creerPiegeProductivite(piege);
        assertNotNull(created);
        verify(piegeRepository).save(piege);
    }

    @Test
    void modifierPiegeProductivite() {
        when(piegeRepository.findById(1L)).thenReturn(Optional.of(piege));
        when(piegeRepository.save(any(PiegeProductivite.class))).thenReturn(piege);

        PiegeProductivite modif = new PiegeProductivite();
        modif.setTitre("Nouveau piège");
        modif.setCreateur(createur);

        PiegeProductivite result = piegeService.modifierPiegeProductivite(1L, modif);
        System.out.println(result);
        assertEquals("Nouveau piège", result.getTitre());
    }

    @Test
    void supprimerPiegeProductivite() {
        when(piegeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(piegeRepository).deleteById(1L);
        assertThrows(ResourceNotFoundException.class, () -> piegeService.supprimerPiegeProductivite(1L));
    }

    @Test
    void supprimerPiegeProductivite_notFound() {
        when(piegeRepository.existsById(999L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> piegeService.supprimerPiegeProductivite(999L));
    }
}
