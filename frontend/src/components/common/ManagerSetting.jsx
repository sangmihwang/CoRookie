import React, { useEffect, useRef } from 'react'
import styled from 'styled-components'

import * as hooks from 'hooks'
import { FaCrown } from 'react-icons/fa'

const ManagerSetting = ({ managerTextRef }) => {
    const { managerOpened, closeManager } = hooks.setManagerState()
    const { project, setProject } = hooks.projectState()
    const { members } = hooks.memberState()
    const managerChange = member => {
        if (window.confirm(`관리자를 ${member}로 변경하시겠습니까?`)) {
            setProject({ ...project, managerName: member })
        }
    }

    let managerRef = useRef(null)
    useEffect(() => {
        const handleOutside = e => {
            if (
                managerRef.current &&
                !managerRef.current.contains(e.target) &&
                !managerTextRef.current.contains(e.target)
            ) {
                closeManager()
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [managerRef])
    return (
        <S.Wrap ref={managerRef}>
            {managerOpened ? (
                <S.Container>
                    <S.Title>관리자 변경</S.Title>
                    <S.Line />
                    {project.isManager &&
                        members.map(member => {
                            return (
                                <S.Member
                                    key={member.memberId}
                                    className={member.name === project.managerName ? 'manager' : null}>
                                    <img
                                        src={
                                            member.memberImageUrl === ''
                                                ? require('images/profile.png').default
                                                : member.memberImageUrl
                                        }
                                        alt={member.memberName}
                                    />
                                    <S.Name>{member.memberName}</S.Name>
                                    {member.memberName !== project.managerName ? (
                                        <S.ManagerButton>
                                            <FaCrown onClick={() => managerChange(member.memberName)} />
                                        </S.ManagerButton>
                                    ) : (
                                        <S.CurrentManager>
                                            <FaCrown />
                                        </S.CurrentManager>
                                    )}
                                </S.Member>
                            )
                        })}
                    {!project.isManager && (
                        <S.Member className="manager">
                            {/* <img src={member.img} alt={member.memberName} /> */}
                            <S.Name>project.managerName</S.Name>
                            <S.ManagerButton>
                                <FaCrown />
                            </S.ManagerButton>
                        </S.Member>
                    )}
                </S.Container>
            ) : null}
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        position: absolute;
        top: 166px;
        left: 8px;
        flex-direction: column;
        width: 180px;
        height: auto;
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        /* border: 1px solid ${({ theme }) => theme.color.lightgray}; */
        z-index: 1000;
        box-shadow: 0px 1px 5px 0px rgba(0, 0, 0, 0.2);
        &.open div {
            height: auto;
        }
    `,
    Container: styled.div`
        display: flex;
        flex-direction: column;
        padding: 8px;
    `,
    Title: styled.div`
        padding: 8px;
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.black};
    `,
    Line: styled.div`
        margin: 4px;
        height: 1px;
        width: 95%;
        align-self: center;
        background-color: ${({ theme }) => theme.color.lightgray};
    `,
    Member: styled.div`
        display: flex;
        align-items: center;
        padding: 8px 12px;
        & > img {
            width: 30px;
            height: 30px;
            margin-right: 16px;
            border-radius: 4px;
        }
        &.manager div {
            color: ${({ theme }) => theme.color.gray};
        }
    `,
    Name: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.black};
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        width: 200px;
    `,
    ManagerButton: styled.div`
        display: flex;
        width: 100%;
        justify-content: flex-end;
        & svg {
            width: 16px;
            height: 16px;
            color: ${({ theme }) => theme.color.lightgray};
            cursor: pointer;
            &:hover {
                color: ${({ theme }) => theme.color.pending};
            }
        }
    `,
    CurrentManager: styled.div`
        display: flex;
        width: 100%;
        justify-content: flex-end;
        & svg {
            width: 16px;
            height: 16px;
            color: ${({ theme }) => theme.color.main};
        }
    `,
}

export default ManagerSetting
