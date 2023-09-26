import React, { useEffect, useRef } from 'react'
import styled from 'styled-components'
import * as components from 'components'

import * as hooks from 'hooks'
import * as api from 'api'

const IssueBoard = ({ projectId }) => {
    const { issueCreateOpened } = hooks.issueCreateState()
    const { tasks, setTasks } = hooks.tasksState()

    useEffect(() => {
        api.apis
            .getIssueList(projectId)
            .then(response => {
                setTasks(response.data)
            })
            .catch(error => {
                console.log('태스크 불러오기 실패', error)
            })
    }, [])

    return (
        <S.Container>
            <S.Wrap>
                {issueCreateOpened && <components.IssueCreate projectId={projectId} />}
                {Array.isArray(tasks) &&
                    tasks.map(task => {
                        return <components.IssuePreview key={task.id} task={task} />
                    })}
            </S.Wrap>
        </S.Container>
    )
}

const S = {
    Container: styled.div`
        display: flex;
        flex-direction: column;
        height: 100%;
        width: auto;
        flex-grow: 1;
        max-height: calc(100vh - 208px);
        margin: 0 16px 16px;
        border-radius: 8px;
        /* background-color: ${({ theme }) => theme.color.white}; */
    `,
    Wrap: styled.div`
        /* padding: 0 16px; */
        min-height: 100%;
        overflow-y: auto;
        &::-webkit-scrollbar {
            height: 0px;
            width: 4px;
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
    `,
}

export default IssueBoard
