package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class ParticipationDefi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime dateInscription;
    private int pointsGagnes;

    @Enumerated(EnumType.STRING)
    private StatutParticipationDefi statut;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private DefiProcrastination defi;
}

