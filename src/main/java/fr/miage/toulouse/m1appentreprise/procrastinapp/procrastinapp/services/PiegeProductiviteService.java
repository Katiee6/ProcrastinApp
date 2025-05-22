package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.PiegeProductiviteRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypePiege;
import org.springframework.stereotype.Service;

import java.time.LocalTime;


@Service
public class PiegeProductiviteService {
    private final PiegeProductiviteRepository piegeProductiviteRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PiegeProductiviteService(PiegeProductiviteRepository piegeProductiviteRepository, UtilisateurRepository utilisateurRepository) {
        this.piegeProductiviteRepository = piegeProductiviteRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public PiegeProductivite creerPiegeProductivite(String titre, String description, TypePiege type, String createurEmail, int niveau, int recompense, int consequence){
        Utilisateur createur = utilisateurRepository.findUtilisateurByAdresseMail(createurEmail);

        PiegeProductivite piegeProductivite = new PiegeProductivite(titre, description, type, niveau, recompense, consequence, LocalTime.now(), createur);

        return piegeProductiviteRepository.save(piegeProductivite);
    }
}
