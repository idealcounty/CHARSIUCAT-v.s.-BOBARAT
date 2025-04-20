package com.example.SBEAM.controller;
import com.example.SBEAM.service.AdvertisementService;
import com.example.SBEAM.vo.AdvertisementVO;
import com.example.SBEAM.vo.ResultVO;
import org.hibernate.boot.jaxb.internal.stax.JpaOrmXmlEventReader;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

    @PostMapping
    public ResultVO<Boolean>createAdvertisement(@RequestBody AdvertisementVO advertisementVO) {
        return ResultVO.buildSuccess(advertisementService.createAdvertisement(advertisementVO));
    }
    @DeleteMapping
    public ResultVO<Boolean>deleteAdvertisement(@RequestParam int advertisementId) {
        return ResultVO.buildSuccess(advertisementService.deleteAdvertisement(advertisementId));
    }
    @PutMapping("/{advertisementId}")
    public ResultVO<Boolean>updateAdvertisement(@PathVariable  int advertisementId,@RequestBody AdvertisementVO advertisementVO) {
        return ResultVO.buildSuccess(advertisementService.updateAdvertisement(advertisementId,advertisementVO));
    }
    @GetMapping
    public ResultVO<List<AdvertisementVO>>getAllAdvertisement() {
        return ResultVO.buildSuccess(advertisementService.showAllAdvertisements());
    }
    @GetMapping("/{advertisementId}")
    public ResultVO<AdvertisementVO>getAdvertisement(@PathVariable int advertisementId) {
        return ResultVO.buildSuccess(advertisementService.getAdvertisementByAdvertisementId(advertisementId));
    }

}
