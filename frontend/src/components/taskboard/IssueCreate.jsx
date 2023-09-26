import React, { useState, useRef, useEffect } from 'react'
import styled from 'styled-components'

import * as components from 'components'
import * as hooks from 'hooks'
import * as utils from 'utils'
import * as api from 'api'
const IssueCreate = ({ projectId }) => {
    const { closeIssueCreate } = hooks.issueCreateState()
    const { value: priority, setValue: setPriority } = hooks.createPriorityState()
    const { value: manager, setValue: setManager } = hooks.createManagerState()
    const { value: category, setValue: setCategory } = hooks.createCategoryState()
    const { closeIssueDetail } = hooks.issueDetailState()
    const { tasks, setTasks } = hooks.tasksState()
    const { project } = hooks.projectState()
    const { members } = hooks.memberState()
    const [title, setTitle] = useState('')
    let createRef = useRef(null)
    let titleInput = useRef(null)

    const postIssue = () => {
        if (title !== '' && priority !== '중요도' && manager !== '책임자' && category !== '분류') {
            const data = {
                topic: title,
                description: '',
                progress: 'todo',
                priority: priority,
                manager: manager,
                category: category,
            }

            closeIssueCreate()

            api.apis
                .createIssue(projectId, data)
                .then(response => {
                    setTasks([...tasks, response.data])
                })
                .catch(error => {
                    console.log(error)
                })
        } else {
            alert('정보를 입력해주세요. ')
        }
    }

    useEffect(() => {
        setPriority('중요도')
        setManager('관리자')
        setCategory('분류')
        titleInput.current.focus()
        closeIssueDetail()
        const handleOutside = e => {
            if (createRef.current && !createRef.current.contains(e.target)) {
                closeIssueCreate()
            }
        }
        document.addEventListener('mousedown', handleOutside)
        return () => {
            document.removeEventListener('mousedown', handleOutside)
        }
    }, [createRef, closeIssueCreate])

    const handleTitleChange = e => setTitle(e.target.value)

    const managerList = members.map(member => member.memberName)
    const priorityList = ['Highest', 'High', 'Normal', 'Low', 'Lowest']
    const categoryList = ['frontend', 'backend', 'design', 'development', 'product', 'other']
    return (
        <S.Container ref={createRef}>
            <S.Title ref={titleInput} value={title} onChange={handleTitleChange} placeholder="제목을 입력하세요" />
            <S.Values>
                <S.Properties>
                    <S.ButtonContainer>
                        <components.ToggleButton btnType={utils.ISSUE_OPTIONS.createManager} list={managerList} />
                    </S.ButtonContainer>
                    <S.ButtonContainer>
                        <components.ToggleButton btnType={utils.ISSUE_OPTIONS.createPriority} list={priorityList} />
                    </S.ButtonContainer>
                    <S.ButtonContainer>
                        <components.ToggleButton btnType={utils.ISSUE_OPTIONS.createCategory} list={categoryList} />
                    </S.ButtonContainer>
                </S.Properties>
                <S.Save onClick={() => postIssue()}>저장</S.Save>
            </S.Values>
        </S.Container>
    )
}

const S = {
    Container: styled.div`
        display: flex;
        justify-content: space-between;
        width: 100%;
        min-height: 48px;
        margin: 4px 0;
        padding: 8px 16px;
        border: solid 2px ${({ theme }) => theme.color.main};
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.white};
    `,
    Values: styled.div`
        display: flex;
        justify-content: space-between;
    `,
    Title: styled.input`
        margin: 0 8px 0 0;
        width: 100%;
        border: none;
        outline: none;
        font-family: 'Noto Sans KR', 'Pretendard', sans-serif;
        font-size: ${({ theme }) => theme.fontsize.content};
    `,
    Properties: styled.div`
        display: flex;
        justify-content: space-between;
        min-width: 340px;
    `,
    ButtonContainer: styled.div`
        display: flex;
        justify-content: center;
        width: 116px;
        padding: 0 8px;
    `,
    Save: styled.div`
        display: flex;
        justify-content: center;
        align-items: center;
        width: auto;
        padding: 0 8px;
        font-size: ${({ theme }) => theme.fontsize.sub1};
        white-space: nowrap;
        border-radius: 8px;
        border: 1px solid ${({ theme }) => theme.color.main};
        background-color: ${({ theme }) => theme.color.main};
        color: ${({ theme }) => theme.color.white};
        margin: 0 0 0 8px;
        transition-duration: 0.2s;
        cursor: pointer;

        &:hover {
            background-color: ${({ theme }) => theme.color.white};
            color: ${({ theme }) => theme.color.main};
            box-shadow: none;
        }
    `,
}

export default IssueCreate
