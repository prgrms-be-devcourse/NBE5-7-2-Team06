package programmers.team6.domain.vacation.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.team6.domain.vacation.rule.vacationgranteligiblities.VacationGrantEligibilities;
import programmers.team6.domain.vacation.service.VacationInfoService;

@Component
@RequiredArgsConstructor
@Slf4j
public class VacationGrantScheduler {

	private final VacationInfoService vacationInfoService;

	@Scheduled(cron = "${schedule.grant-cron}")
	public void grantJob() {
		VacationGrantEligibilities grantEligibilities = vacationInfoService.selectEligiblitiesFrom(LocalDate.now());
		vacationInfoService.updateEligiblities(grantEligibilities);
		log.info("실행");
	}
}
