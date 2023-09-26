import React from 'react'
import styled from 'styled-components'

const LandingIssueCal = () => {
    return (
        <S.Wrap>
            <S.Container>
                <S.Issue>
                    <S.Title>Issue Management</S.Title>
                    <S.SubTitle1>직관적인 이슈 관리</S.SubTitle1>
                    <S.SubTitle2>
                        프로젝트의 이슈들을 팀원들과 공동으로 관리하고 <nav></nav>한 눈에 확인할 수 있습니다.
                    </S.SubTitle2>
                    <S.ImageContent>
                        <img src={require('images/issue_sample1.png').default} alt="채팅1" />
                        <img src={require('images/issue_sample2.png').default} alt="채팅2" />
                    </S.ImageContent>
                </S.Issue>
                <S.Schedule>
                    <S.Title>Schedule Management</S.Title>
                    <S.SubTitle1>일정을 공유할 수 있는 캘린더</S.SubTitle1>
                    <S.SubTitle2>
                        팀원들과 프로젝트에 관련된 일정들을 공유하고 <nav></nav>
                        참여자를 확인할 수 있습니다.
                    </S.SubTitle2>
                    <S.ImageContent>
                        <img src={require('images/schedule_sample1.png').default} alt="채팅1" />
                        <img src={require('images/schedule_sample2.png').default} alt="채팅2" />
                    </S.ImageContent>
                </S.Schedule>
            </S.Container>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        width: 100%;
    `,

    Title: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 40px 8px 16px 8px;
        padding: 8px;
        text-align: left;
        font-weight: 700;
        font-size: ${({ theme }) => theme.fontsize.title1};
    `,
    Container: styled.div`
        display: flex;
        flex-direction: row;
        justify-content: space-evenly;
    `,
    Issue: styled.div`
        margin: 16px;
    `,
    Schedule: styled.div`
        margin: 16px;
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
        width: 450px;
        height: auto;
        margin: 8px 0 8px 8px;

        & img {
            width: 100%;
            height: 100%;
            margin: 12px;
            box-shadow: 0px 15px 15px 0px rgba(0, 0, 0, 0.1);
        }
    `,
}

export default LandingIssueCal
