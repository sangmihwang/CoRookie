import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'

import { format } from 'date-fns'
import { startOfMonth, endOfMonth, startOfWeek, endOfWeek } from 'date-fns'
import { isSameMonth, isSameDay, addDays } from 'date-fns'

import * as hooks from 'hooks'
import * as api from 'api'

const Calendar = ({ currentMonth }) => {
    const { projectId } = useParams()
    const monthStart = startOfMonth(currentMonth)
    const monthEnd = endOfMonth(monthStart)
    const startDate = startOfWeek(monthStart)
    const endDate = endOfWeek(monthEnd)
    const { planStartDate, planEndDate, onDragDate, setPlanStartDate, setPlanEndDate, setOnDragDate } =
        hooks.planDateState()
    const { openPlanRegister, closePlanRegister } = hooks.planRegisterState()
    const { openPlanDetail, closePlanDetail } = hooks.planDetailState()
    const { closeProfile } = hooks.profileState()
    const [calendar, setCalendar] = useState([])
    const [plans, setPlans] = useState([])

    const setDate = date => {
        if (date < planStartDate) {
            setPlanStartDate(date)
            setPlanEndDate(onDragDate)
            return
        }

        if (date > planEndDate) {
            setPlanStartDate(onDragDate)
            setPlanEndDate(date)
        }
    }

    const handleDragStart = e => {
        setOnDragDate(new Date(e.target.firstChild.value))
        setPlanStartDate(new Date(e.target.firstChild.value))
        setPlanEndDate(new Date(e.target.firstChild.value))

        const transparentImg = document.createElement('canvas')
        transparentImg.width = 0
        transparentImg.height = 0
        e.dataTransfer.setDragImage(transparentImg, 0, 0)
        e.dataTransfer.effectAllowed = 'move'
    }

    const handleDragOver = e => {
        e.preventDefault()
        e.currentTarget.style.cursor = 'pointer'
    }

    const dateFormat = date => {
        return (
            date.getFullYear() +
            '-' +
            (date.getMonth() + 1 <= 9 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) +
            '-' +
            (date.getDate() <= 9 ? '0' + date.getDate() : date.getDate())
        )
    }

    const openDetails = plan => {
        openPlanDetail(plan.id)
    }

    const textColorCalculator = backgroundColor => {
        const color = backgroundColor.slice(1)

        const r = parseInt(color.slice(0, 2), 16)
        const g = parseInt(color.slice(2, 4), 16)
        const b = parseInt(color.slice(4, 6), 16)

        const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255
        if (luminance < 0.5) {
            return '#ffffff'
        } else {
            return '#000000'
        }
    }

    useEffect(() => {
        const initCalendar = async () => {
            const plansRes = await api.apis.getPlans(projectId, dateFormat(currentMonth))
            setPlans(plansRes.data)
            let rows = []
            let days = []
            let dayNumbers = []
            let plans = []
            let day = startDate

            let planLength = plansRes.data.length
            while (day <= endDate) {
                for (let i = 0; i < 7; i++) {
                    days.push(
                        <S.Day
                            key={day}
                            draggable={true}
                            onDragStart={handleDragStart}
                            onDragEnter={e => setDate(new Date(e.target.firstChild.value))}
                            onDragOver={e => {
                                // e.preventDefault()
                                e.currentTarget.style.cursor = 'pointer'
                            }}
                            onDragEnd={() => {
                                closePlanDetail()
                                closeProfile()
                                openPlanRegister()
                            }}
                            onClick={e => {
                                setPlanStartDate(new Date(e.target.firstChild.value))
                                setPlanEndDate(new Date(e.target.firstChild.value))
                                closePlanDetail()
                                closeProfile()
                                openPlanRegister()
                            }}>
                            <input type="hidden" value={day} />
                        </S.Day>,
                    )
                    dayNumbers.push(
                        <S.DayNumber
                            key={day}
                            className={format(currentMonth, 'M') !== format(day, 'M') ? 'disable' : ''}>
                            {format(day, 'd')}
                        </S.DayNumber>,
                    )

                    for (let i = 0; i < planLength; i++) {
                        if (isSameDay(day, new Date(plansRes.data[i].planStart))) {
                            plans.push(
                                <S.PlanRow>
                                    <S.DayPlan
                                        key={day}
                                        style={{
                                            left: `${100.0 * (new Date(plansRes.data[i].planStart).getDay() / 7)}%`,
                                            width: `${Math.min(
                                                100.0 - 100.0 * (new Date(plansRes.data[i].planStart).getDay() / 7),
                                                100.0 *
                                                    ((Math.floor(
                                                        (new Date(plansRes.data[i].planEnd).getTime() -
                                                            new Date(plansRes.data[i].planStart).getTime()) /
                                                            (24 * 60 * 60 * 1000),
                                                    ) +
                                                        1) /
                                                        7),
                                            )}%`,
                                        }}
                                        color={plansRes.data[i].color}
                                        onDragOver={handleDragOver}
                                        onClick={() => {
                                            openDetails(plansRes.data[i])
                                            closeProfile()
                                            closePlanRegister()
                                        }}
                                        fontcolor={textColorCalculator(plansRes.data[i].color)}
                                        className="same">
                                        {plansRes.data[i].planName}
                                    </S.DayPlan>
                                </S.PlanRow>,
                            )
                        } else if (
                            day.getDay() === 0 &&
                            day <= new Date(plansRes.data[i].planEnd) &&
                            day >= new Date(plansRes.data[i].planStart)
                        ) {
                            plans.push(
                                <S.PlanRow>
                                    <S.DayPlan
                                        key={day}
                                        style={{
                                            left: `${100.0 * (day.getDay() / 7)}%`,
                                            width: `${Math.min(
                                                100.0 - 100.0 * (day.getDay() / 7),
                                                100.0 *
                                                    ((Math.floor(
                                                        (new Date(plansRes.data[i].planEnd).getTime() - day.getTime()) /
                                                            (24 * 60 * 60 * 1000),
                                                    ) +
                                                        1) /
                                                        7),
                                            )}%`,
                                        }}
                                        onDragOver={handleDragOver}
                                        onClick={() => {
                                            openDetails(plansRes.data[i])
                                            closeProfile()
                                            closePlanRegister()
                                        }}
                                        color={plansRes.data[i].color}
                                        fontColor={textColorCalculator(plansRes.data[i].color)}
                                        className="same">
                                        {plansRes.data[i].planName}
                                    </S.DayPlan>
                                </S.PlanRow>,
                            )
                        }
                    }
                    if (isSameDay(day, planStartDate)) {
                        plans.push(
                            <S.PlanRow>
                                <S.DayPlan
                                    key={day}
                                    style={{
                                        left: `${100.0 * (planStartDate.getDay() / 7)}%`,
                                        width: `${Math.min(
                                            100.0 - 100.0 * (planStartDate.getDay() / 7),
                                            100.0 *
                                                ((Math.floor(
                                                    (planEndDate.getTime() - planStartDate.getTime()) /
                                                        (24 * 60 * 60 * 1000),
                                                ) +
                                                    1) /
                                                    7),
                                        )}%`,
                                    }}
                                    onDragOver={handleDragOver}
                                    color={'#286EF0'}
                                    fontcolor={textColorCalculator('#286EF0')}
                                    className="same"></S.DayPlan>
                            </S.PlanRow>,
                        )
                    } else if (day.getDay() === 0 && day <= planEndDate && day >= planStartDate) {
                        plans.push(
                            <S.PlanRow>
                                <S.DayPlan
                                    key={day}
                                    style={{
                                        left: `${100.0 * (day.getDay() / 7)}%`,
                                        width: `${Math.min(
                                            100.0 - 100.0 * (day.getDay() / 7),
                                            100.0 *
                                                ((Math.floor(
                                                    (planEndDate.getTime() - day.getTime()) / (24 * 60 * 60 * 1000),
                                                ) +
                                                    1) /
                                                    7),
                                        )}%`,
                                    }}
                                    onDragOver={handleDragOver}
                                    color={'#286EF0'}
                                    fontcolor={textColorCalculator('#286EF0')}
                                    className="same"></S.DayPlan>
                            </S.PlanRow>,
                        )
                    }

                    day = addDays(day, 1)
                }
                rows.push(
                    <S.Week key={day}>
                        <S.DayBox>{days}</S.DayBox>
                        <S.DayHeader>{dayNumbers}</S.DayHeader>
                        <S.PlanBox>
                            <S.PlanRows>{plans}</S.PlanRows>
                        </S.PlanBox>
                    </S.Week>,
                )
                days = []
                plans = []
                dayNumbers = []
            }
            setCalendar([rows])
        }
        initCalendar()
    }, [currentMonth, planStartDate, planEndDate])

    return <S.Calendar>{calendar}</S.Calendar>
}

const S = {
    Calendar: styled.ul`
        display: flex;
        flex-direction: column;
        height: 100%;
        max-height: calc(100% - 92px);
    `,
    Week: styled.li`
        position: relative;
        border-top: 1px solid ${({ theme }) => theme.color.middlegray};
        flex: 1 1 0%;
    `,
    DayBox: styled.ul`
        display: flex;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;

        -ms-user-select: none;
        -moz-user-select: -moz-none;
        -khtml-user-select: none;
        -webkit-user-select: none;
        user-select: none;

        z-index: 99;
    `,
    Day: styled.li`
        flex: 1 1 0%;
        cursor: pointer;

        &:not(:last-child) {
            border-right: 1px solid ${({ theme }) => theme.color.middlegray};
        }
    `,
    DayHeader: styled.ul`
        display: flex;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 24px;
    `,
    DayNumber: styled.li`
        display: flex;
        align-items: center;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        padding: 0 12px;
        flex: 1 1 0%;

        &:first-child {
            color: ${({ theme }) => theme.color.warning};
        }

        &:last-child {
            color: ${({ theme }) => theme.color.main};
        }

        &.disable {
            color: ${({ theme }) => theme.color.middlegray};
        }
    `,
    PlanBox: styled.div`
        display: flex;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
    `,
    PlanRows: styled.ul`
        flex: 1 1 0%;
        margin: 24px 0 0;
    `,
    PlanRow: styled.li`
        display: flex;
        position: relative;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        width: 100%;
        height: 24px;
    `,
    DayPlan: styled.li`
        display: flex;
        align-items: center;
        flex: 1 1 0%;
        top: 0;
        position: absolute;
        height: 100%;
        cursor: pointer;
        z-index: 100;
        padding: 0 8px;

        &.same {
            border-bottom: 2px solid ${({ theme, color }) => (color ? color : theme.color.main)};
        }

        &:active {
            cursor: pointer;
        }

        &:hover {
            background-color: ${({ theme, color }) => (color ? color : theme.color.main)};
            color: ${({ theme, fontcolor }) => (fontcolor ? fontcolor : theme.color.white)};
            opacity: 0.6;
            cursor: pointer;
        }
    `,
}

export default Calendar
