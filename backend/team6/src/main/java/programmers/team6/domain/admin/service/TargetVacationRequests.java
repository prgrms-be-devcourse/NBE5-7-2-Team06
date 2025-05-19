package programmers.team6.domain.admin.service;

import java.time.LocalDate;
import java.util.List;

import programmers.team6.domain.vacation.entity.VacationRequest;
import programmers.team6.global.util.DateUtil;

public class TargetVacationRequests {

	private final List<VacationRequest> vacationRequests;

	public TargetVacationRequests(List<VacationRequest> vacationRequests) {
		this.vacationRequests = vacationRequests;
	}

	public long count(Integer year, Integer month) {
		return countFrom(year, month);
	}

	private long countFrom(Integer year, Integer month) {
		long count = 0;
		for (VacationRequest vacationRequest : vacationRequests) {
			if (isInRange(vacationRequest, month)) {
				count += calcVacationDays(vacationRequest, year, month);
			}
		}
		return count;
	}

	private int calcVacationDays(VacationRequest vacationRequest, Integer year, Integer month) {
		LocalDate from = vacationRequest.getFrom().toLocalDate();
		LocalDate to = vacationRequest.getTo().toLocalDate();
		if (from.getMonthValue() == to.getMonthValue()) {
			return DateUtil.calcDaysOfService(from, to) + 1;
		}
		if (from.getMonthValue() == month && from.getYear() == year) {
			return DateUtil.calcDaysOfService(from, DateUtil.lastDateFrom(from)) + 1;
		}

		if (to.getMonthValue() == month && to.getYear() == year) {
			return DateUtil.calcDaysOfService(to, DateUtil.startDateFrom(to)) + 1;
		}
		return 0;
	}

	private boolean isInRange(VacationRequest vacationRequest, Integer month) {
		LocalDate from = vacationRequest.getFrom().toLocalDate();
		LocalDate to = vacationRequest.getTo().toLocalDate();
		if (from.getMonthValue() == month || to.getMonthValue() == month) {
			return true;
		}
		return false;
	}

}
