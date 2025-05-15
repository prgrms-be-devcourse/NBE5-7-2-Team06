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
import programmers.team6.domain.vacation.enums.VacationInfoUpdateResult;
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
	public VacationInfoUpdateResult updateTotalCount(double totalCount) {
		if (isOverUseCount(totalCount)) {
			return VacationInfoUpdateResult.MISS_RULES;
		}
		this.totalCount = totalCount;
		return VacationInfoUpdateResult.SUCCESS;
	}

	@CheckReturnValue
	public VacationInfoUpdateResult init(double totalCount) {
		this.totalCount = totalCount;
		this.useCount = 0;
		return VacationInfoUpdateResult.SUCCESS;
	}

	private boolean isOverUseCount(double totalCount) {
		return this.useCount > totalCount;
	}

	public boolean isSameVersion(Integer version) {
		return this.version == version;
	}

	public void useVacation(double count) {
		this.useCount += count;
	}

	public boolean canUseVacation(double count) {
		return this.useCount + count <= totalCount;
	}

}
