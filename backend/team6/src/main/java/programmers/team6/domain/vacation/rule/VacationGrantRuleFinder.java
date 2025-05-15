package programmers.team6.domain.vacation.rule;

import org.springframework.stereotype.Component;

import programmers.team6.domain.vacation.entity.VacationInfo;
import programmers.team6.domain.vacation.enums.VacationCode;
import programmers.team6.global.entity.Positive;

@Component
public class VacationGrantRuleFinder {


	public VacationGrantRule find(String type) {
		VacationCode vacationCode = VacationCode.findByCode(type).orElseThrow();
		return find(vacationCode);
	}

	public VacationGrantRule find(VacationCode type) {
		switch (type) {
			case VacationCode.ANNUAL -> {
				return AnnualVacationGrantRule.statutory();
			}
			default -> {
				return new DefaultRule(type);
			}
		}
	}

	public static class DefaultRule implements VacationGrantRule {

		private final VacationCode type;

		public DefaultRule(VacationCode type) {
			this.type = type;
		}

		@Override
		public boolean canUpdate(Positive totalCount) {
			return true;
		}

		@Override
		public VacationInfo createVacationInfo(Long memberId) {
			return new VacationInfo(0, type.getCode(), memberId);
		}
	}
}
