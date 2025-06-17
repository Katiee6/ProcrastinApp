package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.config;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

@Configuration
public class InitialDataLoader {

    @Bean
    CommandLineRunner initDatabase(UtilisateurRepository utilisateurRepository) {
        return args -> {
            // Vérifie si l'utilisateur admin existe déjà
            if (!utilisateurRepository.existsByAdresseMail("Gestionnaire_du_temps_perdu@example.com")) {
                Utilisateur admin = new Utilisateur("Admin", "Gestionnaire_du_temps_perdu@example.com", RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU);
                utilisateurRepository.save(admin);
            }
        };
    }
}
