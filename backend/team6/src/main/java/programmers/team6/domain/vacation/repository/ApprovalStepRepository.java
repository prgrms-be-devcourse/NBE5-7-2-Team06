package programmers.team6.domain.vacation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import programmers.team6.domain.admin.dto.ApprovalStepDetailUpdateResponse;
import programmers.team6.domain.vacation.entity.ApprovalStep;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
	@Query(value =
			"select new programmers.team6.domain.admin.dto.ApprovalStepDetailUpdateResponse(m.name,asp.reason) from ApprovalStep asp "
			+ "join VacationRequest vr on asp.vacationRequest=vr join asp.member m "
			+ "where vr.id = :vacationId order by asp.step")
	List<ApprovalStepDetailUpdateResponse> findApprovalStepDetailById(@Param("vacationId") Long vacationId);

	List<ApprovalStep> findApprovalStepsByVacationRequest_IdOrderByStepAsc(Long vacationRequestId);
}
