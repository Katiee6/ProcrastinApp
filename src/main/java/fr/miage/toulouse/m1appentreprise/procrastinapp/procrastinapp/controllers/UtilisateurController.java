package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Créer un nouvel utilisateur.
     * JSON attendu : { "pseudo": "...", "adresseMail": "...", ... }
     * @param utilisateur données du nouvel utilisateur
     * @return utilisateur créé
     */
    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        Utilisateur util = utilisateurService.creerUtilisateurProcrastinateurEnHerbe(utilisateur);
        return new ResponseEntity<>(util, HttpStatus.CREATED);
    }

    /**
     * Créer un nouvel utilisateur.
     * JSON attendu : { "pseudo": "...", "adresseMail": "...", ... }
     * @param utilisateur données du nouvel utilisateur
     * @return utilisateur créé
     */
    @PostMapping("/Anti_procrastinateurRepentis")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public ResponseEntity<Utilisateur> createUtilisateurAnti_procrastinateurRepentis(@RequestBody Utilisateur utilisateur) {
        Utilisateur util = utilisateurService.creerUtilisateurAnti_procrastinateurRepentis(utilisateur);
        return new ResponseEntity<>(util, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les utilisateurs.
     * Autorisé uniquement pour les ANTI_PROCRASTINATEUR_REPENTIS & GESTIONNAIRE_TEMPS_PERDU.
     * @return liste des utilisateurs
     */
    @GetMapping
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public Iterable<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateur();
    }

    /**
     * Récupérer un utilisateur par son identifiant.
     * @param id identifiant de l'utilisateur
     * @return utilisateur correspondant
     */
    @GetMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public Utilisateur getUtilisateurById(@PathVariable("id") Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    /**
     * Modifier un utilisateur existant.
     * JSON attendu : utilisateur modifié
     * @param id identifiant de l'utilisateur à modifier
     * @param utilisateurModifie données modifiées
     * @return utilisateur modifié
     */
    @PutMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public Utilisateur updateUtilisateur(@PathVariable("id") Long id,
                                         @RequestBody Utilisateur utilisateurModifie,
                                         @CurrentUser Utilisateur currentUtilisateur) {
        return utilisateurService.modifierUtilisateur(id, utilisateurModifie, currentUtilisateur);
    }

    /**
     * Supprimer un utilisateur par son identifiant.
     * L'utilisateur ne doit pas être lié à d'autres entités (pièges, défis, etc.).
     * @param id identifiant de l'utilisateur à supprimer
     */
    @DeleteMapping("/{id}")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable("id") Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}