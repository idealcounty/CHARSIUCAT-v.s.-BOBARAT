package com.example.SBEAM.service;
import com.example.SBEAM.vo.ProductVO;
import java.util.List;

public interface ProductService {
    Boolean createProduct(ProductVO productVO);
    List<ProductVO> getAllProducts(Integer storeId);
}