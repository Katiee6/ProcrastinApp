package fr.miage.toulouse.m1appentreprise.procrastinapp.procrastinapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestionnaire global des exceptions pour les contrôleurs Spring.
 * Capture et traite différentes exceptions pour retourner une réponse HTTP appropriée.
 */
@ControllerAdvice
public class GestionnaireExceptions {

    /**
     * Traite les exceptions de type ResourceNotFoundException.
     * @param ex exception levée lorsque la ressource n'est pas trouvée
     * @return réponse HTTP avec le message d'erreur et le statut 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Traite les exceptions de type ConflictException.
     * @param ex exception levée en cas de conflit lors du traitement
     * @return réponse HTTP avec le message d'erreur et le statut 409 (Conflict)
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Traite les exceptions de type InvalidRequestException.
     * @param ex exception levée pour une requête invalide
     * @return réponse HTTP avec le message d'erreur et le statut 400 (Bad Request)
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequest(InvalidRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Traite les exceptions de type ForbiddenOperationException.
     * @param ex exception levée lorsqu'une opération est interdite
     * @return réponse HTTP avec le message d'erreur et le statut 403 (Forbidden)
     */
    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<String> handleForbidden(ForbiddenOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Traite toutes les autres exceptions non spécifiquement gérées.
     * @param ex exception générique levée
     * @return réponse HTTP avec le message d'erreur et le statut 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
