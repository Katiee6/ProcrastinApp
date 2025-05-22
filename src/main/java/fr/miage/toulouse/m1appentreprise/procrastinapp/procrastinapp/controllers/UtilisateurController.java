package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



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

}