package programmers.team6.domain.vacation.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.repository.dto.JdbcUpdated;
import programmers.team6.domain.vacation.repository.dto.UpdatedResults;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantInfo;

@Repository
@RequiredArgsConstructor
@Transactional
public class JdbcVacationEligibilitiesRepository {

	private final JdbcTemplate jdbcTemplate;

	public UpdatedResults<VacationGrantInfo> updateAnnualVacationEligiblities(List<VacationGrantInfo> eligibilities) {
		String sql = "UPDATE vacation_info "
			+ "SET total_count = ? , "
			+ "version = version + 1, "
			+ "use_count = 0, "
			+ "updated_at = NOW()"
			+ "WHERE vacation_id = ? and version = ?";
		int[] flags = update(sql, eligibilities);
		return new UpdatedResults<>(new JdbcUpdated(flags), eligibilities);
	}

	public UpdatedResults<VacationGrantInfo> updateMonthlyVacationEligiblities(List<VacationGrantInfo> eligibilities) {
		String sql = "UPDATE vacation_info "
			+ "SET total_count = ? , "
			+ "version = version + 1, "
			+ "updated_at = NOW()"
			+ "WHERE vacation_id = ? and version = ?";
		int[] flags = update(sql, eligibilities);
		return new UpdatedResults<>(new JdbcUpdated(flags), eligibilities);
	}

	private int[] update(String sql, List<VacationGrantInfo> eligibilities) {
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				VacationGrantInfo vacationGrantInfo = eligibilities.get(i);
				ps.setInt(1, vacationGrantInfo.totalCount());
				ps.setInt(2, vacationGrantInfo.vacationInfoId());
				ps.setInt(3, vacationGrantInfo.version());
			}

			@Override
			public int getBatchSize() {
				return eligibilities.size();
			}
		});
	}
}
