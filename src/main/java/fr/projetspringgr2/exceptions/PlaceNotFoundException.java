package fr.projetspringgr2.exceptions;

/**
 * Exception thrown when a Place object is not found.
 */
public class PlaceNotFoundException extends RuntimeException {

    /**
     * Constructs a new PlaceNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public PlaceNotFoundException(String message) {
        super(message);
    }
}
