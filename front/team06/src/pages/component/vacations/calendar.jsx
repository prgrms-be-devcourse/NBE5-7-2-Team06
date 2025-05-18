import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import axios from 'axios';
import React, { useEffect, useState } from "react";

const Calendar = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        // 날짜 범위 등 필요한 쿼리 파라미터 붙일 수도 있어
        axios
            .get("/vacations/calendar", {
                params: {
                    yearMonth: "2025-05",
                    deptCode: 1
                }
            })
            .then((res) => {
                const formatted = res.data.map((vacation) => ({
                    title: vacation.name + " - " + vacation.positionName,
                    start: vacation.from,
                    end: vacation.to,
                }));
                setEvents(formatted);
            })
            .catch((err) => {
                console.error("일정 데이터 불러오기 실패", err);
            });
    }, []);

    return (
        <div style={{ height: "500px" , margin: "20px"}}>
            <FullCalendar
                plugins={[dayGridPlugin]}
                initialView="dayGridMonth"
                height="100%"
            />
        </div>
    );
}



export default Calendar;




