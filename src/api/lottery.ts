import { axios } from '../utils/request'
import { LOTTERY_MODULE } from './_prefix'

// 创建奖池物品
export const createLotteryItem = (productId: number, productQuantity: number) => {
    return axios.post(`${LOTTERY_MODULE}/createLotteryItem`, null, {
        params: {
            productId,
            productQuantity
        }
    }).then(res => {
        return res
    })
}

// 创建奖池 - 修改为接收奖池名称
export const createLottery = (lotteryName: string) => {
    return axios.post(`${LOTTERY_MODULE}`, null, {
        params: {
            lotteryName
        }
    }).then(res => {
        return res
    })
}

// 添加奖池物品 - 新增接口
export const addLotteryItem = (lotteryId: number, lotteryItemVO: any) => {
    return axios.post(`${LOTTERY_MODULE}/${lotteryId}`, lotteryItemVO, {
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        return res
    })
}

// 获取特定奖池的物品列表
export const getLotteryItemList = (lotteryId: number) => {
    return axios.get(`${LOTTERY_MODULE}/${lotteryId}`)
        .then(res => {
            return res
        })
}

// 抽奖
export const drawLottery = (lotteryId: number, userId: number) => {
    return axios.get(`${LOTTERY_MODULE}/${lotteryId}/draw`, {
        params: {
            userId
        }
    }).then(res => {
        return res
    })
}

// 获取所有奖池列表
export const getLotteryList = () => {
    return axios.get(`${LOTTERY_MODULE}`)
        .then(res => {
            return res
        })
}

// 删除奖池
export const deleteLottery = (lotteryId: number) => {
    return axios.delete(`${LOTTERY_MODULE}/${lotteryId}`)
        .then(res => {
            return res
        })
}

// 删除奖品
export const deleteLotteryItem = (lotteryItemId: number) => {
    return axios.delete(`${LOTTERY_MODULE}/item/${lotteryItemId}`)
        .then(res => {
            return res
        })
}

// 从奖池中移除奖品
export const removeLotteryItemFromLottery = (lotteryId: number, lotteryItemId: number) => {
    return axios.delete(`${LOTTERY_MODULE}/${lotteryId}/item/${lotteryItemId}`)
        .then(res => {
            return res
        })
}

// 更新奖池名称
export const updateLotteryName = (lotteryId: number, newName: string) => {
    return axios.put(`${LOTTERY_MODULE}/${lotteryId}/name`, null, {
        params: {
            newName
        }
    }).then(res => {
        return res
    })
} 