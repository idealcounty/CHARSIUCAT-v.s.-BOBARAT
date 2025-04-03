package com.example.SBEAM.vo;
import com.example.SBEAM.po.CartItem;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@NoArgsConstructor
public class CartItemVO {
    private Integer cartItemId;
    private Integer productId;
    private Integer productQuantity;
    private Double productPrice;
    public CartItem toPO(){
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(cartItemId);
        cartItem.setProductId(productId);
        cartItem.setProductQuantity(productQuantity);
        cartItem.setProductPrice(productPrice);
        return cartItem;
    }
}
