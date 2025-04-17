package com.example.SBEAM.po;

import com.example.SBEAM.vo.CartItemVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @Column(name = "product_id",nullable = false)
    private Integer productId;

    @Column(name = "product_quantity",nullable = false)
    private Integer productQuantity;

    @Column(name = "product_price",nullable = false)
    private Double productPrice;

    public CartItem(Cart cart, Integer productId, Integer quantity, Double price) {
        this.cart = cart;
        this.productId = productId;
        this.productQuantity = quantity;
        this.productPrice = price;
    }

    public CartItemVO toVO(){
        CartItemVO cartItemVO = new CartItemVO();
        cartItemVO.setCartItemId(cartItemId);
        cartItemVO.setProductId(productId);
        cartItemVO.setProductQuantity(productQuantity);
        cartItemVO.setProductPrice(productPrice);
        return cartItemVO;
    }
}
