package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.service.AdvertisementService;
import com.example.SBEAM.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementServiceImpl implements AdvertisementService{
    @Autowired
    private AdvertisementRepository advertisementRepository;
}
