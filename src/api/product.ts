import { axios } from '../utils/request'
import { PRODUCT_MODULE } from './_prefix'

type ProductInfo = {
    storeId: number,
    name: string,
    category: string,
    price: number,
    photoUrlList: string[]
}

// 创建商品
export const createProduct = (productInfo: ProductInfo) => {
    return axios.post(`${PRODUCT_MODULE}/`, productInfo,
        { headers: { 'Content-Type': 'application/json' } })
        .then(res => {
            return res
        })
}

// 获取商店下所有商品
export const getProductsByStoreId = (storeId: number) => {
    return axios.get(`${PRODUCT_MODULE}/?storeId=${storeId}`)
        .then(res => {
            return res
        })
}

// 根据商品Id获取商品信息
export const getProductById = (productId: number) => {
    return axios.get(`${PRODUCT_MODULE}/${productId}`)
        .then(res => {
            return res
        })
}

