package programmers.team6.domain.vacation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.repository.dto.JdbcUpdated;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.repository.VacationEligibilitiesRepository;
import programmers.team6.domain.vacation.rule.VacationGrantRuleFinder;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilities;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilitiesFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@ExtendWith(MockitoExtension.class)
class VacationInfoServiceTest {

	@Mock
	private VacationGrantEligibilitiesFactory factory;

	@Mock
	private VacationInfoRepository repository;

	@Test
	void 업데이트성공_테스트() {
		List<VacationGrantInfo> annualGrantInfos = List.of(new VacationGrantInfo(1, 3, 1));
		List<VacationGrantInfo> monthlyGrantInfos = List.of(new VacationGrantInfo(2, 1, 1));
		VacationInfoService service = new VacationInfoService(repository, factory,
			new TestVacationEligibilitiesRepositoryFake(annualGrantInfos, monthlyGrantInfos), null);
		VacationGrantEligibilities eligibilities = Mockito.mock(VacationGrantEligibilities.class);
		when(eligibilities.getAnnualVacationGrantInfos()).thenReturn(annualGrantInfos);
		when(eligibilities.getMonthlyVacationGrantInfos()).thenReturn(monthlyGrantInfos);

		assertThatCode(() -> service.updateEligiblities(eligibilities)).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@CsvSource({"true,false", "false,true"})
	void 업데이트_실패테스트(boolean annualSuccess, boolean monthlySuccess) {
		VacationInfoService service = new VacationInfoService(repository, factory,
			new SuccessAndFailTestVacationEligibilitiesRepositoryFake(annualSuccess, monthlySuccess), null);
		VacationGrantEligibilities eligibilities = Mockito.mock(VacationGrantEligibilities.class);

		assertThatThrownBy(() -> service.updateEligiblities(eligibilities)).isInstanceOf(RuntimeException.class);
	}

	@Test
	void 휴가총합수정테스트() {
		VacationInfoService service = new VacationInfoService(repository, factory, null, new VacationGrantRuleFinder());
		VacationInfoUpdateTotalCountRequest vacationInfoUpdateTotalCountRequest1 = createVacationDto(1, 12, "01", 0);
		VacationInfoUpdateTotalCountRequest vacationInfoUpdateTotalCountRequest2 = createVacationDto(2, 13, "02", 0);
		VacationInfoUpdateTotalCountRequests request = createUpdateTotalCountRequest(
			vacationInfoUpdateTotalCountRequest1, vacationInfoUpdateTotalCountRequest2);
		VacationInfo vacationInfo1 = createVacationInfo(vacationInfoUpdateTotalCountRequest1,
			VacationInfoUpdateResult.SUCCESS);
		VacationInfo vacationInfo2 = createVacationInfo(vacationInfoUpdateTotalCountRequest2,
			VacationInfoUpdateResult.SUCCESS);
		long memberId = 1L;
		when(repository.findAllByMemberId(memberId)).thenReturn(List.of(vacationInfo1, vacationInfo2));

		service.updateFrom(memberId, request);

		verify(vacationInfo1, times(1)).updateTotalCount(vacationInfoUpdateTotalCountRequest1.version(), 12);
		verify(vacationInfo2, times(1)).updateTotalCount(vacationInfoUpdateTotalCountRequest1.version(), 13);
	}

	private VacationInfoUpdateTotalCountRequest createVacationDto(int id, int totalCount, String type, int version) {
		return new VacationInfoUpdateTotalCountRequest(id, totalCount, type, version);
	}

	private VacationInfoUpdateTotalCountRequests createUpdateTotalCountRequest(
		VacationInfoUpdateTotalCountRequest... vacationInfoUpdateTotalCountRequests) {
		return new VacationInfoUpdateTotalCountRequests(List.of(vacationInfoUpdateTotalCountRequests));
	}

	private VacationInfo createVacationInfo(VacationInfoUpdateTotalCountRequest infoDto,
		VacationInfoUpdateResult result) {
		VacationInfo vacationInfo = mock(VacationInfo.class);
		when(vacationInfo.updateTotalCount(infoDto.version(), infoDto.totalCount())).thenReturn(result);
		when(vacationInfo.getVacationType()).thenReturn(infoDto.type());
		return vacationInfo;
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