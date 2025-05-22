package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;


import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    boolean existsByAdresseMail(@NonNull String adresseMail);
    Utilisateur findUtilisateurByAdresseMail(@NonNull String adresseMail);
    Utilisateur findUtilisateurById(Long id);
}
