package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;

import java.time.LocalTime;


@Entity
public class DefiProcrastination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private DifficulteDefiProcrastination difficulte;

    private String contenu;
    private LocalTime dateDebut;
    private boolean isActif;

    @ManyToOne
    private Utilisateur createur;
}

