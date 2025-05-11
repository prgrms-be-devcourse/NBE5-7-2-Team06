package programmers.team6.domain.vacation.rule.vacationgranteligiblities;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import programmers.team6.domain.vacation.rule.TestMemberVacationInfoBuilder;

class VacationVacationGrantEligibilitiesTest {

	private final List<Integer> ids = List.of(1, 3, 5);
	private final VacationEligibilityCriteria mockCriteria = createMockCriteria(ids);

	@Test
	void 연차대상자_휴가_제공내용조회() {
		List<VacationGrantEligibility> infos = createMemberVacationInfos(5);
		VacationGrantEligibilities vacationGrantEligibilities = new VacationGrantEligibilities(infos, mockCriteria);

		List<VacationGrantInfo> annualVacationGrantInfos = vacationGrantEligibilities.getAnnualVacationGrantInfos();

		List<Integer> annualVacationIds = toIds(annualVacationGrantInfos);
		assertThatList(annualVacationIds).containsAll(ids);
	}

	@Test
	void 월차대상자_휴가_제공내용조회() {
		List<VacationGrantEligibility> infos = createMemberVacationInfos(5);
		VacationGrantEligibilities vacationGrantEligibilities = new VacationGrantEligibilities(infos, mockCriteria);

		List<VacationGrantInfo> monthlyVacationGrantInfos = vacationGrantEligibilities.getMonthlyVacationGrantInfos();

		List<Integer> monthlyVacationIds = toIds(monthlyVacationGrantInfos);
		assertThatList(monthlyVacationIds).doesNotContainAnyElementsOf(ids);
	}

	private List<VacationGrantEligibility> createMemberVacationInfos(int size) {
		List<VacationGrantEligibility> vacationGrantEligibilities = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			vacationGrantEligibilities.add(TestMemberVacationInfoBuilder.defaultInitBuilder().id(i + 1).build());
		}
		return vacationGrantEligibilities;
	}

	private static VacationEligibilityCriteria createMockCriteria(List<Integer> ids) {
		VacationEligibilityCriteria mockCriteria = Mockito.mock(VacationEligibilityCriteria.class);
		when(mockCriteria.isAnnualVacationEligible(argThat(value -> ids.contains(value.id())))).thenReturn(true);
		when(mockCriteria.isMonthlyVacationEligible(argThat(value -> !ids.contains(value.id())))).thenReturn(true);
		when(mockCriteria.toGrantInfo(any()))
			.thenAnswer(invocation -> {
				VacationGrantEligibility info = invocation.getArgument(0);
				return new VacationGrantInfo(info.id(), 0,0);
			});
		return mockCriteria;
	}

	private static List<Integer> toIds(List<VacationGrantInfo> monthlyVacationGrantInfos) {
		return monthlyVacationGrantInfos.stream()
			.map(VacationGrantInfo::vacationInfoId)
			.toList();
	}
}