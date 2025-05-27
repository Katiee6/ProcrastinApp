package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.resolvers;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}