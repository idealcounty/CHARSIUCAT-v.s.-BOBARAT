package com.example.SBEAM.serviceImpl;
import com.example.SBEAM.exception.SBEAMException;
import com.example.SBEAM.po.Advertisement;
import com.example.SBEAM.po.Product;
import com.example.SBEAM.service.AdvertisementService;
import com.example.SBEAM.repository.AdvertisementRepository;
import com.example.SBEAM.vo.AdvertisementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService{
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public Boolean createAdvertisement(AdvertisementVO advertisementVO){
        Advertisement advertisement=advertisementRepository.findByAdvertisementId(advertisementVO.getAdvertisementId());
        if(advertisement!=null){
            throw SBEAMException.advertisementAlreadyExist();
        }
        Advertisement newAdvertisement= advertisementVO.toPO();
        advertisementRepository.save(newAdvertisement);
        return true;
    }

    @Override
    public Boolean deleteAdvertisement(Integer advertisementId){
        Advertisement advertisement=advertisementRepository.findByAdvertisementId(advertisementId);
        if(advertisement==null){
            throw SBEAMException.advertisementNotExist();
        }
        advertisementRepository.delete(advertisement);
        return true;
    }

    @Override
    public Boolean updateAdvertisement(int advertisementId,AdvertisementVO advertisementVO){
        Advertisement advertisement=advertisementRepository.findByAdvertisementId(advertisementId);
        if(advertisement==null){
            throw SBEAMException.advertisementNotExist();
        }
        advertisement.setAdvertisementContent(advertisementVO.getAdvertisementContent());
        advertisement.setAdvertisementTitle(advertisementVO.getAdvertisementTitle());
        advertisement.setAdvertisementImageUrl(advertisementVO.getAdvertisementImageUrl());
        advertisementRepository.save(advertisement);
        return true;
    }
    @Override
    public List<AdvertisementVO> showAllAdvertisements(){
        return advertisementRepository.findAll().stream().map(Advertisement::toVO).collect(Collectors.toList());
    }
    @Override
    public AdvertisementVO getAdvertisementByAdvertisementId(Integer advertisementId){
        Advertisement advertisement=advertisementRepository.findByAdvertisementId(advertisementId);
        if(advertisement==null){
            throw SBEAMException.advertisementNotExist();
        }
        return advertisement.toVO();
    }
}
