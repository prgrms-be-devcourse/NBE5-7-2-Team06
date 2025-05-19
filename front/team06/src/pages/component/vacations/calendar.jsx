import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { useEffect,useState, useRef } from "react";
import api from '../../../api/axiosInstance';

const Calendar = () => {
    const [events, setEvents] = useState([]);

    const lastFetchedMonth = useRef(""); // ✅ 현재 요청한 월을 기억

    const [depts, setDepts] = useState([1]);
    const [selectedDeptId, setSelectedDeptId] = useState(1);

    useEffect(() => {
        api
            .get("/depts")
            .then((res) => {
                setDepts(res.data);
            })
            .catch((err) => {
                console.error("부서 조회 실패", err);
            });
    }, []);

    const colorPalette = [
        "#60a5fa", // 파랑
        "#facc15", // 노랑
        "#34d399", // 초록
        "#f87171", // 빨강
        "#c084fc", // 보라
        "#fb923c", // 주황
        "#4ade80", // 연두
    ];

// 이름에 따라 고유한 색 인덱스 반환
    const getColorByName = (name) => {
        let hash = 0;
        for (let i = 0; i < name.length; i++) {
            hash = name.charCodeAt(i) + ((hash << 5) - hash);
        }
        const index = Math.abs(hash) % colorPalette.length;
        return colorPalette[index];
    };

    const handleDatesSet = (info) => {
        const currentDate = info.view.currentStart; // 또는 info.start도 됨

        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, "0"); // 0부터 시작하므로 +1

        const yearMonth = `${year}-${month}`;

        const fetchKey = `${yearMonth}-${selectedDeptId}`;

        if (lastFetchedMonth.current === fetchKey) return;
        lastFetchedMonth.current = fetchKey;

        // API 호출
        api
            .get("/vacations/calendar", {
                params: {
                    yearMonth: yearMonth,
                    deptId: selectedDeptId,
                },
            })
            .then((res) => {
                const formatted = res.data.map((vacation) => {
                    const from = vacation.from.slice(0, 10);
                    const to = vacation.to.slice(0, 10);

                    const [start, endRaw] = new Date(from) <= new Date(to)
                        ? [from, to]
                        : [to, from];

                    return {
                        title: `${vacation.name} - ${vacation.positionName} (${vacation.typeName})`,
                        start,
                        end: plusOneDay(endRaw), // ✅ 하루 더해서 inclusive하게 표시되도록
                        allDay: true,
                        color: getColorByName(vacation.name),
                    };
                });
                setEvents(formatted);
            })
            .catch((err) => {
                console.error("일정 데이터 불러오기 실패", err);
            });
    };

    const plusOneDay = (dateStr) => {
        const date = new Date(dateStr);
        date.setDate(date.getDate() + 1);
        return date.toISOString().slice(0, 10);
    };

    const calendarRef = useRef(null);

    useEffect(() => {
        const calendarApi = calendarRef.current?.getApi();
        if (calendarApi) {
            const currentDate = calendarApi.getDate();
            handleDatesSet({ view: { currentStart: currentDate } });
        }
    }, [selectedDeptId]);

    return (
        <div style={{ height: "800px" , margin: "20px"}}>
            <select
                value={selectedDeptId}
                onChange={(e) => setSelectedDeptId(Number(e.target.value))}
                style={{
                    fontSize: "16px",
                    fontWeight: "bold",
                    padding: "8px 12px",
                    border: "2px solid #7a807c",
                    borderRadius: "8px",
                    outline: "none",
                    marginBottom: "16px",
                    cursor: "pointer",
                }}
            >

            <option value={0}>전체</option> {}
            {depts.map((dept) => (
                <option key={dept.id} value={dept.id}>
                    {dept.name}
                </option>
            ))}
            </select>
            <FullCalendar
                ref={calendarRef}
                plugins={[dayGridPlugin, interactionPlugin]}
                initialView="dayGridMonth"
                events={events}
                datesSet={handleDatesSet} // 👈 이게 핵심
                height="100%"
            />
        </div>
    );
}



export default Calendar;




