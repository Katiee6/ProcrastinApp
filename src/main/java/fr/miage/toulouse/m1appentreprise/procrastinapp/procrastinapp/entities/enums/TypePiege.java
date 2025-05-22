package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums;

import lombok.Getter;


@Getter
public enum TypePiege {
    JEU("Jeu"),
    DEFI("Defi"),
    MEDITATION("Meditation");

    private final String value;

    TypePiege(String value) {
        this.value = value;
    }
}
