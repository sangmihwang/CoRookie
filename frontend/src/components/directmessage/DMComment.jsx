import React, { useRef, useState, useEffect } from 'react'
import styled from 'styled-components'
import { IoIosArrowDown, IoIosArrowUp } from 'react-icons/io'

const DMComment = () => {
    const text = useRef(null)
    const [overText, setOverText] = useState(false)
    const [closedText, setClosedText] = useState(false)

    useEffect(() => {
        if (text.current.scrollHeight > 140) {
            setOverText(true)
            setClosedText(true)
        }
    }, [])

    const openMoreText = () => {
        if (text) {
            text.current.style.maxHeight = 'none'
            setClosedText(false)
        }
    }

    const hideText = () => {
        text.current.style.maxHeight = '140px'
        setClosedText(true)
    }
    return (
        <S.Wrap>
            <S.ImageBox>
                <img src={require('images/thread_profile.png').default} alt="스레드 이미지" />
            </S.ImageBox>
            <S.ContentBox>
                <S.MemberInfoBox>
                    <S.MemberName>황상미</S.MemberName>
                    <S.CreatedTime>오전 11:12</S.CreatedTime>
                </S.MemberInfoBox>
                <S.Text ref={text}>대대대대ㅐ댓글 가능</S.Text>
                {closedText && (
                    <S.MoreButton>
                        <div onClick={() => openMoreText()}>
                            더보기 <IoIosArrowDown />
                        </div>
                    </S.MoreButton>
                )}
                {overText && !closedText && (
                    <S.MoreButton>
                        <div onClick={() => hideText()}>
                            감추기 <IoIosArrowUp />
                        </div>
                    </S.MoreButton>
                )}
            </S.ContentBox>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        padding: 16px 24px;
    `,
    ImageBox: styled.div`
        width: 40px;
        margin: 0 16px 0 0;

        & img {
            width: 40px;
            height: 40px;
        }
    `,
    ContentBox: styled.div`
        width: 100%;
    `,
    MemberInfoBox: styled.div`
        display: flex;
        align-items: flex-end;
        margin: 0 0 16px 0;
    `,
    MemberName: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title3};
        margin: 0 8px 0 0;
    `,
    CreatedTime: styled.div`
        font-size: 13px;
        color: ${({ theme }) => theme.color.gray};
    `,
    Text: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        line-height: 24px;
        max-height: 140px;
        overflow-y: hidden;
    `,
    MoreButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 40px;
        font-size: ${({ theme }) => theme.fontsize.content};
        color: ${({ theme }) => theme.color.gray};

        & > div {
            cursor: pointer;
        }

        & > div:hover {
            color: ${({ theme }) => theme.color.main};
        }
    `,
}

export default DMComment
