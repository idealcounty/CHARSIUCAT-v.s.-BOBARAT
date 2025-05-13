package com.example.SBEAM.vo;

import com.example.SBEAM.po.Inventory;
import com.example.SBEAM.po.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryVO {
    private Integer cartItemId;
    private User user;
    private Integer productId;
    private Integer productQuantity;
    private Double productPrice;
    public Inventory toPO(){
        Inventory inventory = new Inventory();
        inventory.setCartItemId(cartItemId);
        inventory.setUser(user);
        inventory.setProductId(productId);
        inventory.setProductQuantity(productQuantity);
        inventory.setProductPrice(productPrice);
        return inventory;
    }
}
