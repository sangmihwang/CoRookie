import React, { useEffect, useRef } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'

import * as hooks from 'hooks'
import * as api from 'api'
import { FaCrown } from 'react-icons/fa'
import { IoClose, IoLink } from 'react-icons/io5'

const MemberSetting = ({ memberTextRef }) => {
    const { projectId } = useParams()
    const { members, memberOpened, closeMember, removeMember } = hooks.memberState()
    const { projectMembers, setProjectMembers } = hooks.projectMembersState()
    const { manager } = hooks.setManagerState()
    const { project } = hooks.projectState()
    const { linkActivated, activateLink, deactivateLink } = hooks.linkState()
    let componentRef = useRef(null)

    const copyLink = async text => {
        try {
            await navigator.clipboard.writeText(text)
            alert('초대코드가 복사되었습니다!')
        } catch (e) {
            alert('초대코드 복사에 실패했습니다')
        }
    }

    const handleRemoveMember = member => {
        if (project.isManager && window.confirm(`${member.memberName}님을 팀에서 퇴출시키겠습니까? `)) {
            removeMember(member.memberId)
        }
    }

    const handleLinkActivation = () => {
        if (linkActivated) {
            api.apis.disableInvitation(projectId)
            deactivateLink()
        } else {
            if (!project.isManager) {
                alert('프로젝트 관리자만 초대할 수 있습니다')
                return
            }
            api.apis.enableInvitation(projectId)
            activateLink()
        }
    }

    useEffect(() => {
        if (project.invitationStatus) {
            activateLink()
        } else {
            deactivateLink()
        }
    }, [project])

    useEffect(() => {
        const handleOutside = e => {
            if (
                componentRef.current &&
                !componentRef.current.contains(e.target) &&
                !memberTextRef.current.contains(e.target)
            ) {
                closeMember()
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [componentRef])

    return (
        <S.Wrap ref={componentRef}>
            {memberOpened && (
                <S.Container>
                    <S.Title>멤버</S.Title>
                    <S.Line />
                    {members.map(member => {
                        return (
                            <S.Member
                                key={member.memberId}
                                className={member.memberName === project.managerName ? 'manager' : null}>
                                <img
                                    src={
                                        member.memberImageUrl === ''
                                            ? require('images/profile.png').default
                                            : member.memberImageUrl
                                    }
                                    alt={member.memberName}
                                />
                                <S.Name>{member.memberName}</S.Name>
                                {member.memberName === project.managerName ? (
                                    <S.ManagerButton>
                                        <FaCrown />
                                    </S.ManagerButton>
                                ) : (
                                    <S.RemoveButton className={project.isManager ? 'Manager' : null}>
                                        <IoClose onClick={() => handleRemoveMember(member)} />
                                    </S.RemoveButton>
                                )}
                            </S.Member>
                        )
                    })}
                    {project.isManager && (
                        <>
                            <S.Line />
                            <S.ActivateLink>
                                <S.Text>링크 활성화</S.Text>
                                <S.SlideButton className={linkActivated ? 'active' : ''} onClick={handleLinkActivation}>
                                    <S.InnerButton />
                                </S.SlideButton>
                            </S.ActivateLink>
                        </>
                    )}
                    {project.isManager && linkActivated && (
                        <S.CreateLink>
                            <S.Text>초대링크 복사</S.Text>
                            <IoLink
                                onClick={() =>
                                    copyLink(`https://i9a402.p.ssafy.io/project/invite/${project.invitationLink}`)
                                }
                            />
                        </S.CreateLink>
                    )}
                </S.Container>
            )}
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        position: absolute;
        top: 116px;
        left: 217px;
        z-index: 999;
        background-color: ${({ theme }) => theme.color.white};
        width: 180px;
        height: auto;
        border-radius: 8px;
        /* border: 1px solid ${({ theme }) => theme.color.lightgray}; */
        z-index: 100;
        box-shadow: 0px 1px 5px 0px rgba(0, 0, 0, 0.2);
        &.open div {
            height: auto;
        }
    `,
    Container: styled.div`
        display: flex;
        flex-direction: column;
        padding: 8px;
        width: 100%;
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
        &.Manager {
            height: auto;
        }
    `,
    Name: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.black};
        width: 200px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    `,
    ManagerButton: styled.div`
        display: flex;
        width: 100%;
        justify-content: flex-end;
        & svg {
            width: 16px;
            height: 16px;
            color: ${({ theme }) => theme.color.main};
        }
    `,
    RemoveButton: styled.div`
        display: flex;
        width: 100%;
        justify-content: flex-end;
        height: 0px;
        overflow: hidden;
        & svg {
            width: 16px;
            height: 16px;
            color: ${({ theme }) => theme.color.black};
            cursor: pointer;
            &:hover {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
    ActivateLink: styled.div`
        display: flex;
        width: 100%;
        height: auto;
        justify-content: space-between;
        padding: 8px 12px;
        &.actived div {
            justify-content: flex-end;
            background-color: ${({ theme }) => theme.color.lightgray};
        }
    `,
    Text: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.black};
        white-space: nowrap;
    `,
    SlideButton: styled.div`
        display: flex;
        width: 28px;
        height: 15px;
        align-items: center;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.gray};
        padding: 2px;
        transition: background-color 0.5s;
        cursor: pointer;

        &.active {
            background-color: ${({ theme }) => theme.color.main};
        }
    `,
    InnerButton: styled.div`
        width: 10px;
        height: 10px;
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 100%;
        /* margin-left: 0px; */
        transition: all 0.1s linear;

        .active & {
            margin-left: 13px;
        }
    `,
    CreateLink: styled.div`
        display: flex;
        width: 100%;
        height: auto;
        justify-content: space-between;
        align-items: center;
        padding: 8px 12px;
        transition: 0.3s;
        & svg {
            width: 24px;
            height: 24px;
            color: ${({ theme }) => theme.color.main};
            cursor: pointer;
        }

        & > svg:hover {
            opacity: 0.6;
        }
    `,
}

export default MemberSetting
