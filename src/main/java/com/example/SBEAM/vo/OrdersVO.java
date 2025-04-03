package com.example.SBEAM.vo;
import com.example.SBEAM.po.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdersVO {
    private Integer ordersId;
    private Integer productId;
    private Integer userId;
    private Double productAmount;
    private Double productPrice;

    public Orders toPO(){
        Orders orders = new Orders();
        orders.setOrdersId(ordersId);
        orders.setProductId(productId);
        orders.setUserId(userId);
        orders.setProductAmount(productAmount);
        orders.setProductPrice(productPrice);
        return orders;
    }
}
