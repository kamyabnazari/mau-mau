package htw.berlin.de.material_system.export;

public class EmptyPileOfCardsException extends RuntimeException {
    public EmptyPileOfCardsException(String message) {
        super(message);
    }

    public EmptyPileOfCardsException(Throwable throwable) {
        super(throwable);
    }
}

