package baseball.engine;

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
        while (true) {
            if (gameManager.isGameOver()) {
                return;
            }

            UserAnswer userAnswer = view.readNumber();

        }
    }
}
