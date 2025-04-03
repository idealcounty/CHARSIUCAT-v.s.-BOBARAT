package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.po.Orders;
import com.example.SBEAM.repository.OrdersRepository;
import com.example.SBEAM.service.OrdersService;
import com.example.SBEAM.vo.OrdersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Boolean createOrders(OrdersVO ordersVO) {
        return true;
    }

    @Override
    public List<OrdersVO> getAllOrders(){
        return ordersRepository.findAll().stream().map(Orders::toVO).collect(Collectors.toList());
    }

    @Override
    public OrdersVO getOrdersById(int orderId) {
        return ordersRepository.findByOrdersId(orderId).toVO();
    }
}
