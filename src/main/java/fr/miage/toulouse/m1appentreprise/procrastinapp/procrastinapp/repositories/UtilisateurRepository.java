package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories;


import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Utilisateur findUtilisateurByAdresseMail(@NonNull String adresseMail);

    boolean existsByAdresseMail(@NonNull String adresseMail);
    boolean existsByPseudoAndAdresseMail(String pseudo, String adresseMail);
    boolean existsByPseudoAndAdresseMailAndIdIsNot(String pseudo, String adresseMail, Long id);
}
