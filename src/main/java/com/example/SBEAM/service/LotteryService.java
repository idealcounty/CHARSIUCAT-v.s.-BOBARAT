package com.example.SBEAM.service;

import com.example.SBEAM.vo.LotteryItemVO;

import java.util.List;

public interface LotteryService {
    Boolean createLottery(List<LotteryItemVO> lotteryItemVOList);
    LotteryItemVO createLotteryItem(int productId,int productAmount);
    List<LotteryItemVO> getLotteryItemList(int lotteryId);
    LotteryItemVO drawLottery(int lotteryId,int userId);
}
