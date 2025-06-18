package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutAttributionRecompense;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attribution-recompense")
public class AttributionRecompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateObtention;
    private LocalDateTime dateExpiration;

    private String contexteAttribution;
    private StatutAttributionRecompense statut;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Recompense recompense;
}

