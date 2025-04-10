package com.example.SBEAM.service;
import com.example.SBEAM.vo.ProductVO;
import java.util.List;

public interface ProductService {
    Boolean createProduct(ProductVO productVO);
    List<ProductVO> getAllProductsByStoreId(Integer storeId);
    List<ProductVO> getAllProducts();
    ProductVO getProductByProductId(Integer productId);
    Boolean deleteProduct(ProductVO productVO);
    Boolean updateProduct(Integer productId,ProductVO productVO);
    Boolean updateProductAmount(Integer productId,Integer amount);
    Boolean updateProductDiscount(Integer productId,Double productDiscount);
}