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
        public ResultVO<List<ProductVO>> getAllProducts(@RequestParam("storeId") Integer storeId) {
                return ResultVO.buildSuccess(productService.getAllProducts(storeId));
        }
}
