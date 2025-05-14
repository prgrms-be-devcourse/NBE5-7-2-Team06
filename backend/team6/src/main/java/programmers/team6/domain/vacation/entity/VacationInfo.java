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
public class VacationInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private int vacationId;

	@Getter
	private int totalCount;

	@Getter
	private int useCount;

	@Getter
	private String vacationType;

	@Getter
	private Long memberId;

	@Getter
	@Version
	private int version;

	public VacationInfo(int totalCount, int useCount, String vacationType, Long memberId) {
		this.totalCount = totalCount;
		this.useCount = useCount;
		this.vacationType = vacationType;
		this.memberId = memberId;
		this.version = 0;
	}

	@CheckReturnValue
	public VacationInfoUpdateResult updateTotalCount(Integer version, Integer totalCount) {
		if (!isSameVersion(version)) {
			return VacationInfoUpdateResult.MISS_VERSION;
		}
		if (isOverUseCount(totalCount)) {
			return VacationInfoUpdateResult.MISS_RULES;
		}
		this.totalCount = totalCount;
		return VacationInfoUpdateResult.SUCCESS;
	}

	private boolean isOverUseCount(int totalCount) {
		return this.useCount > totalCount;
	}

	public boolean isSameVersion(Integer version) {
		return this.version == version;
	}

	public void useVacation(int count) {
		this.useCount += count;
	}

	public boolean canUseVacation(int count) {
		return this.useCount + count <= totalCount;
	}

}
