package programmers.team6.domain.admin.utils;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.experimental.UtilityClass;
import programmers.team6.domain.vacation.dto.VacationRequestCalendarResponse;

@UtilityClass
public class QueryDSLUtils {
	public BooleanBuilder createEmptyCondition() {
		return new BooleanBuilder();
	}

	public void dataRange(BooleanBuilder builder, DateTimePath<LocalDateTime> from, DateTimePath<LocalDateTime> to,
		LocalDateTime start, LocalDateTime end) {
		builder.and(from.goe(start));
		builder.and(to.loe(end));
	}

	public void containsIgnoreCase(BooleanBuilder builder, StringPath data, String filterData) {
		builder.and(data.containsIgnoreCase(filterData));
	}

	public <T> void equal(BooleanBuilder builder, SimpleExpression<T> data, T reqData) {
		builder.and(data.eq(reqData));
	}

	public <T extends Comparable> void greaterThanOrEqualTo(BooleanBuilder builder, ComparableExpression<T> data,
		T reqData) {
		builder.and(data.loe(reqData));
	}

	public <T extends Comparable> void lessThan(BooleanBuilder builder, ComparableExpression<T> data, T reqData) {
		builder.and(data.lt(reqData));
	}

	public <T extends Comparable> void lessThanOrEqualTo(BooleanBuilder builder, ComparableExpression<T> data,
		T reqData) {
		builder.and(data.loe(reqData));
	}

	public <T> void in(BooleanBuilder builder, SimpleExpression<T> data, List<T> reqData) {
		builder.and(data.in(reqData));
	}

	public <T> Long count(JPAQueryFactory jpaQueryFactory, BooleanBuilder builder, SimpleExpression<Long> data,
		EntityPath<T> entityPath) {
		return jpaQueryFactory.select(data.count()).from(entityPath).where(builder).fetchOne();
	}

}
