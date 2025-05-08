package programmers.team6.domain.vacation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import programmers.team6.domain.vacation.entity.VacationRequest;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
}
