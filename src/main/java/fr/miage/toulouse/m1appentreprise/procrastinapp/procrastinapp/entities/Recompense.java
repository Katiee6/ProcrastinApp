package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titre;
    private String description;
    private int conditionObtention;

    private int niveauPrestige;

    @Enumerated(EnumType.STRING)
    private TypeRecompense type;
}

