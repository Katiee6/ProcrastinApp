package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TacheAEviter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private int degreGravite;
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    private StatutTacheAEviter statut;

    private int consequenceEchec;
    private LocalDateTime dateCreation;

    @ManyToOne
    private Utilisateur utilisateur;
}

