package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.UtilisateurRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    @Autowired
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur creerUtilisateur(Utilisateur utilisateur){

        System.out.println(utilisateur);

        //Utilisateur util2 = new Utilisateur("jpp","jean.pierre@mail.com", RoleUtilisateur.PROCRASTINATEUR_EN_HERBE, NiveauProcrastination.DEBUTANT, "Je suis JPP",  0);

        return utilisateurRepository.save(utilisateur);
    }
}
