import React, { useState, useEffect, useRef } from 'react'
import styled from 'styled-components'

import { IoMdSend } from 'react-icons/io'

const DMCommentEditBox = () => {
    const [chat, setChat] = useState('')
    const text = useRef(null)

    useEffect(() => {
        if (text) {
            text.current.style.height = 'auto'
            let height = text.current.scrollHeight
            text.current.style.height = `${height + 20}px`
        }
    }, [chat])

    return (
        <S.Wrap>
            <S.Edit ref={text} onChange={e => setChat(e.target.value)} rows={1}></S.Edit>
            <S.SendButton>
                <IoMdSend />
            </S.SendButton>
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
        border: solid;
        border-color: ${({ theme }) => theme.color.gray};
        border-width: 1px;
    `,
    Edit: styled.textarea`
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
        margin: auto;
        transition-duration: 0.2s;
        cursor: pointer;

        & svg {
            width: 20px;
            height: 20px;
        }
    `,
}

export default DMCommentEditBox
