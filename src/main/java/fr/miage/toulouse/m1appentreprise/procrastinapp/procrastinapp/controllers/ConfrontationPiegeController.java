package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ConfrontationPiege;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.ConfrontationPiegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les confrontations aux pièges de productivité.
 */
@RestController
@RequestMapping("/api/confrontation-pieges")
public class ConfrontationPiegeController {

    @Autowired
    private ConfrontationPiegeService confrontationPiegeService;

    /**
     * Récupérer toutes les confrontations aux pièges.
     * L'utilisateur doit avoir le rôle ANTI_PROCRASTINATEUR_REPENTIS.
     * @return liste des confrontations enregistrées
     */
    @GetMapping
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public Iterable<ConfrontationPiege> getConfrontationPieges() {
        return confrontationPiegeService.getAllConfrontationPieges();
    }

    /**
     * Récupérer une confrontation par son identifiant.
     * L'utilisateur doit avoir le rôle ANTI_PROCRASTINATEUR_REPENTIS ou être lié à cette confrontation.
     * @param id identifiant de la confrontation
     * @param utilisateur utilisateur connecté
     * @return la confrontation correspondante
     */
    @GetMapping("/{idConfrontationPiege}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS})
    public ConfrontationPiege getConfrontationPiegeById(@PathVariable("idConfrontationPiege") long id,
                                                        @CurrentUser Utilisateur utilisateur) {
        return confrontationPiegeService.getConfrontationPiegeById(id, utilisateur);
    }

    /**
     * Récupérer toutes les confrontations associées à un piège donné.
     * L'utilisateur doit avoir le rôle ANTI_PROCRASTINATEUR_REPENTIS.
     * @param id identifiant du piège
     * @return liste des confrontations au piège spécifié
     */
    @GetMapping("piege/{piegeId}")
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public Iterable<ConfrontationPiege> getConfrontationsPiegesPiege(@PathVariable("piegeId") long id) {
        return confrontationPiegeService.getAllConfrontationsAuPiege(id);
    }

    /**
     * Récupérer toutes les confrontations de l'utilisateur connecté.
     * @param utilisateur utilisateur connecté
     * @return liste des confrontations de l'utilisateur
     */
    @GetMapping("/user")
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public Iterable<ConfrontationPiege> getConfrontationsPiegesUtilisateur(@CurrentUser Utilisateur utilisateur) {
        return confrontationPiegeService.getAllConfrontationPiegesUtilisateur(utilisateur);
    }

    /**
     * Créer une nouvelle confrontation à un piège.
     * JSON attendu : { "piege": { "id": entier }, "dateConfrontation": "aaaa-MM-dd",
     *                  "succes": true | false, "commentaire": "texte optionnel" }
     * @param confrontationPiege données de la confrontation
     * @param utilisateur utilisateur connecté
     * @return confrontation créée et enregistrée
     */
    @PostMapping
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public ConfrontationPiege createConfrontationPiege(@RequestBody ConfrontationPiege confrontationPiege,
                                                       @CurrentUser Utilisateur utilisateur) {
        return confrontationPiegeService.creerConfrontationPiege(confrontationPiege, utilisateur);
    }

    /**
     * Supprimer une confrontation à un piège.
     * L'utilisateur doit être lié à cette confrontation.
     * @param id identifiant de la confrontation à supprimer
     * @param utilisateur utilisateur connecté
     */
    @DeleteMapping("/{idConfrontationPiege}")
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public void deleteConfrontationPiege(@PathVariable("idConfrontationPiege") long id,
                                         @CurrentUser Utilisateur utilisateur) {
        confrontationPiegeService.supprimerConfrontationPiege(id, utilisateur);
    }

}
