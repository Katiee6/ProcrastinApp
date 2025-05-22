package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class ConfrontationPiege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime dateConfrontation;
    private boolean isSuccess;
    private int score;
    private String commentaire;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private PiegeProductivite piege;
}

