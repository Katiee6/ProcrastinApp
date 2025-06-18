package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.DifficulteDefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ConflictException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.DefiProcrastinationRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.ParticipationDefiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class DefiProcrastinationServiceTest {

    private DefiProcrastinationRepository defiProcrastinationRepository;

    private ParticipationDefiRepository participationDefiRepository;

    private DefiProcrastinationService defiProcrastinationService;

    @BeforeEach
    void setUp() {
        defiProcrastinationRepository = mock(DefiProcrastinationRepository.class);
        participationDefiRepository = mock(ParticipationDefiRepository.class);
        defiProcrastinationService = new DefiProcrastinationService(defiProcrastinationRepository, participationDefiRepository);
    }

    @Test
    void getAllDefis() {
        List<DefiProcrastination> defis = List.of(new DefiProcrastination());
        when(defiProcrastinationRepository.findAll()).thenReturn(defis);
        Iterable<DefiProcrastination> result = defiProcrastinationService.getAllDefis();
        assertEquals(defis, result);
        verify(defiProcrastinationRepository).findAll();
    }

    @Test
    void getAllDefisActifs() {
        List<DefiProcrastination> defisActifs = List.of(new DefiProcrastination());
        when(defiProcrastinationRepository.findDefiProcrastinationByActif(true)).thenReturn(defisActifs);
        Iterable<DefiProcrastination> result = defiProcrastinationService.getAllDefisActifs();
        assertEquals(defisActifs, result);
        verify(defiProcrastinationRepository).findDefiProcrastinationByActif(true);
    }

    @Test
    void getDefiById_whenExists() {
        DefiProcrastination defi = new DefiProcrastination();
        defi.setId(1L);
        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(defi));
        DefiProcrastination result = defiProcrastinationService.getDefiById(1L);
        assertEquals(defi, result);
    }

    @Test
    void getDefiById_whenNotExists() {
        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> defiProcrastinationService.getDefiById(1L));
    }

    @Test
    void creerDefi_success() {
        DefiProcrastination defi = new DefiProcrastination();
        defi.setTitre("Titre");
        defi.setDescription("Description");
        defi.setDuree(3);
        defi.setDifficulte(DifficulteDefiProcrastination.DIFFICILE);
        defi.setPointsAGagner(50);
        defi.setDateDebut(LocalDate.now().plusDays(1));
        defi.setDateFin(LocalDate.now().plusDays(10));
        Utilisateur utilisateur = new Utilisateur();
        when(defiProcrastinationRepository.existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin(
                any(), any(), anyInt(), any(), anyInt(), any(), any())).thenReturn(false);
        when(defiProcrastinationRepository.save(any())).thenReturn(defi);
        DefiProcrastination result = defiProcrastinationService.creerDefi(defi, utilisateur);
        assertEquals(defi, result);
        assertTrue(result.isActif());
        assertEquals(utilisateur, result.getCreateur());
    }

    @Test
    void creerDefi_invalidDateException() {
        DefiProcrastination defi = new DefiProcrastination();
        defi.setDateDebut(LocalDate.now().plusDays(10));
        defi.setDateFin(LocalDate.now());
        assertThrows(InvalidRequestException.class, () -> defiProcrastinationService.creerDefi(defi, new Utilisateur()));
    }

    @Test
    void modifierDefi_success() {
        DefiProcrastination existing = new DefiProcrastination();
        existing.setId(1L);
        existing.setDateDebut(LocalDate.now().plusDays(1));
        existing.setDateFin(LocalDate.now().plusDays(5));

        DefiProcrastination updated = new DefiProcrastination();
        updated.setId(1L);
        updated.setTitre("New Title");
        updated.setDescription("New Desc");
        updated.setDuree(5);
        updated.setDifficulte(DifficulteDefiProcrastination.MOYEN);
        updated.setPointsAGagner(100);
        updated.setDateDebut(LocalDate.now().plusDays(2));
        updated.setDateFin(LocalDate.now().plusDays(6));

        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(defiProcrastinationRepository.save(any())).thenReturn(existing);

        DefiProcrastination result = defiProcrastinationService.modifierDefi(updated);
        assertEquals("New Title", result.getTitre());
        assertEquals("New Desc", result.getDescription());
        assertEquals(100, result.getPointsAGagner());
    }

    @Test
    void modifierDefi_invalidDates() {
        DefiProcrastination existing = new DefiProcrastination();
        existing.setId(1L);
        existing.setDateDebut(LocalDate.now().plusDays(1));
        existing.setDateFin(LocalDate.now().plusDays(3));

        DefiProcrastination toUpdate = new DefiProcrastination();
        toUpdate.setId(1L);
        toUpdate.setDateDebut(LocalDate.now().plusDays(5));
        toUpdate.setDateFin(LocalDate.now().plusDays(2)); // invalid

        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(existing));
        assertThrows(InvalidRequestException.class, () -> defiProcrastinationService.modifierDefi(toUpdate));
    }

    @Test
    void modifierStatutDefi_success() {
        DefiProcrastination existing = new DefiProcrastination();
        existing.setId(1L);
        existing.setActif(true);

        DefiProcrastination mod = new DefiProcrastination();
        mod.setId(1L);
        mod.setActif(false);

        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(defiProcrastinationRepository.save(any())).thenReturn(existing);

        DefiProcrastination result = defiProcrastinationService.modifierStatutDefi(mod);
        assertFalse(result.isActif());
    }

    @Test
    void supprimerDefi_noParticipation_success() {
        DefiProcrastination defi = new DefiProcrastination();
        defi.setId(1L);

        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(defi));
        when(participationDefiRepository.countParticipationDefisByDefi(defi)).thenReturn(0);

        defiProcrastinationService.supprimerDefi(1L);

        verify(defiProcrastinationRepository).delete(defi);
    }

    @Test
    void supprimerDefi_withParticipation_conflict() {
        DefiProcrastination defi = new DefiProcrastination();
        defi.setId(1L);

        when(defiProcrastinationRepository.findById(1L)).thenReturn(Optional.of(defi));
        when(participationDefiRepository.countParticipationDefisByDefi(defi)).thenReturn(3);

        assertThrows(ConflictException.class, () -> defiProcrastinationService.supprimerDefi(1L));
    }
}