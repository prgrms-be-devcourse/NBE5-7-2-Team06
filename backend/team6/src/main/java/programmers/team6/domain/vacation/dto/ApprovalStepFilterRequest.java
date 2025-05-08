package programmers.team6.domain.vacation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStepFilterRequest {
	private Long memberId;
	private Long typeId;
	private String name;
	private LocalDateTime from;
	private LocalDateTime to;
	private ApprovalStatus status;

}
