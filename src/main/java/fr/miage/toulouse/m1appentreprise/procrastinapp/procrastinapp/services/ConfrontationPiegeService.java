package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ForbiddenOperationException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des confrontations aux pièges.
 */
@Service
public class ConfrontationPiegeService {

    @Autowired
    private ConfrontationPiegeRepository confrontationPiegeRepository;
    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;
    @Autowired
    private RecompenseRepository recompenseRepository;
    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public ConfrontationPiegeService(ConfrontationPiegeRepository confrontationPiegeRepository, PiegeProductiviteRepository piegeProductiviteRepository, RecompenseRepository recompenseRepository, AttributionRecompenseRepository attributionRecompenseRepository, UtilisateurRepository utilisateurRepository) {
        this.confrontationPiegeRepository = confrontationPiegeRepository;
        this.piegeProductiviteRepository = piegeProductiviteRepository;
        this.recompenseRepository = recompenseRepository;
        this.attributionRecompenseRepository = attributionRecompenseRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupérer toutes les confrontations aux pièges enregistrées.
     * @return la liste complète des confrontations
     */
    public Iterable<ConfrontationPiege> getAllConfrontationPieges() {
        return confrontationPiegeRepository.findAll();
    }

    /**
     * Récupérer une confrontation spécifique par son identifiant.
     * @param id identifiant de la confrontation
     * @param utilisateur utilisateur effectuant la requête
     * @return la confrontation demandée
     * @throws ResourceNotFoundException si la confrontation n'existe pas
     * @throws ForbiddenOperationException si l'utilisateur n'a pas le droit d'y accéder
     */
    public ConfrontationPiege getConfrontationPiegeById(Long id, Utilisateur utilisateur) {
        ConfrontationPiege confrontation = confrontationPiegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La confrontation id " + id + " n'existe pas"));
        // Un procrastinateur en herbe ne peut accéder qu'à ses confrontations
        if (utilisateur.getRole() == RoleUtilisateur.PROCRASTINATEUR_EN_HERBE
                && !utilisateur.equals(confrontation.getUtilisateur())) {
            throw new ForbiddenOperationException("Accès refusé à la confrontation id " + id);
        }
        return confrontation;
    }

    /**
     * Récupérer toutes les confrontations associées à un piège donné.
     * @param piegeId identifiant du piège concerné
     * @return la liste des confrontations associées à ce piège
     */
    public Iterable<ConfrontationPiege> getAllConfrontationsAuPiege(Long piegeId) {
        return confrontationPiegeRepository.findConfrontationPiegesByPiege_Id(piegeId);
    }

    /**
     * Récupérer toutes les confrontations d’un utilisateur.
     * @param utilisateur utilisateur concerné
     * @return la liste de ses confrontations aux pièges
     */
    public Iterable<ConfrontationPiege> getAllConfrontationPiegesUtilisateur(Utilisateur utilisateur) {
        return confrontationPiegeRepository.findConfrontationPiegesByUtilisateur(utilisateur);
    }

    /**
     * Enregistrer une nouvelle confrontation d’un utilisateur face à un piège de productivité.
     * En cas d’échec, attribue un badge "Procrastinateur en danger" pour 7 jours et retire 50 points.
     * En cas de succès, ajoute 50 points.
     * @param confrontationPiege les informations de la confrontation à créer
     * @param utilisateur l'utilisateur concerné
     * @return la confrontation enregistrée
     * @throws InvalidRequestException si l’identifiant du piège n’est pas renseigné
     * @throws ResourceNotFoundException si le piège ou la récompense n’existe pas
     */
    public ConfrontationPiege creerConfrontationPiege(ConfrontationPiege confrontationPiege, Utilisateur utilisateur) {
        // Récupérer le piege correspondant à l'id renseigné
        Long piegeId = confrontationPiege.getPiege().getId();
        if (piegeId == null) {
            throw new InvalidRequestException("Id du piege non renseigné");
        }
        PiegeProductivite piege = piegeProductiviteRepository.findById(piegeId)
                .orElseThrow(() -> new ResourceNotFoundException("Le piège id " + piegeId + " n'existe pas"));

        int pointsAAjouter = 0;

        if (!confrontationPiege.isSucces()) {
            List<Recompense> recompense = recompenseRepository.findRecompenseByTitreAndType("Procrastinateur en danger", TypeRecompense.BADGE);
            if (recompense.isEmpty()) {
                throw new ResourceNotFoundException("La récompense \"Procrastinateur en danger\" n'a pas été trouvée");
            }
            Recompense recompenseAAttribuer = recompense.get(0);
            AttributionRecompense attributionRecompense = new AttributionRecompense(LocalDateTime.now(), LocalDateTime.now().plusDays(7),
                    "Echec confrontation piège " + piegeId, true, utilisateur, recompenseAAttribuer);
            attributionRecompenseRepository.save(attributionRecompense);
            pointsAAjouter = -50;
        } else {
            pointsAAjouter = 50;
        }

        // Modification des points totaux de l'utilisateur
        int pointsAvant = utilisateur.getPointAccumules();
        utilisateur.setPointAccumules(pointsAvant + pointsAAjouter);
        utilisateurRepository.save(utilisateur);

        confrontationPiege.setUtilisateur(utilisateur);
        confrontationPiege.setPiege(piege);
        confrontationPiege.setPoints(pointsAAjouter);

        return confrontationPiegeRepository.save(confrontationPiege);
    }

    /**
     * Supprimer une confrontation a un piège.
     * @param idConfrontationPiege identifiant de la confrontation
     * @param utilisateur utilisateur effectuant l'action
     */
    public void supprimerConfrontationPiege(Long idConfrontationPiege, Utilisateur utilisateur) {
        // Récupérer la confrontation (vérifie si elle existe et si l'utilisateur peut y accéder)
        ConfrontationPiege confrontationPiege = getConfrontationPiegeById(idConfrontationPiege, utilisateur);
        Long piegeId = confrontationPiege.getPiege().getId();
        confrontationPiegeRepository.delete(confrontationPiege);
        int pointsAAjouter;
        if (confrontationPiege.isSucces()) {
            // on enlève les points ajoutés précédemment
            pointsAAjouter = -50;
        } else {
            // on ajoute les points enlevés précédemment
            pointsAAjouter = 50;
            // on retire le badge précédemment attribué
            List<AttributionRecompense> attributionRecompense = attributionRecompenseRepository
                    .findAttributionRecompenseByContexteAttribution("Echec confrontation piège " + piegeId);
            if (attributionRecompense.isEmpty()) {
                throw new ResourceNotFoundException("L'attribution de récompense n'a pas été trouvée");
            }
            AttributionRecompense attribution = attributionRecompense.get(0);
            attributionRecompenseRepository.delete(attribution);
        }
        int pointsAvant = utilisateur.getPointAccumules();
        utilisateur.setPointAccumules(pointsAvant + pointsAAjouter);
        utilisateurRepository.save(utilisateur);
    }

}
