package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String pseudo;
    private String adresseMail;

    @Enumerated(EnumType.STRING)
    private RoleUtilisateur role;

    @Enumerated(EnumType.STRING)
    private NiveauProcrastination niveauProcrastination;

    private String excusePreferee;
    private LocalDateTime dateInscription;
    private int pointAccumules;

    //@OneToMany(fetch = FetchType.LAZY)
    //@JsonIgnore
    //private List<PiegeProductivite> piegeProductivites;

    public Utilisateur(String pseudo,
                       String adresseMail,
                       RoleUtilisateur role,
                       NiveauProcrastination niveauProcrastination,
                       String excusePreferee,
                       int pointAccumules) {
        this.pseudo = pseudo;
        this.adresseMail = adresseMail;
        this.role = role;
        this.niveauProcrastination = niveauProcrastination;
        this.excusePreferee = excusePreferee;
        this.dateInscription = LocalDateTime.now();
        this.pointAccumules = pointAccumules;
    }

    public Utilisateur(String pseudo,
                       String adresseMail,
                       RoleUtilisateur role) {
        this.pseudo = pseudo;
        this.adresseMail = adresseMail;
        this.role = role;
        this.niveauProcrastination = NiveauProcrastination.DEBUTANT;
        this.excusePreferee = "";
        this.dateInscription = LocalDateTime.now();
        this.pointAccumules = 0;
    }
}
