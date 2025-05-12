package programmers.team6.domain.vacation.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import programmers.team6.domain.member.entity.Code;
import programmers.team6.domain.member.entity.Member;
import programmers.team6.domain.vacation.enums.VacationRequestStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VacationRequest extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "from_date", nullable = false)
	private LocalDate from;

	@Column(name = "to_date", nullable = false)
	private LocalDate to;

	private String reason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_code")
	private Code type;

	@Version
	private Integer version;

	@Enumerated(value = EnumType.STRING)
	private VacationRequestStatus status;

	public VacationRequest(Member member, LocalDate from, LocalDate to, String reason, Code type, Integer version,
		VacationRequestStatus status) {
		this.member = member;
		this.from = from;
		this.to = to;
		this.reason = reason;
		this.type = type;
		this.version = version;
		this.status = VacationRequestStatus.IN_PROGRESS;
	}

	public void update(Code type, LocalDate from, LocalDate to, VacationRequestStatus status, String reason) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.status = status;
		this.reason = reason;
	}
}
