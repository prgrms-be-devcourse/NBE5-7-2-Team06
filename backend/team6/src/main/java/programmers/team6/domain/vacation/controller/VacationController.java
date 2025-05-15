package programmers.team6.domain.vacation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationRequestCalendarResponse;
import programmers.team6.domain.vacation.service.VacationService;

@RestController
@RequestMapping("/vacations")
@RequiredArgsConstructor
public class VacationController {

	private final VacationService vacationService;

	@GetMapping("/calendar")
	public ResponseEntity<?> selectVacationCalendar(@RequestParam String yearMonth, @RequestParam String deptCode) {

		List<VacationRequestCalendarResponse> events
			= vacationService.selectVacationCalendar(yearMonth, deptCode);

		return ResponseEntity.ok(events);
	}

}
