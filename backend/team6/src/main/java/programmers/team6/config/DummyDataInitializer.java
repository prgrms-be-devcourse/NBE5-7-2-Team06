package programmers.team6.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.Role;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;
import programmers.team6.domain.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class DummyDataInitializer implements CommandLineRunner {

	private final CodeRepository codeRepository;
	private final MemberRepository memberRepository;
	private final DeptRepository deptRepository;

	@Override
	@Transactional
	public void run(String... args) {
		// 1. 코드 데이터 생성
		createCodes();

		// 2. 부서 및 사용자 생성
		createDeptAndMembers();
	}

	private void createCodes() {
		// 부서 코드
		insertCode("DEPT", "01", "인사팀");
		insertCode("DEPT", "02", "개발팀");
		insertCode("DEPT", "03", "영업팀");

		// 직급 코드
		insertCode("POSITION", "01", "사원");
		insertCode("POSITION", "02", "대리");
		insertCode("POSITION", "03", "과장");
		insertCode("POSITION", "04", "부장");

		// 휴가 유형 코드 - 이 부분은 필수입니다!
		insertCode("VACATION_TYPE", "ANNUAL", "연차");
		insertCode("VACATION_TYPE", "HALF", "반차");
		insertCode("VACATION_TYPE", "SICK", "병가");
		insertCode("VACATION_TYPE", "SPECIAL", "특별휴가");
	}

	private void createDeptAndMembers() {
		// 직급 코드 조회
		Code managerPosition = codeRepository.findByGroupCodeAndCode("POSITION", "04")
			.orElseThrow(() -> new RuntimeException("부장 직급 코드를 찾을 수 없습니다."));

		Code staffPosition = codeRepository.findByGroupCodeAndCode("POSITION", "01")
			.orElseThrow(() -> new RuntimeException("사원 직급 코드를 찾을 수 없습니다."));

		// 1. 먼저 부서를 생성 (부서장 없이)
		Dept devDept = Dept.builder()
			.deptName("개발팀")
			.build();

		deptRepository.save(devDept);

		// 2. 부서장 생성 (부서와 함께)
		Member deptLeader = Member.builder()
			.name("김부장")
			.dept(devDept)
			.position(managerPosition)
			.role(Role.USER)
			.build();

		memberRepository.save(deptLeader);

		// 3. 부서에 부서장 설정
		devDept.setDeptLeader(deptLeader);
		deptRepository.save(devDept);

		// 4. 부서원 생성
		Member staff = Member.builder()
			.name("이사원")
			.dept(devDept)
			.position(staffPosition)
			.role(Role.USER)
			.build();

		memberRepository.save(staff);
	}

	private void insertCode(String groupCode, String code, String name) {
		if (!codeRepository.existsByGroupCodeAndCode(groupCode, code)) {
			codeRepository.save(Code.builder()
				.groupCode(groupCode)
				.code(code)
				.name(name)
				.build()
			);
		}
	}
}