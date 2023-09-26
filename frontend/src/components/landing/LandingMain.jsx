import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'
import * as components from 'components'
import { BsPlus } from 'react-icons/bs'
import 'react-responsive-carousel/lib/styles/carousel.min.css'
import { Carousel } from 'react-responsive-carousel'

import * as hooks from 'hooks'
import * as api from 'api'

const LandingMain = () => {
    const navigate = useNavigate()
    const accessToken = hooks.getCookie('Authorization')
    const [projects, setProjects] = useState([])
    const [createFormOpened, setCreateFormOpened] = useState(false)
    const [project, setProject] = useState({
        name: '',
        description: '',
    })

    const initProjects = async () => {
        try {
            const projectsRes = await api.apis.getProjects()
            setProjects(projectsRes.data)
        } catch (e) {
            hooks.deleteCookie('Authorization')
            hooks.deleteCookie('Refresh')
        }
    }

    const createProject = async () => {
        try {
            await api.apis.createProject(project)
            initProjects()
            setCreateFormOpened(false)
        } catch (e) {}
    }

    useEffect(() => {
        if (!accessToken) {
            return
        }
        initProjects()
    }, [])

    return (
        <S.Wrap>
            <S.Logo>
                <img src={require('images/logo.png').default} alt={'로고'} />
            </S.Logo>
            {!accessToken && (
                <>
                    <S.Title>
                        웹 개발 초심자를 위한<nav></nav>프로젝트 협업 툴
                    </S.Title>
                    <S.SubTitle>
                        웹 개발 프로젝트에 필요한 기본적인 기능을 모두 제공하여<nav></nav>처음으로 웹 개발을 진행하는
                        사람도 원활한 협업을 할 수 있습니다.
                    </S.SubTitle>
                    <components.LandingStart />
                </>
            )}

            {accessToken && !createFormOpened && (
                <S.ProjectCarousel>
                    <Carousel
                        showStatus={false}
                        centerMode={true}
                        centerSlidePercentage={20}
                        stopOnHover={true}
                        showThumbs={false}
                        autoPlay
                        showIndicators={false}>
                        <S.CreateProjectButton onClick={() => setCreateFormOpened(true)}>
                            <BsPlus />
                        </S.CreateProjectButton>
                        {projects.map((project, index) => (
                            <S.ProjectBox key={index} onClick={() => navigate('/project/' + project.id)}>
                                <S.ProjectName>{project.name}</S.ProjectName>
                                <S.ProjectDescription>{project.description}</S.ProjectDescription>
                            </S.ProjectBox>
                        ))}
                    </Carousel>
                </S.ProjectCarousel>
            )}
            {createFormOpened && (
                <div>
                    <S.ProjectCreateForm className="project-create-form">
                        <S.ProjectNameInput
                            placeholder="프로젝트명을     입력하세요"
                            maxlength="16"
                            onChange={e => setProject({ ...project, name: e.target.value })}
                        />

                        <S.ProjectDescriptionInput
                            placeholder="프로젝트 설명을 입력하세요&#13;&#10;(최대 30자)"
                            maxlength="30"
                            onChange={e => setProject({ ...project, description: e.target.value })}
                        />
                    </S.ProjectCreateForm>

                    <S.CreateAccess>
                        <S.CreateCancelButton onClick={() => setCreateFormOpened(false)}>취소</S.CreateCancelButton>
                        <S.CreateAcceptButton onClick={() => createProject()}>프로젝트 생성</S.CreateAcceptButton>
                    </S.CreateAccess>
                </div>
            )}
            {accessToken && !createFormOpened && (
                <S.Logout
                    onClick={() => {
                        hooks.deleteCookie('Authorization')
                        hooks.deleteCookie('Refresh')
                    }}>
                    로그아웃
                </S.Logout>
            )}
            <S.Footer>
                © 2023 CoRookie. Portions of content used under license from SSAFY. All Rights Reserved.
            </S.Footer>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        position: relative;
    `,

    Title: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 10px 8px 30px 8px;
        padding: 8px;
        text-align: center;
        line-height: 1.5;
        font-size: ${({ theme }) => theme.fontsize.landingtitle};
    `,
    SubTitle: styled.div`
        width: auto;
        color: ${({ theme }) => theme.color.black};
        margin: 16px 8px 32px 8px;
        padding: 8px;
        text-align: center;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        line-height: 2;
    `,
    ProjectBox: styled.div`
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        /* align-items: center;
        /* flex: 0 0 auto; */
        margin: 4px;
        padding: 4px;
        width: 200px;
        height: 200px;
        border-radius: 8px;
        box-shadow: ${({ theme }) => theme.shadow.card};
        background-color: ${({ theme }) => theme.color.main};
        transition-duration: 0.2s;
        color: ${({ theme }) => theme.color.white};
        cursor: pointer;
    `,
    ProjectCarousel: styled.div`
        width: 100%;
        height: 210px;
        margin: 40px;
        padding: 4px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .carousel {
            max-width: 1100px;
            width: 1100px;
        }
        .slider-wrapper {
            width: 1100px;
        }
    `,
    CreateProjectButton: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        flex: 0 0 auto;
        padding: 4px;
        margin: 6px;
        width: 200px;
        height: 200px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        transition-duration: 0.2s;
        cursor: pointer;

        & > svg {
            width: 30%;
            height: 30%;
            color: ${({ theme }) => theme.color.main};
        }
    `,
    ProjectCreateForm: styled.div`
        width: 370px;
        height: 370px;
        margin: 0 auto 8px;
        padding: 16px;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        color: ${({ theme }) => theme.color.lightgray};

        .project-create-form {
        }
        .carousel {
            max-width: 1000px;
        }
        .slider-wrapper {
            /* width: 600px; */
        }
    `,

    ProjectNameInput: styled.input`
        width: 90%;
        height: 220px;
        resize: none;
        border: none;
        outline: none;
        border-radius: 8px;
        margin: 16px 16px 0 16px;
        font-size: 40px;
        font-family: ${({ theme }) => theme.font.main};
        background-color: ${({ theme }) => theme.color.white};
        padding: 0 16px 100px;
        /* overflow-y: hidden; */
        &::placeholder {
            font-size: 40px;
            white-space: pre-wrap;
            color: ${({ theme }) => theme.color.gray};
        }
    `,
    ProjectDescriptionBox: styled.div`
        width: 100%;
        /* display: flex; */
        align-items: center;
        margin: 24px 16px;
    `,

    ProjectDescriptionInput: styled.textarea`
        width: 90%;
        height: 70px;
        resize: none;
        border: none;
        outline: none;
        border-radius: 8px;
        margin: 16px;
        font-size: 20px;
        font-family: ${({ theme }) => theme.font.main};
        background-color: ${({ theme }) => theme.color.white};
        /* border: 1px solid ${({ theme }) => theme.color.black}; */
        padding: 0 16px;
        &::placeholder {
            font-size: 20px;
            white-space: pre-wrap;
            color: ${({ theme }) => theme.color.gray};
        }
    `,
    CreateAccess: styled.div`
        display: flex;
        justify-content: center;
        width: 100%;
    `,
    CreateAcceptButton: styled.button`
        display: flex;
        justify-content: center;
        align-items: center;
        height: 60px;
        width: 180px;
        padding: 20px;
        margin: 16px auto 0 auto;
        border-radius: 4px;
        font-size: ${({ theme }) => theme.fontsize.title3};
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.main};
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        transition: all 0.2s linear;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.main};
        }
    `,
    CreateCancelButton: styled.button`
        display: flex;
        justify-content: center;
        align-items: center;
        height: 60px;
        width: 180px;
        padding: 20px;
        margin: 16px auto 0 auto;
        font-size: ${({ theme }) => theme.fontsize.title3};
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.darkgray};
        background-color: ${({ theme }) => theme.color.darkgray};
        color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        transition: all 0.2s linear;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.darkgray};
        }
    `,
    Logo: styled.div`
        width: auto;
        /* height: auto; */
        padding: auto;
        text-align: center;
        margin: 10px 24px 0 24px;
    `,
    Logout: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        width: 180px;
        padding: 20px;
        margin: 80px auto 0 auto;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.darkgray};
        background-color: ${({ theme }) => theme.color.darkgray};
        color: ${({ theme }) => theme.color.white};
        box-shadow: ${({ theme }) => theme.shadow.card};
        transition: all 0.2s linear;
        cursor: pointer;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.black};
        }
    `,
    ProjectName: styled.div`
        width: 100%;
        height: 60%;
        padding: 28px 28px 16px 28px;
        color: ${({ theme }) => theme.color.white};
        font-size: ${({ theme }) => theme.fontsize.title2};
        line-height: 1.5;
        text-align: left;
    `,
    ProjectDescription: styled.div`
        width: 100%;
        height: 40%;
        padding: 0 28px 28px 28px;
        color: ${({ theme }) => theme.color.white};
        font-size: 13px;
        text-align: left;
        line-height: 1.5;
    `,
    Footer: styled.div`
        position: fixed;
        display: flex;
        align-items: center;
        bottom: 0;
        left: 0;
        width: 100%;
        height: 40px;
        padding: 8px 40px;
        font-size: 11px;
        background-color: ${({ theme }) => theme.color.background};
    `,
}

export default LandingMain
