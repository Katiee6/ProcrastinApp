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
     * Autorisé uniquement pour les PROCRASTINATEUR_EN_HERBE (exemple de restriction).
     * @param utilisateur utilisateur connecté
     * @return liste des utilisateurs
     */
    @GetMapping
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public Iterable<Utilisateur> getAllUtilisateurs(@CurrentUser Utilisateur utilisateur) {
        System.out.println("Utilisateur connecté : " + utilisateur.getPseudo());
        return utilisateurService.getAllUtilisateur();
    }

    /**
     * Récupérer un utilisateur par son identifiant.
     * @param id identifiant de l'utilisateur
     * @return utilisateur correspondant
     */
    @GetMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
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
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public Utilisateur updateUtilisateur(@PathVariable("id") Long id,
                                         @RequestBody Utilisateur utilisateurModifie) {
        return utilisateurService.modifierUtilisateur(id, utilisateurModifie);
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