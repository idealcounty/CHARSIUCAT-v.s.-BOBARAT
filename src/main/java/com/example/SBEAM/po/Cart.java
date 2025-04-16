package com.example.SBEAM.po;
import com.example.SBEAM.vo.CartVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @Column(nullable = false)
    private Integer userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name="cart_item")
    private List<CartItem> cartItems;

    public Cart(Integer userId) {
        this.userId = userId;
        this.cartItems=new ArrayList<CartItem>();
    }
    public CartVO toVO(){
        CartVO cartVO = new CartVO();
        cartVO.setCartId(this.getCartId());
        cartVO.setUserId(this.getUserId());
        cartVO.setCartItems(this.getCartItems());

        return cartVO;
    }
}
