package com.example.SBEAM.service;
import com.example.SBEAM.po.Comment;
import com.example.SBEAM.po.Product;
import com.example.SBEAM.vo.CommentVO;
import com.example.SBEAM.vo.ProductVO;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface ProductService {
    Boolean createProduct(ProductVO productVO);
    List<ProductVO> getAllProductsByStoreId(Integer storeId);
    List<ProductVO> getAllProducts();
    ProductVO getProductByProductId(Integer productId);
    List<ProductVO> searchProduct(String productName);
    Boolean deleteProduct(Integer productId);
    Boolean updateProduct(Integer productId,ProductVO productVO);
    Boolean updateProductAmount(Integer productId,Integer amount);
    Boolean updateProductDiscount(Integer productId,Double productDiscount);
    Boolean judgeOwnProduct(int productId,int userId);
    List<CommentVO> getCommentsByProductId(Product product);
    List<CommentVO> getGoodCommentsByProductId(Product product);
    List<CommentVO> getBadCommentsByProductId(Product product);
}