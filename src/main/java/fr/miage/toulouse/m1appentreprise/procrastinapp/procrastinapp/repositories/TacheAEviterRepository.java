package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TacheAEviterRepository extends CrudRepository<TacheAEviter, Long> {
    boolean existsByUtilisateur(Utilisateur utilisateur);

    boolean existsByUtilisateurIdAndStatutIn(Long utilisateurId, List<StatutTacheAEviter> statuts);

    List<TacheAEviter> findByUtilisateur(Utilisateur utilisateur);
    List<TacheAEviter> findByStatut(StatutTacheAEviter statut);
}
