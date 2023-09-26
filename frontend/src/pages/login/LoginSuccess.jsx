import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'
import FadeLoader from 'react-spinners/FadeLoader'

import * as api from 'api'
import * as hooks from 'hooks'

const LoginSuccess = () => {
    const navigate = useNavigate()

    const params = new URLSearchParams(window.location.search)

    useEffect(() => {
        const auth = async () => {
            const token = {
                authToken: params.get('token'),
            }

            try {
                const res = await api.apis.auth(token)
                hooks.setAccessToken(res.data.accessToken)
                hooks.setRefreshToken(res.data.refreshToken)
                navigate('/')
            } catch (err) {
                alert('로그인에 실패했습니다. 잠시 후 다시 시도해주세요')
            }
        }

        auth()
    }, [])

    return (
        <React.Fragment>
            <LoadingSpinner>
                <FadeLoader color="#286EF0" />
            </LoadingSpinner>
        </React.Fragment>
    )
}

const LoadingSpinner = styled.div`
    width: 100vw;
    height: 100vh;
    position: fixed;
    top: 0;
    left: 0;
    display: flex;
    justify-content: center;
    align-items: center;
`

export default LoginSuccess
