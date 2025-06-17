package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.DifficulteDefiProcrastination;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DefiProcrastination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private DifficulteDefiProcrastination difficulte;

    private String contenu;
    private LocalDate dateDebut;
    private boolean isActif;

    @ManyToOne
    private Utilisateur createur;
}

