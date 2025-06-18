package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ConfrontationPiege;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface ConfrontationPiegeRepository extends CrudRepository<ConfrontationPiege, Long> {
    boolean existsByUtilisateur(Utilisateur utilisateur);
    
    int countConfrontationPiegeByPiege(PiegeProductivite piege);
}
