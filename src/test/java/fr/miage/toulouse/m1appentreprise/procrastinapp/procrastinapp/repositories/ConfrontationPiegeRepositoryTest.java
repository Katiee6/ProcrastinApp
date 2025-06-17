package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ConfrontationPiege;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.NiveauProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypePiege;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ConfrontationPiegeRepositoryTest {

    @Autowired
    private ConfrontationPiegeRepository confrontationPiegeRepository;
    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private PiegeProductivite piege;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        // Creation d'un piège qui pourra être lié à des confrontations
        piege = new PiegeProductivite();
        piege.setTitre("Titre");
        piege.setDescription("Description");
        piege.setType(TypePiege.JEU);
        piegeProductiviteRepository.save(piege);
        // Création d'un utilisateur qui pourra être lié à des confrontations
        utilisateur = new Utilisateur();
        utilisateur.setPseudo("Pseudo");
        utilisateur.setAdresseMail("mail@example.com");
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
        utilisateur.setExcusePreferee("Excuse");
        utilisateur.setPointAccumules(0);
        utilisateurRepository.save(utilisateur);
    }

    @Test
    void findConfrontationPiegesByPiege_Id() {
        assertNotNull(confrontationPiegeRepository);
        // Avant la création : aucune confrontation n'est trouvée
        List<ConfrontationPiege> confrontationsAvant = confrontationPiegeRepository.findConfrontationPiegesByPiege_Id(1L);
        assertTrue(confrontationsAvant.isEmpty(), "Aucune confrontation ne devrait être trouvée");
        // Création d'une confrontation
        ConfrontationPiege confrontation = new ConfrontationPiege();
        confrontation.setPiege(piege);
        confrontationPiegeRepository.save(confrontation);
        // Après la création : on trouve cette confrontation
        List<ConfrontationPiege> confrontationsApres = confrontationPiegeRepository.findConfrontationPiegesByPiege_Id(1L);
        assertTrue(confrontationsApres.contains(confrontation), "La confrontation devrait être trouvée");
        // On ne trouve pas une autre confrontation
        List<ConfrontationPiege> confrontationsAutre = confrontationPiegeRepository.findConfrontationPiegesByPiege_Id(2L);
        assertTrue(confrontationsAutre.isEmpty(), "La confrontation ne devrait pas être trouvée");
    }

    @Test
    void findConfrontationPiegesByUtilisateur() {
        assertNotNull(confrontationPiegeRepository);
        // Avant la création : aucune confrontation n'est trouvée
        List<ConfrontationPiege> confrontationsAvant = confrontationPiegeRepository.findConfrontationPiegesByUtilisateur(utilisateur);
        assertTrue(confrontationsAvant.isEmpty(), "Aucune confrontation ne devrait être trouvée");
        // Création d'une confrontation
        ConfrontationPiege confrontation = new ConfrontationPiege();
        confrontation.setUtilisateur(utilisateur);
        confrontationPiegeRepository.save(confrontation);
        // Après la création : on trouve cette confrontation
        List<ConfrontationPiege> confrontationsApres = confrontationPiegeRepository.findConfrontationPiegesByUtilisateur(utilisateur);
        assertTrue(confrontationsApres.contains(confrontation), "La confrontation devrait être trouvée");
    }
}