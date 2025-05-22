package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;
import java.time.LocalTime;


@Entity
public class AttributionRecompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime dateObtention;
    private String contexteAttribution;
    private boolean isActif;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Recompense recompense;
}

