package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums;

import lombok.Getter;

@Getter
public enum NiveauProcrastination {
    DEBUTANT("Debutant"),
    INTERMEDIAIRE("Intermediaire"),
    EXPERT("Expert");

    private final String value;


    NiveauProcrastination(String value) {
        this.value = value;
    }
}
