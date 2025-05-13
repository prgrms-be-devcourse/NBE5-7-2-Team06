package programmers.team6.domain.vacation.rule;

import org.springframework.stereotype.Component;

@Component
public class VacationGrantRuleFinder {

	private static final String ANNUAL_VACATION_TYPE = "01";

	public VacationGrantRule find(String type) {
		switch (type) {
			case ANNUAL_VACATION_TYPE -> {
				return AnnualVacationGrantRule.statutory();
			}
			default -> {
				return totalCount -> true;
			}
		}
	}
}
