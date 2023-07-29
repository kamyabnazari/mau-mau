package htw.berlin.de.player_manager.export;

public class PlayerNotFoundException extends RuntimeException{
    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(Throwable throwable) {
        super(throwable);
    }
}