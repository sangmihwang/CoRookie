import React, { useState, useEffect } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'
import DatePicker from 'react-datepicker'
import { ko } from 'date-fns/esm/locale'

import { IoIosClose } from 'react-icons/io'

import * as components from 'components'
import * as style from 'style'
import * as hooks from 'hooks'
import * as api from 'api'

const PlanDetail = () => {
    const { projectId } = useParams()
    const { planStartDate, setPlanStartDate, planEndDate, setPlanEndDate } = hooks.planDateState()
    const { planDetailOpened, closePlanDetail } = hooks.planDetailState()
    const [currentPlan, setCurrentPlan] = useState(null)

    useEffect(() => {
        api.apis.getPlan(projectId, planDetailOpened).then(response => {
            setCurrentPlan(response.data)
        })
    }, [planDetailOpened])

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

    if (!currentPlan) {
        return
    }

    return (
        <S.Wrap>
            <S.Header>
                <S.Title>일정 상세</S.Title>
                <S.CloseButton onClick={() => closePlanDetail()}>
                    <IoIosClose />
                </S.CloseButton>
            </S.Header>
            <S.PlanTitleBox>
                <S.PlanTitleLabel>제목</S.PlanTitleLabel>
                <S.PlanTitleInput placeholder="제목 작성">{currentPlan.planName}</S.PlanTitleInput>
            </S.PlanTitleBox>
            <S.PlanDateBox>
                <S.PlanDateLabel>날짜</S.PlanDateLabel>
                <S.PlanDatePickerBox>
                    <S.PlanDatePicker selected={new Date(currentPlan.planStart)} dateFormat="yyyy.MM.dd" locale={ko} />
                    <span> ~ </span>
                    <S.PlanDatePicker selected={new Date(currentPlan.planEnd)} dateFormat="yyyy.MM.dd" locale={ko} />
                </S.PlanDatePickerBox>
            </S.PlanDateBox>
            <S.Label>
                <S.PlanOptionHeader>참여자</S.PlanOptionHeader>
                {currentPlan.members.map(member => (
                    <S.Member>{member.memberName}</S.Member>
                ))}
            </S.Label>
            <S.Label>
                <S.PlanOptionHeader>분류</S.PlanOptionHeader>
                {currentPlan.categories.map(category => (
                    <S.Category color={category.color} fontColor={textColorCalculator(category.color)}>
                        {category.content}
                    </S.Category>
                ))}
            </S.Label>
            <S.PlanContentBox>
                <S.PlanContentHeader>설명</S.PlanContentHeader>
                <S.PlanContentInput placeholder="내용">{currentPlan.description}</S.PlanContentInput>
            </S.PlanContentBox>
            <S.EditButton>수정</S.EditButton>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 400px;
        min-width: 400px;
        border-radius: 8px;
        flex-direction: column;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 16px;
        overflow: auto;
        /* animation: ${style.leftSlide} 0.4s linear; */

        &::-webkit-scrollbar {
            height: 0px;
            width: 4px;
        }
        &::-webkit-scrollbar-track {
            background: transparent;
        }
        &::-webkit-scrollbar-thumb {
            background: ${({ theme }) => theme.color.gray};
            border-radius: 45px;
        }
        &::-webkit-scrollbar-thumb:hover {
            background: ${({ theme }) => theme.color.gray};
        }
    `,
    Header: styled.div`
        display: flex;
        align-items: center;
        height: 34px;
    `,
    Title: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title2};
    `,
    CloseButton: styled.div`
        color: ${({ theme }) => theme.color.gray};
        margin: 0 0 0 auto;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.main};
        }
        & svg {
            width: 40.4px;
            height: 40.4px;
            margin-right: -16px;
        }
    `,
    PlanTitleBox: styled.div`
        display: flex;
        height: 48px;
        border: 1px solid ${({ theme }) => theme.color.gray};
        border-radius: 8px;
        margin: 32px 0 22px;
        padding: 8px 0;
    `,
    PlanTitleLabel: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        flex-grow: 1;
        border-right: 1px solid ${({ theme }) => theme.color.gray};
    `,
    PlanTitleInput: styled.div`
        display: flex;
        align-items: center;
        flex-grow: 5;
        border: none;
        outline: none;
        font-family: ${({ theme }) => theme.font.main};
        font-size: ${({ theme }) => theme.fontsize.title3};
        padding: 0 16px;
    `,
    PlanDateBox: styled.div`
        display: flex;
        align-items: center;
        margin: 10px 0;
        padding: 0 48px 0 14px;
    `,
    PlanDateLabel: styled.div``,
    PlanDatePickerBox: styled.div`
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 230px;
        flex-wrap: nowrap;
        white-space: nowrap;
        margin: 0 0 0 auto;

        & > span {
            flex-grow: 1;
            text-align: center;
        }
    `,
    PlanDatePicker: styled(DatePicker)`
        width: 100px;
        height: 32px;
        padding: 8px 16px;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.gray};
    `,
    PlanContentBox: styled.div`
        min-height: 300px;
        border: 1px solid ${({ theme }) => theme.color.gray};
        border-radius: 8px;
        margin: 28px 8px;
        padding: 0 16px 16px;
    `,
    PlanContentHeader: styled.div`
        display: flex;
        align-items: center;
        height: 48px;
        min-height: 48px;
        border-bottom: 1px solid ${({ theme }) => theme.color.lightgray};
    `,
    PlanContentInput: styled.div`
        height: calc(100% - 48px);
        width: 100%;
        outline: none;
        resize: none;
        border: none;
        padding: 16px 0;
        font-family: ${({ theme }) => theme.font.main};
        font-size: ${({ theme }) => theme.fontsize.sub1};
    `,
    EditButton: styled.button`
        width: 60px;
        height: 38px;
        min-height: 38px;
        align-self: center;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.main};
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        margin: auto 0 0 0;
        transition: all 0.2s linear;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.main};
        }
    `,
    Label: styled.button`
        display: flex;
        justify-content: flex-start;
        align-items: center;
        padding: 0 14px;
        margin: 10px 0;
        cursor: pointer;
        pointer-events: auto;
        border-radius: 8px;
        font-family: ${({ theme }) => theme.font.main};
        font-size: ${({ theme }) => theme.fontsize.sub1};
    `,
    PlanOptionHeader: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title3};
        margin: 0 32px 0 0;
    `,
    Member: styled.div`
        display: flex;
        padding: 0 4px;
    `,
    Category: styled.div`
        display: flex;
        padding: 3px 6px;
        align-items: center;
        justify-content: space-between;
        background-color: ${props => props.color};
        color: ${props => props.fontColor};
        border-radius: 8px;
        margin-right: 2px;
        white-space: nowrap;
        & svg {
            width: 16px;
            height: 16px;
            color: ${props => props.fontColor};
            cursor: pointer;
            &:hover {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
}

export default PlanDetail
