package programmers.team6.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.auth.dto.request.MemberSignUpRequest;
import programmers.team6.domain.auth.service.AuthService;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.BasicCodeInfo;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.member.util.mapper.CodeMapper;

@Configuration
@RequiredArgsConstructor
public class CodeInitializationConfig {
	private final CodeRepository codeRepository;
	private final DeptRepository deptRepository;
	private final MemberRepository memberRepository;
	private final AuthService authService;

	/**
	 * 현재 개발환경이고 다른 엔티티의 변수 수정 가능성이 있는 상황에서 우선적으로 CommandLineRunner를 활용하여 개발하였음, 그럼으로 yml의 profile은 dev로 작성 필요
	 * 만약, 운영환경으로 넘어간다면 flyway로 수정하여 해당 부분또한 insert문이 포함된 sql 실행하도록 바꾸면 좋을듯
	 * @return
	 * @author gunwoong
	 */
	@Bean
	@Profile("dev")
	@Transactional
	public CommandLineRunner initData() {
		return args -> {
			if (codeRepository.count() == 0) {  // 데이터베이스에 데이터가 없으면 삽입
				insert("POSITION", "01", "사원");
				insert("POSITION", "02", "대리");
				insert("POSITION", "03", "과장");
				insert("POSITION", "04", "부장");

				for (int i = 0; i < BasicCodeInfo.values().length; i++) {
					codeRepository.save(CodeMapper.toCode(BasicCodeInfo.values()[i]));
				}

				Dept d1 = DeptInsert("인사팀");
				Dept d2 = DeptInsert("개발팀");
				Dept d3 = DeptInsert("영업팀");

				authService.signUp(
					new MemberSignUpRequest("김부장", "l1@a.com", 1L, "04", LocalDateTime.of(2023, 5, 15, 0, 0), "850101",
						"123456q!"));
				authService.signUp(
					new MemberSignUpRequest("이부장", "l2@a.com", 2L, "04", LocalDateTime.of(2023, 5, 15, 0, 0), "831111",
						"123456q!"));
				authService.signUp(
					new MemberSignUpRequest("박부장", "l3@a.com", 3L, "04", LocalDateTime.of(2023, 5, 15, 0, 0), "871101",
						"123456q!"));

				setDeptLeader(d1, d2, d3);
			}
		};
	}

	private void insert(String groupCode, String code, String name) {
		codeRepository.save(Code.builder()
			.groupCode(groupCode)
			.code(code)
			.name(name)
			.build());
	}

	private Dept DeptInsert(String deptName) {
		return deptRepository.save(Dept.builder().deptName(deptName).build());
	}

	private void setDeptLeader(Dept d1, Dept d2, Dept d3) {
		Member m1 = memberRepository.findById(1L).orElseThrow();
		Member m2 = memberRepository.findById(2L).orElseThrow();
		Member m3 = memberRepository.findById(3L).orElseThrow();

		d1.appointLeader(m1);
		d2.appointLeader(m2);
		d3.appointLeader(m3);

	}

}
