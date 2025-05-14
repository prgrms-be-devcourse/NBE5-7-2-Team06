package programmers.team6.domain.vacation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private LocalDateTime from;

	@Column(name = "to_date", nullable = false)
	private LocalDateTime to;

	private String reason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_code")
	private Code type;

	@Version
	private Integer version;

	@Enumerated(value = EnumType.STRING)
	private VacationRequestStatus status;

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
}
