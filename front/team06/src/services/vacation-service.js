export const getVacationInfo = async (memberId) => {
    const response = await fetch(`/vacations/my/${memberId}`);
    if (!response.ok) {
        throw new Error('휴가 정보를 불러오는데 실패했습니다.');
    }
    return await response.json();
};

export const getVacationList = async (memberId, page = 0) => {
    const response = await fetch(`/vacations/${memberId}?page=${page}`);
    if (!response.ok) {
        throw new Error('휴가 목록을 불러오는데 실패했습니다.');
    }
    return await response.json();
};

export const requestVacation = async (memberId, vacationData) => {
    const response = await fetch(`/vacations/${memberId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(vacationData),
    });

    if (!response.ok) {
        throw new Error('휴가 신청에 실패했습니다.');
    }
    return await response.json();
};