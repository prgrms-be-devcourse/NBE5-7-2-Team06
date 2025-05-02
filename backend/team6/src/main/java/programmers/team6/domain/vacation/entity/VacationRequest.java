package programmers.team6.domain.vacation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import programmers.team6.domain.member.entity.Code;

@Entity
public class VacationRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vacation_request_id")
	private Long id;
	private LocalDateTime from;
	private LocalDateTime to;
	private String reason;
	private Code type;
	@Version
	private Integer version;
	private Code status;
}
