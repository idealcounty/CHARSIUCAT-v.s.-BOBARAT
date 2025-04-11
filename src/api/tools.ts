import { axios } from '../utils/request'
import {API_MODULE, TOOL_MODULE} from './_prefix'

// 上传图片文件
export const uploadImage = (payload: FormData) => {
    return axios.post(`${TOOL_MODULE}/images`, payload, {
        headers: {
            'Content-Type': "multipart/form-data;"
        }
    })
        .then(res => {
            return res
        })
}
