package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums;

import lombok.Getter;

@Getter
public enum RoleUtilisateur {
    PROCRASTINATEUR_EN_HERBE("Procrastinateur_en_herbe"),
    ANTI_PROCRASTINATEUR_REPENTIS("Anti_procrastinateur_repentis"),
    GESTIONNAIRE_TEMPS_PERDU("Gestionnaire_du_temps_perdu");

    private final String label;

    RoleUtilisateur(String label) {
        this.label = label;
    }
}