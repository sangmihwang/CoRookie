import React from 'react'
import styled from 'styled-components'

import * as utils from 'utils'

const Login = () => {
    const loginButton = [
        {
            type: 'Google',
            name: 'Google',
            img: require('images/google_logo.png').default,
            url: utils.API_BASE_URL + '/oauth2/authorization/google',
        },
        {
            type: 'Github',
            name: 'GitHub',
            img: require('images/github_logo.png').default,
            url: utils.API_BASE_URL + '/oauth2/authorization/github',
        },
        {
            type: 'Kakao',
            name: '카카오',
            img: require('images/kakao_logo.svg').default,
            url: utils.API_BASE_URL + '/oauth2/authorization/kakao',
        },
    ]
    return (
        <S.Wrap>
            <S.Container>
                <S.Logo>
                    <img src={require('images/logo.png').default} alt={'로고'} />
                </S.Logo>
                {loginButton.map((item, idx) => {
                    return (
                        <S.SocialLogin href={item.url} key={idx} type={item.type}>
                            <img src={item.img} alt={item.type} />
                            <S.Text>{item.name}로 로그인하기</S.Text>
                        </S.SocialLogin>
                    )
                })}
            </S.Container>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 100%;
        height: 100%;
        /* background-color: ${({ theme }) => theme.color.main}; */
        background-image: url(${require('images/main_background.png').default});
        background-size: 100% 1000px;
        background-position-y: center;
        justify-content: center;
        align-items: center;
    `,
    Container: styled.div`
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 540px;
        height: 587px;
        /* background-color: ${({ theme }) => theme.color.white}; */
        border-radius: 8px;
    `,
    Logo: styled.div`
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 24px;
        font-size: 24px;
        color: ${({ theme }) => theme.color.main};
        margin: 88px 0 100px;
    `,
    SocialLogin: styled.a`
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 10px 0;
        border-radius: 8px;
        cursor: pointer;
        padding: 10px 46px;
        width: 300px;
        height: 60px;
        box-shadow: 0px 1px 3px 0px rgba(0, 0, 0, 0.2);
        background-color: ${props => {
            switch (props.type) {
                case 'Kakao':
                    return '#FEE500'
                default:
                    return props.theme.color.white
            }
        }};

        &:hover {
            background-color: ${props => {
                switch (props.type) {
                    case 'Kakao':
                        return '#f0d803'
                    default:
                        return props.theme.color.lightgray
                }
            }};
        }
        & img {
            width: 24px;
            height: 24px;
        }
    `,
    Text: styled.div`
        padding: 0 16px;
        font-size: ${({ theme }) => theme.fontsize.title3};
    `,
}

export default Login
