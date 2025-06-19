package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.AttributionRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutAttributionRecompense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttributionRecompenseRepository extends CrudRepository<AttributionRecompense, Long> {
    boolean existsByUtilisateur(Utilisateur utilisateur);

    List<AttributionRecompense> findByStatut(StatutAttributionRecompense statut);
    Iterable<AttributionRecompense> findAttributionRecompensesByUtilisateur(Utilisateur utilisateur);
    Iterable<AttributionRecompense> findAttributionRecompenseByContexteAttribution(String contexteAttribution);
}
