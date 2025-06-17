package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.services;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.repositories.PiegeProductiviteRepository;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.PiegeProductivite;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PiegeProductiviteService {
    private final PiegeProductiviteRepository piegeProductiviteRepository;

    public PiegeProductiviteService(PiegeProductiviteRepository piegeProductiviteRepository) {
        this.piegeProductiviteRepository = piegeProductiviteRepository;
    }

    public Iterable<PiegeProductivite> getAllPiegeProductivite(){
        return piegeProductiviteRepository.findAll();
    }

    public PiegeProductivite creerPiegeProductivite(PiegeProductivite piegeProductivite){
        piegeProductivite.setDateCreation(LocalDateTime.now());
        return piegeProductiviteRepository.save(piegeProductivite);
    }



}
