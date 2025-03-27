import { axios } from '../utils/request'
import { STORE_MODULE } from './_prefix'

type StoreInfo = {
    name: string,
    location: string,
    logoUrl: string
}

// 创建商店
export const createStore = (storeInfo: StoreInfo) => {
    return axios.post(`${STORE_MODULE}/`, storeInfo,
        { headers: { 'Content-Type': 'application/json' } })
        .then(res => {
            return res
        })
}

// 获取全部商店
export const getAllStore = () => {
    return axios.get(`${STORE_MODULE}/all`,)
        .then(res => {
            return res
        })
}

// 根据商店Id获取指定商店
export const getStoreById = (storeId: number) => {
    return axios.get(`${STORE_MODULE}/${storeId}`)
        .then(res => {
            return res
        })
}
