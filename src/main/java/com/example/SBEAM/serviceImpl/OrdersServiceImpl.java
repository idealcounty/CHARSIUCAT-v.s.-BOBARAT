package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.Orders;
import com.example.SBEAM.repository.OrdersRepository;
import com.example.SBEAM.service.OrdersService;
import com.example.SBEAM.vo.OrdersVO;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Override
    public Boolean createOrders(OrdersVO ordersVO){
        Orders orders = ordersRepository.findByOrdersId(ordersVO.getOrdersId());
        if(orders!=null){
            throw SBEAMException.orderAlreadyExists();
        }
        Orders newOrders = ordersVO.toPO();
        ordersRepository.save(newOrders);
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

    @Override
    public Boolean deleteOrdersByOrderId(int orderId){
        Orders orders = ordersRepository.findByOrdersId(orderId);
        if(orders==null){
            throw SBEAMException.orderNotExists();
        }
        if(!ordersRepository.existsById(orderId)){
            return false;
        }
        ordersRepository.delete(orders);
        return true;
    }
    @Override
    public OrderStatus getOrderStatusById(int orderId){
        Orders orders = ordersRepository.findByOrdersId(orderId);
        if(orders==null){
            throw SBEAMException.orderNotExists();
        }
        return orders.getOrderStatus();
    }
}
