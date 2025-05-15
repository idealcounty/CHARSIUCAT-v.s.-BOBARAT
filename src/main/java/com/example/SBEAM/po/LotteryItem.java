package com.example.SBEAM.po;
import com.example.SBEAM.vo.LotteryItemVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lottery_item")
public class LotteryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lotteryItemId;

    @Column(name = "product_id",nullable = false)
    private Integer productId;

    @Column(name = "product_quantity",nullable = false)
    private Integer productQuantity;

    @Column(name = "lottery_item_probability",nullable = false)
    private Double lotteryItemProbability;

    @Column(name = "product_value")
    private Double productValue;
    @ManyToOne
    @JoinColumn(name = "lottery_id", nullable = false)
    @JsonIgnore
    private Lottery lottery;

    public LotteryItem(Integer productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
    public LotteryItemVO toVO(){
        LotteryItemVO lotteryItemVO = new LotteryItemVO();
        lotteryItemVO.setLotteryItemId(lotteryItemId);
        lotteryItemVO.setProductId(productId);
        lotteryItemVO.setProductQuantity(productQuantity);
        lotteryItemVO.setLottery(lottery);
        lotteryItemVO.setLotteryItemProbability(lotteryItemProbability);
        lotteryItemVO.setProductValue(productValue);
        return lotteryItemVO;
    }
}
