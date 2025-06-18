package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.CategorieExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ExcuseCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titre;
    private String videoResult;
    private LocalDateTime dateSoumission;

    @Enumerated(EnumType.STRING)
    private CategorieExcuse categorie;

    @Enumerated(EnumType.STRING)
    private StatutExcuse statut;

    @ManyToOne
    private Utilisateur auteur;
}
