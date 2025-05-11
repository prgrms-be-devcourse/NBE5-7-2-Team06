
package programmers.team6.domain.vacation.rule;

import java.time.LocalDate;
import java.time.Period;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

	public static boolean isEqualsOrBefore(LocalDate left, LocalDate right) {
		return left.isBefore(right) || left.isEqual(right);
	}

	public static int calcYearsOfService(LocalDate now, LocalDate joinDate) {
		Period period = Period.between(joinDate, now);
		return Math.abs(period.getYears());
	}
}