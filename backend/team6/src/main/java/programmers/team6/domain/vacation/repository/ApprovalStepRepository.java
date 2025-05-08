package programmers.team6.domain.vacation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
	@Query("""
		select new programmers.team6.domain.vacation.dto.ApprovalFirstStepSelectResponse(
				a.id, vr.type.name, vr.from, vr.to, m.name, m.dept.deptName, m.position.name, a.status
				)
		from ApprovalStep a join a.member m join a.vacationRequest vr
		where m.id = :memberId and a.step = 1
		""")
	List<ApprovalFirstStepSelectResponse> findFirstStepByMemberId(Long memberId);
}
