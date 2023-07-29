package htw.berlin.de.material_system.export;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
