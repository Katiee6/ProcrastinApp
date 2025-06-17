package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Recompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RecompenseRepositoryTest {

    @Autowired
    private RecompenseRepository recompenseRepository;

    @Test
    void findRecompenseByTitreAndType() {
        assertNotNull(recompenseRepository);
        // Avant la création : aucune recompense n'est trouvée
        List<Recompense> recompensesAvant = recompenseRepository.findRecompenseByTitreAndType("Titre", TypeRecompense.BADGE);
        assertTrue(recompensesAvant.isEmpty(), "Aucune récompense ne devrait être trouvée");
        // Création d'une récompense
        Recompense recompense = new Recompense();
        recompense.setTitre("Titre");
        recompense.setType(TypeRecompense.BADGE);
        recompenseRepository.save(recompense);
        // Après la création : on trouve cette recompense
        List<Recompense> recompensesApres = recompenseRepository.findRecompenseByTitreAndType("Titre", TypeRecompense.BADGE);
        assertTrue(recompensesApres.contains(recompense), "La récompense devrait être trouvée");
        // On ne trouve pas une autre récompense
        List<Recompense> recompenses = recompenseRepository.findRecompenseByTitreAndType("Test", TypeRecompense.POUVOIR_SPECIAL);
        assertTrue(recompenses.isEmpty(), "La récompense ne devrait pas être trouvée");
    }
}