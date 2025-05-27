package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Créer un nouveau client
    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        Utilisateur util = utilisateurService.creerUtilisateur(utilisateur);
        return new ResponseEntity<>(util, HttpStatus.CREATED);
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) // Exemple d'autorisation pour un role
    public Iterable<Utilisateur> getAllUtilisateurs(@CurrentUser Utilisateur utilisateur){
        System.out.println("Utilisateur connecté : " + utilisateur.getPseudo());

        return utilisateurService.getAllUtilisateur();
    }
}