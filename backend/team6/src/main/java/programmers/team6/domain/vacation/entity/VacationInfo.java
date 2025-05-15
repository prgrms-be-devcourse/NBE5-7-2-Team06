package programmers.team6.domain.vacation.entity;

import org.springframework.lang.CheckReturnValue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import programmers.team6.global.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VacationInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int vacationId;

	private double totalCount;

	private double useCount;

	private String vacationType;

	private Long memberId;

	@Version
	private int version;

	public VacationInfo(double totalCount, String vacationType, Long memberId) {
		this(totalCount, 0, vacationType, memberId);
	}

	public VacationInfo(double totalCount, double useCount, String vacationType, Long memberId) {
		this.totalCount = totalCount;
		this.useCount = useCount;
		this.vacationType = vacationType;
		this.memberId = memberId;
		this.version = 0;
	}

	@CheckReturnValue
	public VacationInfoLog updateTotalCount(double totalCount) {
		update(totalCount, this.useCount);
		return VacationInfoLog.from(this);
	}

	@CheckReturnValue
	public VacationInfoLog init(double totalCount) {
		update(totalCount, 0);
		return VacationInfoLog.from(this);
	}

	@CheckReturnValue
	public VacationInfoLog useVacation(int count) {
		update(totalCount, count);
		return VacationInfoLog.from(this);
	}

	public boolean isSameVersion(Integer version) {
		return this.version == version;
	}

	public boolean canUseVacation(int count) {
		return this.useCount + count <= totalCount;
	}

	private void update(double totalCount, double useCount) {
		if (useCount > totalCount) {
			//TODO : 추후 예외 설정 예정
			throw new RuntimeException();
		}
		this.totalCount = totalCount;
	}
}
