package com.example.SBEAM.po;
import com.example.SBEAM.vo.AdvertisementVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Table(name = "advertisements")
@Setter
@Getter
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id", nullable = false, columnDefinition = "INT COMMENT '广告id'")
    private Integer advertisementId;

    @Column(name = "advertisement_title", nullable = false, length = 50, columnDefinition = "VARCHAR(50) COMMENT '广告标题，不允许为空'")
    private String advertisementTitle;

    @Column(name = "advertisement_content", nullable = false, length = 500, columnDefinition = "VARCHAR(500) COMMENT '广告内容'")
    private String advertisementContent;

    @Column(name = "advertisement_image_url", nullable = false, length = 500, columnDefinition = "VARCHAR(500) COMMENT '广告图片url'")
    private String advertisementImageUrl;

    @Column(name="product_id", nullable = false)
    private Integer productId;

    public AdvertisementVO toVO(){
        AdvertisementVO advertisementVO = new AdvertisementVO();
        advertisementVO.setAdvertisementId(advertisementId);
        advertisementVO.setAdvertisementTitle(advertisementTitle);
        advertisementVO.setAdvertisementContent(advertisementContent);
        advertisementVO.setAdvertisementImageUrl(advertisementImageUrl);
        advertisementVO.setProductId(productId);
        return advertisementVO;
    }
}