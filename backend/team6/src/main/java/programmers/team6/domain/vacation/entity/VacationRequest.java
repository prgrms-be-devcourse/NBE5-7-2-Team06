package programmers.team6.domain.vacation.entity;

import java.time.LocalDateTime;

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
import programmers.team6.domain.vacation.enums.ApprovalStatus;
import programmers.team6.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VacationRequest extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_request_id")
	private Long id;

	@Column(name = "from_date", nullable = false)
	private LocalDateTime from;

	@Column(name = "to_date", nullable = false)
	private LocalDateTime to;

	private String reason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_code", nullable = false)
	private Code type;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "status_code", nullable = false)
	// private Code status;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;

	// 추가: 휴가 신청자
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id", nullable = false)
	private Member requester;

	@Version
	private Integer version;

	@Builder
	public VacationRequest(LocalDateTime from, LocalDateTime to, String reason, Code type, ApprovalStatus status,
		Member requester) {
		this.from = from;
		this.to = to;
		this.reason = reason;
		this.type = type;
		this.status = status;
		this.requester = requester;
	}

	// 휴가 요청 정보 수정
	public void update(LocalDateTime from, LocalDateTime to, String reason, Code type) {
		// 대기 중인 상태만 수정 가능
		if (this.status != ApprovalStatus.PENDING) {
			throw new IllegalStateException("대기 중인 휴가 요청만 수정할 수 있습니다.");
		}

		this.from = from;
		this.to = to;
		this.reason = reason;
		this.type = type;
	}

	// 현재 요청자가 수정 권한을 가지고 있는지 확인
	public boolean canUpdate(Long memberId) {
		return this.requester.getId().equals(memberId) && this.status == ApprovalStatus.PENDING;
	}

	// 휴가 요청 취소
	public void cancel() {
		if (this.status != ApprovalStatus.PENDING) {
			throw new IllegalStateException("대기 중인 휴가 요청만 취소할 수 있습니다.");
		}

		this.status = ApprovalStatus.CANCELED;
	}

	// 현재 요청자가 취소 권한을 가지고 있는지 학인
	public boolean canCancel(Long memberId) {
		return this.requester.getId().equals(memberId) && this.status == ApprovalStatus.PENDING;
	}
}
