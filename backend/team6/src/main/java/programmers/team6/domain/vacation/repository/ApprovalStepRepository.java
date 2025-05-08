package programmers.team6.domain.vacation.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
	boolean existsByMemberId(Long memberId);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
					a.id, vr.type.name, vr.from, vr.to, vr.member.name,
					vr.member.dept.deptName, vr.member.position.name, a.status
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = :step
		order by a.id desc
		""")
	Page<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId, int step, Pageable pageable);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
					a.id, vr.type.name, vr.from, vr.to, vr.member.name,
					vr.member.dept.deptName, vr.member.position.name, a.status
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = :step
				and (:typeId is null or vr.type.id = :typeId)
				and (:name is null or vr.member.name = :name)
				and (:from is null or :to is null or (vr.from <= :to and  vr.to >= :from))
				and (:status is null or a.status = :status)
		order by a.id desc
		""")
	Page<ApprovalFirstStepSelectResponse> findFirstStepByFilter(Long memberId, Long typeId, String name,
		LocalDateTime from, LocalDateTime to, ApprovalStatus status, int step, Pageable pageable);

}
