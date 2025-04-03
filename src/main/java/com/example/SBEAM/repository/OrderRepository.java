package com.example.SBEAM.repository;

import com.example.SBEAM.po.Order;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAll();
    Order findByOrderId(Integer OrderId);
}
