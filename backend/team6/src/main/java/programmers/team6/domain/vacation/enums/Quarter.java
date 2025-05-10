package programmers.team6.domain.vacation.enums;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public enum Quarter {
	Q1(1), Q2(2), Q3(3), Q4(4), H1(5), H2(6), NONE(0);

	private final int value;

	Quarter(int value) {
		this.value = value;
	}

	public static Optional<LocalDate> getStart(Integer year, Quarter quarter) {
		if (year == null) {
			return Optional.empty();
		}
		if (quarter == null) {
			quarter = Quarter.NONE;
		}

		return switch (quarter) {
			case Q1, Q2, Q3, Q4 ->
				Optional.of(LocalDate.of(year, 1, 1).with(IsoFields.QUARTER_OF_YEAR, quarter.value).with(
					TemporalAdjusters.firstDayOfMonth()));
			case H1, NONE -> Optional.of(LocalDate.of(year, Month.JANUARY, 1));
			case H2 -> Optional.of(LocalDate.of(year, Month.JULY, 1));
		};
	}

	public static Optional<LocalDate> getEnd(Integer year, Quarter quarter) {
		if (year == null) {
			return Optional.empty();
		}
		if (quarter == null) {
			quarter = Quarter.NONE;
		}

		return switch (quarter) {
			case Q1, Q2, Q3, Q4 ->
				Optional.of(LocalDate.of(year, 1, 1).with(IsoFields.QUARTER_OF_YEAR, quarter.value).with(
					TemporalAdjusters.lastDayOfMonth()).plusMonths(2));
			case H1 -> Optional.of(LocalDate.of(year, Month.JUNE, 30));
			case H2, NONE -> Optional.of(LocalDate.of(year, Month.DECEMBER, 31));
		};
	}
}
