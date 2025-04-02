package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.po.Order;
import com.example.SBEAM.repository.OrderRepository;
import com.example.SBEAM.service.OrderService;
import com.example.SBEAM.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Boolean createOrder(OrderVO orderVO) {
        return true;
    }

    @Override
    public List<OrderVO> getAllOrders(){
        return orderRepository.findAll().stream().map(Order::toVO).collect(Collectors.toList());
    }

    @Override
    public OrderVO getOrderById(int orderId) {
        return orderRepository.findByOrderId(orderId).toVO();
    }
}
