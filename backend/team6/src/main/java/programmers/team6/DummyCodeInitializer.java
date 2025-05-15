package programmers.team6;

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
		DeptInsert("인사팀");
		DeptInsert("개발팀");
		DeptInsert("영업팀");

		insert("POSITION", "01", "사원");
		insert("POSITION", "02", "대리");
		insert("POSITION", "03", "과장");
		insert("POSITION", "04", "부장");
	}

	private void DeptInsert(String deptName) {
		deptRepository.save(Dept.builder().deptName(deptName).build());
	}

	private void insert(String groupCode, String code, String name) {

		codeRepository.save(Code.builder()
			.groupCode(groupCode)
			.code(code)
			.name(name)
			.build()
		);

	}
}
