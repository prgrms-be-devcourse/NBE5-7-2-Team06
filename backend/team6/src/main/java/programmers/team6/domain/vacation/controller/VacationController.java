package programmers.team6.domain.vacation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.service.VacationService;

@RestController
@RequestMapping("/vacation")
@RequiredArgsConstructor
public class VacationController {

	private final VacationService vacationService;

	@GetMapping("/read")
	public ResponseEntity<VacationInfoSelectResponseDto> getMyVacationInfo(
		@RequestParam Long memberId) {

		// 휴가 정보 조회
		VacationInfoSelectResponseDto vacationInfo = vacationService.getMyVacationInfo(memberId);

		return ResponseEntity.ok(vacationInfo);
	}
}
