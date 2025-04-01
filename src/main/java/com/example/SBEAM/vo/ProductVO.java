package com.example.SBEAM.vo;

import com.example.SBEAM.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private Integer productId;
    private Integer storeId;
    private String productName;
    private Integer productAmount;
    private Double productPrice;

    public Product toPO(){
        Product product = new Product();
        product.setProductId(productId);
        product.setStoreId(storeId);
        product.setProductName(this.productName);
        product.setProductAmount(this.productAmount);
        product.setProductPrice(this.productPrice);
        return product;
    }
}