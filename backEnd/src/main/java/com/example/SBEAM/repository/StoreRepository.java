package com.example.SBEAM.repository;

import com.example.SBEAM.po.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer>{
    Store findByStoreName(String storeName);
    Store findByStoreId(Number storeId);
}