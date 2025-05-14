package programmers.team6.domain.vacation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibility;

public interface VacationInfoRepository extends JpaRepository<VacationInfo, Integer> {

	@Query(value = "SELECT COUNT(DISTINCT vi.memberId) FROM VacationInfo vi")
	long countAllMemberIds();

	Optional<VacationInfo> findByMemberIdAndVacationType(Long memberId, String vacationType);

	@Query("SELECT vi "
		+ "FROM VacationInfo vi "
		+ "JOIN Member m ON vi.memberId = m.id "
		+ "WHERE (FUNCTION('date', m.joinDate) > :startJoinDate "
		+ "AND FUNCTION('day', m.joinDate) = FUNCTION('day', :currentDate)) "
		+ "   OR (FUNCTION('date', m.joinDate) > :startJoinDate "
		+ "AND FUNCTION('day', m.joinDate) > FUNCTION('day', FUNCTION('last_day', :currentDate)) "
		+ "       AND FUNCTION('day', :currentDate) = FUNCTION('day', FUNCTION('last_day', :currentDate))) ")
	List<VacationInfo> findMonthlyVacationFrom(@Param("startJoinDate") LocalDate startJoinDate,
		@Param("currentDate") LocalDate currentDate);

	@Query("SELECT vi "
		+ "FROM VacationInfo vi "
		+ "JOIN Member m ON vi.memberId = m.id "
		+ "WHERE (FUNCTION('date', m.joinDate) <= :startJoinDate "
		+ "AND FUNCTION('day', m.joinDate) = FUNCTION('day', :currentDate) "
		+ "AND FUNCTION('month', m.joinDate) = FUNCTION('month', :currentDate))")
	List<VacationInfo> findAnnualVacationFrom(@Param("startJoinDate") LocalDate startJoinDate,
		@Param("currentDate") LocalDate currentDate);

	List<VacationInfo> findAllByMemberId(Long memberId);

	List<VacationInfo> findAllByMemberIdIn(List<Long> memberIds);
}
