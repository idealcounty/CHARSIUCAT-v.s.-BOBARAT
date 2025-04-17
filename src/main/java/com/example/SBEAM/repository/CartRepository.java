package com.example.SBEAM.repository;
import com.example.SBEAM.po.Cart;
import com.example.SBEAM.vo.CartVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByCartId(Integer cartId);

    Cart findByUserId(Integer userId);
}
