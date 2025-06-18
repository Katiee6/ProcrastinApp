package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.DifficulteDefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.NiveauProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutParticipationDefi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ParticipationDefiRepositoryTest {

    @Autowired
    private ParticipationDefiRepository participationDefiRepository;
    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private DefiProcrastination defi;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        // Creation d'un défi qui pourra être lié à des participations
        defi = new DefiProcrastination();
        defi.setTitre("Titre");
        defi.setDescription("Description");
        defi.setDuree(5);
        defi.setDifficulte(DifficulteDefiProcrastination.FACILE);
        defi.setPointsAGagner(50);
        defi.setDateDebut(LocalDate.of(2025, 6, 1));
        defi.setDateFin(LocalDate.of(2025, 6, 30));
        defi.setActif(true);
        defiProcrastinationRepository.save(defi);
        // Création d'un utilisateur qui pourra être lié à des participations
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
    void findParticipationDefiByUtilisateur() {
        assertNotNull(participationDefiRepository);
        // Avant la création : aucune participation n'est trouvée
        List<ParticipationDefi> participationsAvant = participationDefiRepository.findParticipationDefiByUtilisateur(utilisateur);
        assertTrue(participationsAvant.isEmpty(), "Aucune participation ne devrait être trouvée");
        // Création d'une participation
        ParticipationDefi participation = new ParticipationDefi();
        participation.setUtilisateur(utilisateur);
        participationDefiRepository.save(participation);
        // Après la création : on trouve cette participation
        List<ParticipationDefi> participationsApres = participationDefiRepository.findParticipationDefiByUtilisateur(utilisateur);
        assertTrue(participationsApres.contains(participation), "La participation devrait être trouvée");
    }

    @Test
    void findParticipationDefiByDefi_Id() {
        assertNotNull(participationDefiRepository);
        // Avant la création : aucune participation n'est trouvée
        List<ParticipationDefi> participationsAvant = participationDefiRepository.findParticipationDefiByDefi_Id(1L);
        assertTrue(participationsAvant.isEmpty(), "Aucune participation ne devrait être trouvée");
        // Création d'une participation
        ParticipationDefi participation = new ParticipationDefi();
        participation.setDefi(defi);
        participationDefiRepository.save(participation);
        // Après la création : on trouve cette participation
        List<ParticipationDefi> participationsApres = participationDefiRepository.findParticipationDefiByDefi_Id(defi.getId());
        assertTrue(participationsApres.contains(participation), "La participation devrait être trouvée");
        // On ne trouve pas une autre participation
        List<ParticipationDefi> participationsAutre = participationDefiRepository.findParticipationDefiByDefi_Id(2L);
        assertTrue(participationsAutre.isEmpty(), "La participation ne devrait pas être trouvée");
    }

    @Test
    void findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual() {
        assertNotNull(participationDefiRepository);
        // Avant la création : aucune participation n'est trouvée
        List<ParticipationDefi> participationsAvant = participationDefiRepository.findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual(
                StatutParticipationDefi.INSCRIT, LocalDate.of(2025, 6, 15));
        assertTrue(participationsAvant.isEmpty(), "Aucune participation ne devrait être trouvée");
        // Création de participations
        ParticipationDefi participation = new ParticipationDefi();
        participation.setStatut(StatutParticipationDefi.INSCRIT);
        participation.setDefi(defi);
        participationDefiRepository.save(participation);
        // Après la création : on trouve cette participation
        List<ParticipationDefi> participationsApres = participationDefiRepository.findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual(
                StatutParticipationDefi.INSCRIT, LocalDate.of(2025, 6, 15));
        assertTrue(participationsApres.contains(participation), "La participation devrait être trouvée");
        // On ne trouve pas une autre participation
        List<ParticipationDefi> participationsAutre = participationDefiRepository.findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual(
                StatutParticipationDefi.INSCRIT, LocalDate.of(2025, 5, 15));
        assertTrue(participationsAutre.isEmpty(), "La participation ne devrait pas être trouvée");
    }

    @Test
    void existsParticipationDefiByDefi_IdAndStatutNot() {
        assertNotNull(participationDefiRepository);
        // Avant la création : aucune participation n'est trouvée
        boolean existeAvant = participationDefiRepository.existsParticipationDefiByDefi_IdAndStatutNot(1L, StatutParticipationDefi.EN_COURS);
        assertFalse(existeAvant, "Aucune participation ne devrait être trouvée");
        // Création d'une participation
        ParticipationDefi participation = new ParticipationDefi();
        participation.setDefi(defi);
        participation.setStatut(StatutParticipationDefi.INSCRIT);
        participationDefiRepository.save(participation);
        // Après la création : on trouve cette participation
        boolean existeApres = participationDefiRepository.existsParticipationDefiByDefi_IdAndStatutNot(defi.getId(), StatutParticipationDefi.EN_COURS);
        assertTrue(existeApres, "La participation devrait être trouvée");
        // On ne trouve pas une autre participation
        boolean existeAutre = participationDefiRepository.existsParticipationDefiByDefi_IdAndStatutNot(2L, StatutParticipationDefi.EN_COURS);
        assertFalse(existeAutre, "La participation ne devrait pas être trouvée");
    }

    @Test
    void countParticipationDefisByUtilisateurAndStatutNot() {
        assertNotNull(participationDefiRepository);
        // Avant la création : aucune participation n'est trouvée
        int nbAvant = participationDefiRepository.countParticipationDefisByUtilisateurAndStatutNot(utilisateur, StatutParticipationDefi.TERMINE);
        assertEquals(0, nbAvant, "L'utilisateur ne devrait avoir aucune participation non terminée");
        // Création d'une participation
        ParticipationDefi participation = new ParticipationDefi();
        participation.setUtilisateur(utilisateur);
        participation.setStatut(StatutParticipationDefi.EN_COURS);
        participationDefiRepository.save(participation);
        // Après la création : on trouve cette participation
        int nbApres = participationDefiRepository.countParticipationDefisByUtilisateurAndStatutNot(utilisateur, StatutParticipationDefi.TERMINE);
        assertEquals(1, nbApres, "L'utilisateur devrait avoir une participation non terminée");
    }

    @Test
    void countParticipationDefisByDefi() {
        assertNotNull(participationDefiRepository);
        // Avant la création : aucune participation n'est trouvée
        int nbAvant = participationDefiRepository.countParticipationDefisByDefi(defi);
        assertEquals(0, nbAvant, "Le défi ne devrait avoir aucune participation");
        // Création d'une participation
        ParticipationDefi participation = new ParticipationDefi();
        participation.setDefi(defi);
        participationDefiRepository.save(participation);
        // Après la création : on trouve cette participation
        int nbApres = participationDefiRepository.countParticipationDefisByDefi(defi);
        assertEquals(1, nbApres, "Le défi devrait avoir une participation");
    }
}