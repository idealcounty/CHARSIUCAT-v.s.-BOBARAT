package com.example.SBEAM.vo;

import com.example.SBEAM.po.Lottery;
import com.example.SBEAM.po.LotteryItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotteryItemVO {
    private Integer LotteryItemId;
    private Integer productId;
    private Integer productQuantity;
    private Lottery lottery;
    private Double lotteryItemProbability;
    private Double productValue;
    public LotteryItem toPO(){
        LotteryItem lotteryItem = new LotteryItem();
        lotteryItem.setLotteryItemId(LotteryItemId);
        lotteryItem.setProductId(productId);
        lotteryItem.setProductQuantity(productQuantity);
        lotteryItem.setLottery(lottery);
        lotteryItem.setLotteryItemProbability(lotteryItemProbability);
        lotteryItem.setProductValue(productValue);
        return lotteryItem;
    }
}
