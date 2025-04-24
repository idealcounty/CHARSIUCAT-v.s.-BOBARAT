import { axios } from '../utils/request'
import { USER_MODULE, ALIPAY_MODULE } from './_prefix'
import { Cart } from './product'

export const createOrder = (userId: number, cartVO: Cart) => {
    return axios.put(`${USER_MODULE}/${userId}/cart`, cartVO, {
        headers: { 'Content-Type': 'application/json' }
    }).then(res => {
        return res
    })
}

export const goToPayment = (orderId: number) => {
    return axios.get(`${ALIPAY_MODULE}/pay`, {
        params: { orderId },
        headers: { 'Content-Type': 'application/json' }
    }).then(res => {
        window.open(`http://localhost:8080${ALIPAY_MODULE}/pay?orderId=${orderId}`)
    })
}

