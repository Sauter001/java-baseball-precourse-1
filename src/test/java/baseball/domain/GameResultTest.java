package baseball.domain;

import baseball.constant.BaseballJudgement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GameResult 테스트
 *
 * - Enum을 Map의 키로 활용
 * - 비즈니스 로직 캡슐화 (isGameClear, isNothing)
 * - 불변성 보장 (생성 시 방어적 복사)
 */
@DisplayName("GameResult 테스트 (Map 일급 컬렉션)")
class GameResultTest {

    @Test
    @DisplayName("3스트라이크면 게임 클리어")
    void 게임_클리어_3스트라이크() {
        // given
        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.STRIKE, 3);
        judgements.put(BaseballJudgement.BALL, 0);

        // when
        GameResult result = new GameResult(judgements);

        // then
        assertThat(result.isGameClear()).isTrue();
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(3);
    }

    @Test
    @DisplayName("스트라이크와 볼이 모두 0이면 낫싱")
    void 낫싱() {
        // given
        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.STRIKE, 0);
        judgements.put(BaseballJudgement.BALL, 0);

        // when
        GameResult result = new GameResult(judgements);

        // then
        assertThat(result.isNothing()).isTrue();
    }

    @Test
    @DisplayName("볼만 있는 경우")
    void 볼만_있음() {
        // given
        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.BALL, 2);
        judgements.put(BaseballJudgement.STRIKE, 0);

        // when
        GameResult result = new GameResult(judgements);

        // then
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(2);
        assertThat(result.isNothing()).isFalse();
        assertThat(result.isGameClear()).isFalse();
    }

    @Test
    @DisplayName("볼과 스트라이크가 혼합된 경우")
    void 볼과_스트라이크_혼합() {
        // given
        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.BALL, 1);
        judgements.put(BaseballJudgement.STRIKE, 2);

        // when
        GameResult result = new GameResult(judgements);

        // then
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(1);
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(2);
        assertThat(result.isNothing()).isFalse();
        assertThat(result.isGameClear()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 판정 타입은 0을 반환 (getOrDefault)")
    void 존재하지_않는_판정_0반환() {
        // given: STRIKE만 설정
        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.STRIKE, 1);

        // when
        GameResult result = new GameResult(judgements);

        // then: BALL은 설정 안 했지만 0 반환
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(0);
    }

    @Test
    @DisplayName("Map 일급 컬렉션의 장점 - 비즈니스 로직 캡슐화")
    void 일급_컬렉션_비즈니스_로직_캡슐화() {
        // 일급 컬렉션 패턴을 사용하지 않았다면:
        //
        // Bad:
        //   Map<BaseballJudgement, Integer> result = calculateResult();
        //   if (result.getOrDefault(STRIKE, 0) == 3) { // 비즈니스 로직 노출
        //       // 게임 클리어 처리
        //   }
        //
        // Good (일급 컬렉션): ✅ 현재 구현
        //   GameResult result = calculateResult();
        //   if (result.isGameClear()) { // 비즈니스 로직 캡슐화
        //       // 게임 클리어 처리
        //   }

        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.STRIKE, 3);
        GameResult result = new GameResult(judgements);

        // 비즈니스 로직이 GameResult 안에 캡슐화됨
        assertThat(result.isGameClear()).isTrue();
    }

    @Test
    @DisplayName("Map 일급 컬렉션의 장점 - Enum을 Map 키로 활용")
    void 일급_컬렉션_Enum_활용() {
        // Enum을 단순 상수가 아닌 Map의 키로 적극 활용
        // 이는 Enum의 타입 안전성을 데이터 구조에까지 확장합니다.
        //
        // Enum 사용 목적:
        // 1. 관련 상수 그룹화 (STRIKE, BALL)
        // 2. 타입 안전성 (컴파일 타임 체크)
        // 3. 의미 있는 이름 (가독성)
        // 4. Map의 키로 활용 (일관성) ✅

        Map<BaseballJudgement, Integer> judgements = new HashMap<>();

        // Enum을 키로 사용 - 오타 방지, IDE 자동완성
        judgements.put(BaseballJudgement.STRIKE, 2);
        judgements.put(BaseballJudgement.BALL, 1);

        GameResult result = new GameResult(judgements);

        // Enum을 통한 안전한 접근
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(2);
        assertThat(result.getCount(BaseballJudgement.BALL)).isEqualTo(1);
    }

    @Test
    @DisplayName("불변성 보장 - 방어적 복사")
    void 불변성_보장() {
        // given: 원본 Map
        Map<BaseballJudgement, Integer> original = new HashMap<>();
        original.put(BaseballJudgement.STRIKE, 1);

        // when: GameResult 생성 후 원본 수정
        GameResult result = new GameResult(original);
        original.put(BaseballJudgement.STRIKE, 999); // 원본 변경 시도

        // then: GameResult는 영향받지 않음 (방어적 복사 덕분)
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(1);
    }

    @Test
    @DisplayName("확장성 - 새로운 판정 타입 추가 가능")
    void 확장성_테스트() {
        // 만약 미래에 FOUL 같은 새로운 판정이 추가된다면?
        // - enum BaseballJudgement에 FOUL 추가
        // - GameResult 코드 수정 불필요 (Map이 자동 처리)
        //
        // 이것이 Map 일급 컬렉션의 확장성입니다.

        Map<BaseballJudgement, Integer> judgements = new HashMap<>();
        judgements.put(BaseballJudgement.STRIKE, 1);
        judgements.put(BaseballJudgement.BALL, 1);
        // 미래: judgements.put(BaseballJudgement.FOUL, 1);

        GameResult result = new GameResult(judgements);

        // getCount() 메서드는 어떤 판정 타입이든 처리 가능
        assertThat(result.getCount(BaseballJudgement.STRIKE)).isEqualTo(1);
    }
}
