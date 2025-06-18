package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ConflictException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.DefiProcrastinationRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.ParticipationDefiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service pour la gestion des défis de procrastination.
 */
@Service
public class DefiProcrastinationService {

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;
    @Autowired
    private ParticipationDefiRepository participationDefiRepository;

    public DefiProcrastinationService(DefiProcrastinationRepository defiProcrastinationRepository, ParticipationDefiRepository participationDefiRepository) {
        this.defiProcrastinationRepository = defiProcrastinationRepository;
        this.participationDefiRepository = participationDefiRepository;
    }

    /**
     * Récupérer tous les défis de procrastination.
     * @return la liste de tous les défis
     */
    public Iterable<DefiProcrastination> getAllDefis() {
        return defiProcrastinationRepository.findAll();
    }

    /**
     * Récupérer tous les défis de procrastination actifs (ouverts à l'inscription).
     * @return la liste des défis actifs
     */
    public Iterable<DefiProcrastination> getAllDefisActifs() {
        return defiProcrastinationRepository.findDefiProcrastinationByActif(true);
    }

    /**
     * Récupérer un défi par son identifiant.
     * @param id identifiant du défi recherché
     * @return le défi correspondant
     * @throws ResourceNotFoundException si aucun défi n'est trouvé
     */
    public DefiProcrastination getDefiById(Long id) {
        return defiProcrastinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Il n'existe pas de défi avec id " + id));
    }

    /**
     * Créer un nouveau défi de procrastination.
     * @param defi le défi à créer
     * @param utilisateur l'utilisateur créateur du défi
     * @return le défi créé
     * @throws ConflictException si un défi identique existe déjà
     * @throws InvalidRequestException si les dates sont incohérentes
     */
    public DefiProcrastination creerDefi(DefiProcrastination defi, Utilisateur utilisateur) {
        // Vérification de la non existence de ce défi
        boolean defiExisteDeja = defiProcrastinationRepository.existsDefiProcrastinationByTitreAndDescriptionAndDureeAndDifficulteAndPointsAGagnerAndDateDebutAndDateFin(
                defi.getTitre(), defi.getDescription(), defi.getDuree(), defi.getDifficulte(), defi.getPointsAGagner(), defi.getDateDebut(), defi.getDateFin());
        if (defiExisteDeja) {
            throw new ConflictException("Ce défi existe déjà");
        }
        // Vérification de la cohérence des dates renseignées
        if (defi.getDateFin().isBefore(defi.getDateDebut())) {
            throw new InvalidRequestException("La date de fin ne doit pas être avant la date de début");
        }
        if (defi.getDateFin().isBefore(LocalDate.now())) {
            throw new InvalidRequestException("La date de fin ne doit pas être avant aujourd'hui");
        }
        // Mettre le statut actif et ajouter le créateur
        defi.setActif(true); // actif = il est possible de s'inscrire
        defi.setCreateur(utilisateur);
        return defiProcrastinationRepository.save(defi);
    }

    /**
     * Modifier les informations d'un défi de procrastination.
     * Le statut (actif/terminé) ne peut pas être modifié ici.
     * @param defi le défi contenant les nouvelles informations
     * @return le défi modifié
     * @throws InvalidRequestException si l'identifiant est manquant ou si les dates sont incohérentes
     */
    public DefiProcrastination modifierDefi(DefiProcrastination defi) {
        Long id = defi.getId();
        if (id == null) {
            throw new InvalidRequestException("Id du défi non renseigné");
        }
        DefiProcrastination defiModif = getDefiById(id); // les infos actuelles du défi à modifier
        boolean dateChanged = false;

        if (defi.getTitre() != null) {
            defiModif.setTitre(defi.getTitre());
        }
        if (defi.getDescription() != null) {
            defiModif.setDescription(defi.getDescription());
        }
        if (defi.getDuree() != 0) {
            defiModif.setDuree(defi.getDuree());
        }
        if (defi.getDifficulte() != null) {
            defiModif.setDifficulte(defi.getDifficulte());
        }
        if (defi.getPointsAGagner() != 0) {
            defiModif.setPointsAGagner(defi.getPointsAGagner());
        }
        if (defi.getDateDebut() != null) {
            defiModif.setDateDebut(defi.getDateDebut());
            dateChanged = true;
        }
        if (defi.getDateFin() != null) {
            defiModif.setDateFin(defi.getDateFin());
            dateChanged = true;
        }
        if (dateChanged && (defiModif.getDateFin().isBefore(defiModif.getDateDebut()) ||
                defiModif.getDateFin().isBefore(LocalDate.now()))) {
            throw new InvalidRequestException("Dates incohérentes : défi non modifié");
        }
        return defiProcrastinationRepository.save(defiModif);
    }

    /**
     * Modifier le statut d'un défi (actif ou terminé).
     * @param defi le défi dont on veut changer le statut (doit contenir l'id et le nouveau statut).
     * @return le défi avec le nouveau statut
     * @throws InvalidRequestException si l'identifiant est manquant
     */
    public DefiProcrastination modifierStatutDefi(DefiProcrastination defi) {
        Long id = defi.getId();
        if (id == null) {
            throw new InvalidRequestException("Id du défi non renseigné");
        }
        DefiProcrastination defiModif = getDefiById(id);
        defiModif.setActif(defi.isActif());
        return defiProcrastinationRepository.save(defiModif);
    }

    /**
     * Supprimer un défi de procrastination.
     * Un défi ne peut pas être supprimé s'il a déjà des participations.
     * @param defiId identifiant du défi à supprimer
     * @throws ConflictException si le défi a des participations existantes
     */
    public void supprimerDefi(Long defiId) {
        DefiProcrastination defiASuppr = getDefiById(defiId);
        int nb = participationDefiRepository.countParticipationDefisByDefi(defiASuppr);
        if (nb > 0) {
            throw new ConflictException("Ce défi a " + nb + " participations, il ne peut pas être supprimé");
        } else {
            defiProcrastinationRepository.delete(defiASuppr);
        }
    }

}
