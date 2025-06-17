package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ConfrontationPiege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dateConfrontation;
    private boolean isSuccess;
    private int score;
    private String commentaire;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private PiegeProductivite piege;
}

