package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TacheAEviterRepository extends CrudRepository<TacheAEviter, Long> {
    boolean existsByUtilisateur(Utilisateur utilisateur);

    boolean existsByUtilisateurIdAndStatutIn(Long utilisateurId, List<StatutTacheAEviter> statuts);

    List<TacheAEviter> findByUtilisateur(Utilisateur utilisateur);

    Optional<TacheAEviter> findById(Long aLong);
}
