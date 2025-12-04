package baseball.view;

import baseball.constant.BaseballJudgement;
import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;
import camp.nextstep.edu.missionutils.Console;

public class ConsoleView implements View {
    private static final String INPUT_PROMPT = "숫자를 입력해주세요 : ";
    private static final String GAME_CLEAR_MESSAGE = "3개의 숫자를 모두 맞히셨습니다! 게임 종료";
    private static final String GAME_CONTINUE_PROMPT = "게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.";
    private static final String NOTHING = "낫싱";
    private static final String BALL = "볼";
    private static final String STRIKE = "스트라이크";

    @Override
    public UserAnswer readNumber() {
        System.out.print(INPUT_PROMPT);
        return new UserAnswer(Console.readLine());
    }

    @Override
    public void displayResult(GameResult result) {
        if (result.isNothing()) {
            System.out.println(NOTHING);
            return;
        }
        System.out.println(formatResult(result));
    }

    private String formatResult(GameResult result) {
        StringBuilder output = new StringBuilder();
        int ballCount = result.getCount(BaseballJudgement.BALL);
        int strikeCount = result.getCount(BaseballJudgement.STRIKE);

        appendBall(output, ballCount);
        appendDelimiter(output, ballCount, strikeCount);
        appendStrike(output, strikeCount);

        return output.toString();
    }

    private void appendBall(StringBuilder output, int ballCount) {
        if (ballCount > 0) {
            output.append(ballCount).append(BALL);
        }
    }

    private void appendDelimiter(StringBuilder output, int ballCount, int strikeCount) {
        if (ballCount > 0 && strikeCount > 0) {
            output.append(" ");
        }
    }

    private void appendStrike(StringBuilder output, int strikeCount) {
        if (strikeCount > 0) {
            output.append(strikeCount).append(STRIKE);
        }
    }

    @Override
    public void displayGameClear() {
        System.out.println(GAME_CLEAR_MESSAGE);
    }

    @Override
    public GameState readGameContinue() {
        System.out.println(GAME_CONTINUE_PROMPT);
        String input = Console.readLine();

        if (GameState.START.equalsInput(input)) {
            return GameState.START;
        }
        return GameState.END;
    }
}
