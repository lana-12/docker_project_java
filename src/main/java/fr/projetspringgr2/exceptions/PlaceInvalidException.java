package fr.projetspringgr2.exceptions;

/**
 * Exception thrown when a Place object is invalid.
 */
public class PlaceInvalidException extends RuntimeException {

    /**
     * Constructs a new PlaceInvalidException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public PlaceInvalidException(String message) {
        super(message);
    }
}
