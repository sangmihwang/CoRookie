import React from 'react'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'

import * as utils from 'utils'
import * as hooks from 'hooks'

const LandingStart = () => {
    const { profileOpened, openProfile, closeProfile } = hooks.profileState()
    const accessToken = hooks.getCookie('Authorization')
    const navigate = useNavigate()

    const toggleProfile = () => {
        if (profileOpened) {
            closeProfile()
        } else {
            openProfile()
        }
    }

    return (
        <S.Wrap>
            {!accessToken && <S.Login onClick={() => navigate(utils.URL.LOGIN.LOGIN)}>시작하기</S.Login>}
            {accessToken && (
                <S.Login
                    onClick={() => {
                        hooks.deleteCookie('Authorization')
                        hooks.deleteCookie('Refresh')
                    }}>
                    Logout
                </S.Login>
            )}
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        top: 0;
        left: 0;
        width: 100%;
        max-width: 100vw;
        height: 56px;
        display: flex;
        align-items: center;
        justify-content: center;
    `,
    Title: styled.div`
        width: 167px;
        font-family: 'Futura PT';
        font-size: ${({ theme }) => theme.fontsize.logo};
        font-weight: 700;
        color: ${({ theme }) => theme.color.main};

        padding: 24px 8px 8px 32px;
        cursor: pointer;
    `,
    Profile: styled.div`
        width: 40px;
        margin: 16px 16px 0 0;
        cursor: pointer;
        & img {
            width: 40px;
            height: 40px;
        }
    `,
    Login: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        width: 180px;
        padding: 10px;
        color: ${({ theme }) => theme.color.main};

        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.main};
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        margin: auto 0 0 0;
        transition: all 0.2s linear;
        cursor: pointer;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.main};
        }
    `,
}

export default LandingStart
