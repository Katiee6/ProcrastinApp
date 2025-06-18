package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recompense")
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String conditionsObtention;

    private int niveauPrestige;

    @Enumerated(EnumType.STRING)
    private TypeRecompense type;
}

