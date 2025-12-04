package baseball.domain;

import baseball.constant.GameConstant;
import baseball.exception.ErrorMessage;
import baseball.exception.GameException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserAnswer {
    private List<Character> answer;

    public UserAnswer(String answer) {
        validate(answer);
        this.answer = answer.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    private void validate(String answer) {
        validateLength(answer);
        validateNumber(answer);
        validateRange(answer);
        validateDuplicate(answer);
    }

    private void validateLength(String answer) {
        if (answer.length() != GameConstant.ANSWER_LENGTH) {
            throw new GameException(ErrorMessage.INVALID_INPUT_LENGTH.getMessage());
        }
    }

    private void validateNumber(String answer) {
        try {
            Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            throw new GameException(ErrorMessage.INVALID_INPUT_NOT_NUMBER.getMessage());
        }
    }

    private void validateRange(String answer) {
        for (char c : answer.toCharArray()) {
            int num = Character.getNumericValue(c);
            if (num < GameConstant.MIN_BASEBALL_NUMBER || num > GameConstant.MAX_BASEBALL_NUMBER) {
                throw new GameException(ErrorMessage.INVALID_INPUT_OUT_OF_RANGE.getMessage());
            }
        }
    }

    private void validateDuplicate(String answer) {
        Set<Character> uniqueChars = new HashSet<>();
        for (char c : answer.toCharArray()) {
            if (!uniqueChars.add(c)) {
                throw new GameException(ErrorMessage.INVALID_INPUT_DUPLICATE.getMessage());
            }
        }
    }
}
