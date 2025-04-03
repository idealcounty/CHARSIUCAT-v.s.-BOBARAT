package com.example.SBEAM.repository;

import com.example.SBEAM.po.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
