package com.example.SBEAM.vo;

import com.example.SBEAM.po.Cart;
import com.example.SBEAM.po.CartItem;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartVO {
    private Integer cartId;
    private Integer userId;
    private List<CartItem> cartItems;
    public Cart toPO(){
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setUserId(userId);
        cart.setCartItems(cartItems);
        return cart;
    }
}
