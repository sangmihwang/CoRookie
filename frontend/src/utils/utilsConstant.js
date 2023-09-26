export const URL = {
    LOGIN: {
        LOGIN: '/signin',
        SUCCESS: '/success',
    },
    HOME: {
        MAIN: '/',
    },
    PROJECT: {
        MAIN: '/project/:projectId',
        INVITE: 'project/invite/:inviteLink',
    },
    CHAT: {
        TEXT: '/project/:projectId/channel/text/:channelId',
        VIDEO: '/project/:projectId/channel/video/:channelId',
        DIRECT: '/project/:projectId/channel/direct/:channelId',
    },
    TASK: {
        BOARD: '/project/:projectId/task',
    },
    PLAN: {
        PLAN: '/project/:projectId/plan',
    },
}

export const ISSUE_OPTIONS = {
    title: 'title',
    priority: 'priority',
    manager: 'manager',
    category: 'category',
    status: 'status',
    createManager: 'createManager',
    createPriority: 'createPriority',
    createCategory: 'createCategory',
    detailManager: 'detailManager',
    detailPriority: 'detailPriority',
    detailCategory: 'detailCategory',
}

export const PLAN_OPTIONS = {
    member: {
        label: '참여자',
        placeholder: '참여자 선택',
        options: ['참여자 선택', '홍길동', '홍길동'],
    },
    category: {
        label: '분류',
        placeholder: '분류 선택',
        options: ['분류 선택', '일반', '일반'],
    },
}

export const IMOTICON_OPTIONS = {
    thumb: 'thumb',
    happy: 'happy',
    sad: 'sad',
}

export const MAX_WIDTH = '1920px'
