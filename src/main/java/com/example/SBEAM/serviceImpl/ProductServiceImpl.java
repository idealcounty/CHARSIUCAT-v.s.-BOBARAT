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

    @Override
    public ProductVO getProductByProductId(Integer productId) {
        return productRepository.findByProductId(productId).toVO();
    }

    @Override
    public Boolean deleteProduct(ProductVO productVO) {
        Product product = productRepository.findByProductId(productVO.getProductId());
        if (product == null) {
            throw SBEAMException.productNotExists();
        }
        Integer productId = product.getProductId();
        if (!productRepository.existsById(productId)) {
            return false;
        }
        productRepository.deleteById(productId);
        return true;
    }

    @Override
    public Boolean updateProduct(Integer productId,ProductVO productVO){
        Product newProduct = productRepository.findByProductId(productId);
        if (newProduct == null) {
            throw SBEAMException.productNotExists();
        }
        Product product = productRepository.findByProductId(productId);
        product.setProductName(productVO.getProductName());
        product.setProductAmount(productVO.getProductAmount());
        product.setProductPrice(productVO.getProductPrice());
        product.setProductDescription(productVO.getProductDescription());
        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean updateProductAmount(Integer productId,Integer amount){
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw SBEAMException.productNotExists();
        }
        product.setProductAmount(amount);
        productRepository.save(product);
        return true;
    }
}
