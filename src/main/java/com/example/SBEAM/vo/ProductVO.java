package com.example.SBEAM.vo;

import com.example.SBEAM.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private Integer productId;
    private Integer storeId;
    private String productName;
    private Integer productAmount;
    private Double productPrice;
    private String productDescription;
    private List<String> productImages;
    private String productLogo;

    public Product toPO(){
        Product product = new Product();
        product.setProductId(productId);
        product.setStoreId(storeId);
        product.setProductName(this.productName);
        product.setProductAmount(this.productAmount);
        product.setProductPrice(this.productPrice);
        product.setProductDescription(this.productDescription);
        product.setProductImages(this.productImages);
        product.setProductLogo(this.productLogo);
        return product;
    }
}