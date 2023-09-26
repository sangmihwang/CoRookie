export const calDate = value => {
    const today = new Date()
    const timeValue = new Date(value)

    // UTC 시간에 9시간 추가
    timeValue.setUTCHours(timeValue.getUTCHours() + 9)

    // 한국 시간으로 변환
    const koreanTimeValue = new Date(timeValue.toLocaleString('en-US', { timeZone: 'Asia/Seoul' }))

    const betweenTime = Math.floor((today.getTime() - koreanTimeValue.getTime()) / 1000 / 60)
    if (betweenTime < 1) return '방금전'
    if (betweenTime < 60) {
        return `${betweenTime}분전`
    }

    const betweenTimeHour = Math.floor(betweenTime / 60)
    if (betweenTimeHour < 24) {
        return `${betweenTimeHour}시간전`
    }

    const betweenTimeDay = Math.floor(betweenTime / 60 / 24)
    if (1 <= betweenTimeDay && betweenTimeDay <= 7) {
        return `${betweenTimeDay}일전`
    } else {
        // 주어진 시간 문자열
        const inputString = value

        // 날짜 부분 추출
        const datePartSplit = inputString.split(' ')[0]

        // 날짜 형식 변경
        const datePartHyphen = datePartSplit.split('-')
        const year = datePartHyphen[2]
        const month = datePartHyphen[0]
        const day = datePartHyphen[1]
        const lastDate = year + '-' + month + '-' + day

        // 최종 결과 출력
        return lastDate
    }

    // return `${Math.floor(betweenTimeDay / 365)}년전`
}
