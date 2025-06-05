package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ConfrontationPiege;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfrontationPiegeRepository extends CrudRepository<ConfrontationPiege, Long> {

    List<ConfrontationPiege> findConfrontationPiegesByPiege_Id(Long piegeId);

    List<ConfrontationPiege> findConfrontationPiegesByUtilisateur(Utilisateur utilisateur);

}
