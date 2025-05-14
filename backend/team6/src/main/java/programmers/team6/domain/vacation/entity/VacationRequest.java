package programmers.team6.domain.vacation.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VacationRequest extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "from_date", nullable = false)
	private LocalDateTime from;

	@Column(name = "to_date", nullable = false)
	private LocalDateTime to;

	private String reason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_code")
	private Code type;

	@Enumerated(value = EnumType.STRING)
	private VacationRequestStatus status;

	@Version
	private Integer version;

	public VacationRequest(Member member, LocalDateTime from, LocalDateTime to, String reason, Code type,
		Integer version) {
		this.member = member;
		this.from = from;
		this.to = to;
		this.reason = reason;
		this.type = type;
		this.version = version;
		this.status = VacationRequestStatus.IN_PROGRESS;
	}

	public void update(Code type, LocalDateTime from, LocalDateTime to, VacationRequestStatus status, String reason) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.status = status;
		this.reason = reason;
	}

	public void updateStatus(VacationRequestStatus vacationRequestStatus) {
		this.status = vacationRequestStatus;
	}

	public void approve() {
		updateStatus(VacationRequestStatus.APPROVED);
	}

	public void reject() {
		updateStatus(VacationRequestStatus.REJECTED);
	}

	public void cancel() {
		updateStatus(VacationRequestStatus.CANCELED);
	}

	public int calcVacationDays() {
		return (int)ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate()) + 1;
	}

	public Long getMemberId() {
		return this.member.getId();
	}

	public String getCode() {
		return this.type.getCode();
	}

}
