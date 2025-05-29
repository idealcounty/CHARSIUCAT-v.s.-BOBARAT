import { axios } from '../utils/request'
import { PRODUCT_MODULE } from './_prefix'
import { CartItem } from './user'

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

export type Cart = {
    cartId: number,
    userId: number,
    cartItems: CartItem[],
}

// 创建商品
export const createProduct = (productInfo: ProductInfo) => {
    return axios.post(`${PRODUCT_MODULE}/createProduct`, productInfo,
        { headers: { 'Content-Type': 'application/json' } })
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
    return axios.get(`${PRODUCT_MODULE}/search/${productId}`)
        .then(res => {
            return res
        })
}

// 搜索商品
export const searchProducts = (productName: string) => {
    return axios.get(`${PRODUCT_MODULE}/search`, {
        params: { productName }
    })
        .then(res => {
            return res
        })
}

// 根据商品Id更新商品信息
export const updateProductByProductId = (productId: number, productInfo: ProductInfo) => {
    return axios.put(`${PRODUCT_MODULE}/productDetail/${productId}`, productInfo,
        { headers: { 'Content-Type': 'application/json' } })
        .then(res => {
            return res
        })
}

export const deleteProductById = (productId: number) => {
    return axios.delete(`${PRODUCT_MODULE}/deleteProduct/${productId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res => {
            return res
        })
}

export const updateCart = (productId: number, numberOfProduct: number, cartVO: Cart) => {
    return axios.put(
        `${PRODUCT_MODULE}/productDetail/${productId}/cart?numberOfProduct=${numberOfProduct}`,
        cartVO,
        {
            headers: { 'Content-Type': 'application/json' }
        }
    );
};

export const productExists = (productId: number, cartId: number) => {
    return axios.post(`${PRODUCT_MODULE}/productDetail/${productId}/${cartId}`)
        .then(res => {
            return res
        })
}

// 扣除商品库存
export const deductProductStock = (productId: number, quantity: number) => {
    return axios.put(`${PRODUCT_MODULE}/deductStock/${productId}?quantity=${quantity}`)
        .then(res => {
            return res
        })
}

// 增加商品库存
export const addProductStock = (productId: number, quantity: number) => {
    return axios.put(`${PRODUCT_MODULE}/addStock/${productId}?quantity=${quantity}`)
        .then(res => {
            return res
        })
}

// 检查商品库存是否足够
export const checkProductStock = (productId: number, quantity: number) => {
    return axios.get(`${PRODUCT_MODULE}/checkStock/${productId}?quantity=${quantity}`)
        .then(res => {
            return res
        })
}

