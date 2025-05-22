package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import jakarta.persistence.*;
import java.time.LocalTime;


@Entity
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private int conditionObtention;

    @Enumerated(EnumType.STRING)
    private TypeRecompense type;
}

