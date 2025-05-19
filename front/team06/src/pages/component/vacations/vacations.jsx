import React, { useState, useEffect } from 'react';
import VacationRequestModal from './vacation-request-modal';

const Vacations = () => {
    // 휴가 데이터 상태 관리
    const [vacationData, setVacationData] = useState({
        totalCount: 15,
        useCount: 2,
        remainCount: 13
    });

    // 휴가 신청 목록 데이터 상태 관리
    const [vacationList, setVacationList] = useState([
        {
            id: 1,
            type: '연차',
            from: '2025-05-15',
            to: '2025-05-15',
            days: 1,
            reason: '개인 사유',
            status: '승인',
            approver: '김부장'
        },
        {
            id: 2,
            type: '반차',
            from: '2025-04-22',
            to: '2025-04-22',
            days: 0.5,
            reason: '병원 진료',
            status: '승인',
            approver: '김부장'
        },
        {
            id: 3,
            type: '연차',
            from: '2025-03-10',
            to: '2025-03-11',
            days: 2,
            reason: '개인 사유',
            status: '취소',
            approver: '-'
        }
    ]);

    // 휴가 신청 모달 상태
    const [showModal, setShowModal] = useState(false);

    // 실제 구현 시에는 API 호출로 데이터를 가져옴
    useEffect(() => {
        const fetchData = async () => {
            try {
                // 사용자 ID는 실제 로그인 정보에서 가져와야 함
                const memberId = 1; // 임시로 1로 설정

                const infoResponse = await fetch(`/vacations/my/${memberId}`);
                if (infoResponse.ok) {
                    const vacationInfo = await infoResponse.json();
                    setVacationData(vacationInfo);
                }

                const listResponse = await fetch(`/vacations/${memberId}?page=0`);
                if (listResponse.ok) {
                    const vacationListData = await listResponse.json();
                    setVacationList(vacationListData.content || []);
                }
            } catch (error) {
                console.error('데이터 로딩 오류:', error);
                // 에러 처리는 실제 구현 시 추가
            }
        };

        // 주석 처리: 실제 API 연동 시 주석 해제
        // fetchData();
    }, []);

    // 휴가 신청 처리 함수
    const handleVacationRequest = async (formData) => {
        try {
            // 사용자 ID는 실제 로그인 정보에서 가져와야 함
            const memberId = 1; // 임시로 1로 설정

            const response = await fetch(`/vacations/${memberId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    from: new Date(formData.from).toISOString(),
                    to: new Date(formData.to).toISOString(),
                    reason: formData.reason,
                    vacationType: formData.type === '연차' ? '01' :
                        formData.type === '반차' ? '05' :
                            formData.type === '병가' ? '02' : '03'
                }),
            });

            if (!response.ok) {
                throw new Error('휴가 신청에 실패했습니다.');
            }

            // 성공 후 데이터 새로고침
            // 휴가 정보 다시 불러오기
            const infoResponse = await fetch(`/vacations/my/${memberId}`);
            if (infoResponse.ok) {
                const vacationInfo = await infoResponse.json();
                setVacationData(vacationInfo);
            }

            // 휴가 목록 다시 불러오기
            const listResponse = await fetch(`/vacations/${memberId}?page=0`);
            if (listResponse.ok) {
                const vacationListData = await listResponse.json();
                setVacationList(vacationListData.content || []);
            }

            setShowModal(false);
            alert('휴가 신청이 완료되었습니다.');
        } catch (error) {
            console.error('휴가 신청 오류:', error);
            alert('휴가 신청에 실패했습니다. 다시 시도해주세요.');
        }
    };

    return (
        <div className="p-6">
            {/* 휴가 현황 카드 섹션 */}
            <div className="mb-6">
                <h2 className="text-xl font-semibold text-gray-800 mb-4">휴가 현황</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex flex-col items-center">
                            <div className="text-sm text-gray-600">부여휴가</div>
                            <div className="text-3xl font-bold text-blue-600 mt-2">{vacationData.totalCount}일</div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex flex-col items-center">
                            <div className="text-sm text-gray-600">사용휴가</div>
                            <div className="text-3xl font-bold text-red-600 mt-2">{vacationData.useCount}일</div>
                        </div>
                    </div>

                    <div className="bg-white rounded-lg shadow p-6">
                        <div className="flex flex-col items-center">
                            <div className="text-sm text-gray-600">잔여휴가</div>
                            <div className="text-3xl font-bold text-green-600 mt-2">{vacationData.remainCount}일</div>
                        </div>
                    </div>
                </div>
            </div>

            {/* 휴가 신청 목록 섹션 */}
            <div className="bg-white rounded-lg shadow">
                <div className="p-6 border-b border-gray-200 flex justify-between items-center">
                    <h2 className="text-xl font-semibold text-gray-800">휴가 신청 목록</h2>
                    <button
                        onClick={() => setShowModal(true)}
                        className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                    >
                        휴가 신청
                    </button>
                </div>

                {/* 테이블 */}
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">번호</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">휴가 유형</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">시작일</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">종료일</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">일수</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">사유</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">상태</th>
                            <th className="py-3 px-6 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">결재자</th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {vacationList.map((vacation) => (
                            <tr key={vacation.id}>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.id}</td>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.type}</td>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.from}</td>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.to}</td>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.days}</td>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.reason}</td>
                                <td className="py-4 px-6 text-sm">
                    <span
                        className={`px-2 py-1 text-xs rounded-full ${
                            vacation.status === '승인'
                                ? 'bg-green-100 text-green-800'
                                : vacation.status === '대기'
                                    ? 'bg-yellow-100 text-yellow-800'
                                    : 'bg-gray-100 text-gray-800'
                        }`}
                    >
                      {vacation.status}
                    </span>
                                </td>
                                <td className="py-4 px-6 text-sm text-gray-900">{vacation.approver}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                {/* 데이터가 없을 경우 표시 */}
                {vacationList.length === 0 && (
                    <div className="py-10 text-center text-gray-500">
                        휴가 신청 내역이 없습니다.
                    </div>
                )}
            </div>

            {/* 휴가 신청 모달 */}
            <VacationRequestModal
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                onSubmit={handleVacationRequest}
            />
        </div>
    );
};

export default Vacations;