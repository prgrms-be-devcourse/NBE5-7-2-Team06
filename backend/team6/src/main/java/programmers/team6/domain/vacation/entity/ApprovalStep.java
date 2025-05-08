package programmers.team6.domain.vacation.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.dto.ApprovalStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalStep extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_step_id")
	private Long approvalStepId;

	private int step;
	@Column(name = "approval_status")
	@Enumerated(value = EnumType.STRING)
	private ApprovalStatus approvalStatus;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "vacation_request_id")
	private VacationRequest vacationRequest;

	@Builder
	public ApprovalStep(int step, ApprovalStatus approvalStatus, Member member, VacationRequest vacationRequest) {
		this.step = step;
		this.approvalStatus = approvalStatus;
		this.member = member;
		this.vacationRequest = vacationRequest;
	}

	public void apply(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
}
