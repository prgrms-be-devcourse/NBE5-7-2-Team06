package programmers.team6.domain.vacation.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
	boolean existsByMemberId(Long memberId);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
					a.id, vr.type.name, vr.from, vr.to, vr.member.name,
					vr.member.dept.deptName, vr.member.position.name, a.approvalStatus
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = :step
		order by a.id desc
		""")
	Page<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId, int step, Pageable pageable);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
					a.id, vr.type.name, vr.from, vr.to, vr.member.name,
					vr.member.dept.deptName, vr.member.position.name, a.approvalStatus
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = :step
				and (:typeId is null or vr.type.id = :typeId)
				and (:name is null or vr.member.name = :name)
				and (:from is null or :to is null or (vr.from <= :to and  vr.to >= :from))
				and (:status is null or a.approvalStatus = :status)
		order by a.id desc
		""")
	Page<ApprovalFirstStepSelectResponse> findFirstStepByFilter(Long memberId, Long typeId, String name,
		LocalDateTime from, LocalDateTime to, ApprovalStatus status, int step, Pageable pageable);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse(
					a2.id, vr.type.name, vr.from, vr.to, vr.member.name,
					vr.member.dept.deptName, vr.member.position.name, a1.approvalStatus, a2.approvalStatus
				)
		from ApprovalStep a2 join a2.vacationRequest vr
				join ApprovalStep a1 on a1.vacationRequest.id = vr.id and a1.step = 1
		where a2.member.id = :memberId and a1.step = :step
		order by a2.id desc
		""")
	Page<ApprovalSecondStepSelectResponse> findSecondStepByMemberId(Long memberId, int step, Pageable pageable);

	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalSecondStepSelectResponse(
					a2.id, vr.type.name, vr.from, vr.to, vr.member.name,
					vr.member.dept.deptName, vr.member.position.name, a1.approvalStatus, a2.approvalStatus
				)
		from ApprovalStep a2 join a2.vacationRequest vr
				join ApprovalStep a1 on a1.vacationRequest.id = vr.id and a1.step = 1
		where a2.member.id = :memberId and a2.step = :step
				and (:typeId is null or vr.type.id = :typeId)
				and (:name is null or vr.member.name = :name)
				and (:from is null or :to is null or (vr.from <= :to and  vr.to >= :from))
				and (:status is null or a2.approvalStatus = :status)
		order by a2.id desc
		""")
	Page<ApprovalSecondStepSelectResponse> findSecondStepByFilter(Long memberId, Long typeId, String name,
		LocalDateTime from, LocalDateTime to, ApprovalStatus status, int step, Pageable pageable);

	Optional<ApprovalStep> findByIdAndMemberIdAndStep(Long id, Long memberId, int step);

	Optional<ApprovalStep> findByVacationRequestIdAndStep(Long vacationRequestId, int step);

}
