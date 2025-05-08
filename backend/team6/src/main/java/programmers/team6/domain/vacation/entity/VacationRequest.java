package programmers.team6.domain.vacation.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.dto.VacationRequestStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VacationRequest extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_request_id")
	private Long id;

	@Column(name = "from_date", nullable = false)
	private LocalDate from;

	@Column(name = "to_date", nullable = false)
	private LocalDate to;

	private String reason;
	private int lastApprovalStep;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "type_code")
	private Code type;

	@Enumerated(value = EnumType.STRING)
	private VacationRequestStatus status;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "member_id")
	private Member member;

	@Version
	private Integer version;

	@Builder
	public VacationRequest(LocalDate from, LocalDate to, String reason, Code type,
		Member member) {
		this.from = from;
		this.to = to;
		this.reason = reason;
		this.lastApprovalStep = 0;
		this.type = type;
		this.status = VacationRequestStatus.IN_PROGRESS;
		this.member = member;
	}

	public void updateStatus(VacationRequestStatus vacationRequestStatus) {
		this.status = vacationRequestStatus;
	}

	public void processVacationRequest(int lastApprovalStepIdx) {
		if (this.lastApprovalStep < lastApprovalStepIdx) {
			this.lastApprovalStep++;
		}
	}
}
