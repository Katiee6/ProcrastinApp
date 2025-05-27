package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.middleware;

import fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.entities.enums.RoleUtilisateur;
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedRoles {
    RoleUtilisateur[] value();
}