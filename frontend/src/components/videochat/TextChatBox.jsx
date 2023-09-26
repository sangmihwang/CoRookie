import React from 'react'
import styled from 'styled-components'

import { IoClose } from 'react-icons/io5'

import * as hooks from 'hooks'
import * as components from 'components'
import * as style from 'style'

const TextChatBox = () => {
    const { closeChatbox } = hooks.chatBoxState()
    return (
        <S.Wrap>
            <S.CloseHeader>
                <S.CloseButton onClick={() => closeChatbox()}>
                    <IoClose />
                </S.CloseButton>
            </S.CloseHeader>
            <S.Header>텍스트 채팅 가능</S.Header>
            <S.CommentSection>
                <components.Comment />
            </S.CommentSection>
            <components.CommentEditBox />
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 400px;
        min-width: 400px;
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

export default TextChatBox
