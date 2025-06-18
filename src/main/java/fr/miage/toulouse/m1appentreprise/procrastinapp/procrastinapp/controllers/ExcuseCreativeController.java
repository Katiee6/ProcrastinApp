package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;


import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ExcuseCreative;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.ExcuseCreativeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les excuses créatives.
 * Permet aux utilisateurs de créer, voter, modérer et récupérer des excuses créatives.
 */
@RestController
@RequestMapping("/api/excuse-creative")
public class ExcuseCreativeController {

    @Autowired
    private ExcuseCreativeService excuseCreativeService;


    /**
     * Récupérer toutes les excuses créatives.
     * L'utilisateur doit avoir le rôle PROCRASTINATEUR_EN_HERBE, ANTI_PROCRASTINATEUR_REPENTIS ou GESTIONNAIRE_TEMPS_PERDU.
     * @return la liste de toutes les excuses créatives
     */
    @GetMapping("")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<Iterable<ExcuseCreative>> getAllExcuses() {
        return ResponseEntity.ok(excuseCreativeService.getAllExcusesCreatives());
    }

    /**
     * Récupérer une excuse créative par son id.
     * L'utilisateur doit avoir le rôle PROCRASTINATEUR_EN_HERBE, ANTI_PROCRASTINATEUR_REPENTIS ou GESTIONNAIRE_TEMPS_PERDU.
     * @param id identifiant de l'excuse
     * @return l'excuse trouvée
     */
    @GetMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<ExcuseCreative> getExcuseById(@PathVariable Long id) {
        return ResponseEntity.ok(excuseCreativeService.getExcuseCreativeById(id));
    }


    /**
     * Créer une nouvelle excuse créative.
     * L'utilisateur doit avoir le rôle PROCRASTINATEUR_EN_HERBE.
     * @param excuse l'excuse à créer
     * @param utilisateur l'utilisateur connecté
     * @return l'excuse créée
     */
    @PostMapping("")
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public ResponseEntity<?> creerExcuseCreative(@RequestBody ExcuseCreative excuse,
                                                              @CurrentUser Utilisateur utilisateur) {
        ExcuseCreative created = excuseCreativeService.creerExcuseCreative(excuse, utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    /**
     * Voter pour une excuse créative.
     * L'utilisateur doit avoir le rôle PROCRASTINATEUR_EN_HERBE.
     * @param id id de l'excuse
     * @param utilisateur utilisateur connecté
     * @param newVote nouveau vote
     * @return excuse mise à jour avec votes
     */
    @PostMapping("/{id}/voter")
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public ResponseEntity<?> voterExcuseCreative(@PathVariable Long id, @CurrentUser Utilisateur utilisateur, @RequestParam int newVote) {
        try {
            ExcuseCreative excuse = excuseCreativeService.voterExcuseCreative(id, utilisateur.getId(), newVote);
            return ResponseEntity.ok(excuse);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Modérer une excuse créative (changer son statut).
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * @param id id de l'excuse
     * @return excuse modérée
     */
    @PostMapping("/{id}/moderation")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public ResponseEntity<?> modererExcuseCreative(@PathVariable Long id) {
        try {
            ExcuseCreative excuse = excuseCreativeService.modererExcuseCreative(id);
            return ResponseEntity.ok(excuse);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Modifier une excuse créative.
     * L'utilisateur doit avoir le rôle ANTI_PROCRASTINATEUR_REPENTIS ou GESTIONNAIRE_TEMPS_PERDU.
     * @param id identifiant de l'excuse à modifier
     * @param excuseCreative les nouvelles données de l'excuse
     * @return l'excuse modifiée
     */
    @PutMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<ExcuseCreative> modifierExcuseCreative(@PathVariable Long id,
                                                       @RequestBody ExcuseCreative excuseCreative) {
        ExcuseCreative updated = excuseCreativeService.modifierExcuseCreative(id, excuseCreative);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer une excuse créative.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * @param id identifiant de l'excuse à supprimer
     * @return réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<Void> supprimerExcuse(@PathVariable Long id) {
        excuseCreativeService.supprimerExcuseCreative(id);
        return ResponseEntity.noContent().build();
    }

}
