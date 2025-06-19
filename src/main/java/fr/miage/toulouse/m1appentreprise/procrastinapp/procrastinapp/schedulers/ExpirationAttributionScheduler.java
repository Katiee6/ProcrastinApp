package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.schedulers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.AttributionRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutAttributionRecompense;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.AttributionRecompenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpirationAttributionScheduler {

    @Autowired
    private AttributionRecompenseRepository attributionRecompenseRepository;

    /**
     * Tâche planifiée qui s'exécute tous les jours à 00h00 pour désactiver les attributions expirées.
     */
    @Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
    public void desactiverAttributionsExpirees() {
        List<AttributionRecompense> actives = attributionRecompenseRepository.findByStatut(StatutAttributionRecompense.ACTIF);

        for (AttributionRecompense attribution : actives) {
            if (attribution.getDateExpiration().isBefore(LocalDateTime.now())) {
                attribution.setStatut(StatutAttributionRecompense.EXPIRE);
                attributionRecompenseRepository.save(attribution);
            }
        }
    }
}
