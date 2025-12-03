package baseball.view;

import baseball.constant.GameState;
import baseball.domain.GameResult;

public class ConsoleView implements View {
    @Override
    public String readNumber() {
        return "";
    }

    @Override
    public void displayResult(GameResult result) {

    }

    @Override
    public void displayGameClear() {

    }

    @Override
    public GameState readGameContinue() {
        return null;
    }
}
