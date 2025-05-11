package programmers.team6.domain.vacation.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VacationInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int vacationId;

	private int totalCount;

	private int useCount;

	private String vacationType;

	private Long memberId;

	@Version
	private int version;

	public VacationInfo(int totalCount, int useCount, String vacationType, Long memberId) {
		this.totalCount = totalCount;
		this.useCount = useCount;
		this.vacationType = vacationType;
		this.memberId = memberId;
	}
}
