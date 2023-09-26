import React from 'react'
import styled from 'styled-components'

import { IoHappyOutline, IoSadOutline, IoThumbsUp } from 'react-icons/io5'
import * as utils from 'utils'

const Imoticon = ({ icon, count, onIconClick }) => {
    const handleClick = () => {
        if (count < 1000) {
            onIconClick()
        }
    }
    return (
        <S.Wrap>
            {count > 0 ? (
                <S.Container icon={icon} onClick={handleClick}>
                    <S.Icon>
                        {icon === utils.IMOTICON_OPTIONS.thumb ? (
                            <IoThumbsUp />
                        ) : icon === utils.IMOTICON_OPTIONS.happy ? (
                            <IoHappyOutline />
                        ) : (
                            <IoSadOutline />
                        )}
                    </S.Icon>
                    <S.Pressed>{count >= 1000 ? '+999' : count}</S.Pressed>
                </S.Container>
            ) : null}
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        align-items: center;
        width: auto;
        height: auto;
    `,
    Container: styled.div`
        display: flex;
        min-width: 40px;
        height: 24px;
        justify-content: space-between;
        align-items: center;
        padding: 3px 8px;
        margin: 0 4px;
        border-radius: 8px;
        background-color: ${({ theme, icon }) =>
            icon === utils.IMOTICON_OPTIONS.thumb
                ? theme.color.success
                : icon === utils.IMOTICON_OPTIONS.happy
                ? theme.color.pending
                : theme.color.warning};
        cursor: pointer;
    `,
    Icon: styled.div`
        width: 16px;
        max-height: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
        & svg {
            width: 16px;
            height: 16px;
            color: ${({ theme }) => theme.color.white};
        }
    `,
    Pressed: styled.div`
        width: auto;
        max-height: 12px;
        display: flex;
        align-items: center;
        padding-left: 4px;
        font-size: 12px;
        color: ${({ theme }) => theme.color.white};
        -ms-user-select: none;
        -moz-user-select: -moz-none;
        -khtml-user-select: none;
        -webkit-user-select: none;
        user-select: none;
    `,
}

export default Imoticon
