package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.schedulers;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.DefiProcrastination;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.DefiProcrastinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpirationDefisScheduler {

    @Autowired
    private DefiProcrastinationRepository defiProcrastinationRepository;

    /**
     * Désactiver les défis dont la date de fin est dépassée.
     */
    @Scheduled(cron = "0 0 1 * * *") // Tous les jours à 1h du matin
    public void desactiverDefisExpires() {
        Iterable<DefiProcrastination> defisActifs = defiProcrastinationRepository.findDefiProcrastinationByActif(true);
        LocalDate aujourdHui = LocalDate.now();

        for (DefiProcrastination defi : defisActifs) {
            if (defi.getDateFin().isBefore(aujourdHui)) {
                defi.setActif(false);
                defiProcrastinationRepository.save(defi);
            }
        }
    }
}
