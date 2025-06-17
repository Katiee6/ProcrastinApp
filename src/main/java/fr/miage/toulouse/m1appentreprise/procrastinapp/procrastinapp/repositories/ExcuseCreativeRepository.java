package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ExcuseCreative;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ExcuseCreativeRepository extends CrudRepository<ExcuseCreative, Long> {

    ExcuseCreative findByIdAndStatut(Long id, StatutExcuse statut);

    Optional<ExcuseCreative> findById(Long id);

    ExcuseCreative findByIdAndAuteur(Long id, Utilisateur auteur);

    boolean existsByTexteExcuse(String texteExcuse);
}
