package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;


import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypePiege;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PiegeProductiviteRepository extends CrudRepository<PiegeProductivite, Long> {
    List<PiegeProductivite> findPiegeProductiviteByCreateur(Utilisateur createur);

    boolean existsByCreateur(Utilisateur createur);
    boolean existsPiegeProductiviteByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateur(
            String titre, String description, TypePiege type, int niveauDifficulte, int recompenseResistance,
            int consequenceEchec, Utilisateur createur);
    boolean existsByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateurAndIdNot(
            String titre, String description, TypePiege type, int niveauDifficulte, int recompenseResistance,
            int consequenceEchec, Utilisateur createur,
            Long id
    );
}
