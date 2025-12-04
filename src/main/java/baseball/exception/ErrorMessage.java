package baseball.exception;

public enum ErrorMessage {
    INVALID_INPUT_LENGTH("입력값은 3자리여야 합니다."),
    INVALID_INPUT_NOT_NUMBER("입력값은 숫자여야 합니다."),
    INVALID_INPUT_OUT_OF_RANGE("입력값은 1부터 9까지의 숫자여야 합니다."),
    INVALID_INPUT_DUPLICATE("입력값에 중복된 숫자가 있습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
