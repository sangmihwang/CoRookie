import React, { useState, useEffect } from 'react'
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd'
import styled from 'styled-components'

import { IoReorderTwoSharp } from 'react-icons/io5'
import { BiChevronsUp, BiChevronUp, BiChevronDown, BiChevronsDown } from 'react-icons/bi'
import * as hooks from 'hooks'
import * as api from 'api'

const KanbanBoard = ({ projectId }) => {
    const { tasks, setTasks } = hooks.tasksState()
    const { project } = hooks.projectState()
    const { issueDetailOpened, openIssueDetail, closeIssueDetail } = hooks.issueDetailState()
    const [columns, setColumns] = useState(taskStatus)

    useEffect(() => {
        api.apis
            .getIssueList(project.id)
            .then(response => {
                setTasks(response.data)
            })
            .catch(error => {
                console.log(error)
            })
    }, [])

    const toggleIssueDetail = id => {
        if (issueDetailOpened === id) {
            closeIssueDetail()
        } else {
            openIssueDetail(id)
        }
    }

    const tasksByStatus = tasks.reduce((acc, task) => {
        if (!acc[task.progress]) {
            acc[task.progress] = []
        }
        acc[task.progress].push(task)
        return acc
    }, {})

    const taskStatus = {
        todo: {
            name: 'To Do',
            items: tasksByStatus['todo'] || [],
        },
        inProgress: {
            name: 'In Progress',
            items: tasksByStatus['inProgress'] || [],
        },
        done: {
            name: 'Done',
            items: tasksByStatus['done'] || [],
        },
    }

    useEffect(() => {
        setColumns(taskStatus)
    }, [tasks])

    const renderPriority = priority => {
        switch (priority) {
            case 'Highest':
                return <BiChevronsUp />
            case 'High':
                return <BiChevronUp />
            case 'Normal':
                return <IoReorderTwoSharp />
            case 'Low':
                return <BiChevronDown />
            case 'Lowest':
                return <BiChevronsDown />
            default:
                return null
        }
    }

    const onDragEnd = (result, columns, setColumns) => {
        if (!result.destination) return
        const { source, destination } = result

        if (source.droppableId !== destination.droppableId) {
            const sourceColumn = columns[source.droppableId]
            const destColumn = columns[destination.droppableId]
            const sourceItems = [...sourceColumn.items]
            const destItems = [...destColumn.items]
            const [removed] = sourceItems.splice(source.index, 1)

            removed.status = destination.droppableId

            api.apis.changeIssueStatus(projectId, removed.id, { progress: destination.droppableId })

            destItems.splice(destination.index, 0, removed)
            setColumns({
                ...columns,
                [source.droppableId]: {
                    ...sourceColumn,
                    items: sourceItems,
                },
                [destination.droppableId]: {
                    ...destColumn,
                    items: destItems,
                },
            })
        } else {
            const column = columns[source.droppableId]
            const copiedItems = [...column.items]
            const [removed] = copiedItems.splice(source.index, 1)
            copiedItems.splice(destination.index, 0, removed)
            setColumns({
                ...columns,
                [source.droppableId]: {
                    ...column,
                    items: copiedItems,
                },
            })
        }
    }

    return (
        <S.Wrap>
            <DragDropContext onDragEnd={result => onDragEnd(result, columns, setColumns)}>
                {columns &&
                    Object.entries(columns).map(([columnId, column], index) => {
                        return (
                            <S.Column key={columnId}>
                                {columnId === 'todo' && <S.Todo>{column.name}</S.Todo>}
                                {columnId === 'inProgress' && <S.InProgress>{column.name}</S.InProgress>}
                                {columnId === 'done' && <S.Done>{column.name}</S.Done>}
                                <S.TaskBox>
                                    <Droppable droppableId={columnId} key={columnId}>
                                        {(provided, snapshot) => {
                                            return (
                                                <S.IssueContainer {...provided.droppableProps} ref={provided.innerRef}>
                                                    {column.items.map((item, index) => {
                                                        return (
                                                            <Draggable
                                                                key={item.id}
                                                                draggableId={item.topic}
                                                                index={index}>
                                                                {(provided, snapshot) => {
                                                                    return (
                                                                        <S.IssueDrag
                                                                            ref={provided.innerRef}
                                                                            {...provided.draggableProps}
                                                                            {...provided.dragHandleProps}
                                                                            isDragging={snapshot.isDragging}
                                                                            style={{
                                                                                ...provided.draggableProps.style,
                                                                            }}>
                                                                            <S.Container
                                                                                onClick={() =>
                                                                                    toggleIssueDetail(item.id)
                                                                                }>
                                                                                <S.IssueTopic>
                                                                                    {item.topic}
                                                                                </S.IssueTopic>
                                                                                <S.IssueInfo>
                                                                                    <S.Type>{item.category}</S.Type>
                                                                                    <S.Priority
                                                                                        priority={item.priority}>
                                                                                        {renderPriority(item.priority)}
                                                                                    </S.Priority>
                                                                                    <S.ProfileImg
                                                                                        src={
                                                                                            item.memberId === null
                                                                                                ? require('images/thread_profile.png')
                                                                                                      .default
                                                                                                : item.memberImageUrl
                                                                                        }
                                                                                        alt="Profile"
                                                                                    />
                                                                                </S.IssueInfo>
                                                                            </S.Container>
                                                                        </S.IssueDrag>
                                                                    )
                                                                }}
                                                            </Draggable>
                                                        )
                                                    })}
                                                    {provided.placeholder}
                                                </S.IssueContainer>
                                            )
                                        }}
                                    </Droppable>
                                </S.TaskBox>
                            </S.Column>
                        )
                    })}
            </DragDropContext>
        </S.Wrap>
    )
}

const S = {
    Wrap: styled.div`
        display: flex;
        justify-content: space-between;
        background-color: ${({ theme }) => theme.color.background};
        margin: 16px;
        height: 100%;
        width: 100%;
        flex-grow: 1;
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
    Column: styled.div`
        display: flex;
        flex-direction: column;
        margin: 0 4px;
        background-color: ${({ theme }) => theme.color.background};
        max-height: calc(100vh - 208px);
        flex: 1;
        min-width: 0;
    `,
    TaskBox: styled.div`
        width: 100%;
    `,
    IssueContainer: styled.div`
        width: 100%;
        border-radius: 8;
        min-height: 500px;
        max-height: calc(100vh - 208px);
    `,
    IssueDrag: styled.div`
        user-select: none;
        padding: 16px;
        margin: 8px 0;
        min-height: 50px;
        border-radius: 8px;
        font-size: ${({ theme }) => theme.fontsize.content};
        background-color: ${({ theme }) => theme.color.white};
        box-shadow: ${props =>
            props.isDragging ? `1px 1px 5px 1px ${props.theme.color.middlegray}` : `${props.theme.shadow.card}`};
    `,
    Container: styled.div`
        height: 100%;
        width: 100%;
    `,

    IssueTopic: styled.div`
        margin: 8px 0;
        overflow: hidden;
    `,
    IssueInfo: styled.div`
        display: flex;
        align-items: center;
        margin: 8px 0 4px 0;
        padding: 12px 0 0 0;
    `,
    Todo: styled.div`
        display: flex;
        align-items: center;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.success};
        padding: 8px 16px;
        width: 100%;
        height: 32px;
        color: ${({ theme }) => theme.color.white};
    `,
    InProgress: styled.div`
        display: flex;
        align-items: center;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.pending};
        padding: 8px 16px;
        width: 100%;
        height: 32px;
        color: ${({ theme }) => theme.color.white};
    `,
    Done: styled.div`
        display: flex;
        align-items: center;
        border-radius: 8px;
        background-color: ${({ theme }) => theme.color.orange};
        padding: 8px 16px;
        width: 100%;
        height: 32px;
        color: ${({ theme }) => theme.color.white};
    `,
    ProfileImg: styled.img`
        width: 20px;
        height: 20px;
        margin: 0 0 0 4px;
        border-radius: 4px;
    `,
    Type: styled.div`
        margin: 4px 0;
        font-size: 13px;
        color: ${({ theme }) => theme.color.middlegray};
    `,
    Priority: styled.div`
        display: flex;
        padding: 0 4px;
        margin: 0 4px 0 0;
        margin-left: auto;
        & svg {
            width: 20px;
            height: 20px;
            color: ${({ theme, priority }) => {
                switch (priority) {
                    case 'Highest':
                        return theme.color.warning
                    case 'High':
                        return theme.color.warning
                    case 'Normal':
                        return theme.color.pending
                    case 'Low':
                        return theme.color.success
                    case 'Lowest':
                        return theme.color.success
                    default:
                        return theme.color.main
                }
            }};
        }
    `,
}

export default KanbanBoard
