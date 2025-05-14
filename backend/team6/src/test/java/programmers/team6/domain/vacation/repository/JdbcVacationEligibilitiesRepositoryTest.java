package programmers.team6.domain.vacation.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.transaction.annotation.Transactional;

import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.repository.factory.TestMemberFactory;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@SpringBootTest
@Transactional
@Import(value = TestMemberFactory.class)
class JdbcVacationEligibilitiesRepositoryTest {

	@Autowired
	private JdbcVacationEligibilitiesRepository vacationEligibilitiesRepository;
	@Autowired
	private VacationInfoRepository vacationInfoRepository;
	@Autowired
	private TestMemberFactory memberFactory;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static VacationGrantInfo createGrantInfo(VacationInfo vacationInfo) {
		return new VacationGrantInfo(vacationInfo.getVacationId(), (int)vacationInfo.getTotalCount() + 1,
			vacationInfo.getVersion());
	}

	@Test
	void 월차_업데이트_성공() {
		VacationInfo vacationInfo = getVacationInfo();

		UpdatedResults<VacationGrantInfo> vacationEligiblities = vacationEligibilitiesRepository.updateMonthlyVacationEligiblities(
			List.of(createGrantInfo(vacationInfo)));

		assertThat(vacationEligiblities.isAllUpdated()).isTrue();

		TestVacationInfo info = findById(vacationInfo.getVacationId());
		assertThat(info.totalCount()).isEqualTo(vacationInfo.getTotalCount() + 1);
		assertThat(info.useCount()).isEqualTo(vacationInfo.getUseCount());
		assertThat(info.version()).isEqualTo(vacationInfo.getVersion() + 1);
	}

	@Test
	void 월차_업데이트_실패() {
		VacationInfo vacationInfo1 = getVacationInfo();
		VacationInfo vacationInfo2 = getVacationInfo();
		List<VacationGrantInfo> grantInfos = List.of(createGrantInfo(vacationInfo1), createGrantInfo(vacationInfo2));
		updateTargetVersion(vacationInfo2);

		UpdatedResults<VacationGrantInfo> vacationEligiblities = vacationEligibilitiesRepository.updateMonthlyVacationEligiblities(
			grantInfos);

		assertThat(vacationEligiblities.isAllUpdated()).isFalse();

		assertThat(vacationEligiblities.updatedItems()).hasSize(1);
		assertThat(vacationEligiblities.updatedItems().getFirst()).isEqualTo(grantInfos.get(0));

		assertThat(vacationEligiblities.nonUpdatedItems()).hasSize(1);
		assertThat(vacationEligiblities.nonUpdatedItems().getFirst()).isEqualTo(grantInfos.get(1));
	}

	@Test
	void 연차_업데이트_성공() {
		VacationInfo vacationInfo = getVacationInfo();

		UpdatedResults<VacationGrantInfo> vacationEligiblities = vacationEligibilitiesRepository.updateAnnualVacationEligiblities(
			List.of(createGrantInfo(vacationInfo)));

		assertThat(vacationEligiblities.isAllUpdated()).isTrue();

		TestVacationInfo info = findById(vacationInfo.getVacationId());
		assertThat(info.totalCount()).isEqualTo(vacationInfo.getTotalCount() + 1);
		assertThat(info.useCount()).isZero();
		assertThat(info.version()).isEqualTo(vacationInfo.getVersion() + 1);
	}

	@Test
	void 년차_업데이트_실패() {
		VacationInfo vacationInfo1 = getVacationInfo();
		VacationInfo vacationInfo2 = getVacationInfo();
		List<VacationGrantInfo> grantInfos = List.of(createGrantInfo(vacationInfo1), createGrantInfo(vacationInfo2));
		updateTargetVersion(vacationInfo2);

		UpdatedResults<VacationGrantInfo> vacationEligiblities = vacationEligibilitiesRepository.updateAnnualVacationEligiblities(
			grantInfos);

		assertThat(vacationEligiblities.isAllUpdated()).isFalse();

		assertThat(vacationEligiblities.updatedItems()).hasSize(1);
		assertThat(vacationEligiblities.updatedItems().getFirst()).isEqualTo(grantInfos.get(0));

		assertThat(vacationEligiblities.nonUpdatedItems()).hasSize(1);
		assertThat(vacationEligiblities.nonUpdatedItems().getFirst()).isEqualTo(grantInfos.get(1));
	}

	private void updateTargetVersion(VacationInfo vacationInfo2) {
		jdbcTemplate.execute("update vacation_info set version = version + 1 where vacation_id = ?",
			(PreparedStatementCallback<Integer>)ps -> {
				ps.setInt(1, vacationInfo2.getVacationId());
				return ps.executeUpdate();
			});
	}

	private VacationInfo getVacationInfo() {
		Member member = memberFactory.defaultMember();
		return vacationInfoRepository.saveAndFlush(new VacationInfo(15, 3, "testType", member.getId()));
	}

	private TestVacationInfo findById(int id) {
		return jdbcTemplate.queryForObject(
			"SELECT * FROM vacation_info WHERE vacation_id = ?",
			(rs, rowNum) -> new TestVacationInfo(
				rs.getInt("total_count"),
				rs.getInt("use_count"),
				rs.getInt("version"),
				rs.getInt("member_id")
			),
			id
		);
	}

	private record TestVacationInfo(
		int totalCount,
		int useCount,
		int version,
		int memberId
	) {
	}

}