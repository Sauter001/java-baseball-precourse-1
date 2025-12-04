package baseball.mock;

import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;
import baseball.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * View 인터페이스의 Mock 구현체
 *
 * 목적:
 * - I/O에 의존하지 않고 GameEngine을 단위 테스트
 * - 특정 시나리오 테스트
 * - 출력 검증을 통해 비즈니스 로직 검증
 */
public class MockView implements View {
    private final Queue<String> inputQueue;
    private final List<String> displayedResults;
    private int gameClearCallCount;
    private GameState continueResponse;

    public MockView(List<String> inputs) {
        this.inputQueue = new LinkedList<>(inputs);
        this.displayedResults = new ArrayList<>();
        this.gameClearCallCount = 0;
    }

    @Override
    public UserAnswer readNumber() {
        if (inputQueue.isEmpty()) {
            throw new IllegalStateException("MockView: 입력이 더 이상 없습니다.");
        }
        return new UserAnswer(inputQueue.poll());
    }

    @Override
    public void displayResult(GameResult result) {
        String resultText = formatResult(result);
        displayedResults.add(resultText);
    }

    private String formatResult(GameResult result) {
        if (result.isNothing()) {
            return "낫싱";
        }

        StringBuilder output = new StringBuilder();
        int ballCount = result.getCount(baseball.constant.BaseballJudgement.BALL);
        int strikeCount = result.getCount(baseball.constant.BaseballJudgement.STRIKE);

        if (ballCount > 0) {
            output.append(ballCount).append("볼");
        }
        if (ballCount > 0 && strikeCount > 0) {
            output.append(" ");
        }
        if (strikeCount > 0) {
            output.append(strikeCount).append("스트라이크");
        }

        return output.toString();
    }

    @Override
    public void displayGameClear() {
        gameClearCallCount++;
    }

    @Override
    public GameState readGameContinue() {
        if (continueResponse == null) {
            throw new IllegalStateException("MockView: continueResponse가 설정되지 않았습니다.");
        }
        return continueResponse;
    }

    // 테스트 검증용 메서드들

    public void setContinueResponse(GameState continueResponse) {
        this.continueResponse = continueResponse;
    }

    public List<String> getDisplayedResults() {
        return new ArrayList<>(displayedResults);
    }

    public int getGameClearCallCount() {
        return gameClearCallCount;
    }

    public boolean hasDisplayedResult(String expected) {
        return displayedResults.contains(expected);
    }
}
