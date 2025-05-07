package programmers.team6.global;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;

@Component
@RequiredArgsConstructor
public class DummyCodeInitializer implements CommandLineRunner {

	private final CodeRepository codeRepository;

	private final DeptRepository deptRepository;

	@Override
	public void run(String... args) {
		insertDept("인사팀");
		insertDept("개발팀");
		insertDept("영업팀");

		insertCode("POSITION", "01", "사원");
		insertCode("POSITION", "02", "대리");
		insertCode("POSITION", "03", "과장");
		insertCode("POSITION", "04", "부장");
	}

	private void insertCode(String groupCode, String code, String name) {

		codeRepository.save(Code.builder()
			.groupCode(groupCode)
			.code(code)
			.name(name)
			.build()
		);

	}

	private void insertDept(String deptName) {
		deptRepository.save(Dept.builder().deptName(deptName).build());
	}
}
