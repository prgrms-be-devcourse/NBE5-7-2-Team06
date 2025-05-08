package programmers.team6.domain.vacation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VacationInfoSelectResponseDto {
	private int totalCount;
	private int useCount;
	private int remainCount;
}
