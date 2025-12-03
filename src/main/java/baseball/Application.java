package baseball;

import baseball.domain.generator.AnswerNumberGenerator;
import baseball.domain.generator.AnswerNumberGeneratorImpl;
import baseball.engine.GameEngine;
import baseball.engine.GameManager;
import baseball.view.ConsoleView;
import baseball.view.View;

public class Application {
    public static void main(String[] args) {
        // 의존성
        View view = new ConsoleView();
        AnswerNumberGenerator answerGenerator = new AnswerNumberGeneratorImpl();
        GameManager gameManager = new GameManager(answerGenerator);
        GameEngine gameEngine = new GameEngine(view, gameManager);

        // 엔진 실행
        gameEngine.run();
    }
}
