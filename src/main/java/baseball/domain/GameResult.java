package baseball.domain;

import baseball.constant.BaseballJudgement;

import java.util.HashMap;
import java.util.Map;

public class GameResult {
    private static final int GAME_CLEAR_STRIKE_COUNT = 3;
    private final Map<BaseballJudgement, Integer> judgements;

    public GameResult(Map<BaseballJudgement, Integer> judgements) {
        this.judgements = new HashMap<>(judgements);
    }

    public int getCount(BaseballJudgement judgement) {
        return judgements.getOrDefault(judgement, 0);
    }

    public boolean isGameClear() {
        return getCount(BaseballJudgement.STRIKE) == GAME_CLEAR_STRIKE_COUNT;
    }

    public boolean isNothing() {
        return getCount(BaseballJudgement.STRIKE) == 0
            && getCount(BaseballJudgement.BALL) == 0;
    }
}
