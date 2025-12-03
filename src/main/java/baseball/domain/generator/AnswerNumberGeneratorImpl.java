package baseball.domain.generator;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;

import static baseball.constant.GameConstant.*;

public class AnswerNumberGeneratorImpl implements AnswerNumberGenerator {

    @Override
    public List<Character> generate() {
        List<Character> answer = new ArrayList<>();
        Set<Character> answerSet = new HashSet<>();

        while (answerSet.size() < MAX_BASEBALL_NUMBER) {
            char numChar = pickNumChar();
            if (answerSet.contains(numChar)) {
                continue;
            }

            answerSet.add(numChar);
            answer.add(numChar);
        }

        return answer;
    }

    private char pickNumChar() {
        int number = Randoms.pickNumberInRange(MIN_BASEBALL_NUMBER, MAX_BASEBALL_NUMBER);
        return Character.forDigit(number, 10);
    }
}
