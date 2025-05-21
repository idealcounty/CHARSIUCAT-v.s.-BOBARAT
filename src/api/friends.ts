import {axios} from '../utils/request'
import {FRIEND_MODULE} from './_prefix'

export const allFriend = (userId: number) => {
    return axios.get(`${FRIEND_MODULE}`, {
        params: {
            userId: userId
        }
    })
        .then(res => {
            return res
        });
};