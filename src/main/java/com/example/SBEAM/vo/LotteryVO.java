package com.example.SBEAM.vo;

import com.example.SBEAM.po.Lottery;
import com.example.SBEAM.po.LotteryItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LotteryVO {
    private Integer LotteryId;
    private List<LotteryItem> lotteryItems;
    public Lottery toPO() {
        Lottery lottery = new Lottery();
        lottery.setLotteryId(LotteryId);
        lottery.setLotteryItems(lotteryItems);
        return lottery;
    }
}
