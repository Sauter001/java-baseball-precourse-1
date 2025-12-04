package baseball.exception;

public class GameException extends IllegalArgumentException {
    public GameException(String message) {
        super(message);
    }

    public GameException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
