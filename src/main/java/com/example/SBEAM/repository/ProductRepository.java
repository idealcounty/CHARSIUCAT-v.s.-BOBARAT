package com.example.SBEAM.repository;

import com.example.SBEAM.po.Product;
import com.example.SBEAM.vo.ProductVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByProductId(Integer productId);
    List<Product> findAllByStoreId(Integer storeId);
    Product findByProductName(String productName);

}
