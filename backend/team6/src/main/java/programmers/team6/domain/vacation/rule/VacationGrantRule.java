package programmers.team6.domain.vacation.rule;

import programmers.team6.global.entity.Positive;

public interface VacationGrantRule {
	boolean canUpdate(Positive totalCount);
}
