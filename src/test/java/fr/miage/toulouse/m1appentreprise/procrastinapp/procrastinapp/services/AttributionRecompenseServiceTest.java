package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttributionRecompenseServiceTest {

    @Autowired
    private AttributionRecompenseService service;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RecompenseRepository recompenseRepository;

    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;

    private Utilisateur utilisateur;
    private Recompense recompense;

    @BeforeEach
    void setUp() {
        attributionRecompenseRepository.deleteAll();
        recompenseRepository.deleteAll();
        utilisateurRepository.deleteAll();

        utilisateur = new Utilisateur();
        utilisateur.setPseudo("UserTest");
        utilisateur.setAdresseMail("user@test.com");
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setNiveauProcrastination(NiveauProcrastination.DEBUTANT);
        utilisateur.setExcusePreferee("Excuse");
        utilisateur.setPointAccumules(10);
        utilisateur = utilisateurRepository.save(utilisateur);

        recompense = new Recompense();
        recompense.setTitre("Badge Courage");
        recompense.setDescription("Avoir affronté un défi !");
        recompense.setType(TypeRecompense.POUVOIR_SPECIAL);
        recompense = recompenseRepository.save(recompense);
    }

    @Test
    void getAllAttributions() {
        assertEquals(0, service.getAllAttributions().spliterator().getExactSizeIfKnown());

        service.creerAttribution(utilisateur.getId(), recompense.getId(), "Test", 3);
        assertEquals(1, service.getAllAttributions().spliterator().getExactSizeIfKnown());
    }

    @Test
    void getAttributionById() {
        AttributionRecompense ar = service.creerAttribution(utilisateur.getId(), recompense.getId(), "Test", 3);
        AttributionRecompense found = service.getAttributionById(ar.getId());
        assertEquals(ar.getId(), found.getId());
    }

    @Test
    void getAttributionsByUtilisateur() {
        assertEquals(0, service.getAttributionsByUtilisateur(utilisateur.getId()).spliterator().getExactSizeIfKnown());

        service.creerAttribution(utilisateur.getId(), recompense.getId(), "Test", 3);
        assertEquals(1, service.getAttributionsByUtilisateur(utilisateur.getId()).spliterator().getExactSizeIfKnown());
    }

    @Test
    void creerAttribution() {
        AttributionRecompense ar = service.creerAttribution(utilisateur.getId(), recompense.getId(), "Création test", 7);
        assertNotNull(ar.getId());
        assertEquals("Création test", ar.getContexteAttribution());
        assertEquals(StatutAttributionRecompense.ACTIF, ar.getStatut());
    }

    @Test
    void supprimerAttribution() {
        AttributionRecompense ar = service.creerAttribution(utilisateur.getId(), recompense.getId(), "Suppression", 2);
        service.supprimerAttribution(ar.getId());
        assertFalse(attributionRecompenseRepository.findById(ar.getId()).isPresent());
    }

    @Test
    void desactiverAttribution() {
        AttributionRecompense ar = service.creerAttribution(utilisateur.getId(), recompense.getId(), "Désactivation", 5);
        AttributionRecompense desactive = service.desactiverAttribution(ar.getId());
        assertEquals(StatutAttributionRecompense.EXPIRE, desactive.getStatut());
    }
}
