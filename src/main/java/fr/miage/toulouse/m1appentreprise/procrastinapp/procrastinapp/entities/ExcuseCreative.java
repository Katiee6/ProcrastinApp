package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.CategorieExcuse;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutExcuse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "excuse_creative")
public class ExcuseCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texteExcuse;
    private String situationApp;
    private int votesRecus;
    private LocalDateTime dateSoumission;

    @Enumerated(EnumType.STRING)
    private CategorieExcuse categorie;

    @Enumerated(EnumType.STRING)
    private StatutExcuse statut;

    @ManyToOne
    private Utilisateur auteur;

}
