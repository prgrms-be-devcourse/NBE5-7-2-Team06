package programmers.team6.domain.vacation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import programmers.team6.domain.vacation.entity.ApprovalStep;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
}
