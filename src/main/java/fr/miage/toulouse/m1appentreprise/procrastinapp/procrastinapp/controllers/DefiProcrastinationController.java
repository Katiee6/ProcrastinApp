package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.DefiProcrastinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les défis de procrastination.
 */
@RestController
@RequestMapping("/api/defis")
public class DefiProcrastinationController {

    @Autowired
    private DefiProcrastinationService defiProcrastinationService;

    /**
     * Récupérer tous les défis de procrastination.
     * @return la liste de tous les défis
     */
    @GetMapping
    public Iterable<DefiProcrastination> getDefis() {
        return defiProcrastinationService.getAllDefis();
    }

    /**
     * Récupérer tous les défis de procrastination actifs.
     * @return la liste des défis actifs uniquement
     */
    @GetMapping("/actifs")
    public Iterable<DefiProcrastination> getDefisActifs() {
        return defiProcrastinationService.getAllDefisActifs();
    }

    /**
     * Récupérer un défi à partir de son identifiant.
     * @param id identifiant du défi à récupérer
     * @return le défi correspondant à l'id
     */
    @GetMapping("/{idDefi}")
    public DefiProcrastination getDefi(@PathVariable("idDefi") Long id) {
        return defiProcrastinationService.getDefiById(id);
    }

    /**
     * Créer un nouveau défi de procrastination.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * JSON attendu : { "titre": "string", "description": "string", "duree": nombre (jours),
     *                  "difficulte": "FACILE" | "MOYEN" | "DIFFICILE", "pointsAGagner": entier,
     *                  "dateDebut": "yyyy-MM-dd", "dateFin": "yyyy-MM-dd" }
     * @param defi défi à créer (données JSON)
     * @param utilisateur utilisateur connecté
     * @return le défi créé
     */
    @PostMapping
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public DefiProcrastination createDefi(@RequestBody DefiProcrastination defi,
                                          @CurrentUser Utilisateur utilisateur) {
        return defiProcrastinationService.creerDefi(defi, utilisateur);
    }

    /**
     * Modifier un défi existant.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * JSON attendu (au moins l'id) : { "id": entier, "titre": "string", "description": "string", "duree": nombre (jours),
     *                                  "difficulte": "FACILE" | "MOYEN" | "DIFFICILE", "pointsAGagner": entier,
     *                                  "dateDebut": "yyyy-MM-dd", "dateFin": "yyyy-MM-dd" }
     * @param defi défi modifié (données JSON : que les élements à changer)
     * @return le défi mis à jour
     */
    @PutMapping
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public DefiProcrastination updateDefi(@RequestBody DefiProcrastination defi) {
        return defiProcrastinationService.modifierDefi(defi);
    }

    /**
     * Modifier le statut actif d’un défi.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * JSON attendu : { "id": entier, "actif": true | false }
     * @param defi défi avec nouveau statut
     * @return le défi mis à jour
     */
    @PutMapping("/statut")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public DefiProcrastination updateDefiStatus(@RequestBody DefiProcrastination defi) {
        return defiProcrastinationService.modifierStatutDefi(defi);
    }

    /**
     * Supprimer un défi par son identifiant.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * @param id identifiant du défi à supprimer
     */
    @DeleteMapping("/{idDefi}")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public void deleteDefi(@PathVariable("idDefi") long id) {
        defiProcrastinationService.supprimerDefi(id);
    }

}
