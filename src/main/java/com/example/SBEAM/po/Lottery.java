package com.example.SBEAM.po;
import com.example.SBEAM.vo.LotteryVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lottery")
public class Lottery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lotteryId;

    @OneToMany(mappedBy = "lottery", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name="lottery_item")
    private List<LotteryItem> lotteryItems;

    public LotteryVO toVO(){
        LotteryVO lotteryVO = new LotteryVO();
        lotteryVO.setLotteryId(lotteryId);
        lotteryVO.setLotteryItems(lotteryItems);
        return lotteryVO;
    }
}
