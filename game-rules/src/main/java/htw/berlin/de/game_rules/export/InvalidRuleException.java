package htw.berlin.de.game_rules.export;

/**
 * Exception for invalid rules.
 */
public class InvalidRuleException extends RuntimeException {
    public InvalidRuleException(String message) {
        super(message);
    }
}