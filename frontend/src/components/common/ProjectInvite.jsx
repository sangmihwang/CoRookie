import React, { useEffect } from 'react'
import { useParams } from 'react-router'
import { useNavigate } from 'react-router-dom'

import * as hooks from 'hooks'
import * as utils from 'utils'
import * as api from 'api'

const ProjectInvite = () => {
    const navigate = useNavigate()
    const { inviteLink } = useParams()
    const { project, setProject } = hooks.projectState()

    useEffect(() => {
        if (!hooks.getCookie('Authorization')) {
            navigate(utils.URL.HOME.MAIN)
            return
        }

        api.apis
            .getInvitedProject(inviteLink)
            .then(response => {
                setProject(response.data)
                navigate(`/project/${response.data.id}`)
            })
            .catch(err => {
                alert('유효하지 않은 초대 코드입니다')
                navigate(utils.URL.HOME.MAIN)
            })
    }, [])

    return <div></div>
}

export default ProjectInvite
