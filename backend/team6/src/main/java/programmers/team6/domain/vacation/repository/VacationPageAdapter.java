package programmers.team6.domain.vacation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.dto.VacationMonthlyStatisticsResponse;

@Repository
@RequiredArgsConstructor
public class VacationPageAdapter implements VacationStatisticsRepository {

	private final VacationRequestRepository vacationRequestRepository;
	private final VacationInfoRepository vacationInfoRepository;

	@Override
	public Page<VacationMonthlyStatisticsResponse> getMonthlyVacationStatistics(Integer targetYear, Pageable pageable) {
		int limit = pageable.getPageSize();
		int offset = pageable.getPageNumber() * limit;
		List<VacationMonthlyStatisticsResponse> result = vacationRequestRepository.getMonthlyVacationStatistics(
			targetYear, limit, offset);

		long count = vacationInfoRepository.countAllMemberIds();
		return new PageImpl<>(result, pageable, count);
	}
}
