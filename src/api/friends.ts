import {axios} from '../utils/request'
import {FRIEND_MODULE, USER_MODULE} from './_prefix'

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

export const allRequest = (userId: number) => {
    return axios.get(`${FRIEND_MODULE}/friendRequest`, {
        params: {
            userId: userId
        }
    })
        .then(res => {
            return res
        });
};

export const searchFriend = (phone:string) => {
    return axios.get(`${USER_MODULE}/search/${phone}`)
        .then(res => {
            return res
        })
}

export const SendFriendRequest = (selfId: number, friendId: number) => {
    return axios.post(`${FRIEND_MODULE}`, null, {
        params: {
            selfId,
            friendId
        }
    }).then(res => {
        return res.data
    })
}

export const AcceptFriendRequest = (selfId: number, friendId: number) => {
    return axios.post(`${FRIEND_MODULE}/friendRequest`, null, {
        params: {
            selfId,
            friendId
        }
    }).then(res => {
        return res.data
    })
}

export const RejectFriendRequest = (selfId: number, friendId: number) => {
    return axios.delete(`${FRIEND_MODULE}/friendRequest`,  {
        params: {
            selfId,
            friendId
        }
    }).then(res => {
        return res.data
    })
}