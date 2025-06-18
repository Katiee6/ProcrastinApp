package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.AttributionRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttributionRecompenseRepository extends CrudRepository<AttributionRecompense, Long> {
    boolean existsByUtilisateur(Utilisateur utilisateur);

    List<AttributionRecompense> findAttributionRecompenseByContexteAttribution(String contexteAttribution);
}
