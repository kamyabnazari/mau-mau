package htw.berlin.de.player_manager.export;

public class CreatePlayerFailedException extends RuntimeException{
    public CreatePlayerFailedException(String message) {
        super(message);
    }

    public CreatePlayerFailedException(Throwable throwable) {
        super(throwable);
    }
}
