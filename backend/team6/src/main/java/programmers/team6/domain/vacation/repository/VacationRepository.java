package programmers.team6.domain.vacation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.vacation.dto.VacationRequestCalendarResponse;
import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public interface VacationRepository extends JpaRepository<VacationInfo, Integer> {
	Optional<VacationInfo> findByMemberId(Long memberId);

	@Query(
		"""
			SELECT m.name,
			d.deptName,
			vr.type.name,
			m.position.name
			FROM VacationRequest vr
			JOIN vr.member m
			JOIN m.dept d
			JOIN m.position p
			WHERE vr.status = :status
			 AND vr.from >= :start
			    AND vr.to < :end
			""")
	List<VacationRequestCalendarResponse> findApprovedVacationsByMonth(@Param("status") VacationRequestStatus status,
		LocalDateTime start, LocalDateTime end);
}
