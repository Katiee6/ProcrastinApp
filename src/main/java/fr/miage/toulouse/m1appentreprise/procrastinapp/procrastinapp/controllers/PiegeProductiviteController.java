package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.PiegeProductiviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les pièges de productivité.
 */
@RestController
@RequestMapping("/api/pieges-productivite")
public class PiegeProductiviteController {

    @Autowired
    private PiegeProductiviteService piegeProductiviteService;

    /**
     * Récupérer tous les pièges de productivité.
     * Accessible sans rôle spécifique (ou à restreindre si besoin).
     * @return liste des pièges
     */
    @GetMapping
    public Iterable<PiegeProductivite> getAllPiegeProductivite() {
        return piegeProductiviteService.getAllPiegeProductivite();
    }

    /**
     * Récupérer tous les pièges créés par l'utilisateur connecté.
     * @param utilisateur utilisateur connecté
     * @return liste des pièges créés par l'utilisateur
     */
    @GetMapping("/user")
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public Iterable<PiegeProductivite> getPiegeProductiviteUtilisateur(@CurrentUser Utilisateur utilisateur) {
        return piegeProductiviteService.getAllPiegeProductiviteUtilisateur(utilisateur);
    }

    /**
     * Récupérer un piège de productivité par son identifiant.
     * @param id identifiant du piège
     * @return piège correspondant
     */
    @GetMapping("/{id}")
    public PiegeProductivite getPiegeProductivite(@PathVariable("id") Long id) {
        return piegeProductiviteService.getPiegeProductiviteById(id);
    }

    /**
     * Créer un nouveau piège de productivité.
     * @param piegeProductivite données du piège
     * @param utilisateur utilisateur connecté
     * @return piège créé
     */
    @PostMapping
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public ResponseEntity<PiegeProductivite> createPiegeProductivite(@RequestBody PiegeProductivite piegeProductivite,
                                                                     @CurrentUser Utilisateur utilisateur) {
        piegeProductivite.setCreateur(utilisateur);
        PiegeProductivite created = piegeProductiviteService.creerPiegeProductivite(piegeProductivite);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Modifier un piège existant.
     * @param id identifiant du piège à modifier
     * @param piegeModifie nouvelles données
     * @return piège modifié
     */
    @PutMapping("/{id}")
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public PiegeProductivite updatePiegeProductivite(@PathVariable("id") Long id,
                                                     @RequestBody PiegeProductivite piegeModifie) {
        return piegeProductiviteService.modifierPiegeProductivite(id, piegeModifie);
    }

    /**
     * Supprimer un piège de productivité.
     * @param id identifiant du piège à supprimer
     * @return réponse HTTP
     */
    @DeleteMapping("/{id}")
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public ResponseEntity<Void> deletePiegeProductivite(@PathVariable("id") Long id) {
        piegeProductiviteService.supprimerPiegeProductivite(id);
        return ResponseEntity.noContent().build();
    }
}