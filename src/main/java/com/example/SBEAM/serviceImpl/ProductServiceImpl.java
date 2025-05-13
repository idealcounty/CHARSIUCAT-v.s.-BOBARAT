package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.*;
import com.example.SBEAM.repository.CommentRepository;
import com.example.SBEAM.repository.ProductRepository;
import com.example.SBEAM.repository.StoreRepository;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.service.ProductService;
import com.example.SBEAM.vo.CommentVO;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
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
    public List<ProductVO> searchProduct(String productName){
        List<Product> list = productRepository.findByProductNameContainingIgnoreCase(productName);
        return list.stream().map(Product::toVO).collect(Collectors.toList());
    }
    @Override
    public ProductVO getProductByProductId(Integer productId) {
        return productRepository.findByProductId(productId).toVO();
    }

    @Override
    public Boolean deleteProduct(Integer productId) {
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw SBEAMException.productNotExists();
        }
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
        product.setProductImages(productVO.getProductImages());
        product.setProductLogo(productVO.getProductLogo());
        product.setProductDiscount(productVO.getProductDiscount());
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

    @Override
    public Boolean updateProductDiscount(Integer productId,Double productDiscount){
                Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw SBEAMException.productNotExists();
        }
        product.setProductDiscount(productDiscount);
        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean judgeOwnProduct(int productId,int userId){
        User user =userRepository.findById(userId).get();
        for(Inventory inventory :user.getInventories()){
            if(inventory.getProductId().equals(productId)){
                return true;
            }
        }
        return false;
    }
    @Override
    public  List<CommentVO> getCommentsByProductId(Product product){
        return commentRepository.findCommentsByProduct(product).stream().map(Comment::toVO).collect(Collectors.toList());
    }
    @Override
    public List<CommentVO> getGoodCommentsByProductId(Product product){
        return commentRepository.findByProductAndCommentScoreGreaterThanEqual(product,4).stream().map(Comment::toVO).collect(Collectors.toList());
    }
    @Override
    public List<CommentVO> getBadCommentsByProductId(Product product){
        return commentRepository.findByProductAndCommentScoreLessThanEqual(product,2).stream().map(Comment::toVO).collect(Collectors.toList());
    }
}
