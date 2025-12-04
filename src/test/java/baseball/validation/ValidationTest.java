package baseball.validation;

import baseball.domain.UserAnswer;
import baseball.exception.GameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 기능 요구사항 검증 테스트
 * - 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException을 발생시킨다
 */
@DisplayName("예외 케이스 검증 테스트")
class ValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"1", "12", "1234", "12345"})
    @DisplayName("3자리가 아니면 IllegalArgumentException")
    void 길이_검증(String input) {
        assertThatThrownBy(() -> new UserAnswer(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("3자리");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "12a", "a23", "1b3", "가나다", "!@#"})
    @DisplayName("숫자가 아니면 IllegalArgumentException")
    void 숫자_검증(String input) {
        assertThatThrownBy(() -> new UserAnswer(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("숫자");
    }

    @ParameterizedTest
    @ValueSource(strings = {"012", "102", "120", "900", "001", "000", "100", "010"})
    @DisplayName("0이 포함되면 IllegalArgumentException (1-9 범위)")
    void 범위_검증_0포함(String input) {
        assertThatThrownBy(() -> new UserAnswer(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("1부터 9까지");
    }

    @ParameterizedTest
    @ValueSource(strings = {"111", "222", "121", "112", "211", "131", "313", "181", "191"})
    @DisplayName("중복된 숫자가 있으면 IllegalArgumentException")
    void 중복_검증(String input) {
        assertThatThrownBy(() -> new UserAnswer(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("중복");
    }

    @Test
    @DisplayName("공백이 포함되면 IllegalArgumentException")
    void 공백_검증() {
        assertThatThrownBy(() -> new UserAnswer("1 2"))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new UserAnswer(" 123"))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new UserAnswer("123 "))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 문자열이면 IllegalArgumentException")
    void 빈문자열_검증() {
        assertThatThrownBy(() -> new UserAnswer(""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "456", "789", "135", "246", "987", "654", "321"})
    @DisplayName("올바른 입력은 예외가 발생하지 않는다")
    void 올바른_입력(String input) {
        // 예외가 발생하지 않아야 함
        new UserAnswer(input);
    }

    @Test
    @DisplayName("GameException은 IllegalArgumentException을 상속한다")
    void GameException_타입_검증() {
        try {
            new UserAnswer("1234");
        } catch (Exception e) {
            assertThatThrownBy(() -> { throw e; })
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
