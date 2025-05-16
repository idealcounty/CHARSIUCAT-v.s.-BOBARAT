import { axios } from '../utils/request'
import {COMMENT_MODULE} from './_prefix'
import {ProductInfo} from "./product.ts";

export type commentInfo = {
    product: ProductInfo,
    commentText: string,
    commentScore: number,
}

export const makeComment = (commentinfo:commentInfo) =>  {
    console.log(commentinfo)
    return axios.post(`${COMMENT_MODULE}`, commentinfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}