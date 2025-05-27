import {axios} from '../utils/request'
import {MESSAGE_MODULE, USER_MODULE} from './_prefix'
import {User} from "./user.ts";

export type messageInfo = {
    sender:User,
    receiver:User,
    messageContent:string,
}

export const FriendInfo = (id:number) => {
    return axios.get(`${USER_MODULE}/get/${id}`)
        .then(res => {
            return res
        })
}

export const SendMessage = (message:messageInfo) => {
    return axios.post(`${MESSAGE_MODULE}/send`, message,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

export const GetMessages = (selfId: number, friendId: number) => {
    return axios.get(`${MESSAGE_MODULE}`, {
        params: {
            userId1: selfId,
            userId2: friendId
        }
    }).then(res => {
        return res
    })
};