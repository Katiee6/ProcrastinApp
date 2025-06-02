package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
