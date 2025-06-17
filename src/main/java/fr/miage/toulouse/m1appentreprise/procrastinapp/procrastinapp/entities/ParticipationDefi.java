package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutParticipationDefi;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ParticipationDefi {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dateInscription;
    private int pointsGagnes;

    @Enumerated(EnumType.STRING)
    private StatutParticipationDefi statut;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private DefiProcrastination defi;
}

