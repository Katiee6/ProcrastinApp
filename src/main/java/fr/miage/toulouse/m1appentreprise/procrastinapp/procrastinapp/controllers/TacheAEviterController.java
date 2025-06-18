package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.TacheAEviterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les tâches à éviter.
 * Les utilisateurs peuvent créer, modifier, supprimer et récupérer des tâches à éviter.
 */
@RestController
@RequestMapping("/api/tache-a-eviter")
public class TacheAEviterController {

    @Autowired
    private TacheAEviterService tacheAEviterService;
    
    /**
     * Récupérer une tâche à éviter par son ID.
     * @param id id de la tâche
     * @return la tâche trouvée
     */
    @GetMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<TacheAEviter> getTacheById(@PathVariable Long id) {
        return ResponseEntity.ok(tacheAEviterService.getTacheById(id));
    }

    /** Récupérer toutes les tâches à éviter pour l'utilisateur connecté.
     * L'utilisateur doit avoir le rôle PROCRASTINATEUR_EN_HERBE, ANTI_PROCRASTINATEUR_REPENTIS ou GESTIONNAIRE_TEMPS_PERDU.
     * @param utilisateur l'utilisateur connecté
     * @return la liste des tâches à éviter de l'utilisateur
     */
    @GetMapping("")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<List<TacheAEviter>> getTachesForUser(@CurrentUser Utilisateur utilisateur) {
        return ResponseEntity.ok(tacheAEviterService.findAllByUtilisateur(utilisateur));
    }

    /**
     * Creer une nouvelle tâche à éviter.
     * L'utilisateur doit avoir le rôle PROCRASTINATEUR_EN_HERBE.
     * @param tache la tâche à créer
     * @param utilisateur l'utilisateur connecté
     * @return la tâche créée
     */
    @PostMapping
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public ResponseEntity<TacheAEviter> creerTacheAEviter(@RequestBody TacheAEviter tache,
                                                           @CurrentUser Utilisateur utilisateur) {
        TacheAEviter created = tacheAEviterService.creerTacheAEviter(tache, utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Modifier une tâche existante.
     * L'utilisateur doit avoir le rôle ANTI_PROCRASTINATEUR_REPENTIS ou GESTIONNAIRE_TEMPS_PERDU.
     * @param id id de la tâche à modifier
     * @param tache nouvelles données
     * @return tâche mise à jour
     */
    @PutMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<TacheAEviter> modifierTacheAEviter(@PathVariable Long id, @RequestBody TacheAEviter tache) {
        return ResponseEntity.ok(tacheAEviterService.modifierTacheAEviter(id, tache));
    }

    /**
     * Supprimer une tâche à éviter.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * @param id id de la tâche
     * @return réponse vide avec code 204
     */
    @DeleteMapping("/{id}")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public ResponseEntity<Void> supprimerTacheAEviter(@PathVariable Long id) {
        tacheAEviterService.supprimerTacheAEviter(id);
        return ResponseEntity.noContent().build();
    }

    // Calculer les points d'une tâche individuelle
    @GetMapping("/{id}/points")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<?> getPointsPourTache(@PathVariable Long id) {
        TacheAEviter tache = tacheAEviterService.getTacheById(id);
        int points = tacheAEviterService.calculerPoints(tache);
        return ResponseEntity.ok(points);
    }

    // Calculer le total des points de l'utilisateur courant
    @GetMapping("/points/total")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<?> getTotalPointsUtilisateur(@CurrentUser Utilisateur utilisateur) {
        int totalPoints = tacheAEviterService.calculerTotalPoints(utilisateur);
        return ResponseEntity.ok(totalPoints);
    }
}

