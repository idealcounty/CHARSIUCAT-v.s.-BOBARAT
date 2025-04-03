package com.example.SBEAM.service;
import com.example.SBEAM.vo.OrdersVO;
import java.util.List;

public interface OrdersService {
    Boolean createOrders(OrdersVO order);
    List<OrdersVO> getAllOrders();
    OrdersVO getOrdersById(int orderId);
}
