package baseball.mock;

import baseball.domain.generator.AnswerNumberGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * AnswerNumberGenerator 인터페이스의 Mock 구현체
 *
 * 목적:
 * - 랜덤성을 제거하고 예측 가능한 정답으로 테스트
 * - GameManager의 판정 로직을 단위 테스트
 */
public class MockAnswerNumberGenerator implements AnswerNumberGenerator {
    private final List<Character> fixedAnswer;

    public MockAnswerNumberGenerator(String answer) {
        if (answer.length() != 3) {
            throw new IllegalArgumentException("정답은 3자리여야 합니다.");
        }
        this.fixedAnswer = Arrays.asList(
            answer.charAt(0),
            answer.charAt(1),
            answer.charAt(2)
        );
    }

    @Override
    public List<Character> generate() {
        return fixedAnswer;
    }
}
