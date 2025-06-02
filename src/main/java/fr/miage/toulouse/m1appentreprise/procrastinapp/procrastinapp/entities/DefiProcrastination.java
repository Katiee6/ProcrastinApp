package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.DifficulteDefiProcrastination;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
public class DefiProcrastination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titre;
    private String description;

    private float duree;

    @Enumerated(EnumType.STRING)
    private DifficulteDefiProcrastination difficulte;

    private int pointsAGagner;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    private boolean actif;

    @ManyToOne
    private Utilisateur createur;
}

