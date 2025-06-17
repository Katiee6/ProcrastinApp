package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des utilisateurs.
 */
@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PiegeProductiviteRepository piegeProductiviteRepository;

    @Autowired
    private ConfrontationPiegeRepository confrontationPiegeRepository;

    @Autowired
    private ExcuseCreativeRepository excuseCreativeRepository;

    @Autowired
    private ParticipationDefiRepository participationDefiRepository;

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;

    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;

    @Autowired
    private TacheAEviterRepository tacheAEviterRepository;

    /**
     * Récupérer tous les utilisateurs enregistrés.
     * @return une liste de tous les utilisateurs
     */
    public Iterable<Utilisateur> getAllUtilisateur() {
        return utilisateurRepository.findAll();
    }

    /**
     * Récupérer un utilisateur par son identifiant.
     * @param id identifiant de l'utilisateur
     * @return l'utilisateur correspondant
     * @throws ResourceNotFoundException si aucun utilisateur n'est trouvé
     */
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun utilisateur trouvé avec l'id " + id));
    }

    /**
     * Créer un nouvel utilisateur : Procrastinateur_en_herbe.
     * Un utilisateur est unique par son pseudo et son adresse mail.
     * @param utilisateur les informations du nouvel utilisateur
     * @return l'utilisateur enregistré
     * @throws ConflictException si un utilisateur identique existe déjà
     */
    public Utilisateur creerUtilisateurProcrastinateurEnHerbe(Utilisateur utilisateur) {
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);

        if (existeDeja(utilisateur)) {
            throw new ConflictException("Cet utilisateur existe déjà.");
        }

        utilisateur.setDateInscription(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Créer un nouvel utilisateur : Anti_procrastinateur_repentis.
     * Un utilisateur est unique par son pseudo et son adresse mail.
     * @param utilisateur les informations du nouvel utilisateur
     * @return l'utilisateur enregistré
     * @throws ConflictException si un utilisateur identique existe déjà
     */
    public Utilisateur creerUtilisateurAnti_procrastinateurRepentis(Utilisateur utilisateur) {
        utilisateur.setRole(RoleUtilisateur.ANTI_PROCRASTINATEUR_REPENTIS);

        if (existeDeja(utilisateur)) {
            throw new ConflictException("Cet utilisateur existe déjà.");
        }

        utilisateur.setDateInscription(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Modifier les informations d’un utilisateur existant.
     * @param id identifiant de l'utilisateur à modifier
     * @param utilisateurModifie les nouvelles informations à enregistrer
     * @return l'utilisateur mis à jour
     * @throws ConflictException si un autre utilisateur possède déjà ces informations
     */
    public Utilisateur modifierUtilisateur(Long id, Utilisateur utilisateurModifie) {
        getUtilisateurById(id); // Vérifie qu’il existe
        utilisateurModifie.setId(id);

        if (existeDejaPourModification(utilisateurModifie)) {
            throw new ConflictException("Un autre utilisateur avec les mêmes informations existe déjà.");
        }

        return utilisateurRepository.save(utilisateurModifie);
    }

    /**
     * Supprimer un utilisateur de la plateforme.
     * La suppression est refusée si l'utilisateur est encore lié à des entités existantes (pièges, défis, etc.).
     * @param id identifiant de l'utilisateur à supprimer
     * @throws ConflictException si des dépendances empêchent la suppression
     */
    public void supprimerUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun utilisateur trouvé avec l'id " + id));

        List<String> entitesList = getEntitesList(utilisateur);

        if (!entitesList.isEmpty()) {
            throw new ConflictException("Impossible de supprimer l'utilisateur : il est encore lié à " +
                    String.join(", ", entitesList));
        }

        utilisateurRepository.delete(utilisateur);
    }

    /*******************
     Méthodes privées
     ********************/

    /**
     * Vérifie si un utilisateur existe déjà (même pseudo + adresse mail).
     * @param utilisateur l'utilisateur à vérifier
     * @return vrai si un utilisateur identique existe
     */
    private boolean existeDeja(Utilisateur utilisateur) {
        return utilisateurRepository.existsByPseudoAndAdresseMail(
                utilisateur.getPseudo(),
                utilisateur.getAdresseMail()
        );
    }

    /**
     * Vérifie si un autre utilisateur (hors celui en cours de modification) a les mêmes informations.
     * @param utilisateur l'utilisateur modifié
     * @return vrai si un doublon existe
     */
    private boolean existeDejaPourModification(Utilisateur utilisateur) {
        return utilisateurRepository.existsByPseudoAndAdresseMailAndIdIsNot(
                utilisateur.getPseudo(),
                utilisateur.getAdresseMail(),
                utilisateur.getId()
        );
    }

    /**
     * Vérifie les entités liées à un utilisateur pour empêcher sa suppression s’il reste des dépendances.
     * @param utilisateur utilisateur à inspecter
     * @return liste des entités liées sous forme de chaînes
     */
    private List<String> getEntitesList(Utilisateur utilisateur) {
        List<String> entites = new ArrayList<>();

        if (piegeProductiviteRepository.existsByCreateur(utilisateur)) {
            entites.add("des pièges de productivité");
        }

        if (confrontationPiegeRepository.existsByUtilisateur(utilisateur)) {
            entites.add("des confrontations de piège");
        }

        if (excuseCreativeRepository.existsByAuteur(utilisateur)) {
            entites.add("des excuses créatives");
        }

        if (participationDefiRepository.existsByUtilisateur(utilisateur)) {
            entites.add("des participations");
        }

        if (defiProcrastinationRepository.existsByCreateur(utilisateur)) {
            entites.add("des defis");
        }

        if (attributionRecompenseRepository.existsByUtilisateur(utilisateur)) {
            entites.add("des récompenses");
        }

        if (tacheAEviterRepository.existsByUtilisateur(utilisateur)) {
            entites.add("des taches à éviter");
        }

        return entites;
    }
}