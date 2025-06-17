package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums;

public enum CategorieExcuse {
    TRAVAIL,
    ETUDES,
    VIE_SOCIALE;

    /**
     * Checks if the given CategorieExcuse is valid.
     *
     * @param categorie the CategorieExcuse to check
     * @return true if the categorie is valid, false otherwise
     */
    public static boolean isValid(CategorieExcuse categorie) {
        for (CategorieExcuse c : CategorieExcuse.values()) {
            if (c == categorie) {
                return true;
            }
        }
        return false;
    }
}
