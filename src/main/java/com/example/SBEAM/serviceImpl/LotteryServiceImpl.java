package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.po.*;
import com.example.SBEAM.repository.LotteryItemRepository;
import com.example.SBEAM.repository.LotteryRepository;
import com.example.SBEAM.repository.ProductRepository;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.service.LotteryService;
import com.example.SBEAM.vo.LotteryItemVO;
import com.example.SBEAM.vo.LotteryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LotteryServiceImpl implements LotteryService {
    @Autowired
    LotteryRepository lotteryRepository;
    @Autowired
    LotteryItemRepository lotteryItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Boolean createLottery(String lotteryName) {
        Lottery lottery = new Lottery();
        lottery.setLotteryName(lotteryName);
        LotteryItem blank = new LotteryItem();
        blank.setLotteryItemProbability(0.7);
        blank.setProductValue(0.0);
        blank.setProductQuantity(1);
        lottery.getLotteryItems().add(blank);
        lotteryRepository.save(lottery);
        return true;
    }
    @Override
    public Boolean addLotteryItem(int lotteryId,LotteryItemVO lotteryItemVO){
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        LotteryItem lotteryItem = new LotteryItem();
        lotteryItem.setLottery(lottery);
        lotteryItem.setProductId(lotteryItemVO.getProductId());
        lotteryItem.setProductQuantity(lotteryItemVO.getProductQuantity());
        lotteryItem.setProductValue(productRepository.findByProductId(lotteryItemVO.getProductId()).getProductPrice());
        lotteryItemRepository.save(lotteryItem);
        lottery.getLotteryItems().add(lotteryItem);
        double totalInverse = 0;
        for (LotteryItem item : lottery.getLotteryItems()) {
            double value = item.getProductValue();
            if (value > 0) {
                totalInverse += 1.0 / value;
            }
        }
        if (totalInverse == 0) {
            return false;
        }
        for (LotteryItem item : lottery.getLotteryItems()) {
            double value = item.getProductValue();
            double probability = (value > 0) ? (1.0 / value) / totalInverse * 0.3 : 0.0;
            item.setLotteryItemProbability(probability);
        }
        lotteryRepository.save(lottery);
        return true;
    }
    @Override
    public List<LotteryItemVO> getLotteryItemList(int lotteryId){
        return lotteryRepository.findByLotteryId(lotteryId).getLotteryItems().stream().map(LotteryItem::toVO).collect(Collectors.toList());
    }
    @Override
    public LotteryItemVO drawLottery(int lotteryId,int userId){
        double cumulativeProbability=0;
        Random random = new Random();
        double randomNumber = random.nextDouble();
        for(LotteryItem item: lotteryRepository.findByLotteryId(lotteryId).getLotteryItems()){
            cumulativeProbability+=item.getLotteryItemProbability();
            if(randomNumber < cumulativeProbability){
                User user=userRepository.findById(userId).get();
                Inventory inventory =new Inventory(user,item.getProductId(),item.getProductQuantity(),item.getProductValue());
                user.getInventories().add(inventory);
                userRepository.save(user);
                return item.toVO();
            }
        }
        return null;
    }
    @Override
    public List<LotteryVO> getLottery(){
        return lotteryRepository.findAll().stream().map(Lottery::toVO).collect(Collectors.toList());
    }
}
