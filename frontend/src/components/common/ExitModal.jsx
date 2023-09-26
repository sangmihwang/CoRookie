import React from 'react'
import styled from 'styled-components'

import * as utils from 'utils'
import * as hooks from 'hooks'

const ExitModal = () => {
    return (
        <S.Wrap>
            <S.Container>
                <S.Title>강퇴</S.Title>
                <S.Message>강퇴하시겠습니까?</S.Message>
                <S.Button>확인</S.Button>
            </S.Container>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        position: absolute;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        min-width: 100%;
        height: 100%;
        background-color: #0004;
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 0 26px;
        z-index: 9999;
        /* animation: ${style.leftSlide} 0.4s linear; */
    `,
    Container: styled.div`
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        width: 480px;
        height: 320px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        padding: 32px;
        opacity: 0;
        transform: translateY(1000px);
    `,
    Title: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title1};
        font-weight: bold;
        color: ${({ theme }) => theme.color.warning};
    `,

    Message: styled.div`
        margin: 100px 0 0 0;
    `,
    Button: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: 48px;
        height: 32px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        font-size: ${({ theme }) => theme.fontsize.sub1};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: auto 0 0 0;
        cursor: pointer;
    `,
}

export default ExitModal
