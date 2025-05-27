package programmers.team6.domain.admin.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import programmers.team6.domain.admin.dto.AdminVacationSearchCondition;
import programmers.team6.domain.admin.dto.VacationRequestSearchResponse;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.Role;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.domain.vacation.enums.ApprovalStatus;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
import programmers.team6.domain.vacation.repository.ApprovalStepRepository;
import programmers.team6.domain.vacation.repository.VacationRequestRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AdminVacationRequestSearchCustom.class)
class AdminVacationRequestSearchCustomTest {
	@Autowired
	private AdminVacationRequestSearchCustom adminVacationRequestSearchCustom;
	@Autowired
	private VacationRequestRepository vacationRequestRepository;
	@Autowired
	private ApprovalStepRepository approvalStepRepository;
	@Autowired
	private CodeRepository codeRepository;
	@Autowired
	private DeptRepository deptRepository;
	@Autowired
	private MemberRepository memberRepository;
	private Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
	private int vacationRequestersCnt = 3;
	/**
	 * 시나리오
	 * 휴가 요청 기간 2025년~2026년, 1월~12월, 각각 1일부터 마지막날까지
	 * 3명의 각기다른 직급과 부서, 휴가타입의 휴가 신청자, 부서별 직급이 같은 1차 결재자, 공통 2차 결재자
	 *
	 * ex)
	 * 날짜
	 * 2025년 1월 1일~31일, 2025년 2월 1일~28일..., 2026 12월 1일~31일
	 * 결재 라인 및 직급 이름
	 * A(부서0, 직급0, 휴가0) - A`(부서0, 직급3) - D(부서0, 직급4)
	 * B(부서1, 직급1, 휴가1) - B`(부서1, 직급3) - D(부서0, 직급4)
	 * C(부서2, 직급2, 휴가2) - C`(부서2, 직급3) - D(부서0, 직급4)
	 *
	 * 전체 휴가 신청 개수 = 2 x 12
	 */
	@BeforeEach
	void setUp() {
		// code
		String vacationGroupCode = UUID.randomUUID().toString();
		List<Code> vacationTypeCodes = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			vacationTypeCodes.add(generateTestCode(vacationGroupCode, i, String.format("휴가%d", i)));
		}
		codeRepository.saveAll(vacationTypeCodes);

		String deptGroupCode = UUID.randomUUID().toString();
		List<Code> deptCodes = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			deptCodes.add(generateTestCode(deptGroupCode, i, String.format("부서%d", i)));
		}
		codeRepository.saveAll(deptCodes);

		String positionGroupCode = UUID.randomUUID().toString();
		List<Code> positionCodes = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			positionCodes.add(generateTestCode(positionGroupCode, i, String.format("직급%d", i)));
		}
		codeRepository.saveAll(positionCodes);

		// dept
		List<Dept> depts = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			depts.add(Dept.builder().deptName(String.format("부서%d", i)).build());
		}
		deptRepository.saveAll(depts);

		// member
		Member secondApprover = memberRepository.save(
			Member.builder()
				.name("D")
				.dept(depts.get(0))
				.position(positionCodes.get(4))
				.joinDate(LocalDateTime.now())
				.role(
					Role.USER)
				.build()
		);

		List<Member> firstApprovers = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			firstApprovers.add(Member.builder()
				.name(String.format("%s`", (char)('A' + i)))
				.dept(depts.get(i))
				.position(positionCodes.get(3))
				.joinDate(LocalDateTime.now())
				.role(
					Role.USER)
				.build());
		}
		memberRepository.saveAll(firstApprovers);

		List<Member> vacationRequesters = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			vacationRequesters.add(Member.builder()
				.name(String.format("%s", (char)('A' + i)))
				.dept(depts.get(i))
				.position(positionCodes.get(i))
				.joinDate(LocalDateTime.now())
				.role(
					Role.USER)
				.build());
		}
		memberRepository.saveAll(vacationRequesters);

		// appoint dept leader
		for (int i = 0; i < 3; i++) {
			depts.get(i).appointLeader(firstApprovers.get(i));
		}

		// entityManager.flush();
		for (int p = 0; p < 3; p++) {
			for (int year = 2025; year < 2027; year++) {
				for (int month = 1; month < 13; month++) {
					YearMonth yearMonth = YearMonth.of(year, month);
					LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
					LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

					VacationRequest vacationRequest = VacationRequest.builder()
						.member(vacationRequesters.get(p))
						.from(start)
						.to(end)
						.reason("testReason")
						.type(vacationTypeCodes.get(p))
						.status(
							month == 1 ? VacationRequestStatus.APPROVED : VacationRequestStatus.IN_PROGRESS).build();
					vacationRequestRepository.save(vacationRequest);

					approvalStepRepository.save(
						ApprovalStep.builder()
							.step(1)
							.approvalStatus(month == 1 ? ApprovalStatus.APPROVED : ApprovalStatus.PENDING)
							.member(firstApprovers.get(p))
							.vacationRequest(vacationRequest)
							.build()
					);
					approvalStepRepository.save(
						ApprovalStep.builder()
							.step(2)
							.approvalStatus(month == 1 ? ApprovalStatus.APPROVED : ApprovalStatus.PENDING)
							.member(secondApprover)
							.vacationRequest(vacationRequest)
							.build()
					);
				}
			}
		}

	}

	private Code generateTestCode(String groupCode, int code, String name) {
		return Code.builder()
			.groupCode(groupCode)
			.code(String.format("%02d", code))
			.name(name)
			.build();
	}

	@Nested
	class should_searchVacationRequests {
		@Test
		void when_default() {
			// given
			AdminVacationSearchCondition defaultSearchCondition = new AdminVacationSearchCondition(null, null, null);

			// when
			Page<VacationRequestSearchResponse> defaultSearchResult = adminVacationRequestSearchCustom.search(
				defaultSearchCondition,
				pageable);

			// then
			assertThat(defaultSearchResult).hasSize(12 * 2 * 3);
		}

		@ParameterizedTest
		@MethodSource("startAndEndProvider")
		void when_givenStartAndEnd(LocalDate start,LocalDate end,int result) {
			// given
			AdminVacationSearchCondition searchCondition = new AdminVacationSearchCondition(
				new AdminVacationSearchCondition.DateRangeCondition(start, end, null, null), null, null);

			// when
			Page<VacationRequestSearchResponse> searchResult = adminVacationRequestSearchCustom.search(searchCondition,
				pageable);
			for (VacationRequestSearchResponse vacationRequestSearchResponse : searchResult) {
				System.out.println(vacationRequestSearchResponse);
			}

			// then
			assertThat(searchResult).hasSize(result);
		}

		private static Stream<Arguments> startAndEndProvider() {
			return Stream.of(
				Arguments.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31), 1*3),
				Arguments.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1), 1*3),
				Arguments.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), 12*3)
			);
		}

	}
}
