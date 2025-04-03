package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.po.CartItem;
import com.example.SBEAM.repository.CartItemRepository;
import com.example.SBEAM.service.CartItemService;
import com.example.SBEAM.vo.CartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public Boolean createCartItem(CartItemVO cartItemVO) {
        CartItem newCartItem = cartItemVO.toPO();
        cartItemRepository.save(newCartItem);
        return true;
    }
}
