package programmers.team6.domain.vacation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
				a.id, vr.type.name, vr.from, vr.to, m.name, m.dept.deptName, m.position.name, a.status
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = :step
		""")
	List<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId, int step);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
				a.id, vr.type.name, vr.from, vr.to, m.name, m.dept.deptName, m.position.name, a.status
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = :step
				and (:typeId is null or vr.type.id = :typeId)
				and (:name is null or m.name = :name)
				and (:from is null or vr.from = :from)
				and (:to is null or vr.to = :to)
				and (:status is null or a.status = :status)
		""")
	List<ApprovalFirstStepSelectResponse> findFirstStepByFilter(
		Long memberId, Long typeId, String name, LocalDateTime from, LocalDateTime to, ApprovalStatus status, int step);

}
