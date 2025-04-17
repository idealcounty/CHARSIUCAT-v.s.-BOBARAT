import {axios} from '../utils/request'
import {USER_MODULE} from './_prefix'

type LoginInfo = {
    phone: string,
    password: string
}

type RegisterInfo = {
    userId: number,
    userName: string,
    userPhone: string,
    userPassword: string,
    userAddress: string,
    userRole: string,
    userCreateTime: string,
    userBalance: number,
    userAvatar: string,
}

export type CartItem = {
    cartId: number,
    productId: number,
    productQuantity: number,
    productPrice: number,
}

type UpdateInfo = {
    name?: string,
    password?: string,
    address?: string,
}


// 如果有“Vue: This may be converted to an async function”警告，可以不管
// 用户登录
export const userLogin = (loginInfo: LoginInfo) => {
    return axios.post(`${USER_MODULE}/login`, null, {params: loginInfo})
        .then(res => {
            return res
        })
}

// 用户注册
export const userRegister = (registerInfo: RegisterInfo) => {
    return axios.post(`${USER_MODULE}/register`, registerInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 获取用户信息
export const userInfo = () => {
    return axios.get(`${USER_MODULE}`)
        .then(res => {
            return res
        })
}

// 更新用户信息
export const userInfoUpdate = (updateInfo: UpdateInfo) => {
    return axios.post(`${USER_MODULE}`, updateInfo, {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

//获取用户购物车
export const getUserCart = (userId: number) => {
    return axios.get(`${USER_MODULE}/${userId}/cart`)
        .then(res => {
            return res
        })
}

export const getUserCartVO = (userId: number) => {
    return axios.get(`${USER_MODULE}/${userId}`)
        .then(res => {
            return res
        })
}