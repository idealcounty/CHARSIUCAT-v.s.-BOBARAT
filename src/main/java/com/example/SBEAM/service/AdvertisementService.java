package com.example.SBEAM.service;
import com.example.SBEAM.vo.AdvertisementVO;

import java.util.List;

public interface AdvertisementService {
    public Boolean createAdvertisement(AdvertisementVO advertisementVO);
    public Boolean deleteAdvertisement(Integer advertisementId);
    public Boolean updateAdvertisement(AdvertisementVO advertisementVO);
    public List<AdvertisementVO> showAllAdvertisements();
    public AdvertisementVO getAdvertisementByAdvertisementId(Integer advertisementId);
}
