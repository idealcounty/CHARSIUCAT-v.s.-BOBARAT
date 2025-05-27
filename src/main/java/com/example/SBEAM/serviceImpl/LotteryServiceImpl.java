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
    public LotteryVO createLottery(String lotteryName) {
        Lottery lottery = new Lottery();
        lottery.setLotteryName(lotteryName);
        Lottery savedLottery = lotteryRepository.save(lottery);
        return savedLottery.toVO();
    }

    @Override
    public LotteryItemVO createLotteryItem(int productId, int productQuantity) {
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            return null;
        }

        LotteryItem lotteryItem = new LotteryItem(productId, productQuantity);
        lotteryItem.setProductValue(product.getProductPrice());
        lotteryItem.setLotteryItemProbability(0.0); // 初始概率为0，添加到奖池时会重新计算

        return lotteryItem.toVO();
    }

    @Override
    public Boolean addLotteryItem(int lotteryId, LotteryItemVO lotteryItemVO) {
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        if (lottery == null) {
            return false;
        }

        Product product = productRepository.findByProductId(lotteryItemVO.getProductId());
        if (product == null) {
            return false;
        }

        LotteryItem lotteryItem = new LotteryItem();
        lotteryItem.setLottery(lottery);
        lotteryItem.setProductId(lotteryItemVO.getProductId());
        lotteryItem.setProductQuantity(lotteryItemVO.getProductQuantity());
        lotteryItem.setProductValue(product.getProductPrice());

        List<LotteryItem> allItems = new ArrayList<>(lottery.getLotteryItems());
        allItems.add(lotteryItem);

        double totalInverse = 0;
        for (LotteryItem item : allItems) {
            double value = item.getProductValue();
            if (value > 0) {
                totalInverse += 1.0 / value;
            }
        }

        if (totalInverse > 0) {
            double probability = (1.0 / lotteryItem.getProductValue()) / totalInverse * 0.3; // 所有商品总概率30%
            lotteryItem.setLotteryItemProbability(probability);
        } else {
            lotteryItem.setLotteryItemProbability(0.0);
        }

        lotteryItemRepository.save(lotteryItem);
        lottery.getLotteryItems().add(lotteryItem);

        if (totalInverse > 0) {
            for (LotteryItem item : lottery.getLotteryItems()) {
                double value = item.getProductValue();
                if (value > 0) {
                    double probability = (1.0 / value) / totalInverse * 0.3;
                    item.setLotteryItemProbability(probability);
                }
            }
        }

        lotteryRepository.save(lottery);
        return true;
    }

    @Override
    public Boolean deleteLottery(int lotteryId) {
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        if (lottery == null) {
            return false;
        }
        lotteryRepository.delete(lottery);
        return true;
    }

    @Override
    public Boolean deleteLotteryItem(int lotteryItemId) {
        LotteryItem lotteryItem = lotteryItemRepository.findByLotteryItemId(lotteryItemId);
        if (lotteryItem == null) {
            return false;
        }
        lotteryItemRepository.delete(lotteryItem);
        return true;
    }

    @Override
    public Boolean removeLotteryItemFromLottery(int lotteryId, int lotteryItemId) {
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        LotteryItem lotteryItem = lotteryItemRepository.findByLotteryItemId(lotteryItemId);

        if (lottery == null || lotteryItem == null) {
            return false;
        }

        lottery.getLotteryItems().remove(lotteryItem);
        lotteryItemRepository.delete(lotteryItem);

        double totalInverse = 0;
        for (LotteryItem item : lottery.getLotteryItems()) {
            double value = item.getProductValue();
            if (value > 0) {
                totalInverse += 1.0 / value;
            }
        }

        if (totalInverse > 0) {
            for (LotteryItem item : lottery.getLotteryItems()) {
                double value = item.getProductValue();
                if (value > 0) {
                    double probability = (1.0 / value) / totalInverse * 0.3;
                    item.setLotteryItemProbability(probability);
                }
            }
        }

        lotteryRepository.save(lottery);
        return true;
    }

    @Override
    public Boolean updateLotteryName(int lotteryId, String newName) {
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        if (lottery == null) {
            return false;
        }
        lottery.setLotteryName(newName);
        lotteryRepository.save(lottery);
        return true;
    }

    @Override
    public List<LotteryItemVO> getLotteryItemList(int lotteryId) {
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        if (lottery == null) {
            return new ArrayList<>();
        }
        return lottery.getLotteryItems().stream().map(LotteryItem::toVO).collect(Collectors.toList());
    }

    @Override
    public LotteryItemVO drawLottery(int lotteryId, int userId) {
        Lottery lottery = lotteryRepository.findByLotteryId(lotteryId);
        if (lottery == null || lottery.getLotteryItems().isEmpty()) {
            return null;
        }

        List<LotteryItem> availableItems = lottery.getLotteryItems().stream()
                .filter(item -> item.getProductQuantity() > 0)
                .collect(Collectors.toList());

        if (availableItems.isEmpty()) {
            return null;
        }

        Random random = new Random();
        double randomNumber = random.nextDouble();

        double totalProbability = 0;
        for (LotteryItem item : availableItems) {
            totalProbability += item.getLotteryItemProbability();
        }

        if (randomNumber > totalProbability) {
            return null;
        }

        double cumulativeProbability = 0;
        for (LotteryItem item : availableItems) {
            cumulativeProbability += item.getLotteryItemProbability();
            if (randomNumber <= cumulativeProbability) {
                // 中奖了，减少奖品数量
                item.setProductQuantity(item.getProductQuantity() - 1);

                // 如果奖品数量变为0，从奖池中移除该奖品
                if (item.getProductQuantity() <= 0) {
                    lottery.getLotteryItems().remove(item);
                    lotteryItemRepository.delete(item);

                    // 重新计算剩余奖品的概率
                    recalculateProbabilities(lottery);
                } else {
                    lotteryItemRepository.save(item);
                }

                lotteryRepository.save(lottery);

                // 添加到用户库存
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    Inventory inventory = new Inventory(user, item.getProductId(), 1,
                            item.getProductValue());
                    user.getInventories().add(inventory);
                    userRepository.save(user);
                }

                return item.toVO();
            }
        }

        return null; // 没中奖
    }

    /**
     * 重新计算奖池中所有奖品的概率
     */
    private void recalculateProbabilities(Lottery lottery) {
        double totalInverse = 0;
        for (LotteryItem item : lottery.getLotteryItems()) {
            double value = item.getProductValue();
            if (value > 0) {
                totalInverse += 1.0 / value;
            }
        }

        if (totalInverse > 0) {
            for (LotteryItem item : lottery.getLotteryItems()) {
                double value = item.getProductValue();
                if (value > 0) {
                    double probability = (1.0 / value) / totalInverse * 0.3;
                    item.setLotteryItemProbability(probability);
                }
            }
        }
    }

    @Override
    public List<LotteryVO> getLottery() {
        return lotteryRepository.findAll().stream().map(Lottery::toVO).collect(Collectors.toList());
    }
}
