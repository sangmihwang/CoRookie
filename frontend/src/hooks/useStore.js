import { create } from 'zustand'

export const textChannelsState = create(set => ({
    textChannels: [],
    setTextChannels: data => set(state => ({ textChannels: data })),
}))

export const directChannelsState = create(set => ({
    directChannels: [],
    setDirectChannels: data => set(state => ({ directChannels: data })),
}))

export const directMessagesState = create(set => ({
    page: 0,
    size: 10,
    sort: 'createdAt',
    direction: 'desc',
    upPage: () => set(state => ({ ...state, page: state.page + 1 })),
    initPage: () => set(state => ({ ...state, page: 0 })),
}))

export const videoChannelsState = create(set => ({
    videoChannels: [],
    setVideoChannels: data => set(state => ({ videoChannels: data })),
}))

export const threadsState = create(set => ({
    page: 0,
    size: 10,
    sort: 'createdAt',
    direction: 'desc',
    upPage: () => set(state => ({ ...state, page: state.page + 1 })),
    initPage: () => set(state => ({ ...state, page: 0 })),
}))

export const commentsState = create(set => ({
    page: 0,
    size: 10,
    sort: 'createdAt',
    direction: 'desc',
    upPage: () => set(state => ({ ...state, page: state.page + 1 })),
    initPage: () => set(state => ({ ...state, page: 0 })),
}))

export const selectedThreadState = create(set => ({
    threadId: null,
    commentCount: 0,
    setThreadId: data => set(state => ({ threadId: data })),
    setCommentCount: data => set(state => ({ commentCount: data })),
}))

export const projectMembersState = create(set => ({
    projectMembers: [],
    setProjectMembers: data => set(state => ({ projectMembers: data })),
}))

export const menuState = create(set => ({
    menu: '',
    openMenu: menu => set(state => ({ openedMenu: menu })),
}))

export const projectState = create(set => ({
    project: {
        id: '',
        name: '',
        description: '',
        createdAt: '',
        updatedAt: '',
        enabled: '',
        invitationLink: '',
        invitationStatus: '',
        managerName: '',
        isManager: '',
    },
    setProject: project =>
        set(state => ({
            project: {
                id: project.id,
                name: project.name,
                description: project.description,
                createdAt: project.createdAt,
                updatedAt: project.updatedAt,
                enabled: project.enabled,
                invitationLink: project.invitationLink,
                invitationStatus: project.invitationStatus,
                managerName: project.managerName,
                isManager: project.isManager,
            },
        })),
}))

export const profileState = create(set => ({
    profileOpened: false,
    profileEdit: false,
    profileName: '황상미',
    profileImage: require('images/profilebox.png').default,
    openProfile: () => set(state => ({ profileOpened: true })),
    closeProfile: () => set(state => ({ profileOpened: false })),
    openEdit: () => set(state => ({ profileEdit: true })),
    closeEdit: () => set(state => ({ profileEdit: false })),
    setName: name => set(state => ({ profileName: name })),
    setProfileImage: imageUrl => set(state => ({ profileImage: imageUrl })),
}))

export const setManagerState = create(set => ({
    managerOpened: false,
    openManager: () => set(state => ({ managerOpened: true })),
    closeManager: () => set(state => ({ managerOpened: false })),
    setManager: manager => set(state => ({ manager: manager })),
}))

export const meState = create(set => ({
    memberId: null,
    memberName: null,
    memberEmail: null,
    memberImageUrl: null,
    setMe: data =>
        set(state => ({
            memberId: data.id,
            memberName: data.name,
            memberEmail: data.email,
            memberImageUrl: data.imageUrl,
        })),
    setName: data => set(state => ({ memberName: data })),
    setImageUrl: data => set(state => ({ imageUrl: data })),
}))

export const memberState = create(set => ({
    memberOpened: false,
    members: [],
    setMembers: members => set(state => ({ members: members })),
    openMember: () => set(state => ({ memberOpened: true })),
    closeMember: () => set(state => ({ memberOpened: false })),
}))

export const linkState = create(set => ({
    linkActivated: false,
    activateLink: () => set(state => ({ linkActivated: true })),
    deactivateLink: () => set(state => ({ linkActivated: false })),
}))

export const commentState = create(set => ({
    commentOpened: false,
    openComment: () => set(state => ({ commentOpened: true })),
    closeComment: () => set(state => ({ commentOpened: false })),
}))

export const dmcommentState = create(set => ({
    dmcommentOpened: false,
    openDmComment: () => set(state => ({ dmcommentOpened: true })),
    closeDmComment: () => set(state => ({ dmcommentOpened: false })),
}))

export const taskState = create(set => ({
    showIssue: true,
    openIssue: () => set(state => ({ showIssue: true })),
    openKanban: () => set(state => ({ showIssue: false })),
}))

export const tasksState = create(set => ({
    tasks: [],
    setTasks: input => set(state => ({ tasks: input })),
}))

export const priorityState = create(set => ({
    value: '중요도',
    setValue: input => set(state => ({ value: input })),
}))

export const detailPriorityState = create(set => ({
    value: '중요도',
    setValue: input => set(state => ({ value: input })),
}))

export const createPriorityState = create(set => ({
    value: '중요도',
    setValue: input => set(state => ({ value: input })),
}))

export const managerState = create(set => ({
    value: { managerId: '', managerName: '책임자' },
    setValue: input => set(state => ({ value: input })),
}))

export const detailManagerState = create(set => ({
    value: { managerId: '', managerName: '책임자' },
    setValue: input => set(state => ({ value: input })),
}))

export const createManagerState = create(set => ({
    value: '책임자',
    setValue: input => set(state => ({ value: input })),
}))

export const categoryState = create(set => ({
    value: '분류',
    setValue: input => set(state => ({ value: input })),
}))

export const detailCategoryState = create(set => ({
    value: '분류',
    setValue: input => set(state => ({ value: input })),
}))

export const createCategoryState = create(set => ({
    value: '분류',
    setValue: input => set(state => ({ value: input })),
}))

export const statusState = create(set => ({
    value: '상태',
    setValue: input => set(state => ({ value: input })),
}))

export const issueDetailState = create(set => ({
    issueDetailOpened: '-1',
    openIssueDetail: input => set(state => ({ issueDetailOpened: input })),
    closeIssueDetail: () => set(state => ({ issueDetailOpened: '-1' })),
}))

export const issueCreateState = create(set => ({
    issueCreateOpened: false,
    openIssueCreate: () => set(state => ({ issueCreateOpened: true })),
    closeIssueCreate: () => set(state => ({ issueCreateOpened: false })),
}))

export const dateState = create(set => ({
    monthStart: -1,
    monthEnd: -1,
    startDate: -1,
    endDate: -1,
    setMonthStart: input => set(state => ({ monthStart: input })),
    setMonthEnd: input => set(state => ({ monthEnd: input })),
    setStartDate: input => set(state => ({ startDate: input })),
    setEndDate: input => set(state => ({ endDate: input })),
}))

export const planDateState = create(set => ({
    onDragDate: null,
    planStartDate: null,
    planEndDate: null,
    setOnDragDate: input => set(state => ({ onDragDate: input })),
    setPlanStartDate: input => set(state => ({ planStartDate: input })),
    setPlanEndDate: input => set(state => ({ planEndDate: input })),
}))

export const chatBoxState = create(set => ({
    chatboxOpened: false,
    openChatbox: () => set(state => ({ chatboxOpened: true })),
    closeChatbox: () => set(state => ({ chatboxOpened: false })),
}))

export const planDetailState = create(set => ({
    planDetailOpened: null,
    openPlanDetail: id => set(state => ({ planDetailOpened: id })),
    closePlanDetail: () => set(state => ({ planDetailOpened: null })),
}))

export const planRegisterState = create(set => ({
    planRegisterOpened: false,
    openPlanRegister: () => set(state => ({ planRegisterOpened: true })),
    closePlanRegister: () => set(state => ({ planRegisterOpened: false })),
}))
