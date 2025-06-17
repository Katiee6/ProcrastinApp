package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.PiegeProductiviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/piegeProductiviteControler")
public class PiegeProductiviteControler {

    @Autowired
    private PiegeProductiviteService piegeProductiviteService;

    // Créer un nouveau piege
    @PostMapping
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTI) // Exemple d'autorisation pour un role
    public ResponseEntity<PiegeProductivite> createPiegeProductivite(@RequestBody PiegeProductivite piegeProductivite,
                                                                     @CurrentUser Utilisateur utilisateur) {
        piegeProductivite.setCreateur(utilisateur);
        PiegeProductivite piege = piegeProductiviteService.creerPiegeProductivite(piegeProductivite);
        return new ResponseEntity<>(piege, HttpStatus.CREATED);
    }

    // Récupérer tous les PiegeProductivites
    @GetMapping
    // @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) // Exemple d'autorisation pour un role
    public Iterable<PiegeProductivite> getAllPiegeProductivite(){
        return piegeProductiviteService.getAllPiegeProductivite();
    }
}