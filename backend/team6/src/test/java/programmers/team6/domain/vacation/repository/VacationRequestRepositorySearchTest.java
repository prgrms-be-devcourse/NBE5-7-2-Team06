package programmers.team6.domain.vacation.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.dto.VacationRequestSearchCustom;

@SpringBootTest
@Transactional
	/**
	 * 현재 조회 및 필터 검색 과정을 테스트하기엔 생성 기능이 제공되지 않아 임시로 테스트 데이터를 작성하고 진행하였음
	 * 추후 필히 제거 요망, 검색 기능 테스트
	 * 조회시 검색 기능을 테스트하기 위해 테스트마다 데이터를 추가 생성하기엔 시간이 많이 걸려 로컬 환경에서 임의로 데이터 생성하고 테스트 진행
	 */
class VacationRequestRepositorySearchTest {
	@Autowired
	private VacationRequestRepository vacationRequestRepository;
	@Autowired
	private ApprovalStepRepository approvalStepRepository;
	@Autowired
	private CodeRepository codeRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	VacationRequestSearchCustom vacationRequestSearchCustom;

	// @Test
	// @Rollback(value = false)
	// void reset() {
	// 	vacationRequestRepository.deleteAll();
	// 	approvalStepRepository.deleteAll();
	// 	codeRepository.deleteAll();
	// 	memberRepository.deleteAll();
	// }
	//
	// @Test
	// @Rollback(value = false)
	// void init(@Autowired EntityManager entityManager) {
	// 	Code positionCode = codeRepository.save(new Code("POSITION", "pos", "posName"));
	//
	// 	for (int i = 0; i < 3; i++) {
	// 		Dept dept = new Dept("dept " + i);
	// 		Member member = new Member("member " + i, dept, positionCode);
	// 		List<Member> approvers = new ArrayList<>();
	// 		for (int j = 0; j < 5; j++) {
	// 			approvers.add(new Member(String.format("approver %d %d", i, j), dept,
	// 				new Code("POSITION", "pos " + (10 * i + j + 1), "posName " + (10 * i + j))));
	// 		}
	//
	// 		Code code = new Code("VACATION" + i, "vac " + i, "vacName " + i);
	//
	// 		// 2023 , 2024 , 2025
	// 		for (int year = 2023; year < 2026; year++) {
	// 			// 1 2 3 4
	// 			for (int month = 1; month < 5; month++) {
	// 				// 1 2 3
	// 				for (int day = 1; day < 4; day++) {
	// 					VacationRequest vr = VacationRequest.builder()
	// 						.from(LocalDate.of(year, month, day))
	// 						.to(LocalDate.of(year, month, day + 1))
	// 						.member(member)
	// 						.type(code)
	// 						.build();
	// 					List<ApprovalStep> approvalSteps = new ArrayList<>();
	// 					for (int j = 0; j < 3; j++) {
	// 						approvalSteps.add(new ApprovalStep(j, ApprovalStatus.IN_PROGRESS, approvers.get(j), vr));
	// 					}
	// 					if (day == 1) {
	// 						approvalSteps.get(2).apply(ApprovalStatus.APPROVED);
	// 						vr.updateStatus(VacationRequestStatus.APPROVED);
	// 						for (int j = 0; j < 2; j++) {
	// 							vr.processVacationRequest(approvalSteps.size() - 1);
	// 						}
	// 					}
	// 					approvalStepRepository.saveAll(approvalSteps);
	// 				}
	// 			}
	// 		}
	//
	// 	}
	// 	entityManager.flush();
	// 	entityManager.close();
	// }
	//
	// @Test
	// void testDefault() {
	// 	Page<VacationRequestReadResponse> result1 = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null,
	// 			null, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	Assertions.assertThat(result1).hasSize(3 * 4 * 3 * 3);
	//
	// 	Page<VacationRequestReadResponse> result2 = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null,
	// 			null, null, null),
	// 		PageRequest.of(0, 3)
	// 	);
	//
	// 	Assertions.assertThat(result2).hasSize(3);
	// }
	//
	// @Test
	// void testDate() {
	// 	Page<VacationRequestReadResponse> result = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1), null, null, null, null,
	// 			null, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	assertThat(result).hasSize(3 * 4 * 3);
	// }
	//
	// @Test
	// void testQuarter() {
	// 	// Page<VacationRequestReadResponse> result = vacationRequestSearchCustom.search(
	// 	// 	new AdminVacationSearchCondition(null, null, 2025, Quarter.Q1, null, null,
	// 	// 		null, null, null, null),
	// 	// 	PageRequest.of(0, Integer.MAX_VALUE)
	// 	// );
	// 	// assertThat(result).hasSize(19 * 2 * 3);
	// 	//
	// 	// Page<VacationRequestReadResponse> result2 = vacationRequestSearchCustom.search(
	// 	// 	new AdminVacationSearchCondition(null, null, 2025, Quarter.H1, null, null,
	// 	// 		null, null, null, null),
	// 	// 	PageRequest.of(0, Integer.MAX_VALUE)
	// 	// );
	// 	// assertThat(result2).hasSize(19 * 3 * 3);
	// }
	//
	// @Test
	// void testApplicantName() {
	// 	Page<VacationRequestReadResponse> result = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, "0", null,
	// 			null, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result) {
	// 		assertThat(vacationRequestReadResponse.getApplicantName()).contains("0");
	// 	}
	// 	assertThat(result).hasSize(3 * 4 * 3);
	// }
	//
	// @Test
	// void testDeptName() {
	// 	Page<VacationRequestReadResponse> result = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, "0",
	// 			null, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result) {
	// 		assertThat(vacationRequestReadResponse.getDeptName()).contains("0");
	// 	}
	// 	assertThat(result).hasSize(3 * 4 * 3);
	// }
	//
	// @Test
	// void testCodeId() {
	// 	// 휴가 종류
	// 	Page<VacationRequestReadResponse> result1 = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null,
	// 			3L, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result1) {
	// 		assertThat(vacationRequestReadResponse.getApplicantName()).isEqualTo("member 0");
	// 	}
	// 	assertThat(result1).hasSize(3 * 4 * 3);
	//
	// 	// 휴가 신청자 포지션
	// 	Page<VacationRequestReadResponse> result2 = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null,
	// 			1L, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result2) {
	// 		assertThat(vacationRequestReadResponse.getApplicantName()).startsWith("member");
	// 	}
	// 	assertThat(result2).hasSize(3 * 4 * 3 * 3);
	//
	// 	// 결재자 포지션
	// 	Page<VacationRequestReadResponse> result = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null,
	// 			2L, null, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	//
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result) {
	// 		assertThat(vacationRequestReadResponse.getApproverName()).isEqualTo("approver 0 0");
	// 	}
	// 	assertThat(result).hasSize((3 - 1) * 4 * 3);
	// }
	//
	// @Test
	// void testVacationRequestStatus() {
	// 	Page<VacationRequestReadResponse> result2 = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null, null,
	// 			VacationRequestStatus.APPROVED, null),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result2) {
	// 		assertThat(vacationRequestReadResponse.getStatus()).isEqualTo(VacationRequestStatus.APPROVED);
	// 	}
	// 	assertThat(result2).hasSize(4 * 3 * 3);
	// }
	//
	// @Test
	// void testApproverName() {
	// 	Page<VacationRequestReadResponse> result2 = vacationRequestSearchCustom.search(
	// 		new AdminVacationSearchCondition(null, null, null, null, null, null, null,
	// 			null, "approver 0"),
	// 		PageRequest.of(0, Integer.MAX_VALUE)
	// 	);
	// 	for (VacationRequestReadResponse vacationRequestReadResponse : result2) {
	// 		assertThat(vacationRequestReadResponse.getApproverName()).startsWith("approver 0");
	// 	}
	// 	assertThat(result2).hasSize(3 * 4 * 3);
	// }
}
