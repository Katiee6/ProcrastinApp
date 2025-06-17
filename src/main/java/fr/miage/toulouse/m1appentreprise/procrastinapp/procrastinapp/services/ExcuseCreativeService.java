package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ExcuseCreative;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.CategorieExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.ExcuseCreativeRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Service pour la gestion des excuses créatives.
 */
@Service
public class ExcuseCreativeService {

    @Autowired
    private ExcuseCreativeRepository excuseCreativeRepository;
    @Autowired
    private TacheAEviterService tacheAEviterService;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Récupérer toutes les excuses créatives.
     * @return la liste de toutes les excuses créatives
     */
    public Iterable<ExcuseCreative> getAllExcusesCreatives() {
        return excuseCreativeRepository.findAll();
    }

    /**
     * Récupérer une excuse créative par son id.
     * @param id identifiant de l'excuse créative recherchée
     * @return l'excuse créative correspondante
     */
    public ExcuseCreative getExcuseCreativeById(Long id) {
        ExcuseCreative excuse = excuseCreativeRepository.findById(id)
                .orElse(null); // Utiliser orElse(null) pour éviter l'exception si non trouvé
        if( excuse == null){
            throw new ResourceNotFoundException("Il n'existe pas d'excuse créative avec id " + id);
        }
        return  excuse;
    }

    /**
        * Créer une nouvelle excuse créative.
     * @return l'excuse créative créée
     */
    public ExcuseCreative creerExcuseCreative(ExcuseCreative excuse, Utilisateur utilisateur) {

        //empêcher la création d'une excuse vide
        if (excuse.getTexteExcuse() == null || excuse.getTexteExcuse().isBlank()) {
            throw new InvalidRequestException("Le texte de l'excuse est obligatoire");
        }
        // Vérifier que la catégorie est valide
        if (excuse.getCategorie() == null || !CategorieExcuse.isValid(excuse.getCategorie())) {
            throw new InvalidRequestException("La catégorie de l'excuse est obligatoire et doit être valide");
        }
        // Vérifier que la situation est renseignée
        if (excuse.getSituationApp() == null || excuse.getSituationApp().isBlank()) {
            throw new InvalidRequestException("La situation de l'application est obligatoire");
        }
        // Vérifier que l'auteur de l'excuse existe
        if (utilisateur == null || utilisateur.getId() == null) {
            throw new ResourceNotFoundException("L'utilisateur auteur de l'excuse n'existe pas");
        }

        // Vérifier si une excuse avec le même texte existe déjà
        if (excuseCreativeRepository.existsByTexteExcuse(excuse.getTexteExcuse())) {
            throw new IllegalArgumentException("Une excuse avec ce texte existe déjà.");
        }

        // Initialiser données
        excuse.setVotesRecus(0); // Initialiser les votes recus à 0
        excuse.setDateSoumission(LocalDateTime.now());
        excuse.setStatut(StatutExcuse.EN_ATTENTE); // en attente de modération
        excuse.setAuteur(utilisateur); // Associer l'auteur de l'excuse

        return excuseCreativeRepository.save(excuse);
    }


    /**
     * Voter pour une excuse créative.
     * @param id identifiant de l'excuse à voter
     * @param userId identifiant de l'utilisateur votant
     * @param newVote le nouveau vote
     * @return l'excuse créative mise à jour avec le nouveau vote
     * @throws ResourceNotFoundException si l'excuse ou l'utilisateur n'existe pas
     */
    public ExcuseCreative voterExcuseCreative(Long id, Long userId, int newVote) {
       // Vérifier si l'utilisateur existe
        Utilisateur auteur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur id " + userId + " non trouvé"));

        // Vérifier si l'excuse existe et est créée par l'utilisateur
        ExcuseCreative excuse = excuseCreativeRepository.findByIdAndAuteur(id, auteur);
        if( excuse == null) {
                throw new ResourceNotFoundException("ExcuseCreative id " + id + " non trouvée ou non créée par l'utilisateur");
        }

        //mise à jour des votes
        if (newVote < 0) {
            throw new IllegalArgumentException("Le vote ne doit pas être négatif.");
        }
        else{
            excuse.setVotesRecus(newVote);
        }
        return excuseCreativeRepository.save(excuse);
    }


    /**
     * Modérer une excuse créative.
     * Change le statut de l'excuse en fonction de l'éligibilité de l'utilisateur.
     * @param idExcuse identifiant de l'excuse à modérer
     * @return l'excuse modérée
     * @throws ResourceNotFoundException si l'excuse n'existe pas ou est déjà modérée
     */
    public ExcuseCreative modererExcuseCreative(Long idExcuse) {
        // Vérifier si l'excuse existe et est en attente de modération
        ExcuseCreative excuse = excuseCreativeRepository.findByIdAndStatut(idExcuse, StatutExcuse.EN_ATTENTE);

        if( excuse == null)
            throw new ResourceNotFoundException("Excuse non trouvée ou déjà modérée.");

        // Vérifier l'auteur de l'excuse
        Long userId = excuse.getAuteur() != null ? excuse.getAuteur().getId() : null;
        if (userId == null) {
            throw new ResourceNotFoundException("Auteur de l'excuse introuvable.");
        }

        // Vérifier l'éligibilité de l'utilisateur
        boolean estEligible = tacheAEviterService.verifierUtilisateurEligibiliteModerer(userId);

        // Mettre à jour le statut de l'excuse
        excuse.setStatut(estEligible ? StatutExcuse.APPROUVEE : StatutExcuse.REJETEE);

        return excuseCreativeRepository.save(excuse);
    }


    /**
     * Mettre à jour une excuse créative.
     * Ne modifie que certains champs (texte, situation, catégorie) et ignore les votes et le statut.
     * @param id identifiant de l'excuse à mettre à jour
     * @param excuseCreative les nouvelles données de l'excuse
     * @return l'excuse mise à jour
     */
    public ExcuseCreative modifierExcuseCreative(Long id, ExcuseCreative excuseCreative) {
        ExcuseCreative existing = getExcuseCreativeById(id);

        // Mettre à jour uniquement certains champs (texte, situation, catégorie)
        existing.setTexteExcuse(excuseCreative.getTexteExcuse());
        existing.setSituationApp(excuseCreative.getSituationApp());
        existing.setCategorie(excuseCreative.getCategorie());
        // Ne pas modifier les votes ou le statut
        return excuseCreativeRepository.save(existing);
    }

    /**
     * Supprimer une excuse créative par son ID.
     * @param id identifiant de l'excuse à supprimer
     * @throws ResourceNotFoundException si l'excuse n'existe pas
     */
    public void supprimerExcuseCreative(Long id) {
        ExcuseCreative excuse = getExcuseCreativeById(id);
        excuseCreativeRepository.delete(excuse);
    }
}
