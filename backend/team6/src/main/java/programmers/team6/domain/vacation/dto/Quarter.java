package programmers.team6.domain.vacation.dto;

import java.time.LocalDate;
import java.util.Optional;

public enum Quarter {
	Q1(1, 1, 3, 31),
	Q2(4, 1, 6, 30),
	Q3(7, 1, 9, 30),
	Q4(10, 1, 12, 31),
	H1(1, 1, 6, 30),
	H2(7, 1, 12, 31);

	private final int startMonth;
	private final int startDay;
	private final int endMonth;
	private final int endDay;

	Quarter(int startMonth, int startDay, int endMonth, int endDay) {
		this.startMonth = startMonth;
		this.startDay = startDay;
		this.endMonth = endMonth;
		this.endDay = endDay;
	}

	public static Optional<LocalDate> getStartDate(Integer year,Quarter quarter) {
		if (quarter == null || year == null) {
			return Optional.empty();
		}
		return Optional.of(LocalDate.of(year, quarter.startMonth, quarter.startDay));
	}

	public static Optional<LocalDate> getEndDate(Integer year,Quarter quarter) {
		if (quarter == null || year == null) {
			return Optional.empty();
		}
		return Optional.of(LocalDate.of(year, quarter.endMonth, quarter.endDay));
	}

}
