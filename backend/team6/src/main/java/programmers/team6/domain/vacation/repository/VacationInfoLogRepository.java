package programmers.team6.domain.vacation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.vacation.entity.VacationInfoLog;

public interface VacationInfoLogRepository extends JpaRepository<VacationInfoLog, Long> {

	@Query("""
		select vl
		from VacationInfoLog vl
		where vl.memberId in :ids and vl.vacationType = :code
		""")
	List<VacationInfoLog> findLastedByMemberIdInAndYear(List<Long> ids, String code);
}
