package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.DifficulteDefiProcrastination;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface DefiProcrastinationRepository extends CrudRepository<DefiProcrastination, Long> {

    List<DefiProcrastination> findDefiProcrastinationByActif(boolean actif);

    boolean existsByCreateur(Utilisateur createur);
    boolean existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin(
            String titre, String description, float duree, DifficulteDefiProcrastination difficulte,
            int pointsAGagner, LocalDate dateDebut, LocalDate dateFin);

}
