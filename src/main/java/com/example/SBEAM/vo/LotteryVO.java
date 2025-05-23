package com.example.SBEAM.vo;

import com.example.SBEAM.po.Lottery;
import com.example.SBEAM.po.LotteryItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LotteryVO {
    private Integer lotteryId;
    private List<LotteryItem> lotteryItems;
    private String lotteryName;
    public Lottery toPO() {
        Lottery lottery = new Lottery();
        lottery.setLotteryId(lotteryId);
        lottery.setLotteryItems(lotteryItems);
        lottery.setLotteryName(lotteryName);
        return lottery;
    }
}
