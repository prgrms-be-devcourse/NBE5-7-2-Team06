package programmers.team6.domain.vacation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import programmers.team6.domain.vacation.repository.dto.JdbcUpdated;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.repository.VacationEligibilitiesRepository;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilities;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilitiesFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

class VacationInfoServiceTest {

	private VacationGrantEligibilitiesFactory factory = Mockito.mock(VacationGrantEligibilitiesFactory.class);

	@Test
	void 업데이트성공_테스트() {
		List<VacationGrantInfo> annualGrantInfos = List.of(new VacationGrantInfo(1, 3, 1));
		List<VacationGrantInfo> monthlyGrantInfos = List.of(new VacationGrantInfo(2, 1, 1));
		VacationInfoService service = new VacationInfoService(factory,
			new TestVacationEligibilitiesRepositoryFake(annualGrantInfos, monthlyGrantInfos));
		VacationGrantEligibilities eligibilities = Mockito.mock(VacationGrantEligibilities.class);
		when(eligibilities.getAnnualVacationGrantInfos()).thenReturn(annualGrantInfos);
		when(eligibilities.getMonthlyVacationGrantInfos()).thenReturn(monthlyGrantInfos);

		assertThatCode(() -> service.updateEligiblities(eligibilities)).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@CsvSource({"true,false", "false,true"})
	void 업데이트_실패테스트(boolean annualSuccess, boolean monthlySuccess) {
		VacationInfoService service = new VacationInfoService(factory,
			new SuccessAndFailTestVacationEligibilitiesRepositoryFake(annualSuccess, monthlySuccess));
		VacationGrantEligibilities eligibilities = Mockito.mock(VacationGrantEligibilities.class);

		assertThatThrownBy(() -> service.updateEligiblities(eligibilities)).isInstanceOf(RuntimeException.class);
	}

	private static class SuccessAndFailTestVacationEligibilitiesRepositoryFake
		extends AbsentTestVacationEligibilitiesRepositoryFake {

		private final boolean annualSuccess;
		private final boolean mothlySuccess;

		public SuccessAndFailTestVacationEligibilitiesRepositoryFake(boolean annualSuccess, boolean mothlySuccess) {
			this.annualSuccess = annualSuccess;
			this.mothlySuccess = mothlySuccess;
		}

		private static List<VacationGrantInfo> createFackeVacationGrantInfos() {
			return List.of(new VacationGrantInfo(1, 1, 1));
		}

		@Override
		public UpdatedResults<VacationGrantInfo> updateAnnualVacationEligiblities(
			List<VacationGrantInfo> eligibilities) {
			if (annualSuccess) {
				return createSuccessUpdateResult(createFackeVacationGrantInfos());
			}
			return createFailUpdateResult(createFackeVacationGrantInfos());
		}

		@Override
		public UpdatedResults<VacationGrantInfo> updateMonthlyVacationEligiblities(
			List<VacationGrantInfo> eligibilities) {
			if (mothlySuccess) {
				return createSuccessUpdateResult(createFackeVacationGrantInfos());
			}
			return createFailUpdateResult(createFackeVacationGrantInfos());
		}
	}

	private static class TestVacationEligibilitiesRepositoryFake extends AbsentTestVacationEligibilitiesRepositoryFake {

		private final List<VacationGrantInfo> annualGrantInfos;
		private final List<VacationGrantInfo> monthlyGrantInfos;

		public TestVacationEligibilitiesRepositoryFake(List<VacationGrantInfo> annualGrantInfos,
			List<VacationGrantInfo> monthlyGrantInfos) {
			this.annualGrantInfos = annualGrantInfos;
			this.monthlyGrantInfos = monthlyGrantInfos;
		}

		@Override
		public UpdatedResults<VacationGrantInfo> updateAnnualVacationEligiblities(
			List<VacationGrantInfo> eligibilities) {
			if (annualGrantInfos.equals(eligibilities)) {
				return createSuccessUpdateResult(eligibilities);
			}
			return createFailUpdateResult(eligibilities);
		}

		@Override
		public UpdatedResults<VacationGrantInfo> updateMonthlyVacationEligiblities(
			List<VacationGrantInfo> eligibilities) {
			if (monthlyGrantInfos.equals(eligibilities)) {
				return createSuccessUpdateResult(eligibilities);
			}
			return createFailUpdateResult(eligibilities);
		}
	}

	private abstract static class AbsentTestVacationEligibilitiesRepositoryFake
		implements VacationEligibilitiesRepository {

		protected UpdatedResults<VacationGrantInfo> createSuccessUpdateResult(
			List<VacationGrantInfo> annualGrantInfos) {
			return new UpdatedResults<>(new JdbcUpdated(new int[] {1}), annualGrantInfos);
		}

		protected UpdatedResults<VacationGrantInfo> createFailUpdateResult(List<VacationGrantInfo> annualGrantInfos) {
			return new UpdatedResults<>(new JdbcUpdated(new int[] {0}), annualGrantInfos);
		}

		@Override
		public List<VacationGrantEligibility> findEligibilities(LocalDate startJoinDate, LocalDate date) {
			return List.of();
		}
	}
}