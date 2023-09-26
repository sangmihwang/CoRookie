import React, { useState, useEffect } from 'react'

import styled from 'styled-components'

const LandingText = () => {
    return (
        <S.Wrap>
            <S.Title>Text Channel</S.Title>
            <S.SubTitle1>기발한 아이디어를 공유하고 효과적인 논의를 위한 스레드 채팅</S.SubTitle1>
            <S.SubTitle2>
                스레드를 열어 관련된 내용에 대해 논의할 수 있는 구조적인 채팅은 협업에 특화되어 있습니다.
            </S.SubTitle2>
            <S.ImageContent>
                <img src={require('images/text_sample.png').default} alt="채팅1" />
                <img src={require('images/text_sample2.png').default} alt="채팅2" />
            </S.ImageContent>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div``,

    Title: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 40px 8px 16px 8px;
        padding: 8px;
        text-align: left;
        font-weight: 700;
        font-size: ${({ theme }) => theme.fontsize.title1};
    `,
    SubTitle1: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.main};
        margin: 0 8px;
        padding: 0 8px;
        text-align: left;
        font-size: ${({ theme }) => theme.fontsize.title2};
        line-height: 1.5;
    `,
    SubTitle2: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 0 8px 20px 8px;
        padding: 0 8px;
        text-align: left;
        font-size: ${({ theme }) => theme.fontsize.title2};
        line-height: 1.5;
    `,
    ImageContent: styled.div`
        display: flex;
        align-items: center;
        justify-content: center;
        height: auto;
        margin: 8px 0 8px 8px;

        & img {
            width: 48%;
            height: 100%;
            margin: 12px;
            box-shadow: 0px 15px 15px 0px rgba(0, 0, 0, 0.1);
        }
    `,
}

export default LandingText
