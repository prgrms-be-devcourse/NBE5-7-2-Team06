package programmers.team6.domain.vacation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import programmers.team6.domain.vacation.enums.ApprovalStatus;

@Getter
@AllArgsConstructor
public class ApprovalFirstStepSelectResponse {
	private Long approvalStepId;
	private String type;
	private LocalDateTime from;
	private LocalDateTime to;
	private String name;
	private String deptName;
	private String positionName;
	private ApprovalStatus status;
}
