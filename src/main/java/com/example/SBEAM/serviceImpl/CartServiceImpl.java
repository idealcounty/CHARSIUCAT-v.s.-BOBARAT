package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.Cart;
import com.example.SBEAM.po.Product;
import com.example.SBEAM.repository.CartRepository;
import com.example.SBEAM.service.CartService;
import com.example.SBEAM.vo.CartVO;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Override
    public Boolean createCart(CartVO cartVO){
        Cart cart = cartRepository.findByCartId(cartVO.getCartId());
        Cart newCart = cartVO.toPO();
        cartRepository.save(newCart);
        return true;
    }
}
