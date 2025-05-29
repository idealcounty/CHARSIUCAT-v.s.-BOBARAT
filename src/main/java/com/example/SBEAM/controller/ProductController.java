package com.example.SBEAM.controller;

import com.example.SBEAM.po.Product;
import com.example.SBEAM.repository.ProductRepository;
import com.example.SBEAM.service.ProductService;
import com.example.SBEAM.vo.CommentVO;
import com.example.SBEAM.vo.ResultVO;
import com.example.SBEAM.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/product/createProduct")
    public ResultVO<Boolean> createProduct(@RequestBody ProductVO productVO) {
        return ResultVO.buildSuccess(productService.createProduct(productVO));
    }

    @GetMapping("/store/storeDetail")
    public ResultVO<List<ProductVO>> getAllProductsByStoreId(@RequestParam("storeId") Integer storeId) {
        return ResultVO.buildSuccess(productService.getAllProductsByStoreId(storeId));
    }

    @DeleteMapping("/product/deleteProduct/{productId}")
    public ResultVO<Boolean> deleteProduct(@PathVariable int productId) {
        boolean success = productService.deleteProduct(productId);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("商品不存在，删除失败");
        }
    }

    @GetMapping("/product")
    public ResultVO<List<ProductVO>> getAllProducts() {
        return ResultVO.buildSuccess(productService.getAllProducts());
    }

    @GetMapping("/product/search/{productId}")
    public ResultVO<ProductVO> getProductByProductId(@PathVariable int productId) {
        return ResultVO.buildSuccess(productService.getProductByProductId(productId));
    }

    @GetMapping("/product/search")
    public ResultVO<List<ProductVO>> searchProduct(@RequestParam("productName") String productName) {
        return ResultVO.buildSuccess(productService.searchProduct(productName));
    }

    @PutMapping("/product/productDetail/{productId}")
    public ResultVO<Boolean> updateProduct(@PathVariable int productId, @RequestBody ProductVO productVO) {
        boolean success = productService.updateProduct(productId, productVO);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("商品不存在，更新失败");
        }
    }

    @GetMapping("/product/productDetail/{productId}")
    public ResultVO<Boolean> updateProductAmount(@PathVariable int productId, @RequestBody Integer productAmount) {
        return ResultVO.buildSuccess(productService.updateProductAmount(productId, productAmount));
    }

    @PatchMapping("/product/productDetail/{productId}")
    public ResultVO<Boolean> updateProductDiscount(@PathVariable int productId, @RequestBody Double productDiscount) {
        return ResultVO.buildSuccess(productService.updateProductDiscount(productId, productDiscount));
    }

    @PutMapping("/product/productDetail/{productId}/comment")
    public ResultVO<Boolean> judgeOwnProduct(@PathVariable int productId, @RequestParam int userId) {
        return ResultVO.buildSuccess(productService.judgeOwnProduct(productId, userId));
    }

    @GetMapping("/product/productDetail/{productId}/comment")
    public ResultVO<List<CommentVO>> getProductComments(@PathVariable int productId) {
        Product product = productRepository.findByProductId(productId);
        return ResultVO.buildSuccess(productService.getCommentsByProductId(product));
    }

    @GetMapping("/product/productDetail/{productId}/comment/good")
    public ResultVO<List<CommentVO>> getProductGoodComments(@PathVariable int productId) {
        Product product = productRepository.findByProductId(productId);
        return ResultVO.buildSuccess(productService.getGoodCommentsByProductId(product));
    }

    @GetMapping("/product/productDetail/{productId}/comment/bad")
    public ResultVO<List<CommentVO>> getProductBadComments(@PathVariable int productId) {
        Product product = productRepository.findByProductId(productId);
        return ResultVO.buildSuccess(productService.getBadCommentsByProductId(product));
    }

    // 扣除商品库存
    @PutMapping("/product/deductStock/{productId}")
    public ResultVO<Boolean> deductProductStock(@PathVariable int productId, @RequestParam int quantity) {
        boolean success = productService.deductProductStock(productId, quantity);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("库存不足或商品不存在");
        }
    }

    // 增加商品库存
    @PutMapping("/product/addStock/{productId}")
    public ResultVO<Boolean> addProductStock(@PathVariable int productId, @RequestParam int quantity) {
        boolean success = productService.addProductStock(productId, quantity);
        if (success) {
            return ResultVO.buildSuccess(true);
        } else {
            return ResultVO.buildFailure("商品不存在");
        }
    }

    // 检查商品库存
    @GetMapping("/product/checkStock/{productId}")
    public ResultVO<Integer> checkProductStock(@PathVariable int productId, @RequestParam int quantity) {
        Integer currentStock = productService.checkProductStock(productId, quantity);
        if (currentStock != null && currentStock >= quantity) {
            return ResultVO.buildSuccess(currentStock);
        } else {
            return ResultVO.buildFailure("库存不足");
        }
    }
}
