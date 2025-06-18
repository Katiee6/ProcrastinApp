package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.*;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutAttributionRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions.ResourceNotFoundException;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des attributions de récompenses.
 */
@Service
public class AttributionRecompenseService {

    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RecompenseRepository recompenseRepository;

    public AttributionRecompenseService(AttributionRecompenseRepository attributionRecompenseRepository,
                                        UtilisateurRepository utilisateurRepository,
                                        RecompenseRepository recompenseRepository) {
        this.attributionRecompenseRepository = attributionRecompenseRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.recompenseRepository = recompenseRepository;
    }

    /**
     * Récupère toutes les attributions de récompenses.
     */
    public Iterable<AttributionRecompense> getAllAttributions() {
        return attributionRecompenseRepository.findAll();
    }

    /**
     * Récupère une attribution par son identifiant.
     */
    public AttributionRecompense getAttributionById(Long id) {
        return attributionRecompenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribution " + id + " non trouvée"));
    }

    /**
     * Récupère toutes les attributions d’un utilisateur donné.
     */
    public List<AttributionRecompense> getAttributionsByUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur " + utilisateurId + " non trouvé"));
        return attributionRecompenseRepository.findAttributionRecompensesByUtilisateur(utilisateur);
    }

    /**
     * Crée une nouvelle attribution de récompense.
     */
    public AttributionRecompense creerAttribution(Long utilisateurId, Long recompenseId, String contexte, int joursValidite) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur " + utilisateurId + " non trouvé"));
        Recompense recompense = recompenseRepository.findById(recompenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Récompense " + recompenseId + " non trouvée"));

        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(utilisateur);
        attribution.setRecompense(recompense);
        attribution.setContexteAttribution(contexte);
        attribution.setDateObtention(LocalDateTime.now());
        attribution.setDateExpiration(LocalDateTime.now().plusDays(joursValidite));
        attribution.setStatut(StatutAttributionRecompense.ACTIF);

        return attributionRecompenseRepository.save(attribution);
    }

    /**
     * Supprime une attribution existante.
     */
    public void supprimerAttribution(Long id) {
        AttributionRecompense attribution = attributionRecompenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribution " + id + " non trouvée"));
        attributionRecompenseRepository.delete(attribution);
    }

    /**
     * Désactive une attribution (ex: manuellement ou à expiration).
     */
    public AttributionRecompense desactiverAttribution(Long id) {
        AttributionRecompense attribution = attributionRecompenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribution " + id + " non trouvée"));
        attribution.setStatut(StatutAttributionRecompense.EXPIRE);
        return attributionRecompenseRepository.save(attribution);
    }
}
