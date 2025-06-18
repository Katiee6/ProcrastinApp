package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class AttributionRecompense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dateObtention;
    private LocalDateTime dateExpiration;

    private String contexteAttribution;
    private boolean actif;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Recompense recompense;

    public AttributionRecompense(LocalDateTime dateObtention, LocalDateTime dateExpiration, String contexteAttribution,
                                 boolean actif, Utilisateur utilisateur, Recompense recompenseAAttribuer) {
        this.dateObtention = dateObtention;
        this.dateExpiration = dateExpiration;
        this.contexteAttribution = contexteAttribution;
        this.actif = actif;
        this.utilisateur = utilisateur;
        this.recompense = recompenseAAttribuer;
    }
}

