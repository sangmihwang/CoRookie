import React from 'react'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'

import * as utils from 'utils'
import * as hooks from 'hooks'

const LandingFooter = () => {
    return (
        <S.Wrap>
            <S.Title>Footer</S.Title>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        width: 100%;
        max-width: 100vw;
        height: 56px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    `,
    Title: styled.div`
        height: 100%;
        width: 167px;
        font-family: 'Futura PT';
        font-weight: 700;
        color: ${({ theme }) => theme.color.white};

        padding: 24px 8px 8px 32px;
        cursor: pointer;
    `,
}

export default LandingFooter
