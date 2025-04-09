package com.example.SBEAM.po;
import com.example.SBEAM.vo.ProductVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "product_name")
    private String productName;

    @Basic
    @Column(name = "product_amount")
    private Integer productAmount;

    @Basic
    @Column(name = "product_price")
    private Double productPrice;

    @Basic
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    @ElementCollection
    @Column(name = "product_images")
    private List<String> productImages;

    public ProductVO toVO() {
        ProductVO productVO = new ProductVO();
        productVO.setProductId(this.productId);
        productVO.setStoreId(this.storeId);
        productVO.setProductName(this.productName);
        productVO.setProductAmount(this.productAmount);
        productVO.setProductPrice(this.productPrice);
        productVO.setProductDescription(this.productDescription);
        productVO.setProductImages(this.productImages);
        return productVO;
    }
}
