package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Service pour la gestion des pièges de productivité.
 */
@Service
public class PiegeProductiviteService {

    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;
    @Autowired
    private ConfrontationPiegeRepository confrontationPiegeRepository;

    /**
     * Récupère tous les pièges de productivité existants en base.
     * @return une liste complète de tous les pièges enregistrés
     */
    public Iterable<PiegeProductivite> getAllPiegeProductivite(){
        return piegeProductiviteRepository.findAll();
    }

    /**
     * Récupère tous les pièges créés par un utilisateur spécifique.
     * @param utilisateur l'utilisateur concerné
     * @return la liste des pièges créés par cet utilisateur
     */
    public Iterable<PiegeProductivite> getAllPiegeProductiviteUtilisateur(Utilisateur utilisateur) {
        return piegeProductiviteRepository.findPiegeProductiviteByCreateur(utilisateur);
    }

    /**
     * Récupère un piège de productivité par son identifiant.
     * @param id identifiant du piège
     * @return le piège correspondant
     * @throws ResourceNotFoundException si aucun piège n'existe avec cet id
     */
    public PiegeProductivite getPiegeProductiviteById(Long id){
        return piegeProductiviteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Il n'existe pas de piège de productivité avec id " + id));
    }

    /**
     * Crée un nouveau piège de productivité après vérification de son unicité.
     * Deux pièges identiques (titre, description, type...) pour un même créateur sont interdits.
     * @param piegeProductivite le piège à créer
     * @return le piège nouvellement enregistré
     * @throws ConflictException si un piège identique existe déjà
     */
    public PiegeProductivite creerPiegeProductivite(PiegeProductivite piegeProductivite){
        if(existeDeja(piegeProductivite)){
            throw new ConflictException("Ce piège existe déjà");
        }

        piegeProductivite.setDateCreation(LocalDateTime.now());
        return piegeProductiviteRepository.save(piegeProductivite);
    }

    /**
     * Modifie un piège de productivité existant après validation.
     * Vérifie l’unicité des champs principaux à l’exception du piège en cours de modification.
     * @param id identifiant du piège à modifier
     * @param piegeModifie les nouvelles données à enregistrer
     * @return le piège modifié
     * @throws ConflictException si un autre piège identique existe déjà
     */
    public PiegeProductivite modifierPiegeProductivite(Long id, PiegeProductivite piegeModifie) {
        getPiegeProductiviteById(id); // vérifie qu’il existe
        piegeModifie.setId(id); // s’assurer que l’ID est bien défini

        if (existeDejaPourModification(piegeModifie)) {
            throw new ConflictException("Un autre piège identique existe déjà !");
        }

        return piegeProductiviteRepository.save(piegeModifie);
    }

    /**
     * Supprime un piège de productivité uniquement s’il n’est associé à aucune confrontation.
     * @param id identifiant du piège à supprimer
     * @throws ResourceNotFoundException si le piège n'existe pas
     * @throws ConflictException si le piège est déjà utilisé dans une ou plusieurs confrontations
     */
    public void supprimerPiegeProductivite(Long id) {
        PiegeProductivite piegeProductivite = piegeProductiviteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun piège de productivité trouvé avec l'id " + id));

        int nb = confrontationPiegeRepository.countConfrontationPiegeByPiege(piegeProductivite);
        if (nb > 0) {
            throw new ConflictException("Ce piège a " + nb + " confrontation, il ne peut pas être supprimé");
        } else {
            piegeProductiviteRepository.delete(piegeProductivite);
        }
    }

    /*******************
        Private Method
     ********************/

    /**
     * Vérifie si un piège identique (mêmes champs) existe déjà en base pour le même utilisateur.
     * @param piege le piège à vérifier
     * @return true si un piège identique existe, false sinon
     */
    private boolean existeDeja(PiegeProductivite piege) {
        return piegeProductiviteRepository.existsPiegeProductiviteByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateur(
                piege.getTitre(), piege.getDescription(), piege.getType(),
                piege.getNiveauDifficulte(), piege.getRecompenseResistance(),
                piege.getConsequenceEchec(), piege.getCreateur()
        );
    }

    /**
     * Vérifie si un autre piège identique existe déjà, en excluant le piège en cours de modification.
     * Utile lors de l'édition pour éviter les doublons involontaires.
     * @param piege le piège à comparer
     * @return true si un piège identique (autre que celui en cours) existe déjà, false sinon
     */
    private boolean existeDejaPourModification(PiegeProductivite piege) {
        return piegeProductiviteRepository.existsByTitreAndDescriptionAndTypeAndNiveauDifficulteAndRecompenseResistanceAndConsequenceEchecAndCreateurAndIdNot(
                piege.getTitre(), piege.getDescription(), piege.getType(),
                piege.getNiveauDifficulte(), piege.getRecompenseResistance(),
                piege.getConsequenceEchec(), piege.getCreateur(),
                piege.getId() // important : exclure l'élément en cours
        );
    }

}
