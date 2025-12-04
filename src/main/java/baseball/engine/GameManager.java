package baseball.engine;

import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;
import baseball.domain.generator.AnswerNumberGenerator;

import java.util.List;

public class GameManager {
    private final AnswerNumberGenerator answerGenerator;

    private List<Character> answer;
    private GameState currentGameState;

    public GameManager(AnswerNumberGenerator answerGenerator) {
        this.answerGenerator = answerGenerator;
        this.currentGameState = GameState.START;
    }

    public void startNewGame() {
        this.answer = this.answerGenerator.generate();
    }

    public GameResult judgeAnswer(UserAnswer userAnswer) {
        return userAnswer.compareWith(answer);
    }

    public void changeStateTo(GameState gameState) {
        this.currentGameState = gameState;
    }

    public boolean isGameOver() {
        return this.currentGameState == GameState.END;
    }
}
