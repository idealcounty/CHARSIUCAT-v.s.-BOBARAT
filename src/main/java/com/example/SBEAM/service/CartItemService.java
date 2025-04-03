package com.example.SBEAM.service;

import com.example.SBEAM.po.CartItem;
import com.example.SBEAM.vo.CartItemVO;

public interface CartItemService {
    Boolean createCartItem(CartItemVO cartItemVO);
}
