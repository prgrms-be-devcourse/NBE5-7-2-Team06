package programmers.team6.domain.vacation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VacationInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int vacationId;

	private int totalCount;

	private int useCount;

	private int remainCount;

	private String vacationType;

    private Long memberId;

    @Version
    private Integer version;

}
