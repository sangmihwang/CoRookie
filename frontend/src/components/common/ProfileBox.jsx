import React, { useRef, useEffect } from 'react'
import styled from 'styled-components'
import uuid from 'react-uuid'

import AWS from 'aws-sdk'
import { IoPencil } from 'react-icons/io5'

import * as style from 'style'
import * as hooks from 'hooks'
import * as api from 'api'

const ProfileBox = () => {
    AWS.config.update({
        region: 'ap-northeast-2',
        credentials: new AWS.CognitoIdentityCredentials({
            IdentityPoolId: 'ap-northeast-2:01b0b700-68e1-41d4-bc17-1318e1b139de',
        }),
    })

    const { memberId, memberName, memberEmail, memberImageUrl, setName, setImageUrl, setMe } = hooks.meState()
    const { profileEdit, openEdit, closeEdit } = hooks.profileState()
    const fileRef = useRef()
    let nameRef = useRef()

    const handleFileInput = e => {
        const file = e.target.files[0]

        const upload = new AWS.S3.ManagedUpload({
            params: {
                Bucket: 'corookie-resources',
                Key: uuid() + '.jpg',
                Body: file,
            },
        })

        const promise = upload.promise()

        promise.then(
            function (data) {
                api.apis.changeMemberProfile(memberId, { imageUrl: data.Location }).then(response => {
                    setImageUrl(response.data.imageUrl)
                })
            },
            function (err) {
                return alert('Error: ' + err.message)
            },
        )
    }

    const handleNameChange = e => setName(e.target.value)

    const nameKeyDown = e => {
        if (e.key === 'Enter') {
            api.apis
                .changeMemberName(memberId, { name: memberName })
                .then(response => {
                    closeEdit()
                })
                .catch(err => {
                    setMe(api.apis.getMe())
                })
        }
    }

    const handleFileClick = e => {
        fileRef.current.click()
    }

    useEffect(() => {
        if (profileEdit) {
            nameRef.current.focus()
        }
    }, [profileEdit])

    return (
        <S.Wrap>
            <S.Header>프로필</S.Header>
            <S.ImageBox onClick={handleFileClick}>
                <img
                    src={memberImageUrl ? memberImageUrl : require('images/profile.png').default}
                    alt="프로필 이미지"
                />
                <S.FileUpload>
                    <input
                        ref={fileRef}
                        type="file"
                        accept="image/*"
                        onChange={handleFileInput}
                        style={{ display: 'none' }}
                    />
                    <IoPencil />
                </S.FileUpload>
            </S.ImageBox>
            <S.ContentBox>
                <S.MemberInfoBox>
                    <S.MemberNameContainer>
                        {!profileEdit ? (
                            <S.MemberName>{memberName}</S.MemberName>
                        ) : (
                            <S.MemberNameEdit
                                ref={nameRef}
                                onChange={handleNameChange}
                                onKeyDown={nameKeyDown}
                                type="text"
                                value={memberName}
                            />
                        )}
                        {!profileEdit && <S.EditName onClick={() => openEdit()}>편집</S.EditName>}
                    </S.MemberNameContainer>
                    <S.MemberEmail>{memberEmail ? memberEmail : '이메일이 없습니다'}</S.MemberEmail>
                </S.MemberInfoBox>
            </S.ContentBox>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 360px;
        min-width: 360px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 0 26px;
        /* animation: ${style.leftSlide} 0.4s linear; */
    `,
    Header: styled.div`
        display: flex;
        position: relative;
        align-items: center;
        white-space: nowrap;
        width: 100%;
        height: fit-content;
        margin: 24px 0 0 0;
        font-size: ${({ theme }) => theme.fontsize.title2};
        color: ${({ theme }) => theme.color.black};

        &::after {
            content: '';
            position: absolute;
            bottom: -20px;
            left: 0;
            width: 100%;
            height: 1px;
            background-color: ${({ theme }) => theme.color.lightgray};
        }
    `,

    ImageBox: styled.div`
        display: flex;
        width: 100%;
        margin: 16px 0;
        padding: 24px;
        position: relative;

        & img {
            width: 100%;
            height: 100%;
            border-radius: 8px;
        }

        & div {
            width: 0px;
            height: 0px;
            margin-right: 16px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        &:hover {
            & img {
                filter: brightness(50%);
            }
            & div {
                width: 100%;
                height: 100%;
                cursor: pointer;
            }
        }
    `,
    FileUpload: styled.div`
        display: flex;
        align-items: center;
        justify-content: center;
        & svg {
            color: ${({ theme }) => theme.color.lightgray};
            width: 70px;
            height: 70px;
        }
    `,
    ContentBox: styled.div`
        width: 100%;
        padding: 0 24px;
    `,
    MemberInfoBox: styled.div`
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        /* margin: 0 16px; */
    `,
    MemberNameContainer: styled.div`
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
    `,
    MemberName: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title2};
        margin: 0 8px 0 0;
        width: 100%;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    `,
    MemberNameEdit: styled.input`
        font-size: ${({ theme }) => theme.fontsize.title2};
        border: none;
        outline: none;
        width: 100%;
        margin: 0 8px 0 0;
        font-family: ${({ theme }) => theme.font.main};
    `,
    MemberEmail: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title3};
        margin: 0 8px 0 0;
        margin-top: 16px;
    `,
    EditName: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title3};
        color: ${({ theme }) => theme.color.main};
        cursor: pointer;
        white-space: nowrap;
    `,
}

export default ProfileBox
