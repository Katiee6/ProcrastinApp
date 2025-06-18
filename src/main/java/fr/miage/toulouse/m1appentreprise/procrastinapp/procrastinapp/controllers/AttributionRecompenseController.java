package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.AttributionRecompenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les attributions de récompenses.
 */
@RestController
@RequestMapping("/api/attributions-recompenses")
public class AttributionRecompenseController {

    @Autowired
    private AttributionRecompenseService attributionRecompenseService;

    /**
     * Récupérer toutes les attributions de récompenses.
     * Réservé aux rôles ANTI_PROCRASTINATEUR_REPENTIS & GESTIONNAIRE_TEMPS_PERDU.
     */
    @GetMapping
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public Iterable<AttributionRecompense> getAllAttributions() {
        return attributionRecompenseService.getAllAttributions();
    }

    /**
     * Récupérer une attribution par son identifiant.
     * Accessible aux deux rôles.
     */
    @GetMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public AttributionRecompense getAttributionById(@PathVariable("id") Long id) {
        return attributionRecompenseService.getAttributionById(id);
    }

    /**
     * Récupérer les attributions liées à l'utilisateur connecté.
     */
    @GetMapping("/user")
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public List<AttributionRecompense> getAttributionsUtilisateur(@CurrentUser Utilisateur utilisateur) {
        return attributionRecompenseService.getAttributionsByUtilisateur(utilisateur.getId());
    }

    /**
     * Récupérer les attributions liées à un utilisateur.
     */
    @GetMapping("/user/{id}")
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public List<AttributionRecompense> getAttributionsByUtilisateur(@PathVariable("id") Long id) {
        return attributionRecompenseService.getAttributionsByUtilisateur(id);
    }

    /**
     * Créer une attribution de récompense.
     * JSON attendu : { "recompenseId": long, "contexte": "texte", "joursValidite": int }
     */
    @PostMapping
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public AttributionRecompense creerAttribution(@RequestBody AttributionRequest request,
                                                  @CurrentUser Utilisateur utilisateur) {
        return attributionRecompenseService.creerAttribution(
                utilisateur.getId(),
                request.recompenseId,
                request.contexte,
                request.joursValidite
        );
    }

    /**
     * Supprimer une attribution par son id.
     */
    @DeleteMapping("/{id}")
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public void supprimerAttribution(@PathVariable("id") Long id) {
        attributionRecompenseService.supprimerAttribution(id);
    }

    /**
     * Désactiver manuellement une attribution.
     */
    @PatchMapping("/{id}/desactiver")
    @AllowedRoles(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS)
    public AttributionRecompense desactiverAttribution(@PathVariable("id") Long id) {
        return attributionRecompenseService.desactiverAttribution(id);
    }

    /**
     * Classe pour parser les données de création d’attribution.
     */
    public static class AttributionRequest {
        public Long recompenseId;
        public String contexte;
        public int joursValidite;
    }
}
