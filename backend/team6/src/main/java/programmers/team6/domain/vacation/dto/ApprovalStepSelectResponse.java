package programmers.team6.domain.vacation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

@Getter
@AllArgsConstructor
public class ApprovalStepSelectResponse {
	private Long approvalStepId;
	private String type;
	private LocalDateTime from;
	private LocalDateTime to;
	private String name;
	private String dept;
	private String position;
	private ApprovalStatus status;
}
