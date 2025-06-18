package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutParticipationDefi;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ParticipationDefiRepository extends CrudRepository<ParticipationDefi, Long> {

    List<ParticipationDefi> findParticipationDefiByUtilisateur(Utilisateur utilisateur);
    List<ParticipationDefi> findParticipationDefiByDefi_Id(Long id);
    List<ParticipationDefi> findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual(StatutParticipationDefi statut, LocalDate debut);

    boolean existsByUtilisateur(Utilisateur utilisateur);
    boolean existsParticipationDefiByDefi_IdAndStatutNot(Long defiId, StatutParticipationDefi statut);

    int countParticipationDefisByUtilisateurAndStatutNot(Utilisateur utilisateur, StatutParticipationDefi statut);
    int countParticipationDefisByDefi(DefiProcrastination defi);

}
