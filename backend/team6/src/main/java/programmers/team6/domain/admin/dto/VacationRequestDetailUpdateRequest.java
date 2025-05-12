package programmers.team6.domain.admin.dto;

import java.time.LocalDate;
import java.util.List;

import programmers.team6.domain.vacation.enums.VacationRequestStatus;

public record VacationRequestDetailUpdateRequest(Long typeId, LocalDate from, LocalDate to,
												 VacationRequestStatus vacationRequestStatus, String reason,
												 List<String> approvalReason) {

}
