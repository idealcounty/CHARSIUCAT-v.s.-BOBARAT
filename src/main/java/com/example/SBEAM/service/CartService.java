package com.example.SBEAM.service;
import com.example.SBEAM.vo.CartItemVO;
import com.example.SBEAM.vo.CartVO;
import com.example.SBEAM.vo.OrdersVO;

import java.util.List;
public interface CartService {
    Boolean createCart(CartVO cartVO);
    CartVO getCart(int userId);
    Boolean judgeCartItemExist(Integer productId,Integer cartId);
    Boolean updateCart(int productId,int numberOfProduct,CartVO cartVO);
    OrdersVO settleAccount(int userId, CartVO cartVO);
    List<CartItemVO> showAllCartItems(int userId);
    int getCartItemCount(int cartId);
}
