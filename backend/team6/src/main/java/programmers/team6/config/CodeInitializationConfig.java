package programmers.team6.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.member.enums.BasicCodeInfo;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.util.mapper.CodeMapper;

@Configuration
@RequiredArgsConstructor
public class CodeInitializationConfig {
	private final CodeRepository codeRepository;

	/**
	 * 현재 개발환경이고 다른 엔티티의 변수 수정 가능성이 있는 상황에서 우선적으로 CommandLineRunner를 활용하여 개발하였음, 그럼으로 yml의 profile은 dev로 작성 필요
	 * 만약, 운영환경으로 넘어간다면 flyway로 수정하여 해당 부분또한 insert문이 포함된 sql 실행하도록 바꾸면 좋을듯
	 * @return
	 * @author gunwoong
	 */
	@Bean
	@Profile("dev")
	public CommandLineRunner initData() {
		return args -> {
			if (codeRepository.count() == 0) {  // 데이터베이스에 데이터가 없으면 삽입
				for (int i = 0; i < BasicCodeInfo.values().length; i++) {
					codeRepository.save(CodeMapper.toCode(BasicCodeInfo.values()[i]));
				}
			}
		};
	}

}
