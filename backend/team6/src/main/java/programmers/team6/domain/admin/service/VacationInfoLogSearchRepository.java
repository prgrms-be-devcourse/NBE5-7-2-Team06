package programmers.team6.domain.admin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.admin.utils.QueryDSLUtils;
import programmers.team6.domain.vacation.entity.QVacationInfoLog;

@Repository
@RequiredArgsConstructor
public class VacationInfoLogSearchRepository {

	private final JPAQueryFactory queryFactory;

	public List<Long> queryContainVacationInfoMemberIds(LocalDateTime localDateTime, String code) {
		QVacationInfoLog vacationInfoLog = QVacationInfoLog.vacationInfoLog;

		BooleanBuilder builder = QueryDSLUtils.createEmptyCondition();
		QueryDSLUtils.equal(builder, vacationInfoLog.vacationType, code);
		QueryDSLUtils.lessThanOrEqualTo(builder, vacationInfoLog.logDate, localDateTime);

		return queryFactory
			.select(vacationInfoLog.memberId)
			.from(vacationInfoLog)
			.where(builder)
			.fetch();
	}
}
