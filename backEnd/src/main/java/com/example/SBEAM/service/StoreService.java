package com.example.SBEAM.service;

import com.example.SBEAM.po.Store;
import com.example.SBEAM.vo.StoreVO;
import java.util.List;


public interface StoreService {
    Boolean createStore(StoreVO storeVO);
     StoreVO getStore(Number Id);
     List<Store> getAllStores();
}
