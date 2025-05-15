package programmers.team6.domain.vacation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.auth.dto.request.MemberSignUpRequest;
import programmers.team6.domain.auth.service.AuthService;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationRequestCalendarResponse;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
import programmers.team6.domain.vacation.repository.VacationRepository;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;

@Slf4j
@SpringBootTest
class VacationServiceTests {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	AuthService authService;
	@Autowired
	private VacationRequestRepository vacationRequestRepository;
	@Autowired
	private VacationRepository vacationRepository;
	@Autowired
	private CodeRepository codeRepository;

	@Test
	@DisplayName("캘린더 결과 테스트")
	void select_calendar_test() throws Exception {

		MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(
			"홍길동",
			"hong@naver.com",
			1L,
			"01",
			LocalDateTime.now(),
			"1990-01-01",
			"password1234");

		authService.signUp(memberSignUpRequest);

		Member savedMember = memberRepository.findByEmail("hong@naver.com")
			.orElseThrow(() -> new RuntimeException("회원 저장 실패 "));

		Code code = codeRepository.save(new Code("VACATIOIN_TYPE", "01", "연차"));

		VacationRequest vacationRequest = new VacationRequest(savedMember, LocalDateTime.now(), LocalDateTime.now(),
			"사유", code,
			1);

		vacationRequestRepository.save(vacationRequest);
		vacationRequestRepository.save(vacationRequest);

		List<VacationRequestCalendarResponse> approvedVacationsByMonth = vacationRepository.findApprovedVacationsByMonth(
			VacationRequestStatus.IN_PROGRESS, LocalDateTime.now(),
			LocalDateTime.now());
		log.info("approvedVacationsByMonth.size() = {}", approvedVacationsByMonth.size());
		for (VacationRequestCalendarResponse vacationRequestCalendarResponse : approvedVacationsByMonth) {
			log.info("vacationRequestCalendarResponse = {}", vacationRequestCalendarResponse);
		}
	}

}