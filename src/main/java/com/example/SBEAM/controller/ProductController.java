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
        @DeleteMapping("")//路径尚未确定
        public ResultVO<Boolean> deleteProduct(@RequestBody ProductVO productVO) {
                boolean success = productService.deleteProduct(productVO);
                if (success) {
                        return ResultVO.buildSuccess(true);
                }
                else {
                        return ResultVO.buildFailure("商品不存在，删除失败");
                }
        }
        @GetMapping("/store1")//路径尚未确定
        public ResultVO<List<ProductVO>> getAllProducts() {
                return ResultVO.buildSuccess(productService.getAllProducts());
        }
        @GetMapping("/store2")//路径尚未确定
        public ResultVO<ProductVO> getProductByProductId(@RequestParam Integer productId) {
                return ResultVO.buildSuccess(productService.getProductByProductId(productId));
        }
        @PutMapping("3")//路径尚未确定
                public ResultVO<Boolean> updateProduct(@RequestParam Integer productId, @RequestBody ProductVO productVO) {
                boolean success = productService.updateProduct(productId, productVO);
                if (success) {
                        return ResultVO.buildSuccess(true);
                }
                else {
                        return ResultVO.buildFailure("商品不存在，更新失败");
                }
        }
        @PutMapping("4")
        public ResultVO<Boolean> updateProductAmount(@RequestParam Integer productId,@RequestParam Integer productAmount) {
                return ResultVO.buildSuccess(productService.updateProductAmount(productId,productAmount));
        }
        @PutMapping("5")
        public ResultVO<Boolean> updateProductDiscount(@RequestParam Integer productId,@RequestParam Double productDiscount) {
                return ResultVO.buildSuccess(productService.updateProductDiscount(productId,productDiscount));
        }
}
