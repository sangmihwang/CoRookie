import React, { useRef, useState, useEffect } from 'react'
import styled from 'styled-components'

import { IoIosArrowForward, IoIosArrowDown, IoIosArrowUp } from 'react-icons/io'
import * as components from 'components'
import * as hooks from 'hooks'
import * as utils from 'utils'
import * as api from 'api'

const DMThread = ({ projectId, channelId, message }) => {
    const text = useRef(null)
    const [overText, setOverText] = useState(false)
    const [closedText, setClosedText] = useState(false)
    const [addImoticon, setAddImoticon] = useState(false)
    const { closeProfile } = hooks.profileState()
    const { dmcommentOpened, openDmComment, closeDmComment } = hooks.dmcommentState()

    const [thumbCnt, setThumbCnt] = useState(0)
    const [happyCnt, setHappyCnt] = useState(0)
    const [sadCnt, setSadCnt] = useState(0)

    const updateCounts = {
        [utils.IMOTICON_OPTIONS.thumb]: () => setThumbCnt(thumbCnt + 1),
        [utils.IMOTICON_OPTIONS.happy]: () => setHappyCnt(happyCnt + 1),
        [utils.IMOTICON_OPTIONS.sad]: () => setSadCnt(sadCnt + 1),
    }

    const msg = '안녕하세요'

    // const msg =
    //     '```java\npackage boj;\nimport java.io.BufferedReader;\nimport java.io.ioException;\n"문자열"\nimport java.io.InputStreamReader;\npublic class Problem2847\n\tpublic static void main(String[] args)throws Exception, IOException(\n\t\tBufferedReader br = new BufferedReader(new InputStreamReader(System.in));\n\t\tint N = Integer.parseInt(br.readLine());\n\t\tint[] score = new int[N];\n\t\tfor (int i=0; i<N; i++) {\n\t\t\tscore[i]=Integer.parseInt(br.readLine());\n\t\t}\n\t\t int prev=score[N-1];\n\t\t int cnt=0;\n\t\t for (int i=N-2; i>=0; i--) {\n\t\t\t if (score[i]>=prev) {\n\t\t\t	cnt+=score[i]-prev+1;\n\t\t\t	score[i]=prev-1;\n\t\t}\n\t\t prev=score[i];\n\t\t }\n\t\t System.out.println(cnt);\n\t }\n}\n```'

    const regex = /```(\w*)\n([\s\S]*?)\n```/
    const matches = msg.match(regex)

    let isCode = false
    let language = null
    let code = null

    if (matches && matches.length > 2) {
        isCode = true
        language = matches[1]
        code = matches[2]
    } else {
        code = msg
    }

    useEffect(() => {
        if (text.current.scrollHeight > 140) {
            setOverText(true)
            setClosedText(true)
        }
    }, [])

    const openMoreText = () => {
        if (text) {
            text.current.style.maxHeight = 'none'
            setClosedText(false)
        }
    }

    const hideText = () => {
        text.current.style.maxHeight = '140px'
        setClosedText(true)
    }

    const toggleDmComment = () => {
        if (dmcommentOpened) {
            closeDmComment()
        } else {
            openDmComment()
            closeProfile()
        }
    }

    const childImoticonData = [
        { angle: -60, dist: 40, type: utils.IMOTICON_OPTIONS.thumb },
        { angle: 0, dist: 40, type: utils.IMOTICON_OPTIONS.happy },
        { angle: 60, dist: 40, type: utils.IMOTICON_OPTIONS.sad },
    ]

    const calculatePosition = (angle, dist) => {
        const radian = angle * (Math.PI / 180)
        const x = dist * Math.cos(radian)
        const y = dist * Math.sin(radian)
        return { x, y }
    }

    let imoticonRef = useRef(null)

    useEffect(() => {
        const handleOutside = e => {
            if (imoticonRef.current && !imoticonRef.current.contains(e.target)) {
                setAddImoticon(false)
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [imoticonRef])

    return (
        <S.Wrap>
            <S.ChatBox>
                <S.ImageBox>
                    <img
                        src={
                            message && message.writer.imageUrl
                                ? message.writer.imageUrl
                                : require('images/profile.png').default
                        }
                        alt="스레드 이미지"
                    />
                </S.ImageBox>
                <S.ContentBox>
                    <S.MemberInfoBox>
                        <S.MemberName>{message && message.writer.name}</S.MemberName>
                        <S.CreatedTime>{message && utils.calDate(message.createdAt)}</S.CreatedTime>
                        {/* <S.CommentButton onClick={() => toggleDmComment()} open={dmcommentOpened}> */}
                        {/* <div>
                                <img src={require('images/profile.png').default} alt="프로필" />
                                <img src={require('images/profile.png').default} alt="프로필" />
                                <img src={require('images/profile.png').default} alt="프로필" />
                            </div> */}
                        {/* 0개의 댓글 <IoIosArrowForward />
                        </S.CommentButton> */}
                    </S.MemberInfoBox>
                    <S.Text ref={text}>
                        <components.Message isCode={isCode} text={code} language={language} thread={message} />
                    </S.Text>
                    {closedText && (
                        <S.MoreButton>
                            <div onClick={() => openMoreText()}>
                                더보기 <IoIosArrowDown />
                            </div>
                        </S.MoreButton>
                    )}
                    {overText && !closedText && (
                        <S.MoreButton>
                            <div onClick={() => hideText()}>
                                감추기 <IoIosArrowUp />
                            </div>
                        </S.MoreButton>
                    )}
                </S.ContentBox>
            </S.ChatBox>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        border: 2px solid
            ${({ open, theme }) => {
                return open ? theme.color.main : theme.color.white
            }};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 24px;

        &:first-child {
            margin-top: 0;
        }

        &:last-child {
            margin-bottom: 0;
        }
    `,
    ChatBox: styled.div`
        display: flex;
        width: 100%;
        height: 100%;
    `,
    ImageBox: styled.div`
        width: 40px;
        margin: 0 16px 0 0;

        & img {
            width: 40px;
            height: 40px;
            border-radius: 8px;
        }
    `,
    ContentBox: styled.div`
        width: 100%;
    `,
    MemberInfoBox: styled.div`
        display: flex;
        align-items: flex-end;
        margin: 0 0 16px 0;
    `,
    MemberName: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title3};
        margin: 0 8px 0 0;
    `,
    CreatedTime: styled.div`
        font-size: 13px;
        color: ${({ theme }) => theme.color.gray};
    `,
    CommentButton: styled.div`
        display: flex;
        align-items: center;
        margin: 0 0 0 auto;
        font-size: 13px;
        color: ${({ theme }) => theme.color.gray};
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.main};

            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 20px;
            height: 20px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 24px;
            height: 24px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
    Text: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        line-height: 24px;
        max-height: 140px;
        overflow-y: hidden;
    `,
    MoreButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 40px;
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.gray};

        & > div {
            cursor: pointer;
        }

        & > div:hover {
            color: ${({ theme }) => theme.color.main};
        }
    `,
    HideButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 40px;
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.main};

        & > div {
            cursor: pointer;
        }
    `,
}

export default DMThread
