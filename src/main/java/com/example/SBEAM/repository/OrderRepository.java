package com.example.SBEAM.repository;

import com.example.SBEAM.po.Order;


import java.util.List;

public interface OrderRepository {
    List<Order> findAll();
    Order findByOrderId(Integer OrderId);
}
