package programmers.team6.domain.vacation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationInfoUpdateTotalCountRequestsList;
import programmers.team6.domain.vacation.service.VacationInfoService;

@RestController
@RequestMapping("/vacations/infos")
@RequiredArgsConstructor
public class VacationInfoController {

	private final VacationInfoService vacationInfoService;

	@PatchMapping
	@ResponseStatus(value = HttpStatus.OK)
	public void updateTotalCount(
		@Validated
		@RequestBody VacationInfoUpdateTotalCountRequestsList request) {
		vacationInfoService.updateFrom(request);
	}
}
