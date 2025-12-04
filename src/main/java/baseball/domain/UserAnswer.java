package baseball.domain;

import baseball.constant.BaseballJudgement;
import baseball.constant.GameConstant;
import baseball.exception.ErrorMessage;
import baseball.exception.GameException;

import java.util.*;
import java.util.stream.Collectors;

public class UserAnswer {
    private final List<Character> answer;

    public UserAnswer(String answer) {
        validate(answer);
        this.answer = answer.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    }

    public GameResult compareWith(List<Character> correctAnswer) {
        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.STRIKE, countStrike(correctAnswer));
        judgements.put(BaseballJudgement.BALL, countBall(correctAnswer));
        return new GameResult(judgements);
    }

    private int countStrike(List<Character> correctAnswer) {
        int strikeCount = 0;
        for (int i = 0; i < answer.size(); i++) {
            if (answer.get(i).equals(correctAnswer.get(i))) {
                strikeCount++;
            }
        }
        return strikeCount;
    }

    private int countBall(List<Character> correctAnswer) {
        int ballCount = 0;
        for (int i = 0; i < answer.size(); i++) {
            if (isBall(correctAnswer, i)) {
                ballCount++;
            }
        }
        return ballCount;
    }

    private boolean isBall(List<Character> correctAnswer, int index) {
        return !answer.get(index).equals(correctAnswer.get(index))
            && correctAnswer.contains(answer.get(index));
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
