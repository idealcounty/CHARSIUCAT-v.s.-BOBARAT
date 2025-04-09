package com.example.SBEAM.po;

import com.example.SBEAM.vo.StoreVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;

    @Column(name = "store_name", unique = true, nullable = false)
    private String storeName;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "logo_url")
    private String logoUrl;


    public StoreVO toVO() {
        StoreVO storeVO = new StoreVO();
        storeVO.setStoreId(this.storeId);
        storeVO.setStoreName(this.storeName);
        storeVO.setLikes(this.likes);
        storeVO.setLogoUrl(this.logoUrl);

        return storeVO;
    }
}
