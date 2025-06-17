package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

    import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ExcuseCreative;
    import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
    import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;

    @DataJpaTest
    class ExcuseCreativeRepositoryTest {

        @Autowired
        ExcuseCreativeRepository excuseCreativeRepository;

        @Autowired
        UtilisateurRepository utilisateurRepository;


        @Test
        void findByIdAndStatut() {
            Utilisateur auteur = new Utilisateur();
            auteur.setPseudo("Katia");
            auteur = utilisateurRepository.save(auteur);

            ExcuseCreative excuse = new ExcuseCreative();
            excuse.setTexteExcuse("Je dois nourrir mon cerveau avec de la vitamine R (Repos)");
            excuse.setStatut(StatutExcuse.APPROUVEE);
            excuse.setAuteur(auteur);
            excuse = excuseCreativeRepository.save(excuse);

            ExcuseCreative found = excuseCreativeRepository.findByIdAndStatut(excuse.getId(), StatutExcuse.APPROUVEE);
            assertNotNull(found);
            assertEquals(excuse.getId(), found.getId());

            ExcuseCreative notFound = excuseCreativeRepository.findByIdAndStatut(excuse.getId(), StatutExcuse.EN_ATTENTE);
            assertNull(notFound);
        }

        @Test
        void findById() {
            Utilisateur auteur = new Utilisateur();
            auteur.setPseudo("Melanie");
            auteur = utilisateurRepository.save(auteur);

            ExcuseCreative excuse = new ExcuseCreative();
            excuse.setTexteExcuse("Je suis en pleine phase de procrastination créative avec mon chat");
            excuse.setStatut(StatutExcuse.EN_ATTENTE);
            excuse.setAuteur(auteur);
            excuse = excuseCreativeRepository.save(excuse);

            Optional<ExcuseCreative> found = excuseCreativeRepository.findById(excuse.getId());
            assertTrue(found.isPresent());
            assertEquals(excuse.getId(), found.get().getId());

            Optional<ExcuseCreative> notfound = excuseCreativeRepository.findById(999L);
            assertTrue(notfound.isEmpty());
        }

        @Test
        void findByIdAndAuteur() {
            Utilisateur auteur = new Utilisateur();
            auteur.setPseudo("Jeremy");
            auteur = utilisateurRepository.save(auteur);

            ExcuseCreative excuse = new ExcuseCreative();
            excuse.setTexteExcuse("Je dois tester une nouvelle recette de cookies au chocolat");
            excuse.setStatut(StatutExcuse.EN_ATTENTE);
            excuse.setAuteur(auteur);
            excuse = excuseCreativeRepository.save(excuse);

            ExcuseCreative found = excuseCreativeRepository.findByIdAndAuteur(excuse.getId(), auteur);
            assertNotNull(found);
            assertEquals(excuse.getId(), found.getId());

            Utilisateur autreAuteur = new Utilisateur();
            autreAuteur.setPseudo("BOSS");
            autreAuteur = utilisateurRepository.save(autreAuteur);

            ExcuseCreative notFound = excuseCreativeRepository.findByIdAndAuteur(excuse.getId(), autreAuteur);
            assertNull(notFound);
        }

        @Test
        void existsByTexteExcuse() {
            Utilisateur auteur = new Utilisateur();
            auteur.setPseudo("Thanos");
            auteur = utilisateurRepository.save(auteur);

            ExcuseCreative excuse = new ExcuseCreative();
            excuse.setTexteExcuse("Je dois sauver le monde avec les pierres d'infinité");
            excuse.setStatut(StatutExcuse.EN_ATTENTE);
            excuse.setAuteur(auteur);
            excuseCreativeRepository.save(excuse);

            boolean exists = excuseCreativeRepository.existsByTexteExcuse("Je dois sauver le monde avec les pierres d'infinité");
            assertTrue(exists);

            boolean notExists = excuseCreativeRepository.existsByTexteExcuse("Je dois revoir les Avengers je pense :)");
            assertFalse(notExists);
        }
    }