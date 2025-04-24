package com.example.SBEAM.service;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.vo.OrdersVO;
import java.util.List;

public interface OrdersService {
    Boolean createOrders(OrdersVO ordersVO);
    List<OrdersVO> getAllOrders();
    OrdersVO getOrdersById(int orderId);
    Boolean deleteOrdersByOrderId(int orderId);
    OrderStatus getOrderStatusById(int orderId);
}
