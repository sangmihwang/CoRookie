import React, { useState } from 'react'
import styled from 'styled-components'

import { IoIosChatboxes, IoMdSettings, IoMdMic, IoMdMicOff } from 'react-icons/io'
import { IoVideocam, IoVideocamOff } from 'react-icons/io5'
import { LuScreenShare } from 'react-icons/lu'
import { MdCallEnd } from 'react-icons/md'
import * as hooks from 'hooks'

const VideoControl = () => {
    const { closeProfile } = hooks.profileState()
    const { chatboxOpened, openChatbox, closeChatbox } = hooks.chatBoxState()

    const toggleChatbox = () => {
        if (chatboxOpened) {
            closeChatbox()
        } else {
            openChatbox()
            closeProfile()
        }
    }
    const [micOn, setMicOn] = useState(true)
    const toggleMic = () => {
        setMicOn(prevMicOn => !prevMicOn)
    }

    const [videoOn, setVideoOn] = useState(true)
    const toggleVideo = () => {
        setVideoOn(prevVideoOn => !prevVideoOn)
    }

    return (
        <S.Wrap>
            <S.ControlBox>
                <S.SettingButton>
                    <IoMdSettings />
                </S.SettingButton>
                <S.ShareButton>
                    <LuScreenShare />
                </S.ShareButton>
                <S.MicButton onClick={toggleMic}>{micOn ? <IoMdMic /> : <IoMdMicOff />}</S.MicButton>
                <S.VidButton onClick={toggleVideo}>{videoOn ? <IoVideocam /> : <IoVideocamOff />}</S.VidButton>
                <S.CallOffButton>
                    <MdCallEnd />
                </S.CallOffButton>

                <S.TextChatButton onClick={() => toggleChatbox()} open={chatboxOpened}>
                    <IoIosChatboxes />
                </S.TextChatButton>
            </S.ControlBox>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        width: 100%;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        border: 2px solid
            ${({ open, theme }) => {
                return open ? theme.color.main : theme.color.white
            }};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 8px 16px;
        padding: 8px 16px;

        &:first-child {
            margin-top: 0;
        }

        &:last-child {
            margin-bottom: 0;
        }
    `,

    ControlBox: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 36px;
        padding: 0 16px;
    `,
    SettingButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        color: ${({ theme }) => theme.color.gray};
        margin: 4px;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.main};

            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 26px;
            height: 26px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 26px;
            height: 26px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
    MicButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        color: ${({ theme }) => theme.color.gray};
        margin: 0 8px;
        padding: 4px 8px;
        border: 1px solid ${({ theme }) => theme.color.gray};
        border-radius: 16px;
        width: 64px;
        height: 100%;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.white};
            background-color: ${({ theme }) => theme.color.main};
            border: 1px solid ${({ theme }) => theme.color.main};

            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 26px;
            height: 26px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 26px;
            height: 26px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
    VidButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        color: ${({ theme }) => theme.color.gray};
        margin: 8px;
        padding: 4px 8px;
        border: 1px solid ${({ theme }) => theme.color.gray};
        border-radius: 16px;
        width: 64px;
        height: 100%;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.white};
            background-color: ${({ theme }) => theme.color.main};
            border: 1px solid ${({ theme }) => theme.color.main};

            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 26px;
            height: 26px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 24px;
            height: 24px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
    ShareButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        color: ${({ theme }) => theme.color.gray};
        margin: 8px 8px 8px auto;
        padding: 4px 8px;
        border: 1px solid ${({ theme }) => theme.color.gray};
        border-radius: 16px;
        width: 64px;
        height: 100%;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.white};
            background-color: ${({ theme }) => theme.color.main};
            border: 1px solid ${({ theme }) => theme.color.main};

            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 26px;
            height: 26px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 24px;
            height: 24px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
    CallOffButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        color: ${({ theme }) => theme.color.warning};
        margin: 8px auto 8px 8px;
        padding: 4px 8px;
        border: 1px solid ${({ theme }) => theme.color.warning};
        border-radius: 16px;
        width: 64px;
        height: 100%;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.white};
            background-color: ${({ theme }) => theme.color.warning};
            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 26px;
            height: 26px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 24px;
            height: 24px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
    TextChatButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 4px;
        color: ${({ theme }) => theme.color.gray};
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            color: ${({ theme }) => theme.color.main};

            & > div img {
                margin: 0 4px 0 0;
            }
        }

        & svg {
            width: 26px;
            height: 26px;
        }

        & > div {
            margin: 0 10px 0 0;
        }

        & img {
            width: 26px;
            height: 26px;
            transition-duration: 0.2s;
        }

        & img:not(:last-child) {
            margin: 0 -10px 0 0;
        }
    `,
}

export default VideoControl
