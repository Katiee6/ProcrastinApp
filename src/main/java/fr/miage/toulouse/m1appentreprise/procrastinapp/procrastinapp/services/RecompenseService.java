package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Recompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.InvalidRequestException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.RecompenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/* *
 * Service de gestion des récompenses
 */
@Service
public class RecompenseService {
    @Autowired
    private RecompenseRepository recompenseRepository;

    public RecompenseService(RecompenseRepository recompenseRepository) {
        this.recompenseRepository = recompenseRepository;
    }

    /* * Créer une nouvelle récompense
        * @param recompense la récompense à créer
        * @return la récompense créée
        * @throws InvalidRequestException si les données de la récompense sont invalides
     */
    public Recompense creerRecompense(Recompense recompense) {
        validerRecompense(recompense);
        return recompenseRepository.save(recompense);
    }

    /* * Lire une récompense par son id
     * @param id l'identifiant de la récompense
     * @return la récompense correspondante
     */
    public Recompense getRecompenseById(Long id) {
        return recompenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Récompense non trouvée avec l'identifiant : " + id));
    }

    /* * Lire toutes les récompenses
     * @return une liste de toutes les récompenses
     */
    public List<Recompense> getAllRecompenses() {
        return (List<Recompense>) recompenseRepository.findAll();
    }

    /* * Modifier une récompense existante
     * @param id l'identifiant de la récompense à modifier
     * @param nouvelleRecompense les nouvelles données de la récompense
     * @return la récompense modifiée
     */
    public Recompense modifierRecompense(Long id, Recompense nouvelleRecompense) {
        Recompense ancienne = getRecompenseById(id);
        validerRecompense(nouvelleRecompense);

        ancienne.setTitre(nouvelleRecompense.getTitre());
        ancienne.setDescription(nouvelleRecompense.getDescription());
        ancienne.setConditionsObtention(nouvelleRecompense.getConditionsObtention());
        ancienne.setNiveauPrestige(nouvelleRecompense.getNiveauPrestige());
        ancienne.setType(nouvelleRecompense.getType());

        return recompenseRepository.save(ancienne);
    }

    /* * Supprimer une récompense par son id
     * @param id l'identifiant de la récompense à supprimer
     */
    public void supprimerRecompense(Long id) {
        Recompense recompense = getRecompenseById(id);
        recompenseRepository.delete(recompense);
    }

    /* * Valider les données d'une récompense
     * @param recompense la récompense à valider
     * @throws InvalidRequestException si les données sont invalides
     */
    private void validerRecompense(Recompense recompense) {
        if (recompense.getTitre() == null || recompense.getTitre().isBlank()) {
            throw new InvalidRequestException("Le titre de la récompense ne peut pas être vide");
        }
        if (recompense.getDescription() == null || recompense.getDescription().isBlank()) {
            throw new InvalidRequestException("La description de la récompense ne peut pas être vide");
        }
        if (recompense.getConditionsObtention() == null || recompense.getConditionsObtention().isBlank()) {
            throw new InvalidRequestException("Les conditions d'obtention de la récompense ne peuvent pas être vides");
        }
        if (recompense.getNiveauPrestige() < 0) {
            throw new InvalidRequestException("Le niveau de prestige doit être positif");
        }
        if (recompense.getType() == null) {
            throw new InvalidRequestException("Le type de récompense doit être renseigné");
        }
    }
}
