package com.example.SBEAM.controller;

import com.example.SBEAM.service.ProductService;
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
        @PostMapping("/product/createProduct")
        public ResultVO<Boolean> createProduct(@RequestBody ProductVO productVO) {
            return ResultVO.buildSuccess(productService.createProduct(productVO));
        }
        @GetMapping("/store/storeDetail")
        public ResultVO<List<ProductVO>> getAllProductsByStoreId(@RequestParam("storeId") Integer storeId) {
                return ResultVO.buildSuccess(productService.getAllProductsByStoreId(storeId));
        }
        @DeleteMapping("/product/deleteProduct")
        public ResultVO<Boolean> deleteProduct(@RequestBody Integer productId) {
                boolean success = productService.deleteProduct(productId);
                if (success) {
                        return ResultVO.buildSuccess(true);
                }
                else {
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
        @PutMapping("/product/productDetail/{productId}")
                public ResultVO<Boolean> updateProduct(@PathVariable int productId, @RequestBody ProductVO productVO) {
                boolean success = productService.updateProduct(productId, productVO);
                if (success) {
                        return ResultVO.buildSuccess(true);
                }
                else {
                        return ResultVO.buildFailure("商品不存在，更新失败");
                }
        }
        @GetMapping("/product/productDetail/{productId}")
        public ResultVO<Boolean> updateProductAmount(@PathVariable int productId,@RequestParam Integer productAmount) {
                return ResultVO.buildSuccess(productService.updateProductAmount(productId,productAmount));
        }
        @PatchMapping("/product/productDetail/{productId}")
        public ResultVO<Boolean> updateProductDiscount(@PathVariable int productId,@RequestParam Double productDiscount) {
                return ResultVO.buildSuccess(productService.updateProductDiscount(productId,productDiscount));
        }
}
