package com.example.SBEAM.po;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.enums.RoleEnum;
import com.example.SBEAM.vo.OrdersVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orders_id")
    private Integer ordersId;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

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

    public Orders(int userId, Double totalPrice){
        this.userId = userId;
        this.totalPrice = totalPrice;

    }
    public OrdersVO toVO() {
        OrdersVO orderVO = new OrdersVO();
        orderVO.setOrdersId(ordersId);
        orderVO.setUserId(userId);
        orderVO.setTotalPrice(totalPrice);
        orderVO.setPayMethod(payMethod);
        orderVO.setOrderStatus(orderStatus);
        orderVO.setCreateTime(createTime);
        orderVO.setOutTradeNo(outTradeNo);
        return orderVO;
    }
}
