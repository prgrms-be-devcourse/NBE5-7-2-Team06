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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.enums.ApprovalStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalStep extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_step_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vacation_request_id", nullable = false)
	private VacationRequest vacationRequest;

	private int step;

	@Column(name = "approval_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ApprovalStatus approvalStatus;

	private String reason;

	@Builder
	public ApprovalStep(int step, ApprovalStatus approvalStatus, Member member, VacationRequest vacationRequest) {
		this.step = step;
		this.approvalStatus = approvalStatus;
		this.member = member;
		this.vacationRequest = vacationRequest;
	}

	public void updateStatus(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void updateStatus(ApprovalStatus approvalStatus, String reason) {
		this.approvalStatus = approvalStatus;
		this.reason = reason;
	}

	public boolean isApprovable() {
		return this.approvalStatus == ApprovalStatus.IN_PROGRESS;
	}

}
