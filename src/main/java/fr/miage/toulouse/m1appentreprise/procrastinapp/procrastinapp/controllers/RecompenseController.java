package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Recompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.RecompenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recompense")
public class RecompenseController {

    @Autowired
    private RecompenseService recompenseService;

    /*Récupérer une récompense par son ID
        * @param id L'identifiant de la récompense
        * @return ResponseEntity<Recompense> La récompense correspondante
        * @return 200 OK avec la récompense trouvée
        * @return 403 Forbidden si l'utilisateur n'a pas le rôle requis
        * @return 404 Not Found si la récompense n'existe pas
     */
    @GetMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<Recompense> getRecompenseById(@PathVariable Long id) {
        return ResponseEntity.ok(recompenseService.getRecompenseById(id));
    }

    /*Récupérer toutes les récompenses
        * @return ResponseEntity<List<Recompense>> La liste de toutes les récompenses
        * @return 200 OK avec la liste des récompenses
        * @return 403 Forbidden si l'utilisateur n'a pas le rôle requis
     */
    @GetMapping("")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<List<Recompense>> getAllRecompenses() {
        return ResponseEntity.ok(recompenseService.getAllRecompenses());
    }

    /*Créer une nouvelle récompense
        * @param recompense Les données de la récompense à créer
        * @return ResponseEntity<Recompense> La récompense créée
     */
    @PostMapping
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<Recompense> creerRecompense(@RequestBody Recompense recompense) {
        Recompense created = recompenseService.creerRecompense(recompense);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /*Modifier une récompense existante
        * @param id L'identifiant de la récompense à modifier
        * @param recompense Les nouvelles données de la récompense
        * @return ResponseEntity<Recompense> La récompense modifiée
     */
    @PutMapping("/{id}")
    @AllowedRoles({RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ResponseEntity<Recompense> modifierRecompense(@PathVariable Long id, @RequestBody Recompense recompense) {
        return ResponseEntity.ok(recompenseService.modifierRecompense(id, recompense));
    }

    /* * Supprimer une récompense
       * @param id L'identifiant de la récompense à supprimer
       * @return ResponseEntity<Void> Réponse HTTP sans contenu
     */
    @DeleteMapping("/{id}")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public ResponseEntity<Void> supprimerRecompense(@PathVariable Long id) {
        recompenseService.supprimerRecompense(id);
        return ResponseEntity.noContent().build();
    }
}