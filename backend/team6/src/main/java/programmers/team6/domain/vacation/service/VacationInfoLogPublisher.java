package programmers.team6.domain.vacation.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.entity.VacationInfoLog;
import programmers.team6.domain.vacation.repository.VacationInfoLogRepository;

@Component
@RequiredArgsConstructor
public class VacationInfoLogPublisher {

	private final VacationInfoLogRepository vacationInfoRepository;

	public void publish(VacationInfoLog vacationInfoLog) {
		vacationInfoRepository.save(vacationInfoLog);
	}
}
