package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Getter
@Setter
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
    private LocalTime dateInscription;
    private int pointAccumules;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PiegeProductivite> piegeProductivites;

    public Utilisateur(@NonNull String pseudo,
                       @NonNull String adresseMail,
                       @NonNull RoleUtilisateur role,
                       @NonNull NiveauProcrastination niveauProcrastination,
                       @NonNull String excusePreferee,
                       int pointAccumules) {
        this.pseudo = pseudo;
        this.adresseMail = adresseMail;
        this.role = role;
        this.niveauProcrastination = niveauProcrastination;
        this.excusePreferee = excusePreferee;
        this.dateInscription = LocalTime.now();
        this.pointAccumules = pointAccumules;
    }

}
