package baseball.domain.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 기능 요구사항 검증 테스트
 * - 컴퓨터는 1에서 9까지 서로 다른 임의의 수 3개를 선택한다
 */
@DisplayName("컴퓨터 정답 생성 검증")
class AnswerNumberGeneratorImplTest {

    private final AnswerNumberGenerator generator = new AnswerNumberGeneratorImpl();

    @RepeatedTest(100)
    @DisplayName("생성된 정답은 정확히 3자리다")
    void 정답_길이_3자리() {
        List<Character> answer = generator.generate();
        assertThat(answer).hasSize(3);
    }

    @RepeatedTest(100)
    @DisplayName("생성된 정답은 모두 서로 다른 숫자다 (중복 없음)")
    void 정답_중복_없음() {
        List<Character> answer = generator.generate();
        Set<Character> uniqueChars = new HashSet<>(answer);

        // Set 크기가 3이면 중복이 없다는 의미
        assertThat(uniqueChars).hasSize(3);
    }

    @RepeatedTest(100)
    @DisplayName("생성된 정답은 1부터 9까지의 숫자만 포함한다")
    void 정답_범위_1부터_9() {
        List<Character> answer = generator.generate();

        for (Character ch : answer) {
            int num = Character.getNumericValue(ch);
            assertThat(num).isBetween(1, 9);
        }
    }

    @RepeatedTest(100)
    @DisplayName("생성된 정답에 0이 포함되지 않는다")
    void 정답_0포함_안됨() {
        List<Character> answer = generator.generate();

        for (Character ch : answer) {
            assertThat(ch).isNotEqualTo('0');
        }
    }

    @Test
    @DisplayName("여러 번 생성해도 항상 요구사항을 만족한다 (통합 검증)")
    void 통합_검증_1000번() {
        for (int i = 0; i < 1000; i++) {
            List<Character> answer = generator.generate();

            // 1. 길이 3
            assertThat(answer).hasSize(3);

            // 2. 중복 없음
            Set<Character> uniqueChars = new HashSet<>(answer);
            assertThat(uniqueChars).hasSize(3);

            // 3. 1-9 범위
            for (Character ch : answer) {
                int num = Character.getNumericValue(ch);
                assertThat(num).isBetween(1, 9);
            }
        }
    }

    @Test
    @DisplayName("랜덤성 검증: 100번 생성 시 다양한 값이 나온다")
    void 랜덤성_검증() {
        Set<String> generatedAnswers = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            List<Character> answer = generator.generate();
            String answerStr = answer.toString();
            generatedAnswers.add(answerStr);
        }

        // 100번 생성했을 때 최소 50개 이상의 다른 값이 나와야 함
        // (완전히 랜덤하면 거의 100개가 나오겠지만, 여유를 둠)
        assertThat(generatedAnswers.size()).isGreaterThan(50);
    }
}
