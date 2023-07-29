package htw.berlin.de.game_manager.export;


public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
    public GameNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
