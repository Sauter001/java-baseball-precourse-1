package baseball.view;

import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;
import camp.nextstep.edu.missionutils.Console;

public class ConsoleView implements View {
    @Override
    public UserAnswer readNumber() {
        System.out.print("숫자를 입력해주세요 : ");
        return new UserAnswer(Console.readLine());
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
