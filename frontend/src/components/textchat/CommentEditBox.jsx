import React, { useEffect, useRef } from 'react'
import styled from 'styled-components'

import { IoMdSend } from 'react-icons/io'

const CommentEditBox = ({ currentComment, setCurrentComment, send }) => {
    const text = useRef(null)

    const inputComment = e => {
        if (e.key === 'Enter') {
            if (e.shiftKey) {
                return
            }

            if (currentComment.content.replace(/\s+/gi, '') === '') {
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
    }, [currentComment])

    return (
        <S.Wrap>
            <S.Edit
                ref={text}
                onKeyPress={e => inputComment(e)}
                onChange={e => setCurrentComment({ ...currentComment, content: e.target.value })}
                value={currentComment.content}
                rows={1}></S.Edit>
            <S.SendButton onClick={() => send()}>
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
        margin: 0 0 4px 0;
        transition-duration: 0.2s;
        cursor: pointer;

        & svg {
            width: 20px;
            height: 20px;
        }
    `,
}

export default CommentEditBox
