package baseball.constant;

public enum GameState {
    START("1"), END("2");

    private final String command;

    GameState(String command) {
        this.command = command;
    }

    public boolean equalsInput(String input) {
        return command.equals(input);
    }
}
