package com.example.SBEAM.serviceImpl;

import com.example.SBEAM.po.*;
import com.example.SBEAM.repository.LotteryItemRepository;
import com.example.SBEAM.repository.LotteryRepository;
import com.example.SBEAM.repository.ProductRepository;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.service.LotteryService;
import com.example.SBEAM.vo.LotteryItemVO;
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
    public Boolean createLottery(List<LotteryItemVO> lotteryItemVOList){
        Lottery lottery = new Lottery();
        List<LotteryItem> items = new ArrayList<>();
        for (LotteryItemVO vo : lotteryItemVOList) {
            LotteryItem item = lotteryItemRepository.findByLotteryItemId(vo.getLotteryItemId());
            items.add(item);
        }
        double totalInverse = 0;
        for (LotteryItem item : items) {
            double value = item.getProductValue();
            if (value > 0) {
                totalInverse += 1.0 / value;
            }
        }
        if (totalInverse == 0) {
            return false;
        }
        for (LotteryItem item : items) {
            double value = item.getProductValue();
            double probability = (value > 0) ? (1.0 / value) / totalInverse * 0.3 : 0.0;
            item.setLottery(lottery);
            item.setLotteryItemProbability(probability);
            lotteryItemRepository.save(item);
            lottery.getLotteryItems().add(item);
        }

        LotteryItem blank = new LotteryItem();
        blank.setLottery(lottery);
        blank.setLotteryItemProbability(0.7);
        blank.setLotteryItemId(0);
        blank.setProductValue(0.0);
        blank.setProductQuantity(0);
        lotteryItemRepository.save(blank);

        lottery.getLotteryItems().add(blank);
        lotteryRepository.save(lottery);
        return true;
    }
    @Override
    public LotteryItemVO createLotteryItem(int productId,int productAmount){
        LotteryItem lotteryItem = new LotteryItem(productId,productAmount);
        lotteryItem.setProductValue(productRepository.findByProductId(productId).getProductPrice()*productAmount);
        lotteryItemRepository.save(lotteryItem);
        return lotteryItem.toVO();
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
}
