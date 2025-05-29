import {axios} from '../utils/request'
import {CHAT_MODULE} from './_prefix'

export type chatRequest = {
    message: string,
}

export const Send = (message:chatRequest) => {
    return axios.post(`${CHAT_MODULE}/send`, message,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}