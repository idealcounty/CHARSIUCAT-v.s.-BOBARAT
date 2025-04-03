package com.example.SBEAM.po;

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
    private Cart cart;

    @Column(name = "product_id",nullable = false)
    private Integer productId;

    @Column(name = "product_quantity",nullable = false)
    private Integer productQuantity;

    @Column(name = "product_price",nullable = false)
    private Double productPrice;
}
