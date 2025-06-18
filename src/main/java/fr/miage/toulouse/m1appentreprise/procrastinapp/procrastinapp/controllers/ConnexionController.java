package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer la connexion.
 */
@RestController
@RequestMapping("/api/connexion")
public class ConnexionController {

    /**
     * Vérifie si un utilisateur l'adresse mail fournis existe.
     * @param utilisateur Utilisateur contenant adresse mail
     * @return true si l'utilisateur existe, exception sinon
     */
    @PostMapping
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE,
            RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS,
            RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public Utilisateur verifierConnexion(@CurrentUser Utilisateur utilisateur) {
        // AllowRoles & CurrentUser return current user with header: X-User-Email
        return utilisateur;
    }
}
