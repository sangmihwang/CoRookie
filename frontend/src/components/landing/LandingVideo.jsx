import React from 'react'
import styled from 'styled-components'

const LandingVideo = () => {
    return (
        <S.Wrap>
            <S.Title>Video Channel</S.Title>
            <S.SubTitle1>언제 어디서나 대면 소통할 수 있는 화상 채널</S.SubTitle1>
            <S.SubTitle2>
                WebRTC를 사용하여 팀원들과의 생생한 비대면 회의를 할 수 있습니다.<nav></nav>
                회의 내용을 놓치는 일이 없도록 획기적인 자동 회의록 기능도 제공합니다.
            </S.SubTitle2>
            <S.ImageContent>
                <img src={require('images/video_sample1.png').default} alt="채팅1" />
                <img src={require('images/video_sample1.png').default} alt="채팅1" />
                {/* <img src={require('images/video_sample2.PNG').default} alt="채팅2" /> */}
            </S.ImageContent>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        width: 100%;
        padding: 4px;
    `,

    Title: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 40px 16px 16px 8px;
        padding: 8px;
        text-align: right;
        font-weight: 700;
        font-size: ${({ theme }) => theme.fontsize.title1};
    `,
    SubTitle1: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.main};
        margin: 0 8px;
        padding: 0 16px;
        text-align: right;
        font-size: ${({ theme }) => theme.fontsize.title2};
        line-height: 1.5;
    `,
    SubTitle2: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 0 8px;
        padding: 0 16px;
        text-align: right;
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

export default LandingVideo
