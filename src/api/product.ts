import {axios} from '../utils/request'
import {PRODUCT_MODULE} from './_prefix'

export type ProductInfo = {
    productId: number,
    productName: string,
    productAmount: number,
    productPrice: number,
    productDiscount: number,
    productDescription: string,
    productLogo: string,
    productImages: string[],
}

// 创建商品
export const createProduct = (productInfo: ProductInfo) => {
    return axios.post(`${PRODUCT_MODULE}/createProduct`, productInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 获取所有商品
export const getAllProducts = () => {
    return axios.get(`${PRODUCT_MODULE}`)
        .then(res => {
            return res
        })
}

// 根据商品Id获取商品信息
export const getProductByProductId = (productId: number) => {
    return axios.get(`${PRODUCT_MODULE}/${productId}`)
        .then(res => {
            return res
        })
}

export const deleteProductById = (productId: number) => {
    return axios.delete(`${PRODUCT_MODULE}/deleteProduct`, {
        data: productId,
        headers: {'Content-Type': 'application/json'}
    })
        .then(res => {
            return res
        })
}



