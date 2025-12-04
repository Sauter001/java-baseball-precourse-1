package baseball.engine;

import baseball.constant.GameState;
import baseball.mock.MockAnswerNumberGenerator;
import baseball.mock.MockView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * - I/O 의존성 제거: 실제 콘솔 입출력 없이 순수 로직 테스트
 * - 특정 입력 시퀀스로 원하는 상황 재현
 * - 빠른 실행: 사용자 입력 대기 없이 즉시 테스트 완료
 * - 출력 검증: displayResult() 호출 여부 및 내용 검증 가능
 */
@DisplayName("GameEngine 테스트 (View 모킹 활용)")
class GameEngineTest {

    @Test
    @DisplayName("한 번에 정답을 맞히면 게임이 종료된다")
    void 한번에_정답을_맞히면_게임_종료() {
        // given: 정답이 "123"인 게임
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("123");
        GameManager gameManager = new GameManager(generator);

        // View 모킹: "123" 입력 후 게임 종료
        MockView mockView = new MockView(Arrays.asList("123"));
        mockView.setContinueResponse(GameState.END);

        GameEngine engine = new GameEngine(mockView, gameManager);

        // when: 게임 실행
        engine.run();

        // then: 3스트라이크가 출력되고, 게임 클리어 메시지가 표시됨
        assertThat(mockView.hasDisplayedResult("3스트라이크")).isTrue();
        assertThat(mockView.getGameClearCallCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("여러 번의 시도 끝에 정답을 맞힌다")
    void 여러번_시도_후_정답() {
        // given: 정답이 "456"인 게임
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("456");
        GameManager gameManager = new GameManager(generator);

        // View 모킹: 여러 입력 시도
        // "123" -> 낫싱
        // "789" -> 낫싱
        // "456" -> 3스트라이크
        MockView mockView = new MockView(Arrays.asList("123", "789", "456"));
        mockView.setContinueResponse(GameState.END);

        GameEngine engine = new GameEngine(mockView, gameManager);

        // when: 게임 실행
        engine.run();

        // then: 각 입력에 대한 결과가 올바르게 표시됨
        List<String> results = mockView.getDisplayedResults();
        assertThat(results).hasSize(3);
        assertThat(results.get(0)).isEqualTo("낫싱");
        assertThat(results.get(1)).isEqualTo("낫싱");
        assertThat(results.get(2)).isEqualTo("3스트라이크");
        assertThat(mockView.getGameClearCallCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("볼과 스트라이크가 섞인 결과를 올바르게 표시한다")
    void 볼과_스트라이크_혼합() {
        // given: 정답이 "135"인 게임
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("135");
        GameManager gameManager = new GameManager(generator);

        // View 모킹
        // "246" -> 낫싱 (공통 숫자 없음)
        // "351" -> 3볼 (모두 위치 다름: 3은 index 1, 5는 index 2, 1은 index 0)
        // "135" -> 3스트라이크
        MockView mockView = new MockView(Arrays.asList("246", "351", "135"));
        mockView.setContinueResponse(GameState.END);

        GameEngine engine = new GameEngine(mockView, gameManager);

        // when
        engine.run();

        // then
        List<String> results = mockView.getDisplayedResults();
        assertThat(results).hasSize(3);
        assertThat(results.get(0)).isEqualTo("낫싱");
        assertThat(results.get(1)).isEqualTo("3볼");
        assertThat(results.get(2)).isEqualTo("3스트라이크");
    }

    @Test
    @DisplayName("게임 클리어 후 종료를 선택하면 게임이 종료된다")
    void 게임_클리어_후_종료() {
        // given: 정답이 "789"인 게임
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("789");
        GameManager gameManager = new GameManager(generator);

        // View 모킹: 게임 클리어 -> 종료 선택
        MockView mockView = new MockView(Arrays.asList("456", "789"));
        mockView.setContinueResponse(GameState.END);

        GameEngine engine = new GameEngine(mockView, gameManager);

        // when
        engine.run();

        // then: 게임 클리어 메시지가 표시되고 게임이 종료됨
        assertThat(mockView.getGameClearCallCount()).isEqualTo(1);
        assertThat(mockView.getDisplayedResults()).contains("3스트라이크");
        assertThat(gameManager.isGameOver()).isTrue();
    }

    @Test
    @DisplayName("View 모킹 덕분에 I/O 없이 순수 로직만 빠르게 테스트 가능")
    void View_모킹의_장점_어필() {
        // 이 테스트는 View 인터페이스 분리의 가치를 보여줍니다.
        //
        // 만약 View 인터페이스가 없었다면:
        // - GameEngine이 직접 System.in/out에 의존
        // - 테스트 시 실제 콘솔 입력이 필요
        // - 자동화된 테스트 불가능
        // - 특정 시나리오 재현 어려움
        //
        // View 인터페이스 덕분에:
        // ✅ MockView로 입력/출력 완전 제어
        // ✅ 예측 가능한 테스트 시나리오 구성
        // ✅ 출력 검증을 통한 비즈니스 로직 검증
        // ✅ I/O 없이 밀리초 단위 빠른 실행

        // given
        MockAnswerNumberGenerator generator = new MockAnswerNumberGenerator("246");
        GameManager gameManager = new GameManager(generator);
        MockView mockView = new MockView(Arrays.asList("123", "246"));
        mockView.setContinueResponse(GameState.END);

        GameEngine engine = new GameEngine(mockView, gameManager);

        // when
        long startTime = System.currentTimeMillis();
        engine.run();
        long endTime = System.currentTimeMillis();

        // then: 매우 빠르게 실행됨 (I/O 대기 없음)
        assertThat(endTime - startTime).isLessThan(100); // 100ms 이내
        assertThat(mockView.getDisplayedResults()).hasSize(2);
    }
}
