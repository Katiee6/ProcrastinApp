package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.StatutTacheAEviter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tache_a_eviter")
public class TacheAEviter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private int degreUrgence;

    private LocalDateTime dateLimite;

    private String consequencesPotentielles;

    @Enumerated(EnumType.STRING)
    private StatutTacheAEviter statut;

    private LocalDateTime dateCreation;

    private LocalDateTime dateFin;

    @ManyToOne
    private Utilisateur utilisateur;

    // setter pour mettre à jour la date de fin lors de la modification du statut
    public void setStatut(StatutTacheAEviter nouveauStatut) {
        this.statut = nouveauStatut;

        if (nouveauStatut == StatutTacheAEviter.EVITEE_AVEC_SUCCES
                || nouveauStatut == StatutTacheAEviter.REALISEE_EN_EXTREMIS
                || nouveauStatut == StatutTacheAEviter.CATASTROPHE) {

            this.dateFin = LocalDateTime.now();
        }
    }

}

