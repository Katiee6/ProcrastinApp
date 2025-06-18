package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;


import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UtilisateurRepositoryTest {

    @Autowired
    UtilisateurRepository utilisateurRepository;

    private Utilisateur creerEtSauvegarderUtilisateur(String pseudo, String email) {
        Utilisateur u = new Utilisateur();
        u.setPseudo(pseudo);
        u.setAdresseMail(email);
        u.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        u.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
        u.setExcusePreferee("J'avais oublié !");
        u.setPointAccumules(0);
        return utilisateurRepository.save(u);
    }

    @Test
    void findUtilisateurByAdresseMail() {
        Utilisateur utilisateur = creerEtSauvegarderUtilisateur("jeremy", "jeremy@example.com");

        Optional<Utilisateur> found = Optional.ofNullable(utilisateurRepository.findUtilisateurByAdresseMail("jeremy@example.com"));

        assertTrue(found.isPresent());
        assertEquals(utilisateur.getId(), found.get().getId());
    }

    @Test
    void existsByAdresseMail() {
        creerEtSauvegarderUtilisateur("lucie", "lucie@example.com");

        assertTrue(utilisateurRepository.existsByAdresseMail("lucie@example.com"));
        assertFalse(utilisateurRepository.existsByAdresseMail("inconnu@example.com"));
    }

    @Test
    void existsByPseudoAndAdresseMail() {
        creerEtSauvegarderUtilisateur("nina", "nina@example.com");

        assertTrue(utilisateurRepository.existsByPseudoAndAdresseMail("nina", "nina@example.com"));
        assertFalse(utilisateurRepository.existsByPseudoAndAdresseMail("fake", "nina@example.com"));
    }

    @Test
    void existsByPseudoAndAdresseMailAndIdIsNot() {
        Utilisateur u1 = creerEtSauvegarderUtilisateur("sam", "sam@example.com");
        Utilisateur u2 = creerEtSauvegarderUtilisateur("sam", "sam@example.com");

        // La combinaison existe bien, mais pas pour cet ID -> true
        assertTrue(utilisateurRepository.existsByPseudoAndAdresseMailAndIdIsNot("sam", "sam@example.com", u1.getId()));
    }
}