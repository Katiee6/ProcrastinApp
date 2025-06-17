package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
