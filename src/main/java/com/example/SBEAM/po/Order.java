package com.example.SBEAM.po;
import com.example.SBEAM.vo.OrderVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private Integer orderId;

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

    public OrderVO toVO() {
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setProductId(productId);
        orderVO.setUserId(userId);
        orderVO.setProductAmount(productAmount);
        orderVO.setProductPrice(productPrice);
        return orderVO;
    }
}
