package com.example.SBEAM.service;

import com.example.SBEAM.vo.LotteryItemVO;
import com.example.SBEAM.vo.LotteryVO;
import java.util.List;

public interface LotteryService {
    LotteryVO createLottery(String lotteryName);

    LotteryItemVO createLotteryItem(int productId, int productQuantity);

    Boolean addLotteryItem(int lotteryId, LotteryItemVO lotteryItemVO);

    Boolean deleteLottery(int lotteryId);

    Boolean deleteLotteryItem(int lotteryItemId);

    Boolean removeLotteryItemFromLottery(int lotteryId, int lotteryItemId);

    Boolean updateLotteryName(int lotteryId, String newName);

    List<LotteryItemVO> getLotteryItemList(int lotteryId);

    LotteryItemVO drawLottery(int lotteryId, int userId);

    List<LotteryVO> getLottery();
}
