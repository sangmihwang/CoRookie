import React, { useState, useEffect, useRef } from 'react'
import styled from 'styled-components'

import { IoMdSend } from 'react-icons/io'
import { AiOutlinePaperClip } from 'react-icons/ai'

const EditBox = ({ currentChat, setCurrentChat, send }) => {
    const text = useRef(null)

    const inputChat = e => {
        if (e.key === 'Enter') {
            if (e.shiftKey) {
                return
            }

            if (currentChat.content.replace(/\s+/gi, '') === '') {
                e.preventDefault()
                return
            }

            send()
            e.preventDefault()
            return
        }
    }

    useEffect(() => {
        if (text) {
            text.current.style.height = 'auto'
            let height = text.current.scrollHeight
            text.current.style.height = `${height + 20}px`
        }
    }, [currentChat])

    return (
        <S.Wrap>
            <S.Edit
                ref={text}
                onChange={e => setCurrentChat({ ...currentChat, content: e.target.value })}
                onKeyPress={e => inputChat(e)}
                value={currentChat.content}
                rows={1}></S.Edit>
            <S.SendButton onClick={() => send()}>
                <IoMdSend />
            </S.SendButton>
            <S.FileButton>
                <AiOutlinePaperClip />
            </S.FileButton>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        align-items: flex-end;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 23px 26px;
    `,
    Edit: styled.textarea`
        display: flex;
        justify-content: center;
        align-items: center;
        flex-grow: 1;
        bottom: 16px;
        height: 30px;
        max-height: 300px;
        font-size: ${({ theme }) => theme.fontsize.title3};
        font-family: 'Noto Sans KR', 'Pretendard';
        outline: none;
        resize: none;
        border: none;
        margin: 0 16px 0 0;
    `,
    SendButton: styled.div`
        color: ${({ theme }) => theme.color.main};
        margin: auto 4px;
        transition-duration: 0.2s;
        cursor: pointer;

        & svg {
            width: 20px;
            height: 20px;
        }
    `,
    FileButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        margin: auto 4px;
        width: 30px;
        height: 30px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.lightgray};
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
        }

        & svg {
            width: 20px;
            height: 20px;
        }
    `,
}

export default EditBox
