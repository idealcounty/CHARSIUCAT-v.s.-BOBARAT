package com.example.SBEAM.po;

import com.example.SBEAM.vo.InventoryVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "product_id",nullable = false)
    private Integer productId;

    @Column(name = "product_quantity",nullable = false)
    private Integer productQuantity;

    @Column(name = "product_price",nullable = false)
    private Double productPrice;

    public Inventory(User user, Integer productId, Integer quantity, Double price) {
        this.user = user;
        this.productId = productId;
        this.productQuantity = quantity;
        this.productPrice = price;
    }
    public InventoryVO toVO(){
        InventoryVO inventoryVO = new InventoryVO();
        inventoryVO.setCartItemId(this.cartItemId);
        inventoryVO.setProductId(this.productId);
        inventoryVO.setProductQuantity(this.productQuantity);
        inventoryVO.setProductPrice(this.productPrice);
        return inventoryVO;
    }
}
