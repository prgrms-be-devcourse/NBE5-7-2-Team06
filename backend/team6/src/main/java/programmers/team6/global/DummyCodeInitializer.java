package programmers.team6.global;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.repository.CodeRepository;

@Component
@RequiredArgsConstructor
public class DummyCodeInitializer implements CommandLineRunner {

	private final CodeRepository codeRepository;

	@Override
	public void run(String... args) {
		insert("DEPT", "01", "인사팀");
		insert("DEPT", "02", "개발팀");
		insert("DEPT", "03", "영업팀");

		insert("POSITION", "01", "사원");
		insert("POSITION", "02", "대리");
		insert("POSITION", "03", "과장");
		insert("POSITION", "04", "부장");
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
