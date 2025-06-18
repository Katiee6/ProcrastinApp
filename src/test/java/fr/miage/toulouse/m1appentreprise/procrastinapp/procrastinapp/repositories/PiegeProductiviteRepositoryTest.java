package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypePiege;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PiegeProductiviteRepositoryTest {

    @Autowired
    PiegeProductiviteRepository piegeProductiviteRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    private Utilisateur createAndSaveUser(String pseudo) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo(pseudo);
        utilisateur.setAdresseMail(pseudo + "@example.com");
        return utilisateurRepository.save(utilisateur);
    }

    private PiegeProductivite createAndSavePiege(Utilisateur utilisateur, String titre) {
        PiegeProductivite piege = new PiegeProductivite();
        piege.setTitre(titre);
        piege.setDescription("Attention ça fait perdre du temps");
        piege.setType(TypePiege.JEU);
        piege.setNiveauDifficulte(2);
        piege.setRecompenseResistance(5);
        piege.setConsequenceEchec(10);
        piege.setDateCreation(LocalDateTime.now());
        piege.setCreateur(utilisateur);
        return piegeProductiviteRepository.save(piege);
    }

    @Test
    void findAllByCreateur() {
        Utilisateur utilisateur = createAndSaveUser("lucas");
        createAndSavePiege(utilisateur, "TikTok");

        Iterable<PiegeProductivite> pieges = piegeProductiviteRepository.findAllByCreateur(utilisateur);
        assertNotEquals(0, pieges.spliterator().getExactSizeIfKnown()); //Empty
        assertEquals(utilisateur.getId(), pieges.iterator().next().getCreateur().getId());
    }

    @Test
    void existsByCreateur() {
        Utilisateur utilisateur = createAndSaveUser("marie");
        createAndSavePiege(utilisateur, "Netflix");

        boolean exists = piegeProductiviteRepository.existsByCreateur(utilisateur);
        assertTrue(exists);

        Utilisateur inconnu = createAndSaveUser("phantom");
        boolean notExists = piegeProductiviteRepository.existsByCreateur(inconnu);
        assertFalse(notExists);
    }

    @Test
    void existsPiegeProductiviteByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateur() {
        Utilisateur utilisateur = createAndSaveUser("chris");
        PiegeProductivite piege = createAndSavePiege(utilisateur, "Youtube");

        boolean exists = piegeProductiviteRepository
                .existsPiegeProductiviteByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateur(
                        piege.getTitre(),
                        piege.getDescription(),
                        piege.getType(),
                        piege.getNiveauDifficulte(),
                        piege.getRecompenseResistance(),
                        piege.getConsequenceEchec(),
                        piege.getCreateur()
                );
        assertTrue(exists);
    }

    @Test
    void existsByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateurAndIdNot() {
        Utilisateur utilisateur = createAndSaveUser("claire");
        PiegeProductivite piege = createAndSavePiege(utilisateur, "Instagram");

        boolean conflict = piegeProductiviteRepository
                .existsByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateurAndIdNot(
                        piege.getTitre(),
                        piege.getDescription(),
                        piege.getType(),
                        piege.getNiveauDifficulte(),
                        piege.getRecompenseResistance(),
                        piege.getConsequenceEchec(),
                        piege.getCreateur(),
                        -1L // ID différent : simulateur de "autre"
                );
        assertTrue(conflict);

        boolean notConflict = piegeProductiviteRepository
                .existsByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateurAndIdNot(
                        piege.getTitre(),
                        piege.getDescription(),
                        piege.getType(),
                        piege.getNiveauDifficulte(),
                        piege.getRecompenseResistance(),
                        piege.getConsequenceEchec(),
                        piege.getCreateur(),
                        piege.getId() // même ID => pas conflit
                );
        assertFalse(notConflict);
    }
}
