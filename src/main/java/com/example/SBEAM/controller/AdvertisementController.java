package com.example.SBEAM.controller;
import com.example.SBEAM.service.AdvertisementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("/api")
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

}
