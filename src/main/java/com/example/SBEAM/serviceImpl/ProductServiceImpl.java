package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.Product;
import com.example.SBEAM.po.Store;
import com.example.SBEAM.repository.ProductRepository;
import com.example.SBEAM.repository.StoreRepository;
import com.example.SBEAM.service.ProductService;
import com.example.SBEAM.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Override
    public Boolean createProduct(ProductVO productVO) {
        Product product = productRepository.findByProductId(productVO.getProductId());
        if (product != null) {
            throw SBEAMException.productNameAlreadyExists();
        }
        Product newProduct = productVO.toPO();
        productRepository.save(newProduct);
        return true;
    }
    @Override
    public List<ProductVO> getAllProductsByStoreId(Integer storeId) {
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            throw SBEAMException.storeNotExists();
        }
        return productRepository.findAllByStoreId(storeId).stream().map(Product::toVO).collect(Collectors.toList());
    }

    @Override
    public List<ProductVO> getAllProducts(){
        return productRepository.findAll().stream().map(Product::toVO).collect(Collectors.toList());
    }
}
