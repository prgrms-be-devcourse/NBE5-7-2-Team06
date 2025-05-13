package programmers.team6.domain.vacation.repository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.repository.factory.TestMemberFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Transactional
@Import(value = TestMemberFactory.class)
class VacationInfoRepositoryTest {

	@Autowired
	private VacationInfoRepository vacationInfoRepository;
	@Autowired
	private TestMemberFactory memberFactory;

	@ParameterizedTest
	@CsvSource(value = {"2026-10-31,1", "2026-10-30,0", "2026-09-30,1"}, delimiter = ',')
	void 휴가대상자_검색(LocalDate now, int count) {
		Member member = memberFactory.defaultMember();
		vacationInfoRepository.save(new VacationInfo(15, 0, "testType", member.getId()));

		List<VacationGrantEligibility> result = vacationInfoRepository.findEligibilities(now.minusYears(1), now);

		assertThat(result).hasSize(count);
	}
}