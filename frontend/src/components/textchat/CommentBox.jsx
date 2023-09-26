import React, { useState, useEffect, useRef, useCallback } from 'react'
import styled from 'styled-components'

import { IoClose } from 'react-icons/io5'

import * as hooks from 'hooks'
import * as utils from 'utils'
import * as components from 'components'
import * as style from 'style'
import * as api from 'api'

import * as StompJs from '@stomp/stompjs'

const CommentBox = ({ projectId, channelId }) => {
    const { closeComment } = hooks.commentState()
    const { page, size, sort, direction, upPage, initPage } = hooks.commentsState()
    const { threadId, setThreadId, commentCount, setCommentCount } = hooks.selectedThreadState()
    const [comments, setComments] = useState([])
    const [currentComment, setCurrentComment] = useState({
        threadId: threadId,
        writerId: null,
        content: '',
    })
    const [prevScrollHeight, setPrevScrollHeight] = useState(null)
    const scrollRef = useRef(null)
    const client = useRef({})
    const target = useRef(null)
    const preventRef = useRef(true) //옵저버 중복 실행 방지
    const endRef = useRef(false) //모든 글 로드 확인

    const connectComment = () => {
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
                client.current.subscribe('/topic/comment/' + threadId, message => {
                    const jsonBody = JSON.parse(message.body)
                    setComments(comments => [jsonBody, ...comments])
                })
                client.current.publish({
                    destination: '/app/comment/',
                    body: JSON.stringify(comments),
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

        if (currentComment.content.replace(/\s+/gi, '') === '') {
            return
        }

        client.current.publish({
            destination: '/app/comment',
            body: JSON.stringify(currentComment),
        })

        setCurrentComment({
            ...currentComment,
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
        const initComment = async () => {
            const memberRes = await api.apis.getMe()
            setCurrentComment({
                ...currentComment,
                threadId: threadId,
                writerId: memberRes.data.id,
            })
        }
        initComment()
        connectComment()

        return () => {
            observer.disconnect()
            disconnect()
        }
    }, [threadId])

    const getComments = useCallback(async () => {
        api.apis.getComments(projectId, channelId, threadId, page, size, sort, direction).then(response => {
            if (page === 0) {
                setComments(response.data)
                return
            }
            setComments(comments => [...comments, ...response.data])
        })
        setPrevScrollHeight(scrollRef.current?.scrollHeight)
        preventRef.current = true
    }, [threadId, page])

    useEffect(() => {
        getComments()
    }, [page])

    useEffect(() => {
        const getThreadDetails = async () => {
            const threadRes = await api.apis.getThread(projectId, channelId, threadId)
            setCommentCount(threadRes.data.commentCount)
        }
        if (prevScrollHeight) {
            scrollRef.current.scrollTop = scrollRef.current?.scrollHeight - prevScrollHeight
            return setPrevScrollHeight(null)
        }
        scrollRef.current.scrollTop = scrollRef.current?.scrollHeight - scrollRef.current?.clientHeight
        getThreadDetails()
    }, [comments])

    return (
        <S.Wrap>
            <S.CloseHeader>
                <S.CloseButton
                    onClick={() => {
                        closeComment()
                        setThreadId(null)
                    }}>
                    <IoClose />
                </S.CloseButton>
            </S.CloseHeader>
            <S.Header>{commentCount}개의 댓글</S.Header>
            <S.CommentSection ref={scrollRef}>
                <div ref={target} />
                {comments &&
                    [...comments].reverse().map(comment => <components.Comment key={comment.id} comment={comment} />)}
            </S.CommentSection>
            <components.CommentEditBox
                currentComment={currentComment}
                setCurrentComment={setCurrentComment}
                send={send}
            />
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 360px;
        min-width: 360px;
        border-radius: 8px;
        flex-direction: column;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 0 16px 16px;
        /* animation: ${style.leftSlide} 0.4s linear; */
    `,
    CloseHeader: styled.div`
        display: flex;
        justify-content: flex-end;
        align-items: center;
        margin: 8px 8px 0 0;
    `,
    CloseButton: styled.div`
        color: ${({ theme }) => theme.color.gray};
        cursor: pointer;
        &:hover {
            color: ${({ theme }) => theme.color.main};
        }
        & svg {
            width: 20px;
            height: 20px;
        }
    `,
    Header: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        white-space: nowrap;
        width: 100%;
        height: fit-content;
        margin: 0 0 8px 0;
        padding: 0 26px;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        color: ${({ theme }) => theme.color.gray};

        &::before {
            content: '';
            margin: 0 8px 0 0;
            background-color: ${({ theme }) => theme.color.lightgray};
            height: 1px;
            width: 100%;
        }

        &::after {
            content: '';
            margin: 0 0 0 8px;
            background-color: ${({ theme }) => theme.color.lightgray};
            height: 1px;
            width: 100%;
        }
    `,
    CommentSection: styled.div`
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

export default CommentBox
