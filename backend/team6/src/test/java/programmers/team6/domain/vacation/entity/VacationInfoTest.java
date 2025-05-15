package programmers.team6.domain.vacation.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;

class VacationInfoTest {

	@Test
	void 휴가정보업데이트() {
		VacationInfo info = new VacationInfo(15, 13, "test", 1L);
		double updateTotalCount = 13;

		VacationInfoUpdateResult result = info.updateTotalCount(updateTotalCount);

		assertThat(result.isSuccess()).isTrue();
		assertThat(updateTotalCount).isEqualTo(info.getTotalCount());
	}

	@Test
	void 사용휴가정보보다_적게_부여휴가를_비업데이트() {
		double totalCount = 15;
		VacationInfo info = new VacationInfo(totalCount, 13, "test", 1L);
		double updateTotalCount = 12;

		VacationInfoUpdateResult result = info.updateTotalCount(updateTotalCount);

		assertThat(result.isSuccess()).isFalse();
		assertThat(result).isEqualTo(VacationInfoUpdateResult.MISS_RULES);
		assertThat(totalCount).isEqualTo(info.getTotalCount());
	}

}