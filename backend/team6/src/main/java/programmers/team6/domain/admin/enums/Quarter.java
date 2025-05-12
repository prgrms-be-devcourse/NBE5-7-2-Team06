package programmers.team6.domain.admin.enums;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public enum Quarter {
	Q1(1), Q2(2), Q3(3), Q4(4), H1(5), H2(6), NONE(0);

	private final int value;

	Quarter(int value) {
		this.value = value;
	}

	public LocalDate getStart(int year) {
		return switch (this) {
			case Q1, Q2, Q3, Q4 -> makeQuarter(year, TemporalAdjusters.firstDayOfMonth());
			case H1, NONE -> LocalDate.of(year, Month.JANUARY, 1);
			case H2 -> LocalDate.of(year, Month.JULY, 1);
		};
	}

	public LocalDate getEnd(int year) {
		return switch (this) {
			case Q1, Q2, Q3, Q4 -> makeQuarter(year, TemporalAdjusters.lastDayOfMonth()).plusMonths(2);
			case H1 -> LocalDate.of(year, Month.JUNE, 30);
			case H2, NONE -> LocalDate.of(year, Month.DECEMBER, 31);
		};
	}

	private LocalDate makeQuarter(int year, TemporalAdjuster temporalAdjuster) {
		return LocalDate.of(year, 1, 1).with(IsoFields.QUARTER_OF_YEAR, this.value).with(temporalAdjuster);
	}

}
