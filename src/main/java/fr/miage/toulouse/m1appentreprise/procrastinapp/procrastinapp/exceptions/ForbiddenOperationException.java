package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
