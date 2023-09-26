import axios from 'axios'
import * as utils from 'utils'
import * as hooks from 'hooks'

export const instance = axios.create({
    baseURL: utils.API_BASE_URL,
    headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        Accept: 'application/json',
    },
})

instance.interceptors.request.use(
    function (config) {
        const accessToken = hooks.getCookie('Authorization')
        config.headers['Authorization'] = accessToken
        return config
    },
    function (error) {
        return Promise.reject(error)
    },
)

export const apis = {
    auth: token => instance.post('/api/v1/auth', token),

    createProject: data => instance.post('/api/v1/projects', data),
    getProjects: () => instance.get('/api/v1/projects'),
    getProject: projectId => instance.get(`/api/v1/projects/${projectId}`),
    getInvitedProject: inviteLink => instance.get(`/api/v1/projects/invite/${inviteLink}`),
    enableInvitation: projectId => instance.put(`/api/v1/projects/${projectId}/invite/enable`),
    disableInvitation: projectId => instance.put(`/api/v1/projects/${projectId}/invite/disable`),

    getProjectMembers: projectId => instance.get(`/api/v1/projects/${projectId}/projectmembers`),

    getTextChannel: (projectId, textChannelId) =>
        instance.get(`/api/v1/projects/${projectId}/text-channels/${textChannelId}`),
    getTextChannels: projectId => instance.get(`/api/v1/projects/${projectId}/text-channels`),
    textChannelPin: (projectId, textChannelId) =>
        instance.post(`/api/v1/projects/${projectId}/text-channels/${textChannelId}/pin`),
    textChannelUnpin: (projectId, textChannelId) =>
        instance.delete(`/api/v1/projects/${projectId}/text-channels/${textChannelId}/unpin`),
    createTextChannel: (projectId, name) => instance.post(`/api/v1/projects/${projectId}/text-channels`, name),
    createVideoChannel: (projectId, name) => instance.post(`/api/v1/projects/${projectId}/video-channels`, name),
    getVideoChannel: (projectId, videoChannelId) =>
        instance.get(`/api/v1/projects/${projectId}/video-channels/${videoChannelId}`),
    getVideoChannels: projectId => instance.get(`/api/v1/projects/${projectId}/video-channels`),

    createAnalysis: (videoChannelId, formData, token) =>
        instance.post(`/api/v1/projects/1/video-channels/${videoChannelId}/analysis`, formData, {
            headers: {
                Authorization: token,
                'Content-Type': 'multipart/form-data',
            },
        }),
    getAnalysisList: videoChannelId => instance.get(`/api/v1/projects/1/video-channels/${videoChannelId}/analysis`),
    getAnalysisDetail: analysisId => instance.get(`/api/v1/projects/1/video-channels/1/analysis/${analysisId}`),

    getThread: (projectId, textChannelId, threadId) =>
        instance.get(`/api/v1/projects/${projectId}/text-channels/${textChannelId}/threads/${threadId}`),
    getThreads: (projectId, textChannelId, page, size, sort, direction) =>
        instance.get(
            `/api/v1/projects/${projectId}/text-channels/${textChannelId}/threads?page=${page}&size=${size}&sort=${sort},${direction}`,
        ),
    getImojis: (projectId, channelId, threadId) =>
        instance.get(`/api/v1/projects/${projectId}/text-channels/${channelId}/threads/${threadId}/emojis`),

    deleteImoji: (projectId, textChannelId, threadId, emoji) =>
        instance.delete(
            `/api/v1/projects/${projectId}/text-channels/${textChannelId}/threads/${threadId}/emojis/${emoji}`,
        ),
    createImoji: (projectId, textChannelId, threadId, emoji) =>
        instance.post(`/api/v1/projects/${projectId}/text-channels/${textChannelId}/threads/${threadId}/emojis`, emoji),

    getComments: (projectId, textChannelId, threadId, page, size, sort, direction) =>
        instance.get(
            `/api/v1/projects/${projectId}/text-channels/${textChannelId}/threads/${threadId}/comments?page=${page}&size=${size}&sort=${sort},${direction}`,
        ),

    getDirectChannels: projectId => instance.get(`/api/v1/projects/${projectId}/directs`),
    getDirectChannel: (projectId, channelId) => instance.get(`/api/v1/projects/${projectId}/directs/${channelId}`),
    getDirectMessages: (projectId, directChannelId, page, size, sort, direction) =>
        instance.get(
            `/api/v1/projects/${projectId}/directs/${directChannelId}/messages?page=${page}&size=${size}&sort=${sort},${direction}`,
        ),

    getIssueList: projectId => instance.get(`/api/v1/projects/${projectId}/issues`),
    createIssue: (projectId, data) => instance.post(`/api/v1/projects/${projectId}/issues`, data),
    deleteIssue: (projectId, issueId) => instance.delete(`/api/v1/projects/${projectId}/issues/${issueId}`),
    getIssueDetail: (projectId, issueId) => instance.get(`/api/v1/projects/${projectId}/issues/${issueId}`),
    changeIssueTitle: (projectId, issueId, topic) =>
        instance.put(`/api/v1/projects/${projectId}/issues/${issueId}/topic`, topic),
    changeIssueContent: (projectId, issueId, description) =>
        instance.put(`/api/v1/projects/${projectId}/issues/${issueId}/description`, description),
    changeIssueStatus: (projectId, issueId, progress) =>
        instance.put(`/api/v1/projects/${projectId}/issues/${issueId}/progress`, progress),
    changeIssuePriority: (projectId, issueId, priority) =>
        instance.put(`/api/v1/projects/${projectId}/issues/${issueId}/priority`, priority),
    changeIssueManager: (projectId, issueId, managerId) =>
        instance.put(`/api/v1/projects/${projectId}/issues/${issueId}/manager`, managerId),
    changeIssueCategory: (projectId, issueId, category) =>
        instance.put(`/api/v1/projects/${projectId}/issues/${issueId}/category`, category),
    filterIssue: (projectId, type, condition) =>
        instance.get(`/api/v1/projects/${projectId}/issues/filter?type=${type}&condition=${condition}`),

    // instance.put 'https://naver.com?issue=123'
    getMember: memberId => instance.get(`/api/v1/members/${memberId}`),
    changeMemberProfile: (memberId, data) => instance.put(`/api/v1/members/${memberId}/profile`, data),
    changeMemberName: (memberId, data) => instance.put(`/api/v1/members/${memberId}/name`, data),
    getMe: () => instance.get(`api/v1/members/me`),

    createPlan: (projectId, data) => instance.post(`/api/v1/projects/${projectId}/plans`, data),
    getPlans: (projectId, date) => instance.get(`/api/v1/projects/${projectId}/plans?date=${date}`),
    getPlan: (projectId, planId) => instance.get(`/api/v1/projects/${projectId}/plans/${planId}`),

    createPlanCategory: (projectId, data) => instance.post(`/api/v1/projects/${projectId}/plan-categories`, data),
    getPlanCategories: projectId => instance.get(`/api/v1/projects/${projectId}/plan-categories`),
    deletePlanCategory: (projectId, categoryId) =>
        instance.delete(`/api/v1/projects/${projectId}/plan-categories/${categoryId}`),

    checkMeInProject: projectId => instance.get(`/api/v1/projects/${projectId}/projectmembers/me`),
}
