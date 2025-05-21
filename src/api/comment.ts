import { axios } from '../utils/request'
import {COMMENT_MODULE, PRODUCT_MODULE} from './_prefix'
import {ProductInfo} from "./product.ts";

export type commentInfo = {
    product: ProductInfo,
    commentText: string,
    commentScore: boolean,
    userId: number,
    commentSendTime: Date,
}

export const makeComment = (commentinfo:commentInfo) =>  {
    console.log(commentinfo)
    return axios.post(`${COMMENT_MODULE}`, commentinfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

export const getGoodCommentsByProductId = (productId:number) => {
    return axios.get(`${PRODUCT_MODULE}/productDetail/${productId}/comment/good`)
        .then(res => {
            console.log(res.data.result)
            return res
        })
}

export const getBadCommentsByProductId = (productId:number) => {
    return axios.get(`${PRODUCT_MODULE}/productDetail/${productId}/comment/bad`)
        .then(res => {
            console.log(res.data.result)
            return res
        })
}

export const getCommentsByProductId = (productId:number) => {
    return axios.get(`${PRODUCT_MODULE}/productDetail/${productId}/comment`)
        .then(res => {
            console.log(res.data.result)
            return res
        })
}