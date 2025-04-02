package com.example.SBEAM.service;
import com.example.SBEAM.vo.OrderVO;
import java.util.List;

public interface OrderService {
    Boolean createOrder(OrderVO order);
    List<OrderVO> getAllOrders();
    OrderVO getOrderById(int orderId);
}
