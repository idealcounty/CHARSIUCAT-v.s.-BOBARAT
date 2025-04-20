package com.example.SBEAM.vo;
import com.example.SBEAM.po.Advertisement;
import com.example.SBEAM.po.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdvertisementVO {
    private int advertisementId;
    private String advertisementTitle;
    private String advertisementContent;
    private String advertisementImageUrl;
    private Integer productId;
    public Advertisement toPO(){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementId(this.advertisementId);
        advertisement.setAdvertisementTitle(this.advertisementTitle);
        advertisement.setAdvertisementContent(this.advertisementContent);
        advertisement.setAdvertisementImageUrl(this.advertisementImageUrl);
        advertisement.setProductId(this.productId);
        return advertisement;
    }
}
