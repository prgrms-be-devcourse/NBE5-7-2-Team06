package programmers.team6.utils;

import lombok.experimental.UtilityClass;
import programmers.team6.domain.vacation.entity.ApprovalStep;
import programmers.team6.domain.vacation.entity.VacationRequest;

@UtilityClass
public class TestUtils {
    public static ApprovalStep genApprovalStep(VacationRequest vacationRequest,int step,String reason) {
        return new ApprovalStep(null, vacationRequest, null, step, reason);
    }
}
