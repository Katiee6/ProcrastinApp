package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Recompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.RecompenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaire pour la classe RecompenseService
 */
class RecompenseServiceTest {

    private RecompenseRepository recompenseRepository;
    private RecompenseService recompenseService;

    @BeforeEach
    void setUp() {
        recompenseRepository = mock(RecompenseRepository.class);
        recompenseService = new RecompenseService(recompenseRepository);
    }

    @Test
    void creerRecompense_Success() {
        Recompense recompense = new Recompense();
        recompense.setTitre("Titre");
        recompense.setDescription("Description");
        recompense.setConditionsObtention("Condition");
        recompense.setNiveauPrestige(1);
        recompense.setType(TypeRecompense.BADGE);

        when(recompenseRepository.save(any())).thenReturn(recompense);

        Recompense result = recompenseService.creerRecompense(recompense);
        assertNotNull(result);
        verify(recompenseRepository, times(1)).save(recompense);
    }

    @Test
    void creerRecompenseErreurTitreVide() {
        Recompense recompense = new Recompense();
        recompense.setTitre("");
        recompense.setDescription("desc");
        recompense.setConditionsObtention("cond");
        recompense.setNiveauPrestige(1);
        recompense.setType(TypeRecompense.BADGE);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> recompenseService.creerRecompense(recompense));
        assertEquals("Le titre de la récompense ne peut pas être vide", exception.getMessage());
    }

    @Test
    void getRecompenseByIdSuccess() {
        Recompense recompense = new Recompense();
        recompense.setId(1L);
        when(recompenseRepository.findById(1L)).thenReturn(Optional.of(recompense));

        Recompense result = recompenseService.getRecompenseById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getRecompenseByIdNotFound() {
        when(recompenseRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recompenseService.getRecompenseById(1L));
        assertTrue(exception.getMessage().contains("Récompense non trouvée"));
    }

    @Test
    void getAllRecompenses() {
        when(recompenseRepository.findAll()).thenReturn(Collections.emptyList());
        List<Recompense> result = recompenseService.getAllRecompenses();
        assertNotNull(result);
        verify(recompenseRepository, times(1)).findAll();
    }

    @Test
    void modifierRecompenseSuccess() {
        Recompense ancienne = new Recompense();
        ancienne.setId(1L);
        ancienne.setTitre("Ancien titre");
        ancienne.setDescription("Ancienne desc");
        ancienne.setConditionsObtention("Ancienne cond");
        ancienne.setNiveauPrestige(1);
        ancienne.setType(TypeRecompense.BADGE);

        Recompense nouvelle = new Recompense();
        nouvelle.setTitre("Nouveau titre");
        nouvelle.setDescription("Nouvelle desc");
        nouvelle.setConditionsObtention("Nouvelle cond");
        nouvelle.setNiveauPrestige(2);
        nouvelle.setType(TypeRecompense.BADGE);

        when(recompenseRepository.findById(1L)).thenReturn(Optional.of(ancienne));
        when(recompenseRepository.save(any())).thenReturn(ancienne);

        Recompense result = recompenseService.modifierRecompense(1L, nouvelle);
        assertEquals("Nouveau titre", result.getTitre());
        assertEquals("Nouvelle desc", result.getDescription());
        assertEquals("Nouvelle cond", result.getConditionsObtention());
        assertEquals(2, result.getNiveauPrestige());
        assertEquals(TypeRecompense.BADGE, result.getType());
        verify(recompenseRepository, times(1)).save(ancienne);
    }

    @Test
    void modifierRecompenseNotFound() {
        Recompense nouvelle = new Recompense();
        nouvelle.setTitre("Titre");
        nouvelle.setDescription("Desc");
        nouvelle.setConditionsObtention("Cond");
        nouvelle.setNiveauPrestige(1);
        nouvelle.setType(TypeRecompense.BADGE);

        when(recompenseRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recompenseService.modifierRecompense(1L, nouvelle));
        assertTrue(exception.getMessage().contains("Récompense non trouvée"));
    }

    @Test
    void supprimerRecompenseSuccess() {
        Recompense recompense = new Recompense();
        recompense.setId(1L);
        when(recompenseRepository.findById(1L)).thenReturn(Optional.of(recompense));
        doNothing().when(recompenseRepository).delete(recompense);

        recompenseService.supprimerRecompense(1L);
        verify(recompenseRepository, times(1)).findById(1L);
        verify(recompenseRepository, times(1)).delete(recompense);
    }

    @Test
    void supprimerRecompenseNotFound() {
        when(recompenseRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> recompenseService.supprimerRecompense(1L));
        assertTrue(exception.getMessage().contains("Récompense non trouvée"));
    }
}