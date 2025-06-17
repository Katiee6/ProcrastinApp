package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.DifficulteDefiProcrastination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DefiProcrastinationRepositoryTest {

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;

    @Test
    void findDefiProcrastinationByActif() {
        assertNotNull(defiProcrastinationRepository);
        // Avant la création : aucun défi n'est trouvé
        List<DefiProcrastination> defisAvant = defiProcrastinationRepository.findDefiProcrastinationByActif(true);
        assertTrue(defisAvant.isEmpty(), "La liste des défis devrait être vide");
        // Création des defis
        DefiProcrastination defiActif = new DefiProcrastination();
        defiActif.setActif(true);
        defiProcrastinationRepository.save(defiActif);
        DefiProcrastination defiInactif = new DefiProcrastination();
        defiInactif.setActif(false);
        defiProcrastinationRepository.save(defiInactif);
        // Apres la création : le défi actif est trouvé, mais pas le défi inactif
        List<DefiProcrastination> defisApres = defiProcrastinationRepository.findDefiProcrastinationByActif(true);
        assertTrue(defisApres.contains(defiActif), "La liste devrait contenir le défi actif");
        assertFalse(defisApres.contains(defiInactif), "La liste ne devrait pas contenir le défi inactif");
    }

    @Test
    void existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin() {
        assertNotNull(defiProcrastinationRepository);
        // Avant sa création : le défi n'est pas trouvé
        boolean existeAvantCreation = defiProcrastinationRepository.existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin(
                "Titre", "Description", 5, DifficulteDefiProcrastination.FACILE, 50, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 30));
        assertFalse(existeAvantCreation, "Aucun défi ne devrait être trouvé");
        // Création du defi
        DefiProcrastination defi = new DefiProcrastination();
        defi.setTitre("Titre");
        defi.setDescription("Description");
        defi.setDuree(5);
        defi.setDifficulte(DifficulteDefiProcrastination.FACILE);
        defi.setPointsAGagner(50);
        defi.setDateDebut(LocalDate.of(2025, 6, 1));
        defi.setDateFin(LocalDate.of(2025, 6, 30));
        defiProcrastinationRepository.save(defi);
        // Après sa création : le défi est trouvé
        boolean existeApresCreation = defiProcrastinationRepository.existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin(
                "Titre", "Description", 5, DifficulteDefiProcrastination.FACILE, 50, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 30));
        assertTrue(existeApresCreation, "Le défi devrait exister après sa création");
        // Vérification pour un défi qui n'existe pas
        boolean existeAutre = defiProcrastinationRepository.existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin(
                "Titre Autre", "Description Autre", 5, DifficulteDefiProcrastination.FACILE, 50, LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 30));
        assertFalse(existeAutre, "Le défi ne devrait pas exister");
    }
}