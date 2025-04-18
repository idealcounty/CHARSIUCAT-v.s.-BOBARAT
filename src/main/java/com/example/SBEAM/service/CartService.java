package com.example.SBEAM.service;
import com.example.SBEAM.po.Orders;
import com.example.SBEAM.vo.CartItemVO;
import com.example.SBEAM.vo.CartVO;
import java.util.List;
public interface CartService {
    Boolean createCart(CartVO cartVO);
    CartVO getCart(int userId);
    Boolean judgeCartItemExist(Integer productId,CartVO cartVO);
    Boolean updateCart(int productId,int numberOfProduct,CartVO cartVO);
    Orders settleAccount(int userId, CartVO cartVO);
    List<CartItemVO> showAllCartItems(int userId);
}
