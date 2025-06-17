package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.TacheAEviterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/* *
    Service pour gérer les tâches à éviter.
 **/
@Service
public class TacheAEviterService {
    @Autowired
    private TacheAEviterRepository tacheAEviterRepository;

    public TacheAEviterService(TacheAEviterRepository tacheAEviterRepository) {
        this.tacheAEviterRepository = tacheAEviterRepository;
    }

    /** Récupérer toutes les tâches à éviter d'un utilisateur
     * @param utilisateur l'utilisateur dont on veut récupérer les tâches
     * @return la liste des tâches à éviter de l'utilisateur
     */
    public List<TacheAEviter> findAllByUtilisateur(Utilisateur utilisateur) {
        return tacheAEviterRepository.findByUtilisateur(utilisateur);
    }

    /** Valider une tâche à éviter
     * @param tache la tâche à éviter à valider
     * @throws InvalidRequestException si la tâche n'est pas valide
     */
    private void validerTacheAEviter(TacheAEviter tache) {
        if (tache.getDescription() == null || tache.getDescription().isBlank()) {
            throw new InvalidRequestException("La description de la tâche ne peut pas être vide");
        }
        if (tache.getDegreUrgence() < 1 || tache.getDegreUrgence() > 5) {
            throw new InvalidRequestException("Le degré d'urgence doit être compris entre 1 et 5");
        }
        if (tache.getDateLimite() != null && tache.getDateLimite().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("La date limite doit être dans le futur");
        }
        if (tache.getConsequencesPotentielles() == null || tache.getConsequencesPotentielles().isBlank()) {
            throw new InvalidRequestException("Les conséquences potentielles doivent être renseignées");
        }
    }

    /** Créer une nouvelle tâche à éviter
     * @param tache la tâche à éviter à créer
     * @param utilisateur l'utilisateur qui crée la tâche
     * @return la tâche créée
     */
    public TacheAEviter creerTacheAEviter(TacheAEviter tache, Utilisateur utilisateur) {
        // Vérifier que l'utilisateur n'est pas null
        if (utilisateur == null) {
            throw new InvalidRequestException("L'utilisateur ne peut pas être nul");
        }

        // Vérifier que la validite de la tâche avant de l'enregistrer
        validerTacheAEviter(tache);

        tache.setUtilisateur(utilisateur);
        tache.setStatut(StatutTacheAEviter.EN_ATTENTE); //
        tache.setDateCreation(LocalDateTime.now());

        // Enregistrer la tâche
        return tacheAEviterRepository.save(tache);
    }

    /** READ - une tâche par son identifiant
     * @param id identifiant de la tâche à récupérer
     * @return la tâche correspondante
     */
    public TacheAEviter getTacheById(Long id) {
        TacheAEviter tache = tacheAEviterRepository.findById(id)
                .orElse(null); // Utiliser orElse(null) pour éviter l'exception si non trouvé
        if( tache == null) {
            throw new RuntimeException("Tâche non trouvée avec l'identifiant : " + id);
        }
        return tache;
    }


    /** modifier une tâche par son identifiant
     * @param id identifiant de la tâche à modifier
     * @param nouvelleTache nouvelle tâche avec les valeurs mises à jour
     * @return la tâche mise à jour
     */
    public TacheAEviter modifierTacheAEviter(Long id, TacheAEviter nouvelleTache) {
        TacheAEviter ancienne = getTacheById(id);
        validerTacheAEviter(nouvelleTache);

        // Mettre à jour les champs de l'ancienne tâche avec les valeurs de la nouvelle tâche
        ancienne.setDescription(nouvelleTache.getDescription());
        ancienne.setDegreUrgence(nouvelleTache.getDegreUrgence());
        ancienne.setDateLimite(nouvelleTache.getDateLimite());
        ancienne.setStatut(nouvelleTache.getStatut());
        ancienne.setConsequencesPotentielles(nouvelleTache.getConsequencesPotentielles());
        return tacheAEviterRepository.save(ancienne);
    }

    /** supprimer une tâche par son identifiant
     * @param id identifiant de la tâche à supprimer
     */
    public void supprimerTacheAEviter(Long id) {
        TacheAEviter tache = getTacheById(id);
        tacheAEviterRepository.delete(tache);
    }

    /** Calculer les points gagnés pour une tâche évitée avec succès
     * @param tache la tâche à éviter pour laquelle on calcule les points
     * @return le nombre de points gagnés
     * */
    public int calculerPoints(TacheAEviter tache) {
        if (tache.getStatut() != StatutTacheAEviter.EVITEE_AVEC_SUCCES){
            throw new IllegalArgumentException("La tâche doit être évitée avec succès pour calculer les points.");
        }
        // Calculer les points basés sur le degré d'urgence et les jours
        long joursRetard = ChronoUnit.DAYS.between(tache.getDateLimite(), tache.getDateFin());
        joursRetard = Math.max(0, joursRetard); // Ne pas pénaliser si la tâche est terminée avant la date limite

        int points = (tache.getDegreUrgence() * 10) + (int)(joursRetard * 5);
        return Math.min(points, 200);
    }

    // Calculer le total des points d’un utilisateur
    public int calculerTotalPoints(Utilisateur utilisateur) {
        List<TacheAEviter> taches = tacheAEviterRepository.findByUtilisateur(utilisateur);
        return taches.stream()
                .filter(t -> t.getStatut() == StatutTacheAEviter.EVITEE_AVEC_SUCCES)
                .mapToInt(this::calculerPoints)
                .sum();
    }


    /** Vérifier si un utilisateur est éligible pour modérer les excuses créatives.
     * Un utilisateur est éligible s'il a au moins une tâche évitée avec succès ou une catastrophe.
     * @param utilisateurId l'identifiant de l'utilisateur à vérifier
     * @return true si l'utilisateur est éligible, false sinon
     */
    public boolean verifierUtilisateurEligibiliteModerer(Long utilisateurId) {
        if (utilisateurId == null) {
            throw new InvalidRequestException("L'identifiant de l'utilisateur ne peut pas être nul");
        }
        return tacheAEviterRepository.existsByUtilisateurIdAndStatutIn(
                utilisateurId,
                List.of(StatutTacheAEviter.EVITEE_AVEC_SUCCES, StatutTacheAEviter.CATASTROPHE)
        );
    }
}
