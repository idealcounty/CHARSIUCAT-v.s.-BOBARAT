package com.example.SBEAM.po;

import com.example.SBEAM.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lottery_orders")
public class LotteryOrder {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "lottery_order_id")
    private Integer lotteryOrderId;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "lottery_chances_count")
    private Integer lotteryChancesCount;

    @Basic
    @Column(name = "total_price")
    private Double totalPrice;

    @Basic
    @Column(name = "pay_method")
    private String payMethod;

    @Basic
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Basic
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    @Column(name = "out_trade_no")
    private String outTradeNo;

    public LotteryOrder(Integer userId, Integer lotteryChancesCount, Double totalPrice) {
        this.userId = userId;
        this.lotteryChancesCount = lotteryChancesCount;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.PENDING;
        this.createTime = new Date();
    }
}