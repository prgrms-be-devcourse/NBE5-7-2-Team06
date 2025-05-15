package programmers.team6.domain.vacation.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.MonthRange;
import programmers.team6.domain.vacation.dto.VacationRequestCalendarResponse;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
import programmers.team6.domain.vacation.repository.VacationRepository;

@Service
@RequiredArgsConstructor
public class VacationService {

	private final VacationRepository vacationRepository;

	@Transactional(readOnly = true)
	public List<VacationRequestCalendarResponse> selectVacationCalendar(String deptCode, String yearMonthStr) {

		MonthRange monthRange = getMonthRange(yearMonthStr);

		return vacationRepository.findApprovedVacationsByMonth(VacationRequestStatus.APPROVED,
			monthRange.start(),
			monthRange.end());
	}

	private MonthRange getMonthRange(String yearMonthStr) {
		YearMonth yearMonth = YearMonth.parse(yearMonthStr);

		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

		return new MonthRange(start, end);
	}
}
