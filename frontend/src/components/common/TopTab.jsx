import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'

import * as utils from 'utils'
import * as hooks from 'hooks'

const TopTab = () => {
    const { profileOpened, openProfile, closeProfile } = hooks.profileState()
    const { closeComment } = hooks.commentState()
    const { closeChatbox } = hooks.chatBoxState()
    const { closeIssueDetail } = hooks.issueDetailState()
    const { closeDmComment } = hooks.dmcommentState()
    const { setThreadId, setCommentCount } = hooks.selectedThreadState()
    const { memberImageUrl } = hooks.meState()
    const { closePlanDetail } = hooks.planDetailState()
    const { closePlanRegister } = hooks.planRegisterState()
    const navigate = useNavigate()

    const toggleProfile = () => {
        if (profileOpened) {
            closeProfile()
        } else {
            openProfile()
            closeComment()
            closeIssueDetail()
            closeChatbox()
            closeDmComment()
            setThreadId(null)
            setCommentCount(0)
            closePlanDetail()
            closePlanRegister()
        }
    }

    return (
        <S.Wrap style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <S.Title onClick={() => navigate(utils.URL.HOME.MAIN)}>
                <img src={require('images/logo.png').default} alt={'로고'} />
            </S.Title>
            <S.Profile onClick={() => toggleProfile()}>
                <img
                    src={memberImageUrl ? memberImageUrl : require('images/profile.png').default}
                    alt="스레드 이미지"
                />
            </S.Profile>
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
    `,
    Title: styled.div`
        cursor: pointer;
        margin: 16px;
        padding: 20px 0 0 0;
        & img {
            width: 180px;
        }
    `,
    Profile: styled.div`
        width: 40px;
        margin: 16px 16px 0 0;
        cursor: pointer;
        border-radius: 8px;
        & img {
            width: 40px;
            height: 40px;
            border-radius: 8px;
        }
    `,
}

export default TopTab
