import React, { useState } from 'react'
import Calendar from 'react-calendar'
import 'react-calendar/dist/Calendar.css'
import styled from 'styled-components'

const CalendarBoard = () => {
    const [value, onChange] = useState(new Date())

    return (
        <S.Wrap>
            <Calendar onChange={onChange} value={value} />
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        margin: 16px;
        padding: 16px 16px;
    `,
}

export default CalendarBoard
