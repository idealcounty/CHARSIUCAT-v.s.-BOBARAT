package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.Store;
import com.example.SBEAM.repository.StoreRepository;
import com.example.SBEAM.service.StoreService;
import com.example.SBEAM.util.TokenUtil;
import com.example.SBEAM.vo.StoreVO;
import com.example.SBEAM.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public Boolean createStore(StoreVO storeVO){
        Store store = storeRepository.findByStoreName(storeVO.getStoreName());
        if (store != null) {
            throw SBEAMException.storeNameAlreadyExists();
        }
        Store newStore = storeVO.toPO();
        storeRepository.save(newStore);
        return true;
    }
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public StoreVO getStore(Number StoreId) {
        Store store = storeRepository.findByStoreId(StoreId);
        if (store == null) {
            throw SBEAMException.storeNotExists();
        }
        return store.toVO();
    }
}