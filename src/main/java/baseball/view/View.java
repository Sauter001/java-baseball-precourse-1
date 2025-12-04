package baseball.view;

import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;

public interface View {
    UserAnswer readNumber();

    void displayResult(GameResult result);

    void displayGameClear();

    GameState readGameContinue();
}
