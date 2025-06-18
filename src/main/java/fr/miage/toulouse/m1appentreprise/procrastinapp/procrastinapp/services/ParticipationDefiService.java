package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.ParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutParticipationDefi;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ConflictException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ForbiddenOperationException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.DefiProcrastinationRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.ParticipationDefiRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service pour la gestion des participations aux défis.
 */
@Service
public class ParticipationDefiService {

    @Autowired
    private ParticipationDefiRepository participationDefiRepository;
    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public ParticipationDefiService(ParticipationDefiRepository participationDefiRepository, DefiProcrastinationRepository defiProcrastinationRepository, UtilisateurRepository utilisateurRepository) {
        this.participationDefiRepository = participationDefiRepository;
        this.defiProcrastinationRepository = defiProcrastinationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }


    /**
     * Récupérer la liste de toutes les participations aux défis.
     * @return toutes les participations existantes
     */
    public Iterable<ParticipationDefi> getAllParticipationsDefis() {
        return participationDefiRepository.findAll();
    }

    /**
     * Récupérer toutes les participations d’un utilisateur.
     * @param utilisateur l’utilisateur concerné
     * @return la liste des participations de l’utilisateur
     */
    public Iterable<ParticipationDefi> getAllParticipationsDefisUtilisateur(Utilisateur utilisateur) {
        return participationDefiRepository.findParticipationDefiByUtilisateur(utilisateur);
    }

    /**
     * Récupérer toutes les participations associées à un défi.
     * @param defiId identifiant du défi
     * @return la liste des participations pour ce défi
     */
    public Iterable<ParticipationDefi> getAllParticipationsAuDefi(Long defiId) {
        return participationDefiRepository.findParticipationDefiByDefi_Id(defiId);
    }

    /**
     * Récupérer une participation à un défi par son identifiant.
     * @param id identifiant de la participation
     * @param utilisateur utilisateur effectuant la requête
     * @return la participation demandée
     * @throws ResourceNotFoundException si la participation n'existe pas
     * @throws ForbiddenOperationException si l'utilisateur n'a pas le droit d'y accéder
     */
    public ParticipationDefi getParticipationDefiById(Long id, Utilisateur utilisateur) {
        ParticipationDefi participation = participationDefiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La participation id " + id + " n'existe pas"));
        // Un procrastinateur en herbe ne peut accéder qu'à ses participations
        if (utilisateur.getRole() == RoleUtilisateur.PROCRASTINATEUR_EN_HERBE
                && !utilisateur.equals(participation.getUtilisateur())) {
            throw new ForbiddenOperationException("Accès refusé à la participation id " + id);
        }
        return participation;
    }

    /**
     * Inscrire un utilisateur à un défi.
     * Règles de validation : le défi doit exister et être actif, un utilisateur ne peut pas s'inscrire deux fois au même défi en cours,
     * un utilisateur ne peut participer qu’à 3 défis en même temps, un défi ne peut accueillir que 5 participants.
     * @param participationDefi les éléments de la participation à créer
     * @param utilisateur l'utilisateur à inscrire
     * @return la nouvelle participation enregistrée
     * @throws InvalidRequestException si l'identifiant du défi est manquant
     * @throws ResourceNotFoundException si le défi n'existe pas
     * @throws ForbiddenOperationException si le défi n'est pas actif
     * @throws ConflictException si les règles de participation sont violées
     */
    public ParticipationDefi creerParticipationDefi(ParticipationDefi participationDefi, Utilisateur utilisateur) {
        // Récupérer le défi correspondant à l'id renseigné
        Long defiId = participationDefi.getDefi().getId();
        if (defiId == null) {
            throw new InvalidRequestException("Id de la participation non renseigné");
        }
        DefiProcrastination defi = defiProcrastinationRepository.findById(defiId)
                .orElseThrow(() -> new ResourceNotFoundException("Le défi id " + defiId + " n'existe pas"));
        // Le défi doit être actif pour y participer
        if (!defi.isActif()) {
            throw new ForbiddenOperationException("Le défi id " + defiId + " n'est pas actif");
        }
        // On ne peut pas participer plusieurs fois en même temps au même défi
        boolean participeDeja = participationDefiRepository.existsParticipationDefiByDefi_IdAndStatutNot(defiId, StatutParticipationDefi.TERMINE);
        if (participeDeja) {
            throw new ConflictException("Participe deja à ce défi");
        }
        // On ne peut participer qu'à 3 défis en même temps
        int nbParticipationsDefisEnCours = participationDefiRepository.countParticipationDefisByUtilisateurAndStatutNot(utilisateur, StatutParticipationDefi.TERMINE);
        if (nbParticipationsDefisEnCours >= 3) {
            throw new ConflictException("L'utilisateur " + utilisateur.getId() + " est déja inscrit à 3 défis");
        }
        // Un défi ne peut accueillir que 5 participants
        int nbParticipantsDefi = participationDefiRepository.countParticipationDefisByDefi(defi);
        if (nbParticipantsDefi >= 5) {
            throw new ConflictException("Le defi id " + defiId + " a déja 5 participants");
        }
        // Créer la participation
        participationDefi.setDateInscription(LocalDateTime.now());
        participationDefi.setDefi(defi);
        participationDefi.setUtilisateur(utilisateur);
        participationDefi.setPointsGagnes(0); // 0 points à l'inscription : ils sont ajoutés ensuite
        // Statut inscrit ou en cours selon la date de début
        if (LocalDate.now().isBefore(participationDefi.getDefi().getDateDebut())) {
            participationDefi.setStatut(StatutParticipationDefi.INSCRIT);
        } else {
            participationDefi.setStatut(StatutParticipationDefi.EN_COURS);
        }
        return participationDefiRepository.save(participationDefi);
    }


    /**
     * Démarrer les participations dont la date de début est atteinte.
     * Passe le statut de INSCRIT à EN_COURS si la date de début du défi est aujourd'hui ou déjà passée.
     */
    public void commencerLesParticipationsDefis() {
        participationDefiRepository.findParticipationDefiByStatutAndDefi_DateDebutLessThanEqual(
                StatutParticipationDefi.INSCRIT, LocalDate.now())
                .forEach(participationDefi -> {
                    participationDefi.setStatut(StatutParticipationDefi.EN_COURS);
                    participationDefiRepository.save(participationDefi);
                });
    }


    /**
     * Terminer une participation à un défi.
     * L'utilisateur peut déclarer sa participation comme terminée avec un certain nombre de points gagnés.
     * Le nombre de points est limité par le maximum possible du défi.
     * @param participationDefi les informations à mettre à jour (id + points gagnés)
     * @param utilisateur l'utilisateur effectuant l'action
     * @return la participation mise à jour
     * @throws InvalidRequestException si l'identifiant est manquant
     * @throws ResourceNotFoundException si la participation n'existe pas
     * @throws ForbiddenOperationException si l'utilisateur ne peut pas modifier cette participation
     */
    public ParticipationDefi terminerParticipationDefi(ParticipationDefi participationDefi, Utilisateur utilisateur) {
        Long id = participationDefi.getId();
        if (id == null) {
            throw new InvalidRequestException("Id de la participation non renseigné");
        }
        ParticipationDefi participation = participationDefiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La participation id " + id + " n'existe pas"));
        // Un procrastinateur en herbe ne peut agir que sur ses participations
        if (utilisateur.getRole() == RoleUtilisateur.PROCRASTINATEUR_EN_HERBE
                && !utilisateur.equals(participation.getUtilisateur())) {
            throw new ForbiddenOperationException("Interdit");
        }
        // On ne peut terminer une participation qu'une seule fois
        if (participation.getStatut().equals(StatutParticipationDefi.TERMINE)) {
            throw new ConflictException("La participation id " + id + " est déja terminée");
        }
        // Attribution des points : ceux renseignés ou le max possible
        int points = participationDefi.getPointsGagnes(); // points renseignés json
        int pointsMax = participation.getDefi().getPointsAGagner(); // points possibles pour ce défi
        int pointsGagnes = Math.min(points, pointsMax); // points renseignés ou max
        participation.setPointsGagnes(pointsGagnes);
        participation.setStatut(StatutParticipationDefi.TERMINE); // participation terminée

        // Modification des points totaux de l'utilisateur
        int pointsAvant = utilisateur.getPointAccumules();
        utilisateur.setPointAccumules(pointsAvant + pointsGagnes);
        utilisateurRepository.save(utilisateur);

        return participationDefiRepository.save(participation);
    }

}
