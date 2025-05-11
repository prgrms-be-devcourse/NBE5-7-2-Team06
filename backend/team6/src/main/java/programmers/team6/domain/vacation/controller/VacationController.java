package programmers.team6.domain.vacation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationCreateRequestDto;
import programmers.team6.domain.vacation.dto.VacationCreateResponseDto;
import programmers.team6.domain.vacation.dto.VacationInfoSelectResponseDto;
import programmers.team6.domain.vacation.dto.VacationListResponseDto;
import programmers.team6.domain.vacation.service.VacationService;

@RestController
@RequestMapping("/vacations")
@RequiredArgsConstructor
public class VacationController {

	private final VacationService vacationService;

	@GetMapping("/my")
	public ResponseEntity<VacationInfoSelectResponseDto> getMyVacationInfo(
		@RequestParam Long memberId) {

		// 휴가 정보 조회
		VacationInfoSelectResponseDto vacationInfo = vacationService.getMyVacationInfo(memberId);

		return ResponseEntity.ok(vacationInfo);
	}

	// 휴가 신청
	@PostMapping("/request")
	public ResponseEntity<VacationCreateResponseDto> requestVacation(
		@Validated @RequestBody VacationCreateRequestDto requestDto,
		@RequestParam Long memberId) {
		VacationCreateResponseDto response = vacationService.requestVacation(memberId, requestDto);
		return ResponseEntity.ok(response);
	}

	// 휴가 신청 리스트 조회
	@GetMapping("/list")
	public ResponseEntity<List<VacationListResponseDto>> getVacationRequestList(
		@RequestParam Long memberId) {

		List<VacationListResponseDto> vacationRequests = vacationService.getVacationRequestList(memberId);
		return ResponseEntity.ok(vacationRequests);
	}
}
