package programmers.team6.domain.vacation.entity;

import jakarta.persistence.*;
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

    private int remainCount;

    private String vacationType;

    private Long memberId;

    @Version
    private Integer version;

}
