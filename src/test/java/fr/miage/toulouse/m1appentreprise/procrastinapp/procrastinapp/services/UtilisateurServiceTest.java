package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UtilisateurServiceTest {

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setPseudo("TestUser");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
        utilisateur.setExcusePreferee("Je le ferai demain");
        utilisateur.setPointAccumules(0);
        utilisateur = utilisateurRepository.save(utilisateur);
    }

    @Test
    void getAllUtilisateur() {
        Iterable<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateur();
        assertNotNull(utilisateurs);
        assertEquals(2, utilisateurs.spliterator().getExactSizeIfKnown());
    }

    @Test
    void getUtilisateurById() {
        Utilisateur found = utilisateurService.getUtilisateurById(utilisateur.getId());
        assertNotNull(found);
        assertEquals(utilisateur.getId(), found.getId());
    }

    @Test
    void creerUtilisateurProcrastinateurEnHerbe() {
        Utilisateur nouvelUtilisateur = new Utilisateur("Nouveau", "nouveau@example.com", RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, NiveauProcrastination.DEBUTANT, "Je suis fatigué", 0);
        Utilisateur created = utilisateurService.creerUtilisateurProcrastinateurEnHerbe(nouvelUtilisateur);
        assertNotNull(created.getId());
    }

    @Test
    void creerUtilisateurAnti_procrastinateurRepentis() {
        Utilisateur nouvelUtilisateur = new Utilisateur("Repenti", "repenti@example.com", RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, NiveauProcrastination.EXPERT, "J'ai changé", 100);
        Utilisateur created = utilisateurService.creerUtilisateurAnti_procrastinateurRepentis(nouvelUtilisateur);
        assertNotNull(created.getId());
    }

    @Test
    void modifierUtilisateur() {
        utilisateur.setPseudo("UpdatedUser");
        Utilisateur updated = utilisateurService.modifierUtilisateur(utilisateur.getId(), utilisateur, utilisateur);
        assertEquals("UpdatedUser", updated.getPseudo());
    }

    @Test
    void supprimerUtilisateur() {
        utilisateurService.supprimerUtilisateur(utilisateur.getId());
        assertFalse(utilisateurRepository.findById(utilisateur.getId()).isPresent());
    }
}
