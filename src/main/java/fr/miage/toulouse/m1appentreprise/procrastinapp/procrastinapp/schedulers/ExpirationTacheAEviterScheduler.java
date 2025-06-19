package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.schedulers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.TacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.TacheAEviterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpirationTacheAEviterScheduler {

    @Autowired
    private TacheAEviterRepository tacheAEviterRepository;

    /**
     * Tâche planifiée qui s'exécute tous les jours à 01h00
     * et met les tâches en attente dont la date limite est dépassée en CATASTROPHE.
     */
    @Scheduled(cron = "0 0 1 * * *") // Tous les jours à 01h00
    public void verifierTachesDepassees() {
        List<TacheAEviter> enAttente = tacheAEviterRepository.findByStatut(StatutTacheAEviter.EN_ATTENTE);

        LocalDateTime maintenant = LocalDateTime.now();

        for (TacheAEviter tache : enAttente) {
            if (tache.getDateLimite() != null && tache.getDateLimite().isBefore(maintenant)) {
                tache.setStatut(StatutTacheAEviter.CATASTROPHE);
                tacheAEviterRepository.save(tache);
            }
        }
    }
}
