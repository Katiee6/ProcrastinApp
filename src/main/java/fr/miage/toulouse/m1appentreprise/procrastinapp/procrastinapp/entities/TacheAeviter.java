package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class TacheAEviter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private int degreGravite;
    private LocalTime dateLimite;

    @Enumerated(EnumType.STRING)
    private StatutTacheAEviter statut;

    private int consequenceEchec;
    private LocalDate dateCreation;

    @ManyToOne
    private Utilisateur utilisateur;
}

