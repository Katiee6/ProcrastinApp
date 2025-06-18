package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.TypeRecompense;
import jakarta.persistence.*;


@Entity
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titre;
    private String description;
    private int conditionObtention;

    @Enumerated(EnumType.STRING)
    private TypeRecompense type;
}

