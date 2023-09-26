import React, { useEffect, useState, useRef } from 'react'
import styled from 'styled-components'

import * as utils from 'utils'
import * as hooks from 'hooks'
import { IoAdd } from 'react-icons/io5'
import { apis } from 'api'

const PlanOptionToggle = ({ state, plan, setPlan }) => {
    const [isActive, setIsActive] = useState(false)
    const [members, setMembers] = useState([])
    const { projectMembers } = hooks.projectMembersState()
    const optionRef = useRef(null)

    useEffect(() => {
        const handleOutside = e => {
            if (optionRef.current && !optionRef.current.contains(e.target)) {
                setIsActive(false)
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [optionRef])

    const clickSelectedMember = id => {
        setMembers(members.filter(member => member.memberId !== id))
        setPlan({
            ...plan,
            memberIds: plan.memberIds.filter(memberId => memberId !== id),
        })
    }

    return (
        <S.PlanOptionBox>
            <S.PlanOptionLabel>참여자</S.PlanOptionLabel>
            <S.Selector className={isActive ? 'active' : null}>
                <S.Label onClick={() => setIsActive(!isActive)}>
                    {members.length === 0
                        ? '참여자'
                        : members.map((selectedMember, index) => (
                              <S.SelectedMember
                                  key={index}
                                  onClick={() => clickSelectedMember(selectedMember.memberId)}>
                                  {selectedMember.memberName}
                              </S.SelectedMember>
                          ))}
                </S.Label>
                <S.Options ref={optionRef}>
                    {projectMembers.map((option, index) => (
                        <S.Option
                            key={index}
                            onClick={() => {
                                if (!members.some(person => person.memberId === option.memberId)) {
                                    setIsActive(false)
                                    setMembers([...members, option])
                                    setPlan({
                                        ...plan,
                                        memberIds: [...members, option.memberId],
                                    })
                                }
                            }}>
                            {option.memberName}
                        </S.Option>
                    ))}
                </S.Options>
            </S.Selector>
        </S.PlanOptionBox>
    )
}

const S = {
    PlanOptionBox: styled.div`
        display: flex;
        align-items: center;
        margin: 10px 0;
        padding: 0 48px 0 14px;
    `,
    PlanOptionLabel: styled.div`
        display: flex;
        margin: 10px 0;
    `,
    Selector: styled.div`
        position: relative;
        display: flex;
        border: solid 1px ${({ theme }) => theme.color.gray};
        border-radius: 8px;
        height: 32px;
        width: 230px;
        background-color: ${({ isActive, theme }) => (!isActive ? theme.color.white : theme.color.main)};
        margin: 0 0 0 auto;
        cursor: pointer;
        &.active ul {
            border: solid 1px ${({ theme }) => theme.color.gray};
            max-height: 500px;
        }
    `,
    Label: styled.button`
        display: flex;
        justify-content: flex-start;
        align-items: center;
        height: 100%;
        flex-grow: 1;
        padding: 0 16px;
        cursor: pointer;
        pointer-events: auto;
        border-radius: 8px;
        font-family: ${({ theme }) => theme.font.main};
        font-size: ${({ theme }) => theme.fontsize.sub1};
    `,
    SelectedMember: styled.div`
        display: flex;
        padding: 0 4px;
    `,
    Options: styled.ul`
        position: absolute;
        top: 31px;
        left: 0;
        width: inherit;
        background: #fff;
        color: #000;
        list-style-type: none;
        padding: 0;
        border-radius: 8px;
        overflow: auto;
        max-height: 0;
        transition: 0.3s;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        z-index: 999;

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
    Option: styled.li`
        padding: 8px;
        cursor: pointer;
        white-space: nowrap;
        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
        }

        &:first-child {
            border-radius: 8px 8px 0 0;
        }

        &:last-child {
            border-radius: 0 0 8px 8px;
        }
    `,
}

export default PlanOptionToggle
