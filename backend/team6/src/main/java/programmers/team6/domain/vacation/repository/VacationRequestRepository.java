package programmers.team6.domain.vacation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import programmers.team6.domain.vacation.entity.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

}
