package baseball.engine;

import baseball.constant.BaseballJudgement;
import baseball.constant.GameState;
import baseball.domain.GameResult;
import baseball.domain.UserAnswer;
import baseball.mock.MockAnswerNumberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * - 랜덤성 제거: 고정된 정답으로 예측 가능한 테스트
 * - DIP 구현: GameManager가 구체 구현이 아닌 인터페이스에 의존
 * - 단위 테스트: judgeAnswer() 로직만 독립적으로 검증
 */
@DisplayName("GameManager 테스트 (Generator 모킹 활용)")
class GameManagerTest {

    @Test
    @DisplayName("정답과 동일한 입력이면 3스트라이크")
    void 정답_일치_3스트라이크() {
        // given: 정답이 "123"으로 고정된 GameManager
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("123");
        GameManager gameManager = new GameManager(generator);
        gameManager.startNewGame();

        // when: "123" 입력
        UserAnswer userAnswer = new UserAnswer("123");
        GameResult result = gameManager.judgeAnswer(userAnswer);

        // then: 3스트라이크
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(3);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
        assertThat(result.isGameClear()).isTrue();
    }

    @Test
    @DisplayName("숫자는 모두 맞지만 위치가 모두 다르면 3볼")
    void 위치_모두_다름_3볼() {
        // given: 정답 "135"
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("135");
        GameManager gameManager = new GameManager(generator);
        gameManager.startNewGame();

        // when: "351" 입력 (숫자는 같지만 위치 모두 다름)
        UserAnswer userAnswer = new UserAnswer("351");
        GameResult result = gameManager.judgeAnswer(userAnswer);

        // then: 3볼
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(3);
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(0);
    }

    @Test
    @DisplayName("일부는 위치 맞고 일부는 숫자만 맞으면 볼과 스트라이크 혼합")
    void 볼과_스트라이크_혼합() {
        // given: 정답 "246"
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("246");
        GameManager gameManager = new GameManager(generator);
        gameManager.startNewGame();

        // when: "264" 입력 (2는 위치 맞음, 4와 6은 위치 다름)
        UserAnswer userAnswer = new UserAnswer("264");
        GameResult result = gameManager.judgeAnswer(userAnswer);

        // then: 1스트라이크 2볼
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(1);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(2);
    }

    @Test
    @DisplayName("일치하는 숫자가 전혀 없으면 낫싱")
    void 일치하는_숫자_없음_낫싱() {
        // given: 정답 "123"
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("123");
        GameManager gameManager = new GameManager(generator);
        gameManager.startNewGame();

        // when: "456" 입력 (공통 숫자 없음)
        UserAnswer userAnswer = new UserAnswer("456");
        GameResult result = gameManager.judgeAnswer(userAnswer);

        // then: 낫싱
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(0);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
        assertThat(result.isNothing()).isTrue();
    }

    @Test
    @DisplayName("게임 상태를 올바르게 관리한다")
    void 게임_상태_관리() {
        // given
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("789");
        GameManager gameManager = new GameManager(generator);

        // when & then: 초기 상태는 게임 종료 아님
        assertThat(gameManager.isGameOver()).isFalse();

        // when: 게임 종료 상태로 변경
        gameManager.changeStateTo(GameState.END);

        // then: 게임이 종료됨
        assertThat(gameManager.isGameOver()).isTrue();
    }

    @Test
    @DisplayName("Generator 모킹 덕분에 판정 로직을 독립적으로 테스트 가능")
    void Generator_모킹의_장점_어필() {
        // 이 테스트는 AnswerNumberGenerator 인터페이스 분리의 가치를 보여줍니다.
        //
        // 만약 Generator 인터페이스가 없었다면:
        // - GameManager가 항상 랜덤한 정답을 생성
        // - 테스트마다 정답이 달라져서 예측 불가능
        // - 판정 로직만 독립적으로 테스트 불가능
        //
        // Generator 인터페이스 덕분에:
        // ✅ MockGenerator로 정답 고정
        // ✅ 예측 가능한 테스트 케이스 작성
        // ✅ judgeAnswer() 로직만 집중적으로 검증
        // ✅ 엣지 케이스 쉽게 재현 (3볼, 3스트라이크 등)

        // given: 다양한 정답으로 테스트
        String[] testAnswers = {"123", "456", "789", "135", "246"};

        for (String answer : testAnswers) {
            MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator(answer);
            GameManager gameManager = new GameManager(generator);
            gameManager.startNewGame();

            // when: 정답과 동일한 입력
            UserAnswer userAnswer = new UserAnswer(answer);
            GameResult result = gameManager.judgeAnswer(userAnswer);

            // then: 항상 3스트라이크
            assertThat(result.isGameClear()).isTrue();
        }
    }
}
