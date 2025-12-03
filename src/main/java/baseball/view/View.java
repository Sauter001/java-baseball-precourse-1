package baseball.view;

import baseball.constant.GameState;
import baseball.domain.GameResult;

public interface View {
    String readNumber();

    void displayResult(GameResult result);

    void displayGameClear();

    GameState readGameContinue();
}
