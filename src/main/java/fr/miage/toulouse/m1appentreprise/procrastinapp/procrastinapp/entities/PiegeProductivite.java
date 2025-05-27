package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypePiege;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "piege_productivite")
public class PiegeProductivite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private TypePiege type;

    private int niveauDifficulte;
    private int recompenseResistance;
    private int consequenceEchec;
    private LocalTime dateCreation;

    @ManyToOne
    @JsonBackReference
    private Utilisateur createur;

    public PiegeProductivite(String titre,
                             String description,
                             TypePiege type,
                             int niveauDifficulte,
                             int recompenseResistance,
                             int consequenceEchec,
                             LocalTime dateCreation,
                             Utilisateur createur) {
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.niveauDifficulte = niveauDifficulte;
        this.recompenseResistance = recompenseResistance;
        this.consequenceEchec = consequenceEchec;
        this.dateCreation = dateCreation;
        this.createur = createur;
    }
}
