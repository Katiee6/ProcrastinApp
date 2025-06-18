package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ConfrontationPiege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate dateConfrontation;
    private boolean succes;
    private int points;

    private String commentaire;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private PiegeProductivite piege;
}

