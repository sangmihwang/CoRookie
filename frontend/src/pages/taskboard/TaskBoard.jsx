import React, { useEffect } from 'react'
import { useParams } from 'react-router'
import styled from 'styled-components'

import * as hooks from 'hooks'
import * as components from 'components'

const TaskBoard = () => {
    const { projectId } = useParams()
    const { setValue: setPriorityValue } = hooks.priorityState()
    const { setValue: setManagerValue } = hooks.managerState()
    const { setValue: setCategoryValue } = hooks.categoryState()
    const { setValue: setStatusValue } = hooks.statusState()
    const { showIssue, openIssue } = hooks.taskState()
    const { issueDetailOpened, closeIssueDetail } = hooks.issueDetailState()

    useEffect(() => {
        setPriorityValue('중요도')
        setManagerValue('책임자')
        setCategoryValue('분류')
        setStatusValue('상태')
        closeIssueDetail()
        openIssue()
    }, [])

    useEffect(() => {
        setPriorityValue('중요도')
        setManagerValue('책임자')
        setCategoryValue('분류')
        setStatusValue('상태')
        closeIssueDetail()
    }, [showIssue])

    return (
        <S.Wrap>
            <components.TaskHeader projectId={projectId} />
            <S.IssueContainer>
                {showIssue ? (
                    <components.IssueBoard projectId={projectId} />
                ) : (
                    <components.KanbanBoard projectId={projectId} />
                )}
                {issueDetailOpened !== '-1' && <components.IssueDetail id={issueDetailOpened} />}
            </S.IssueContainer>
        </S.Wrap>
    )
}
const S = {
    Wrap: styled.div`
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
    `,
    IssueContainer: styled.div`
        display: flex;
        width: 100%;
        height: 100%;
        max-height: calc(100vh - 208px);
    `,
}

export default TaskBoard
