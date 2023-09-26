import React, { useState, useEffect, useRef, useCallback } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'

import * as components from 'components'
import * as hooks from 'hooks'
import * as utils from 'utils'
import * as api from 'api'

import * as StompJs from '@stomp/stompjs'

const DirectMessage = () => {
    const { projectId, channelId } = useParams()
    const { closeProfile } = hooks.profileState()
    const { closeComment } = hooks.commentState()
    const { closeChatbox } = hooks.chatBoxState()
    const { closeIssueDetail } = hooks.issueDetailState()
    const { closeDmComment } = hooks.dmcommentState()
    const { memberId } = hooks.meState()
    const [directChannel, setDirectChannel] = useState(null)
    const [messages, setMessages] = useState([])
    const { page, upPage, initPage, size, sort, direction } = hooks.directMessagesState()
    const [currentChat, setCurrentChat] = useState({
        content: '',
        directChannelId: null,
        writerId: null,
    })
    const [prevScrollHeight, setPrevScrollHeight] = useState()
    const target = useRef(null)
    const preventRef = useRef(true) //옵저버 중복 실행 방지
    const endRef = useRef(false) //모든 글 로드 확인
    const scrollRef = useRef(null)
    const client = useRef({})

    const connectThread = () => {
        client.current = new StompJs.Client({
            brokerURL: utils.WEBSOCKET_BASE_URL + '/ws/chat',
            connectHeaders: {
                Authorization: hooks.getCookie('Authorization'),
            },
            debug: function (message) {
                console.log(message)
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => {
                console.log('Connected')
                client.current.subscribe('/topic/message/' + channelId, message => {
                    const jsonBody = JSON.parse(message.body)
                    setMessages(messages => [jsonBody, ...messages])
                })
                client.current.publish({
                    destination: '/app/message',
                    body: JSON.stringify(messages),
                })
            },
            onStompError: frame => {
                console.error(frame)
            },
        })

        client.current.activate()
    }

    const disconnect = () => {
        client.current.deactivate()
    }

    const send = () => {
        if (!client.current.connected) {
            return
        }

        if (currentChat.content.replace(/\s+/gi, '') === '') {
            return
        }

        client.current.publish({
            destination: '/app/message',
            body: JSON.stringify(currentChat),
        })

        setCurrentChat({
            ...currentChat,
            content: '',
        })
        if (scrollRef.current) {
            scrollRef.current.scrollTop = scrollRef.current.scrollHeight
        }
    }

    const obsHandler = entries => {
        //옵저버 콜백함수
        const entry = entries[0]
        if (!endRef.current && entry.isIntersecting && preventRef.current) {
            //옵저버 중복 실행 방지
            preventRef.current = false //옵저버 중복 실행 방지
            upPage()
        }
    }

    useEffect(() => {
        initPage()
        const observer = new IntersectionObserver(obsHandler, { threshold: 0.5 })
        if (target.current) {
            observer.observe(target.current)
        }
        const initChannel = async () => {
            const memberRes = await api.apis.getMe()
            const directChannelRes = await api.apis.getDirectChannel(projectId, channelId)
            setDirectChannel(directChannelRes.data)

            console.log(directChannelRes.data)
            setCurrentChat({
                ...currentChat,
                directChannelId: directChannelRes.data.id,
                writerId: memberRes.data.id,
            })
        }
        closeComment()
        closeIssueDetail()
        closeChatbox()
        closeDmComment()
        closeProfile()
        initChannel()
        connectThread()

        return () => {
            observer.disconnect()
            disconnect()
        }
    }, [projectId, channelId])

    const getMessages = useCallback(async () => {
        api.apis.getDirectMessages(projectId, channelId, page, size, sort, direction).then(response => {
            if (page === 0) {
                setMessages(response.data)
                return
            }
            setMessages(messages => [...messages, ...response.data])
        })
        setPrevScrollHeight(scrollRef.current?.scrollHeight)
        preventRef.current = true
    }, [channelId, page])

    useEffect(() => {
        getMessages()
    }, [page])

    useEffect(() => {
        console.log(messages)
        if (prevScrollHeight) {
            scrollRef.current.scrollTop = scrollRef.current?.scrollHeight - prevScrollHeight
            return setPrevScrollHeight(null)
        }
        scrollRef.current.scrollTop = scrollRef.current?.scrollHeight - scrollRef.current?.clientHeight
    }, [messages])

    return (
        <S.Wrap>
            <S.Header>
                <S.Title>
                    {directChannel &&
                        (directChannel.member1Id === memberId ? directChannel.member2Name : directChannel.member1Name)}
                </S.Title>
            </S.Header>
            <S.Container>
                <S.ChatBox>
                    <S.ThreadBox ref={scrollRef}>
                        <div ref={target}>
                            {messages &&
                                [...messages]
                                    .reverse()
                                    .map(message => (
                                        <components.DMThread
                                            key={message.id}
                                            projectId={projectId}
                                            channelId={channelId}
                                            message={message}
                                        />
                                    ))}
                        </div>
                        {/* <components.DMThread />
                        <components.DMThread /> */}
                    </S.ThreadBox>
                    <components.DMEditBox currentChat={currentChat} setCurrentChat={setCurrentChat} send={send} />
                </S.ChatBox>
                {/* {dmcommentOpened && <components.DMCommentBox />} */}
            </S.Container>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
    `,
    Header: styled.div`
        display: flex;
        align-items: center;
        height: 64px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 0 26px;
    `,
    Title: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title2};
    `,
    Container: styled.div`
        display: flex;
        height: 100%;
        max-height: calc(100vh - 152px);
    `,
    ChatBox: styled.div`
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
        transition: width 0.2s;
    `,
    ThreadBox: styled.div`
        width: 100%;
        flex-grow: 1;
        /* max-height: calc(100vh - 300px); */
        overflow-y: auto;
        padding: 0 0 16px;
        margin: 0 0 auto 0;

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
}

export default DirectMessage
