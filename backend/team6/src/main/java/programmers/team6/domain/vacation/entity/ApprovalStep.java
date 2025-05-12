package programmers.team6.domain.vacation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.enums.ApprovalStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalStep extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_step_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne
	@JoinColumn(name = "vacation_request_id", nullable = false)
	private VacationRequest vacationRequest;

	@Column(name = "approval_status", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private ApprovalStatus approvalStatus;

	private int step;

	private String reason;

	public ApprovalStep(Member member, VacationRequest vacationRequest, ApprovalStatus approvalStatus, int step,
		String reason) {
		this.member = member;
		this.vacationRequest = vacationRequest;
		this.approvalStatus = approvalStatus;
		this.step = step;
		this.reason = reason;
	}

	public void update(String reason) {
		this.reason = reason;
	}
}
