package com.example.SBEAM.po;
import com.example.SBEAM.vo.OrdersVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


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
    @Column(name = "product_id")
    private Integer productId;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "product_amount")
    private Double productAmount;

    @Basic
    @Column(name = "product_price")
    private Double productPrice;

    public OrdersVO toVO() {
        OrdersVO orderVO = new OrdersVO();
        orderVO.setOrdersId(ordersId);
        orderVO.setProductId(productId);
        orderVO.setUserId(userId);
        orderVO.setProductAmount(productAmount);
        orderVO.setProductPrice(productPrice);
        return orderVO;
    }
}
