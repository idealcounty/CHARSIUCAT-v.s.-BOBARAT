package com.example.SBEAM.vo;

import com.example.SBEAM.po.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreVO {
    private Integer storeId;
    private String storeName;
    private Integer likes;
    private String logoUrl;

    public Store toPO() {
        Store store = new Store();
        store.setStoreId(this.storeId);
        store.setStoreName(this.storeName);
        store.setLikes(this.likes);
        store.setLogoUrl(this.logoUrl);
        return store;
    }
    @Override
    public String toString() {
        return "StoreVO{" +
                "storeName='" + storeName + '\'' +
                ", likes=" + likes +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
