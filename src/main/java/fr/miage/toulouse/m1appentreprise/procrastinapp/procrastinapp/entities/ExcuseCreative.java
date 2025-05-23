package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.CategorieExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class ExcuseCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String videoResult;
    private LocalTime dateSoumission;

    @Enumerated(EnumType.STRING)
    private CategorieExcuse categorie;

    @Enumerated(EnumType.STRING)
    private StatutExcuse statut;

    @ManyToOne
    private Utilisateur auteur;
}
