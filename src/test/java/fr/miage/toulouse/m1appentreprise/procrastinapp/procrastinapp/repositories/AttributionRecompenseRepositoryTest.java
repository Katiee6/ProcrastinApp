package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AttributionRecompenseRepositoryTest {

    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RecompenseRepository recompenseRepository;

    private Utilisateur utilisateur;
    private Recompense recompense;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setPseudo("JeanTest");
        utilisateur.setAdresseMail("jean@test.fr");
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setNiveauProcrastination(NiveauProcrastination.INTERMEDIAIRE);
        utilisateur.setExcusePreferee("Pas envie aujourd'hui");
        utilisateur.setPointAccumules(50);
        utilisateurRepository.save(utilisateur);

        recompense = new Recompense();
        recompense.setTitre("Première Récompense");
        recompense.setDescription("Récompense de test");
        recompense.setConditionsObtention("Aucune");
        recompense.setNiveauPrestige(1);
        recompense.setType(TypeRecompense.BADGE);
        recompenseRepository.save(recompense);
    }

    @Test
    void existsByUtilisateur() {
        boolean existsAvant = attributionRecompenseRepository.existsByUtilisateur(utilisateur);
        assertFalse(existsAvant, "Aucune attribution ne devrait exister");

        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(utilisateur);
        attribution.setRecompense(recompense);
        attribution.setDateObtention(LocalDateTime.now());
        attribution.setDateExpiration(LocalDateTime.now().plusDays(30));
        attribution.setStatut(StatutAttributionRecompense.ACTIF);
        attribution.setContexteAttribution("Inscription");
        attributionRecompenseRepository.save(attribution);

        boolean existsApres = attributionRecompenseRepository.existsByUtilisateur(utilisateur);
        assertTrue(existsApres, "Une attribution devrait maintenant exister");
    }

    @Test
    void findAttributionRecompensesByUtilisateur() {
        Iterable<AttributionRecompense> avant = attributionRecompenseRepository.findAttributionRecompensesByUtilisateur(utilisateur);
        assertEquals(0, avant.spliterator().getExactSizeIfKnown(),  "Liste vide attendue");


        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(utilisateur);
        attribution.setRecompense(recompense);
        attribution.setDateObtention(LocalDateTime.now());
        attribution.setDateExpiration(LocalDateTime.now().plusDays(15));
        attribution.setStatut(StatutAttributionRecompense.ACTIF);
        attribution.setContexteAttribution("Défi hebdo");
        attributionRecompenseRepository.save(attribution);

        Iterable<AttributionRecompense> apres = attributionRecompenseRepository.findAttributionRecompensesByUtilisateur(utilisateur);
        assertEquals(1, apres.spliterator().getExactSizeIfKnown(),  "Liste vide attendue");
        assertEquals("Défi hebdo", apres.iterator().next().getContexteAttribution());
    }

    @Test
    void findAttributionRecompenseByContexteAttribution() {
        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(utilisateur);
        attribution.setRecompense(recompense);
        attribution.setDateObtention(LocalDateTime.now());
        attribution.setDateExpiration(LocalDateTime.now().plusDays(10));
        attribution.setStatut(StatutAttributionRecompense.EXPIRE);
        attribution.setContexteAttribution("Défi mensuel");
        attributionRecompenseRepository.save(attribution);

        Iterable<AttributionRecompense> liste = attributionRecompenseRepository.findAttributionRecompenseByContexteAttribution("Défi mensuel");
        assertEquals(1, liste.spliterator().getExactSizeIfKnown(),  "Liste vide attendue");
        assertEquals("Défi mensuel", liste.iterator().next().getContexteAttribution());
    }
}
