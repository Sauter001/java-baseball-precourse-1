package baseball.engine;

import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;
import baseball.view.View;

public class GameEngine {
    private final View view;
    private final GameManager gameManager;

    public GameEngine(View view, GameManager gameManager) {
        this.view = view;
        this.gameManager = gameManager;
    }

    public void run() {
        gameManager.startNewGame();

        while (!gameManager.isGameOver()) {
            playRound();
        }
    }

    private void playRound() {
        UserAnswer userAnswer = view.readNumber();
        GameResult result = gameManager.judgeAnswer(userAnswer);
        view.displayResult(result);

        if (result.isGameClear()) {
            handleGameClear();
        }
    }

    private void handleGameClear() {
        view.displayGameClear();
        GameState nextState = view.readGameContinue();
        gameManager.changeStateTo(nextState);

        if (nextState == GameState.START) {
            gameManager.startNewGame();
        }
    }
}
