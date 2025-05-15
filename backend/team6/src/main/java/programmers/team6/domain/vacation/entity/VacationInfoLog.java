package programmers.team6.domain.vacation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VacationInfoLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double totalCount;

	private double useCount;

	private String vacationType;

	private Long memberId;

	public VacationInfoLog(double totalCount, double useCount, String vacationType, Long memberId) {
		this.totalCount = totalCount;
		this.useCount = useCount;
		this.vacationType = vacationType;
		this.memberId = memberId;
	}

	public static VacationInfoLog from(VacationInfo vacationInfo) {
		return new VacationInfoLog(vacationInfo.getTotalCount(), vacationInfo.getUseCount(),
			vacationInfo.getVacationType(), vacationInfo.getMemberId());
	}
}
