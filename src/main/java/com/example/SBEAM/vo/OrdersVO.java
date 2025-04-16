package com.example.SBEAM.vo;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.po.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrdersVO {
    private Integer ordersId;
    private Integer userId;
    private Double totalPrice;
    private String payMethod;
    private OrderStatus orderStatus;
    private Date createTime;
    private String outTradeNo;

    public Orders toPO(){
        Orders orders = new Orders();
        orders.setOrdersId(ordersId);
        orders.setUserId(userId);
        orders.setTotalPrice(totalPrice);
        orders.setPayMethod(payMethod);
        orders.setOrderStatus(orderStatus);
        orders.setCreateTime(createTime);
        orders.setOutTradeNo(outTradeNo);
        return orders;
    }
}
