package com.example.SBEAM.vo;
import com.example.SBEAM.po.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@NoArgsConstructor
public class OrderVO {
    private Integer orderId;
    private Integer productId;
    private Integer userId;
    private Double productAmount;
    private Double productPrice;

    public Order toPO(){
        Order order = new Order();
        order.setOrderId(orderId);
        order.setProductId(productId);
        order.setUserId(userId);
        order.setProductAmount(productAmount);
        order.setProductPrice(productPrice);
        return order;
    }
}
