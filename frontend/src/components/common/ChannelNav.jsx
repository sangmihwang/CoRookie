import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'

import { IoIosArrowUp } from 'react-icons/io'
import { BsPlus } from 'react-icons/bs'
import { AiFillPushpin } from 'react-icons/ai'

import * as api from 'api'
import * as hooks from 'hooks'

const ChannelNav = () => {
    const { projectId } = useParams()
    const navigate = useNavigate()
    const { project } = hooks.projectState()
    const { memberId } = hooks.meState()
    const { setProjectMembers } = hooks.projectMembersState()
    const { directChannels } = hooks.directChannelsState()
    const { textChannels, setTextChannels } = hooks.textChannelsState()
    const { videoChannels, setVideoChannels } = hooks.videoChannelsState()
    const [openText, setOpenText] = useState(true)
    const [openDm, setOpenDm] = useState(true)
    const [openVideo, setOpenVideo] = useState(true)
    const [createTextChannel, setCreateTextChannel] = useState(false)
    const [createVideoChannel, setCreateVideoChannel] = useState(false)
    const [channelTitle, setChannelTitle] = useState('')

    useEffect(() => {
        const initProjectMembers = async () => {
            const projectMembersRes = await api.apis.getProjectMembers(projectId)
            setProjectMembers(projectMembersRes.data)
            console.log(projectMembersRes.data)
        }

        initProjectMembers()
    }, [])

    let createTextChannelRef = useRef(null)
    let createVideoChannelRef = useRef(null)

    const clickCreateTextChannel = () => {
        setCreateTextChannel(true)
    }

    const clickCreateVideoChannel = () => {
        setCreateVideoChannel(true)
    }

    useEffect(() => {
        const handleOutside = e => {
            if (createTextChannelRef.current && !createTextChannelRef.current.contains(e.target)) {
                setChannelTitle('')
                setCreateTextChannel(false)
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [createTextChannelRef])

    useEffect(() => {
        const handleOutside = e => {
            if (createVideoChannelRef.current && !createVideoChannelRef.current.contains(e.target)) {
                setChannelTitle('')
                setCreateVideoChannel(false)
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [createVideoChannelRef])

    const handleTitleChange = e => setChannelTitle(e.target.value)

    const textTitleKeyDown = async e => {
        if (e.key === 'Enter') {
            api.apis.createTextChannel(projectId, { name: channelTitle }).then(response => {
                console.log(response.data)
                setTextChannels([...textChannels, response.data])
            })
            setCreateTextChannel(false)
            setChannelTitle('')
        }
    }

    const videoTitleKeyDown = async e => {
        if (e.key === 'Enter') {
            api.apis.createVideoChannel(projectId, { name: channelTitle }).then(response => {
                console.log(response.data)
                setVideoChannels([...videoChannels, response.data])
            })
            setCreateVideoChannel(false)
            setChannelTitle('')
        }
    }

    useEffect(() => {
        if (createTextChannel) {
            createTextChannelRef.current.focus()
        }
    }, [createTextChannel])
    useEffect(() => {
        if (createVideoChannel) {
            createVideoChannelRef.current.focus()
        }
    }, [createVideoChannel])

    return (
        <S.Wrap>
            <S.Container>
                <S.TextChannelList className={openText ? 'opened' : ''}>
                    <S.ChannelHead onClick={() => setOpenText(!openText)}>
                        텍스트 채널 &nbsp; <IoIosArrowUp />
                    </S.ChannelHead>
                    {textChannels.map((textChannel, index) => (
                        <S.Channel
                            key={textChannel.id}
                            onClick={() => navigate('/project/' + project.id + '/channel/text/' + textChannel.id)}>
                            {textChannel.name}
                            {textChannel.isPinned && <AiFillPushpin />}
                        </S.Channel>
                    ))}
                    {createTextChannel && (
                        <S.AddChannel
                            ref={createTextChannelRef}
                            value={channelTitle}
                            onChange={handleTitleChange}
                            onKeyDown={textTitleKeyDown}
                            placeholder="채널 이름을 입력하세요"></S.AddChannel>
                    )}
                    <S.AddChannelButton onClick={clickCreateTextChannel}>
                        <BsPlus /> 채널 추가
                    </S.AddChannelButton>
                </S.TextChannelList>
                <S.DmList className={openDm ? 'opened' : ''}>
                    <S.ChannelHead onClick={() => setOpenDm(!openDm)}>
                        Direct Message &nbsp; <IoIosArrowUp />
                    </S.ChannelHead>
                    {directChannels &&
                        directChannels.map(directChannel => (
                            <S.DmMember
                                key={directChannel.id}
                                onClick={() =>
                                    navigate('/project/' + project.id + '/channel/direct/' + directChannel.id)
                                }>
                                <S.DmProfileImage>
                                    <img
                                        src={
                                            directChannel.member1Id === memberId
                                                ? directChannel.member2ImageUrl
                                                    ? directChannel.member2ImageUrl
                                                    : require('images/profile.png').default
                                                : directChannel.member1ImageUrl
                                                ? directChannel.member1ImageUrl
                                                : require('images/profile.png').default
                                        }
                                        alt="프로필"
                                    />
                                </S.DmProfileImage>
                                {directChannel.member1Id === memberId
                                    ? directChannel.member2Name
                                    : directChannel.member1Name}
                            </S.DmMember>
                        ))}
                </S.DmList>
                <S.VideoChannelList className={openVideo ? 'opened' : ''}>
                    <S.ChannelHead onClick={() => setOpenVideo(!openVideo)}>
                        화상 채널 &nbsp; <IoIosArrowUp />
                    </S.ChannelHead>
                    {/* <S.Channel onClick={() => navigate('/project/' + project.id + '/channel/video/' + '1')}>
                        1. 회의
                    </S.Channel>
                    <S.Channel>2. 자유</S.Channel> */}
                    {videoChannels.map((videoChannel, index) => (
                        <S.Channel
                            key={videoChannel.id}
                            onClick={() => navigate('/project/' + project.id + '/channel/video/' + videoChannel.id)}>
                            {videoChannel.name}
                            {/* {videoChannel.isPinned && <AiFillPushpin />} */}
                        </S.Channel>
                    ))}
                    {createVideoChannel && (
                        <S.AddChannel
                            ref={createVideoChannelRef}
                            value={channelTitle}
                            onChange={handleTitleChange}
                            onKeyDown={videoTitleKeyDown}
                            placeholder="채널 이름을 입력하세요"></S.AddChannel>
                    )}
                    <S.AddChannelButton onClick={clickCreateVideoChannel}>
                        <BsPlus /> 채널 추가
                    </S.AddChannelButton>
                </S.VideoChannelList>
            </S.Container>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        width: 216px;
        max-height: calc(100vh - 344px);
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px 0;
        overflow-y: auto;

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
    Container: styled.div`
        width: 100%;
        height: 100%;
    `,
    Button: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 50px;
        transition-duration: 0.2s;
        cursor: pointer;

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
    TextChannelList: styled.ul`
        border-bottom: 1px solid ${({ theme }) => theme.color.lightgray};
    `,
    ChannelHead: styled.li`
        font-size: ${({ theme }) => theme.fontsize.title3};
        display: flex;
        align-items: center;
        padding: 16px 16px 10px;
        cursor: pointer;

        :not(.opened) & svg {
            transition-duration: 0.2s;
            transform: rotateZ(180deg);
        }

        .opened & svg {
            transition-duration: 0.2s;
            transform: rotateZ(360deg);
        }
    `,
    PinButton: styled.div`
        color: ${({ theme }) => theme.color.main};

        & svg {
            width: 16px;
            height: 16px;
        }
    `,
    Channel: styled.li`
        font-size: ${({ theme }) => theme.fontsize.sub1};
        padding: 10px 20px;
        cursor: pointer;
        transition-duration: 0.2s;
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: relative;

        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};

            & > div {
                & svg {
                    color: ${({ theme }) => theme.color.white};
                }
            }
        }

        &:last-child {
            border-radius: 0 0 8px 8px;
        }

        :not(.opened) & {
            display: none;
        }

        .opened & {
            display: flex;
        }
    `,
    AddChannel: styled.input`
        display: flex;
        border: none;
        outline: none;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        padding: 10px 20px;
        cursor: pointer;
    `,
    AddChannelButton: styled.div`
        display: flex;
        align-items: center;
        justify-content: flex-start;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        padding: 12px 16px;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            transform: translateY(-2px);
            color: ${({ theme }) => theme.color.main};
        }

        & svg {
            width: 20px;
            height: 20px;
            margin: 0 0 0 -4px;
            color: ${({ theme }) => theme.color.main};
        }
    `,
    DmList: styled.ul`
        border-bottom: 1px solid ${({ theme }) => theme.color.lightgray};
    `,
    DmMember: styled.li`
        display: flex;
        align-items: center;
        padding: 6px 20px;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        cursor: pointer;

        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
        }

        :not(.opened) & {
            display: none;
        }

        .opened & {
            display: flex;
        }
    `,
    DmProfileImage: styled.div`
        display: flex;
        align-items: center;
        margin: 0 10px 0 0;

        & img {
            border-radius: 4px;
            width: 20px;
        }
    `,
    VideoChannelList: styled.ul``,
}

export default ChannelNav
