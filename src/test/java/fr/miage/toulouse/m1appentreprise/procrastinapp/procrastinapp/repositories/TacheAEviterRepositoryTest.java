package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TacheAEviterRepositoryTest {

    @Autowired
    TacheAEviterRepository tacheAEviterRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Test
    void existsByUtilisateurIdAndStatutIn() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("Katia");
        utilisateur = utilisateurRepository.save(utilisateur);

        TacheAEviter tache = new TacheAEviter();
        tache.setUtilisateur(utilisateur);
        tache.setStatut(StatutTacheAEviter.EN_ATTENTE);
        tacheAEviterRepository.save(tache);

        boolean exists = tacheAEviterRepository.existsByUtilisateurIdAndStatutIn(
                utilisateur.getId(),
                Arrays.asList(StatutTacheAEviter.EN_ATTENTE, StatutTacheAEviter.EN_ATTENTE)
        );
        assertTrue(exists);

        boolean notExists = tacheAEviterRepository.existsByUtilisateurIdAndStatutIn(
                utilisateur.getId(),
                List.of(StatutTacheAEviter.CATASTROPHE)
        );
        assertFalse(notExists);
    }

    @Test
    void findByUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("Jeremy");
        utilisateur = utilisateurRepository.save(utilisateur);

        TacheAEviter tache1 = new TacheAEviter();
        tache1.setUtilisateur(utilisateur);
        tache1.setStatut(StatutTacheAEviter.EN_ATTENTE);
        tacheAEviterRepository.save(tache1);

        List<TacheAEviter> taches = tacheAEviterRepository.findByUtilisateur(utilisateur);
        assertNotNull(taches);
        assertEquals(1, taches.size());
        assertEquals(utilisateur.getId(), taches.get(0).getUtilisateur().getId());
    }

    @Test
    void findById() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("Melanie");
        utilisateur = utilisateurRepository.save(utilisateur);

        TacheAEviter tache = new TacheAEviter();
        tache.setUtilisateur(utilisateur);
        tache.setStatut(StatutTacheAEviter.EVITEE_AVEC_SUCCES);
        tache = tacheAEviterRepository.save(tache);

        Optional<TacheAEviter> found = tacheAEviterRepository.findById(tache.getId());
        assertTrue(found.isPresent());
        assertEquals(tache.getId(), found.get().getId());

        Optional<TacheAEviter> notFound = tacheAEviterRepository.findById(999L); // ID qui n'existe pas
        assertTrue(notFound.isEmpty());
    }
}