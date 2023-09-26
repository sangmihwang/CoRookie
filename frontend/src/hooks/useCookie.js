import { Cookies } from 'react-cookie'

const cookies = new Cookies()

const getCookie = token => {
    if (cookies.get(token)) {
        return cookies.get(token)
    } else {
        return null
    }
}

const setAccessToken = token => {
    document.cookie = `Authorization=${token}; max-age=604800; path=/; secure; samesite=none`
}

const setRefreshToken = token => {
    document.cookie = `Refresh=${token}; max-age=604800; path=/; secure; samesite=none`
}

const deleteCookie = token => {
    const date = new Date('2020-01-01').toUTCString()
    document.cookie = `${token}=; expires=${date}; path=/;`
    window.location.reload()
}

export { getCookie, setAccessToken, setRefreshToken, deleteCookie }
