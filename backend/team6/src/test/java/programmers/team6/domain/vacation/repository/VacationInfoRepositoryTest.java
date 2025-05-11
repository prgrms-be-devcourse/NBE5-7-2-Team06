package programmers.team6.domain.vacation.repository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Dept;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.member.enums.Role;
import programmers.team6.domain.member.repository.CodeRepository;
import programmers.team6.domain.member.repository.DeptRepository;
import programmers.team6.domain.member.repository.MemberRepository;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Transactional
class VacationInfoRepositoryTest {

	@Autowired
	private VacationInfoRepository vacationInfoRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private CodeRepository codeRepository;
	@Autowired
	private DeptRepository deptRepository;

	@ParameterizedTest
	@CsvSource(value = {"2026-10-31,1","2026-10-30,0","2026-09-30,1"},delimiter = ',')
	void findEligibilities(LocalDate now,int count) {
		Member member = getMember();
		vacationInfoRepository.save(new VacationInfo(15,0,"testType",member.getId()));

		List<VacationGrantEligibility> result = vacationInfoRepository.findEligibilities(now.minusYears(1), now);

		assertThat(result).hasSize(count);
	}

	private Member getMember() {
		LocalDateTime joinDate = LocalDateTime.of(2025, 10, 31, 0, 0, 0);
		Code code = codeRepository.save( new Code("1", "1", "code"));
		Dept dept = deptRepository.save(new Dept("code", null));
		Member member = new Member("test1", dept, code, joinDate, Role.USER);
		return memberRepository.save(member);
	}
}