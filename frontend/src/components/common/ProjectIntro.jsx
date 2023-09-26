import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'

import { IoPeople, IoChevronForward, IoClose, IoRocket, IoArrowForwardOutline } from 'react-icons/io5'
import { AiOutlineCheckSquare } from 'react-icons/ai'

import * as hooks from 'hooks'
import * as components from 'components'
import * as api from 'api'
import { useNavigate } from 'react-router-dom'

const ProjectIntro = () => {
    const { projectId } = useParams()
    const { manager, managerOpened, openManager, closeManager } = hooks.setManagerState()
    const { projectMembers } = hooks.projectMembersState()
    const { members, setMembers, memberOpened, openMember, closeMember } = hooks.memberState()
    const { project } = hooks.projectState()
    const [projects, setProjects] = useState([])
    const [flip, setFlip] = useState(false)
    const managerTextRef = useRef(null)
    const memberTextRef = useRef(null)
    const navigate = useNavigate()

    useEffect(() => {
        api.apis
            .getProjects()
            .then(response => setProjects(response.data))
            .catch(error => console.log(error))
    }, [])

    let flippedRef = useRef(null)

    const clickManager = e => {
        if (managerOpened) {
            closeManager()
            return
        }
        if (project.isManager) {
            openManager()
        }
    }

    const clickMember = () => {
        if (memberOpened) {
            closeMember()
        } else {
            openMember()
        }
    }

    useEffect(() => {
        api.apis
            .getProjectMembers(projectId)
            .then(response => {
                setMembers(response.data)
            })
            .catch(error => {
                console.log('멤버 불러오기 실패', error)
            })
    }, [])

    useEffect(() => {
        const handleOutside = e => {
            if (
                flippedRef.current &&
                !flippedRef.current.contains(e.target) &&
                !flippedRef.current.contains(e.target)
            ) {
                setFlip(false)
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [flippedRef])

    return (
        <S.Wrap>
            <S.RotateContainer className={flip ? 'card' : null} ref={flippedRef}>
                <S.Container className="cards">
                    <S.Title>
                        {project.name}
                        <IoChevronForward onClick={() => setFlip(!flip)} />
                    </S.Title>
                    <S.Description>{project.description}</S.Description>
                    <S.Line />
                    <S.MemberInfo>
                        <S.Manager>
                            <S.ManagerText ref={managerTextRef} onClick={e => clickManager(e)}>
                                관리자
                            </S.ManagerText>
                            <S.ManagerName>{project.managerName}</S.ManagerName>
                        </S.Manager>
                        <S.Members ref={memberTextRef} onClick={() => clickMember()}>
                            <IoPeople />
                            {projectMembers.length}
                        </S.Members>
                        <components.MemberSetting memberTextRef={memberTextRef} />
                    </S.MemberInfo>
                </S.Container>
                <S.ProjectsContainer className="cards">
                    {projects.map((project, index) => {
                        return (
                            <S.Project
                                key={index}
                                onClick={() => {
                                    if (window.confirm(`${project.name}으로 이동하시겠습니까?`)) {
                                        navigate('/project/' + project.id)
                                        window.location.reload()
                                        alert(`${project.name} 이동완료`)
                                    }
                                }}>
                                <S.ProjectText>{project.name}</S.ProjectText>
                                <S.MoveIcon>
                                    <IoArrowForwardOutline />
                                </S.MoveIcon>
                            </S.Project>
                        )
                    })}
                </S.ProjectsContainer>
            </S.RotateContainer>
            <components.ManagerSetting managerTextRef={managerTextRef} />
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 216px;
        height: 165px;
        flex-direction: column;
        transition: transform 0.3s;
        transform: perspective(800px) rotateY(0deg);
        position: relative;
        z-index: 10000;
        & > .card {
            transform: rotateY(180deg);
        }
    `,
    RotateContainer: styled.div`
        display: flex;
        transition: all 0.5s;
        transform-style: preserve-3d;
        height: 100%;
        width: 100%;
        margin: 16px 0 0;
        position: relative;
        overflow: visible;
        & > .cards {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            backface-visibility: hidden;
        }
    `,
    Container: styled.div`
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: left;
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        box-shadow: ${({ theme }) => theme.shadow.card};
        padding: 20px 20px 2px;
        width: 100%;
        height: 100%;
    `,
    ProjectsContainer: styled.div`
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        z-index: 5;
        transform: rotateY(180deg);
        padding: 8px;
        overflow-y: scroll;
        width: 100%;
        height: 100%;
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
    ProjectText: styled.div`
        display: flex;
        align-items: center;
        font-size: ${({ theme }) => theme.fontsize.content};
        z-index: 7;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 100%;
    `,
    MoveIcon: styled.div`
        display: flex;
        align-items: center;
        justify-content: center;
        width: 20px;
        height: 20px;
        border-radius: 100%;

        background-color: ${({ theme }) => theme.color.middlegray};
        & svg {
            width: 16px;
            height: 16px;
            transform: rotate(-45deg);
        }
    `,
    Project: styled.div`
        display: flex;
        align-items: center;
        width: 200px;
        height: 48px;
        padding: 0 16px;
        margin-bottom: 8px;
        justify-content: space-between;
        font-size: ${({ theme }) => theme.fontsize.content};
        cursor: pointer;
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        position: relative;
        filter: drop-shadow(1px 2px 7px rgba(0, 0, 0, 0.2));
        transition-duration: 0.2s;
        &:first-child {
            margin-top: 4px;
        }
        &:hover {
            transform: translateY(-3px);
        }
    `,
    Title: styled.div`
        display: flex;
        font-size: ${({ theme }) => theme.fontsize.title2};
        margin: 0 0 16px 0;
        justify-content: space-between;
        align-items: center;
        & svg {
            width: 20px;
            height: 20px;
            color: ${({ theme }) => theme.color.gray};
            cursor: pointer;
            & :hover {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
    Description: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        line-height: ${({ theme }) => theme.lineheight.content};
        padding-bottom: 8px;
        max-height: 54px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    `,
    Line: styled.div`
        margin: auto 8px 0;
        min-height: 1px;
        width: 206px;
        align-self: center;
        z-index: 100;
        background-color: ${({ theme }) => theme.color.lightgray};
    `,
    MemberInfo: styled.div`
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    `,
    Manager: styled.div`
        display: flex;
        width: 130px;
        height: auto;
        padding: 8px 0;
    `,
    ManagerText: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.main};
        white-space: nowrap;
        margin-right: 4px;
        cursor: pointer;
    `,
    ManagerName: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.black};
        max-width: 100%;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    `,
    Members: styled.div`
        display: flex;
        align-items: center;
        font-size: ${({ theme }) => theme.fontsize.title3};
        color: ${({ theme }) => theme.color.gray};
        cursor: pointer;
        & svg {
            width: 20px;
            height: 20px;
            color: ${({ theme }) => theme.color.gray};
            margin-right: 4px;
        }
        &:hover {
            color: ${({ theme }) => theme.color.main};
            & svg {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
}

export default ProjectIntro
