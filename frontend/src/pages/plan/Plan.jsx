import React, { useState, useEffect } from 'react'
import styled from 'styled-components'

import { format, addMonths, subMonths } from 'date-fns'
import { DndProvider } from 'react-dnd'
import { HTML5Backend } from 'react-dnd-html5-backend'

import { BsPlus } from 'react-icons/bs'
import { IoIosArrowForward, IoIosArrowBack } from 'react-icons/io'

import * as components from 'components'
import * as hooks from 'hooks'

const Plan = () => {
    const [currentMonth, setCurrentMonth] = useState(new Date())
    const { planRegisterOpened, openPlanRegister } = hooks.planRegisterState()
    const { closePlanDetail } = hooks.planDetailState()
    const { planDetailOpened } = hooks.planDetailState()
    const { closeProfile } = hooks.profileState()

    const prevMonth = () => {
        setCurrentMonth(subMonths(currentMonth, 1))
    }

    const nextMonth = () => {
        setCurrentMonth(addMonths(currentMonth, 1))
    }

    return (
        <S.Wrap>
            <S.Container>
                <S.CalendarHeader>
                    <S.CalendarAccess>
                        <S.MonthBox>
                            <IoIosArrowBack onClick={() => prevMonth()} />
                            <div>
                                {format(currentMonth, 'yyyy')}년 {format(currentMonth, 'M')}월
                            </div>
                            <IoIosArrowForward onClick={() => nextMonth()} />
                        </S.MonthBox>
                        <S.CreateButton
                            onClick={() => {
                                openPlanRegister()
                                closePlanDetail()
                                closeProfile()
                            }}>
                            <BsPlus /> 생성
                        </S.CreateButton>
                    </S.CalendarAccess>
                    <S.WeekHeader>
                        <S.DayOfWeek>S</S.DayOfWeek>
                        <S.DayOfWeek>M</S.DayOfWeek>
                        <S.DayOfWeek>T</S.DayOfWeek>
                        <S.DayOfWeek>W</S.DayOfWeek>
                        <S.DayOfWeek>T</S.DayOfWeek>
                        <S.DayOfWeek>F</S.DayOfWeek>
                        <S.DayOfWeek>S</S.DayOfWeek>
                    </S.WeekHeader>
                </S.CalendarHeader>
                <DndProvider backend={HTML5Backend}>
                    <components.Calendar currentMonth={currentMonth} />
                </DndProvider>
            </S.Container>
            {planRegisterOpened && <components.PlanRegister />}
            {planDetailOpened && <components.PlanDetail />}
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 100%;
        height: 100%;
        max-height: calc(100vh - 56px);
    `,
    Container: styled.div`
        width: 100%;
        max-height: calc(100vh - 56px);
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        margin: 16px 16px 16px 0;
    `,
    CalendarHeader: styled.div`
        position: relative;
        width: 100%;
    `,
    CalendarAccess: styled.div`
        width: 100%;
        height: 60px;
        display: flex;
        justify-content: center;
        align-items: center;
    `,
    MonthBox: styled.div`
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: ${({ theme }) => theme.fontsize.title2};

        & > div {
            margin: 0 24px;
        }

        & > svg {
            width: 24px;
            height: 24px;
            margin: 2px 0 0;
            cursor: pointer;
            transition-duration: 0.2s;
        }

        & > svg:first-child:hover {
            color: ${({ theme }) => theme.color.main};
            transform: translateX(-2px);
        }

        & > svg:last-child:hover {
            color: ${({ theme }) => theme.color.main};
            transform: translateX(2px);
        }

        -ms-user-select: none;
        -moz-user-select: -moz-none;
        -khtml-user-select: none;
        -webkit-user-select: none;
        user-select: none;
    `,
    CreateButton: styled.button`
        display: flex;
        justify-content: center;
        align-items: center;
        position: absolute;
        right: 0;
        width: 56px;
        height: 30px;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.main};
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        margin: 0 16px;
        transition-duration: 0.2s;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.main};
            box-shadow: none;
        }
    `,
    DetailButton: styled.button`
        display: flex;
        justify-content: center;
        align-items: center;
        position: absolute;
        right: 80px;
        width: 56px;
        height: 30px;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.main};
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        margin: 0 16px;
        transition-duration: 0.2s;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.main};
            box-shadow: none;
        }
    `,

    WeekHeader: styled.div`
        width: 100%;
        height: 32px;
        display: flex;
        align-items: center;
    `,
    DayOfWeek: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: ${({ theme }) => theme.fontsize.title2};
        padding: 0 12px;
        flex: 1 1 0%;

        &:first-child {
            color: ${({ theme }) => theme.color.warning};
        }

        &:last-child {
            color: ${({ theme }) => theme.color.main};
        }
    `,
}

export default Plan
