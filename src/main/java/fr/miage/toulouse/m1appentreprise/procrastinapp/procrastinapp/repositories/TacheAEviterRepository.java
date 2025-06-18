package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface TacheAEviterRepository extends CrudRepository<TacheAEviter, Long> {
    boolean existsByUtilisateur(Utilisateur utilisateur);
}
