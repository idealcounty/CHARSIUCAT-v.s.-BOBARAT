package com.example.SBEAM.service;

import com.example.SBEAM.vo.LotteryItemVO;
import com.example.SBEAM.vo.LotteryVO;
import java.util.List;

public interface LotteryService {
    Boolean createLottery(String lotteryName);
    Boolean addLotteryItem(int lotteryId,LotteryItemVO lotteryItemVO);
    List<LotteryItemVO> getLotteryItemList(int lotteryId);
    LotteryItemVO drawLottery(int lotteryId,int userId);
    List<LotteryVO> getLottery();
}
