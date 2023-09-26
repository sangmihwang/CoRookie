import React, { useState } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'

import * as components from 'components'
import * as hooks from 'hooks'
import * as api from 'api'

import { IoChevronForward, IoChevronBack } from 'react-icons/io5'
import { useEffect } from 'react'
import axios from 'axios'

const VideoChat = () => {
    const { chatboxOpened } = hooks.chatBoxState()
    const { projectId, channelId } = useParams()
    const [videoChannel, setVideoChannel] = useState(null)
    const [selectedFile, setSelectedFile] = useState(null)
    const [selectedFileName, setSelectedFileName] = useState(null)
    const [uploadStatus, setUploadStatus] = useState('')
    const [summary, setSummary] = useState([])
    const [isSummaryVisible, setIsSummaryVisible] = useState(false)
    const [focusedRecording, setFocusedRecording] = useState(null)
    const [focusedSummary, setFocusedSummary] = useState(null)
    const [focusedSummaryUrl, setFocusedSummaryUrl] = useState(null)
    const [focusedSttText, setFocusedSttText] = useState(null)

    const setSummaryList = async () => {
        const summaryRes = await api.apis.getAnalysisList(channelId)
        console.log(summaryRes.data)
        setSummary(summaryRes.data)
    }

    const toggleVisibility = () => {
        if (!isSummaryVisible) {
            setIsSummaryVisible(!isSummaryVisible)
        }
    }

    useEffect(() => {
        const initChannel = async () => {
            const videoChannelRes = await api.apis.getVideoChannel(projectId, channelId)
            console.log(videoChannelRes.data)
            setVideoChannel(videoChannelRes.data)
        }
        setVideoChannel(null)
        setFocusedSummary(null)
        setFocusedSummaryUrl(null)
        setIsSummaryVisible(false)
        initChannel()
        setSummaryList()
    }, [projectId, channelId])

    if (!videoChannel) {
        return
    }

    const handleFileChange = event => {
        setSelectedFile(event.target.files[0])
        const idx = summary.length
        console.log(idx)
        setSelectedFileName(`${videoChannel.name} 요약 ${idx + 1}`)
    }

    const handleUpload = async () => {
        if (!selectedFile) {
            setUploadStatus('Please select a file.')
            return
        }

        const formData = new FormData()
        formData.append('file', selectedFile)
        formData.append('recordName', selectedFileName)

        const cookies = document.cookie // 쿠키 문자열 가져오기
        const cookieArray = cookies.split(';') // 세미콜론으로 구분된 쿠키 문자열을 배열로 분할
        let token = null
        // 각 쿠키를 순회하며 원하는 쿠키를 찾기
        for (const cookie of cookieArray) {
            const [name, value] = cookie.trim().split('=') // 쿠키 문자열을 이름과 값으로 분할
            if (name === 'Authorization') {
                token = value
                break // 원하는 쿠키를 찾았으면 루프 종료
            }
        }

        try {
            const response = await api.apis.createAnalysis(channelId, formData, token)

            console.log(response.data)

            setSelectedFile(null)
            setSelectedFileName(null)
            if (response.status === 200) {
                setUploadStatus('File uploaded successfully.')
                // Handle success scenario
            } else {
                setUploadStatus('Upload failed. Please try again.')
                // Handle failure scenario
            }
        } catch (error) {
            setUploadStatus('An error occurred during upload.')
            console.log(error)
            // Handle error scenario
        } finally {
            setSummaryList()
        }
    }

    return (
        <S.Wrap>
            <S.Header>
                <S.Title>{videoChannel.name}</S.Title>
            </S.Header>
            <S.Content>
                <S.Container>
                    <iframe
                        // src={`http://localhost:4200/#/${videoChannel.sessionId}`} // local 시험할 때
                        src={`https://i9a402.p.ssafy.io:8443/#/${videoChannel.sessionId}`} // 우리 서버에서
                        allow="camera;microphone;fullscreen;autoplay"
                        width="100%"
                        height="100%">
                        <p>사용 중인 브라우저는 iframe을 지원하지 않습니다.</p>
                    </iframe>
                </S.Container>
                {!isSummaryVisible ? (
                    <S.SummaryContainer>
                        <S.SummaryHeader>회의록</S.SummaryHeader>
                        <S.CreateSummaryHeader>새로운 회의록 만들기</S.CreateSummaryHeader>
                        <S.CreateSummary>
                            <S.FileInputWrapper>
                                <S.ChosenFile>
                                    {selectedFile ? selectedFileName : '회의록을 업로드하세요.'}
                                </S.ChosenFile>
                                <input type="file" id="file" placeholder="첨부파일" onChange={handleFileChange} />
                                <label htmlFor="file">
                                    <S.FileButton>파일 선택</S.FileButton>
                                </label>
                            </S.FileInputWrapper>
                            <S.UploadButton onClick={handleUpload}>요약 생성하기</S.UploadButton>
                            {/* <S.NoFileMessage>{uploadStatus}</S.NoFileMessage> */}
                        </S.CreateSummary>
                        <S.SummaryList>
                            {summary.map((data, index) => (
                                <S.Recording
                                    className={focusedRecording === data.id ? 'focused' : ''}
                                    key={data.id}
                                    onClick={() =>
                                        api.apis.getAnalysisDetail(data.id).then(response => {
                                            console.log(response.data)
                                            toggleVisibility()
                                            setFocusedSummary(response.data.summarizationText)
                                            setFocusedSummaryUrl(response.data.s3URL)
                                            setFocusedSttText(response.data.sttText)
                                            setFocusedRecording(data)
                                        })
                                    }>
                                    <S.SummaryContent>
                                        <S.SummaryTitle>
                                            {index + 1}. {data.recordName}
                                        </S.SummaryTitle>
                                        <S.SummaryDate>{data.createdAt}</S.SummaryDate>
                                    </S.SummaryContent>
                                    <IoChevronForward />
                                </S.Recording>
                            ))}
                        </S.SummaryList>
                    </S.SummaryContainer>
                ) : (
                    <S.SummaryContainer>
                        <IoChevronBack onClick={() => setIsSummaryVisible(false)} />
                        <S.DetailHeader>{focusedRecording.recordName} 전문</S.DetailHeader>
                        <S.SummaryDetail>
                            <S.DetailContent>{focusedSttText}</S.DetailContent>
                        </S.SummaryDetail>
                        <S.DetailHeader>{focusedRecording.recordName} 요약본</S.DetailHeader>
                        <S.SummaryDetail>
                            <S.DetailContent>{focusedSummary}</S.DetailContent>
                            <S.DetailUrl onClick={() => window.open({ focusedSummaryUrl })}>
                                {focusedSummaryUrl}
                            </S.DetailUrl>
                        </S.SummaryDetail>
                    </S.SummaryContainer>
                )}
            </S.Content>
        </S.Wrap>
    )
}

const S = {
    TextBox: styled.div`
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        height: 120px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 8px 16px;
        padding: 0 16px;
    `,
    Wrap: styled.div`
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
    `,
    Content: styled.div`
        display: flex;
        width: 100%;
        height: 100%;
    `,
    Header: styled.div`
        display: flex;
        align-items: center;
        height: 64px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        margin: 16px;
        padding: 0 26px;
    `,
    Title: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title2};
        display: flex;
        height: 64px;
        justify-content: space-between;
        align-items: center;
        position: relative;
    `,
    Container: styled.div`
        display: flex;
        height: 100%;
        width: 100%;
        max-height: calc(100vh - 152px);
        margin: 0 16px;
        border-radius: 8px;
    `,
    SummaryContainer: styled.div`
        height: 100%;
        min-width: 318px;
        background-color: ${({ theme }) => theme.color.white};
        border-radius: 8px;
        display: flex;
        flex-direction: column;
        margin-right: 16px;
        padding: 8px;
        & svg {
            width: 16px;
            height: 16px;
            margin: 8px;
            cursor: pointer;
            color: ${({ theme }) => theme.color.black};
            justify-self: flex-end;
            &:hover {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
    SummaryHeader: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title2};
        font-weight: bold;
        margin: 16px 0 16px 8px;
    `,
    CreateSummaryHeader: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title3};
        padding: 16px 0 16px 16px;
    `,
    CreateSummary: styled.div`
        display: flex;
        flex-direction: column;
    `,
    FileInputWrapper: styled.div`
        display: flex;
        align-items: center;
        margin-bottom: 16px;
        & > input {
            display: none;
        }
    `,
    ChosenFile: styled.div`
        display: flex;
        align-items: center;
        padding: 4px 4px 4px 8px;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        height: 30px;
        width: 200px;
        margin: 0 0 0 16px;
        border: 1px solid ${({ theme }) => theme.color.middlegray};
        border-top-left-radius: 8px;
        border-bottom-left-radius: 8px;
    `,

    FileButton: styled.div`
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 72px;
        height: 30px;
        border-top-right-radius: 8px;
        border-bottom-right-radius: 8px;
        background-color: ${({ theme }) => theme.color.middlegray};
        font-size: ${({ theme }) => theme.fontsize.sub1};
        &:hover {
            background-color: ${({ theme }) => theme.color.main};
            color: ${({ theme }) => theme.color.white};
        }
        & > input {
            width: 100%;
            height: 100%;
            padding: 0;
            overflow: hidden;
        }
    `,
    UploadButton: styled.div`
        display: flex;
        align-self: center;
        background-color: ${({ theme }) => theme.color.black};
        color: ${({ theme }) => theme.color.white};
        padding: 8px 16px;
        align-items: center;
        justify-content: center;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        border-radius: 4px;
        margin-bottom: 8px;
        cursor: pointer;
        &:hover {
            border: 1px solid ${({ theme }) => theme.color.black};
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.black};
        }
    `,
    NoFileMessage: styled.div`
        font-size: 13px;
        align-self: center;
        height: 15px;
        color: ${({ theme }) => theme.color.warning};
    `,
    SummaryList: styled.div`
        width: 100%;
        height: auto;
        overflow-y: auto;
        &::-webkit-scrollbar {
            height: 3px;
            width: 0px;
        }
        &::-webkit-scrollbar-track {
            background: transparent;
        }
        &::-webkit-scrollbar-thumb {
            background: ${({ theme }) => theme.color.gray};
            border-radius: 45px;
        }
        &::-webkit-scrollbar-thumb:hover {
            background: ${({ theme }) => theme.color.gray};
        }
        & > div .focused {
            background-color: ${({ theme }) => theme.color.lightgray};
            & svg {
                color: ${({ theme }) => theme.color.main};
            }
        }
    `,
    Recording: styled.div`
        width: 100%;
        height: 70px;
        display: flex;
        padding: 16px 24px;
        justify-content: space-between;
        align-items: center;
        cursor: pointer;
        &:hover {
            background-color: ${({ theme }) => theme.color.lightgray};
            & svg {
                color: ${({ theme }) => theme.color.main};
            }
        }
        & svg {
            width: 16px;
            height: 16px;
            color: ${({ theme }) => theme.color.white};
        }
    `,
    SummaryDetail: styled.div`
        display: flex;
        width: 310px;
        height: auto;
        padding: 16px 16px;
        flex-direction: column;
    `,
    DetailHeader: styled.div`
        font-size: ${({ theme }) => theme.fontsize.title2};
        font-weight: bold;
        margin: 16px 0 16px 16px;
    `,
    DetailContent: styled.div`
        line-height: 24px;
        margin: 16px 0;
    `,
    DetailUrl: styled.div`
        cursor: pointer;
        font-size: 13px;
        &:hover {
            color: ${({ theme }) => theme.color.main};
        }
    `,
    SummaryContent: styled.div`
        display: flex;
        width: 245px;
        height: 100%;
        flex-direction: column;
        justify-content: space-between;
    `,
    SummaryTitle: styled.div`
        font-size: ${({ theme }) => theme.fontsize.content};
        font-weight: bold;
    `,
    SummaryDate: styled.div`
        font-size: 11px;
        color: ${({ theme }) => theme.color.gray};
    `,
}

export default VideoChat
