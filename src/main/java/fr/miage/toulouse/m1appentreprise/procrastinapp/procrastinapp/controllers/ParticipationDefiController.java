package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.controllers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware.AllowedRoles;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers.CurrentUser;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services.ParticipationDefiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour gérer les participations aux défis de procrastination.
 */
@RestController
@RequestMapping("/api/participations-defis")
public class ParticipationDefiController {

    @Autowired
    private ParticipationDefiService participationDefiService;

    /**
     * Récupérer toutes les participations aux défis.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * @return liste des participations
     */
    @GetMapping
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public Iterable<ParticipationDefi> getParticipationsDefis() {
        return participationDefiService.getAllParticipationsDefis();
    }

    /**
     * Récupérer les participations de l'utilisateur connecté.
     * @param utilisateur utilisateur connecté
     * @return liste des participations de l'utilisateur
     */
    @GetMapping("user")
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public Iterable<ParticipationDefi> getParticipationsDefisUtilisateur(@CurrentUser Utilisateur utilisateur) {
        return participationDefiService.getAllParticipationsDefisUtilisateur(utilisateur);
    }

    /**
     * Récupérer toutes les participations pour un défi donné.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU.
     * @param id identifiant du défi
     * @return liste des participations au défi
     */
    @GetMapping("/defi/{defiId}")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public Iterable<ParticipationDefi> getParticipationsDefisDefi(@PathVariable("defiId") long id) {
        return participationDefiService.getAllParticipationsAuDefi(id);
    }

    /**
     * Récupérer une participation par son id.
     * L'utilisateur doit avoir le rôle GESTIONNAIRE_TEMPS_PERDU ou être lié à cette participation.
     * @param id identifiant de la participation
     * @param utilisateur utilisateur connecté
     * @return la participation demandée
     */
    @GetMapping("/{idParticipationDefi}")
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ParticipationDefi getParticipationDefi(@PathVariable("idParticipationDefi") long id,
                                                  @CurrentUser Utilisateur utilisateur) {
        return participationDefiService.getParticipationDefiById(id, utilisateur);
    }

    /**
     * Créer une nouvelle participation à un défi.
     * JSON attendu : { "defi": { "id": entier } }
     * @param participationDefi données de la participation (id du défi)
     * @param utilisateur utilisateur connecté
     * @return participation créée
     */
    @PostMapping
    @AllowedRoles(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE)
    public ParticipationDefi createParticipationDefi(@RequestBody ParticipationDefi participationDefi,
                                                     @CurrentUser Utilisateur utilisateur) {
        return participationDefiService.creerParticipationDefi(participationDefi, utilisateur);
    }

    /**
     * Commencer les participations aux défis dont la date de début est atteinte.
     */
    @PutMapping("/commencer")
    @AllowedRoles(RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU)
    public void startParticipationsDefis() {
        participationDefiService.commencerLesParticipationsDefis();
    }

    /**
     * Terminer une participation (mettre à jour avec points gagnés).
     * JSON attendu : { "id": entier, "pointsGagnes": entier }
     * @param participationDefi données mises à jour (id de la participation et points gagnés)
     * @param utilisateur utilisateur connecté
     * @return participation mise à jour
     */
    @PutMapping
    @AllowedRoles({RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, RoleUtilisateur.GESTIONNAIRE_TEMPS_PERDU})
    public ParticipationDefi endParticipationDefi(@RequestBody ParticipationDefi participationDefi,
                                                  @CurrentUser Utilisateur utilisateur) {
        return participationDefiService.terminerParticipationDefi(participationDefi, utilisateur);
    }

}
