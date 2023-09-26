import React from 'react'
import styled from 'styled-components'

import * as hooks from 'hooks'
import { IoReorderTwoSharp } from 'react-icons/io5'
import { BiChevronsUp, BiChevronUp, BiChevronDown, BiChevronsDown } from 'react-icons/bi'

const IssuePreview = ({ task }) => {
    const { issueDetailOpened, openIssueDetail, closeIssueDetail } = hooks.issueDetailState()
    const { memberId, membereImageUrl } = hooks.meState()

    const renderPriority = priority => {
        switch (priority) {
            case 'Highest':
                return <BiChevronsUp />
            case 'High':
                return <BiChevronUp />
            case 'Normal':
                return <IoReorderTwoSharp />
            case 'Low':
                return <BiChevronDown />
            case 'Lowest':
                return <BiChevronsDown />
            default:
                return null
        }
    }

    const toggleIssueDetail = id => {
        if (issueDetailOpened === id) {
            closeIssueDetail()
        } else {
            openIssueDetail(id)
        }
    }

    const renderProfile = task => {
        console.log(task)
        if (task.memberImageUrl === '' || task.memberId === null) {
            return <img src={require('images/profile.png').default} alt="프로필 이미지" />
        }
        return <img src={task.memberImageUrl} alt="프로필 이미지" />
    }

    if (!task) {
        return
    }

    return (
        <S.Wrap onClick={() => toggleIssueDetail(task.id)} id={task.id} issueDetailOpened={issueDetailOpened}>
            {task.topic}
            <S.Description>
                <S.Status status={task.progress} />
                <S.Type>{task.category}</S.Type>
                <S.Profile>{renderProfile(task)}</S.Profile>
                <S.Priority priority={task.priority}>{renderPriority(task.priority)}</S.Priority>
            </S.Description>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        min-height: 48px;
        margin: 8px 0;
        padding: 8px 16px;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        background-color: ${({ theme }) => theme.color.white};
        border: 2px solid
            ${({ theme, id, issueDetailOpened }) => (id === issueDetailOpened ? theme.color.main : theme.color.white)};
        border-radius: 8px;
        &:first-child {
            margin-top: 0;
            &:hover {
                transform: translateY(0);
                box-shadow: 1px 1px 5px 1px ${({ theme }) => theme.color.middlegray};
            }
        }
        &:last-child {
            margin-bottom: 0;
        }
        transition-duration: 0.2s;
        cursor: pointer;
        &:hover {
            transform: translateY(-3px);
            box-shadow: 1px 1px 5px 1px ${({ theme }) => theme.color.middlegray};
        }
    `,
    Status: styled.div`
        padding: 4px;
        width: 2px;
        height: 2px;
        border-radius: 100%;
        background-color: ${({ theme, status }) => {
            switch (status) {
                case 'todo':
                    return theme.color.success
                case 'inProgress':
                    return theme.color.pending
                default:
                    return theme.color.orange
            }
        }};
    `,
    Description: styled.div`
        display: flex;
        justify-content: space-between;
        align-items: center;
    `,
    Type: styled.div`
        margin: 0 8px;
        font-size: 13px;
        color: ${({ theme }) => theme.color.middlegray};
    `,
    Profile: styled.div`
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 8px;
        & img {
            width: 30px;
            height: 30px;
            border-radius: 4px;
        }
    `,
    Priority: styled.div`
        display: flex;
        width: 20px;
        margin: 0 8px;
        justify-content: center;
        & svg {
            width: 100%;
            height: 20px;
            color: ${({ theme, priority }) => {
                switch (priority) {
                    case 'Highest':
                        return theme.color.warning
                    case 'High':
                        return theme.color.warning
                    case 'Normal':
                        return theme.color.pending
                    case 'Low':
                        return theme.color.success
                    case 'Lowest':
                        return theme.color.success
                    default:
                        return theme.color.main
                }
            }};
        }
    `,
}

export default IssuePreview
