package programmers.team6.domain.vacation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
	// 휴가 요청에 대한 첫 번째 결재 단계 조회 (단계 오름차순)
	Optional<ApprovalStep> findFirstByVacationRequestOrderByStepAsc(VacationRequest vacationRequest);
}
