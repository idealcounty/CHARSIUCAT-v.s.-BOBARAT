import {axios} from '../utils/request'
import {ADVERTISEMENT_MODULE} from './_prefix'

export type AdvertisementInfo = {
    advertisementId: number,
    advertisementTitle: string,
    advertisementContent: string,
    advertisementImageUrl: string,
    productId: number,
}

// 创建广告
export const createAdvertisement = (advertisementInfo: AdvertisementInfo) => {
    return axios.post(`${ADVERTISEMENT_MODULE}`, advertisementInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 获取所有广告
export const getAllAdvertisements = () => {
    return axios.get(`${ADVERTISEMENT_MODULE}`)
        .then(res => {
            return res
        })
}

// 根据商品Id获取商品信息
export const getAdvertisementByAdvertisementId = (advertisementId: number) => {
    return axios.get(`${ADVERTISEMENT_MODULE}/${advertisementId}`)
        .then(res => {
            return res
        })
}

// 根据广告Id更新广告信息
export const updateAdvertisementByAdvertisementId = (AdvertisementId: number, advertisementInfo: AdvertisementInfo) => {
    return axios.put(`${ADVERTISEMENT_MODULE}/${AdvertisementId}`, advertisementInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

//删除广告
export const deleteAdvertisementById = (AdvertisementId: number) => {
    return axios.delete(`${ADVERTISEMENT_MODULE}`, {
        data: AdvertisementId,
        headers: {'Content-Type': 'application/json'}
    })
        .then(res => {
            return res
        })
}