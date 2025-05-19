package programmers.team6.domain.admin.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import programmers.team6.domain.vacation.entity.VacationInfoLog;
import programmers.team6.domain.vacation.repository.VacationInfoLogRepository;

@Component
@RequiredArgsConstructor
public class VacationInfoLogReader {

	private final VacationInfoLogRepository repository;

	public VacationInfoLogs lastedLogsFrom(List<Long> ids, String code) {
		List<VacationInfoLog> lastedByMemberIdInAndYear = repository.findLastedByMemberIdInAndYear(ids, code);
		return new VacationInfoLogs(toLastedMap(lastedByMemberIdInAndYear));
	}

	private Map<Long, VacationInfoLog> toLastedMap(List<VacationInfoLog> logs) {
		return logs.stream()
			.collect(Collectors.toMap(VacationInfoLog::getMemberId,
				Function.identity(),
				VacationInfoLogReader::lastedLog));
	}

	private static VacationInfoLog lastedLog(VacationInfoLog log1, VacationInfoLog log2) {
		return log1.getLogDate().isAfter(log2.getLogDate()) ? log1 : log2;
	}
}
