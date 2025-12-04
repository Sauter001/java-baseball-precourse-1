package baseball.domain;

import baseball.constant.BaseballJudgement;
import baseball.exception.GameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * UserAnswer 테스트
 *
 * 테스트 범위:
 * 1. 입력 검증 로직 (예외 발생)
 * 2. 정답 비교 로직 (Tell, Don't Ask 패턴)
 */
@DisplayName("UserAnswer 테스트")
class UserAnswerTest {

    // ========== 입력 검증 테스트 ==========

    @Test
    @DisplayName("3자리 숫자가 아니면 예외 발생")
    void 길이_검증_실패() {
        assertThatThrownBy(() -> new UserAnswer("12"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("3자리");

        assertThatThrownBy(() -> new UserAnswer("1234"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("3자리");
    }

    @Test
    @DisplayName("숫자가 아니면 예외 발생")
    void 숫자_검증_실패() {
        assertThatThrownBy(() -> new UserAnswer("abc"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("숫자");

        assertThatThrownBy(() -> new UserAnswer("12a"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("숫자");
    }

    @ParameterizedTest
    @ValueSource(strings = {"012", "120", "900", "001"})
    @DisplayName("0이 포함되면 예외 발생 (1-9 범위)")
    void 범위_검증_실패_0포함(String input) {
        assertThatThrownBy(() -> new UserAnswer(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("1부터 9까지");
    }

    @Test
    @DisplayName("중복된 숫자가 있으면 예외 발생")
    void 중복_검증_실패() {
        assertThatThrownBy(() -> new UserAnswer("112"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("중복");

        assertThatThrownBy(() -> new UserAnswer("121"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("중복");

        assertThatThrownBy(() -> new UserAnswer("111"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("중복");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "456", "789", "135", "987"})
    @DisplayName("올바른 입력이면 UserAnswer 생성 성공")
    void 올바른_입력(String input) {
        // 예외가 발생하지 않아야 함
        UserAnswer userAnswer = new UserAnswer(input);
        assertThat(userAnswer).isNotNull();
    }

    // ========== 비교 로직 테스트 (Tell, Don't Ask) ==========

    @Test
    @DisplayName("정답과 완전히 일치하면 3스트라이크")
    void 완전_일치_3스트라이크() {
        // given
        UserAnswer userAnswer = new UserAnswer("123");
        List<Character> correctAnswer = Arrays.asList('1', '2', '3');

        // when
        GameResult result = userAnswer.compareWith(correctAnswer);

        // then
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(3);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
        assertThat(result.isGameClear()).isTrue();
    }

    @Test
    @DisplayName("숫자는 맞지만 위치가 모두 다르면 3볼")
    void 숫자만_일치_3볼() {
        // given: 정답 "123"
        UserAnswer userAnswer = new UserAnswer("312");
        List<Character> correctAnswer = Arrays.asList('1', '2', '3');

        // when
        GameResult result = userAnswer.compareWith(correctAnswer);

        // then: 3, 1, 2는 모두 정답에 있지만 위치가 모두 다름
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(3);
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(0);
    }

    @Test
    @DisplayName("일부는 위치 맞고 일부는 숫자만 맞으면 볼과 스트라이크 혼합")
    void 볼과_스트라이크_혼합() {
        // given: 정답 "135"
        UserAnswer userAnswer = new UserAnswer("153");
        List<Character> correctAnswer = Arrays.asList('1', '3', '5');

        // when
        GameResult result = userAnswer.compareWith(correctAnswer);

        // then: 1은 위치 맞음(스트라이크), 3과 5는 위치 다름(볼)
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(1);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(2);
    }

    @Test
    @DisplayName("공통 숫자가 없으면 낫싱")
    void 공통_숫자_없음_낫싱() {
        // given: 정답 "123"
        UserAnswer userAnswer = new UserAnswer("456");
        List<Character> correctAnswer = Arrays.asList('1', '2', '3');

        // when
        GameResult result = userAnswer.compareWith(correctAnswer);

        // then
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(0);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
        assertThat(result.isNothing()).isTrue();
    }

    @Test
    @DisplayName("Tell Don't Ask 패턴 - UserAnswer가 비교 책임을 가짐")
    void Tell_Dont_Ask_패턴() {
        // UserAnswer는 단순히 데이터만 가지는 것이 아니라
        // 정답과 비교하는 행위(compareWith)를 스스로 수행합니다.
        //
        // Bad (Ask):
        //   List<Character> userDigits = userAnswer.getDigits(); // getter
        //   // 외부에서 비교 로직 수행
        //
        // Good (Tell): 현재 구현
        //   GameResult result = userAnswer.compareWith(correctAnswer);
        //   // UserAnswer가 스스로 비교 수행

        UserAnswer userAnswer = new UserAnswer("246");
        List<Character> correctAnswer = Arrays.asList('2', '4', '6');

        GameResult result = userAnswer.compareWith(correctAnswer);

        // UserAnswer가 책임을 다했으므로 GameResult만 검증
        assertThat(result.isGameClear()).isTrue();
    }

    @Test
    @DisplayName("엣지 케이스 - 첫 번째 자리만 맞음")
    void 첫번째_자리만_스트라이크() {
        UserAnswer userAnswer = new UserAnswer("167");
        List<Character> correctAnswer = Arrays.asList('1', '2', '3');

        GameResult result = userAnswer.compareWith(correctAnswer);

        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(1);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
    }

    @Test
    @DisplayName("엣지 케이스 - 마지막 자리만 맞음")
    void 마지막_자리만_스트라이크() {
        UserAnswer userAnswer = new UserAnswer("453");
        List<Character> correctAnswer = Arrays.asList('1', '2', '3');

        GameResult result = userAnswer.compareWith(correctAnswer);

        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(1);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
    }
}
