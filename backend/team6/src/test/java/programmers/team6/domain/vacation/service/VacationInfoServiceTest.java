package programmers.team6.domain.vacation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.Role;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequest;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequests;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.repository.VacationInfoRepository;
import programmers.team6.domain.vacation.rule.VacationGrantRuleFinder;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.AnnualVacationInfosFactory;

@ExtendWith(MockitoExtension.class)
class VacationInfoServiceTest {

	@Mock
	private AnnualVacationInfosFactory factory;

	@Mock
	private VacationInfoRepository repository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private VacationInfoLogPublisher publisher;

	@Test
	void 업데이트성공_테스트() {
		VacationInfoService service = createService(new AnnualVacationInfosFactory());
		VacationInfo info = new VacationInfo(3, 0, "test", 1L);
		List<VacationInfo> infos = List.of(info);
		when(repository.findAnnualVacationFrom(any(), any())).thenReturn(infos);
		when(repository.findMonthlyVacationFrom(any(), any())).thenReturn(infos);
		LocalDateTime joinDate = LocalDateTime.of(2024, 10, 18, 0, 0, 0);
		when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member("", null, null, joinDate, Role.ADMIN)));
		LocalDateTime localDateTime = joinDate.plusYears(1);

		assertThatCode(() -> service.grantAnnualEligiblities(localDateTime.toLocalDate())).doesNotThrowAnyException();
		assertThat(info.getTotalCount()).isEqualTo(19);
		assertThat(info.getUseCount()).isZero();
	}

	@Test
	void 휴가총합수정테스트() {
		VacationInfoService service = createService(factory);
		VacationInfoUpdateTotalCountRequest vacationInfoUpdateTotalCountRequest1 = createVacationDto(1, 12, "01", 0);
		VacationInfoUpdateTotalCountRequest vacationInfoUpdateTotalCountRequest2 = createVacationDto(2, 13, "02", 0);
		VacationInfoUpdateTotalCountRequests request = createUpdateTotalCountRequest(
			vacationInfoUpdateTotalCountRequest1, vacationInfoUpdateTotalCountRequest2);
		VacationInfo vacationInfo1 = createVacationInfo(vacationInfoUpdateTotalCountRequest1);
		VacationInfo vacationInfo2 = createVacationInfo(vacationInfoUpdateTotalCountRequest2);
		when(repository.findAllByVacationIdIn(List.of(1, 2))).thenReturn(List.of(vacationInfo1, vacationInfo2));

		service.updateFrom(new VacationInfoUpdateTotalCountRequestsList(List.of(request)));

		assertThat(vacationInfo1.getTotalCount()).isEqualTo(12);
		assertThat(vacationInfo2.getTotalCount()).isEqualTo(13);
	}

	private VacationInfoService createService(AnnualVacationInfosFactory factory) {
		return new VacationInfoService(memberRepository, repository, factory,
			new VacationGrantRuleFinder(), publisher);
	}

	private VacationInfoUpdateTotalCountRequest createVacationDto(int id, int totalCount, String type, int version) {
		return new VacationInfoUpdateTotalCountRequest(id, totalCount, type, version);
	}

	private VacationInfoUpdateTotalCountRequests createUpdateTotalCountRequest(
		VacationInfoUpdateTotalCountRequest... vacationInfoUpdateTotalCountRequests) {
		return new VacationInfoUpdateTotalCountRequests(1L, List.of(vacationInfoUpdateTotalCountRequests));
	}

	private VacationInfo createVacationInfo(VacationInfoUpdateTotalCountRequest infoDto) {
		return new VacationInfo(infoDto.totalCount(), 0, infoDto.type(), 1L);
	}
}