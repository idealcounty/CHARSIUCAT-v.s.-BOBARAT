package com.example.SBEAM.po;
import com.example.SBEAM.vo.LotteryVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Basic
    @Column(name ="lottery_name")
    private String lotteryName;

    @OneToMany(mappedBy = "lottery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LotteryItem> lotteryItems;

    public Lottery(){
        lotteryItems = new ArrayList<LotteryItem>();
    }
    public LotteryVO toVO(){
        LotteryVO lotteryVO = new LotteryVO();
        lotteryVO.setLotteryId(lotteryId);
        lotteryVO.setLotteryItems(lotteryItems);
        lotteryVO.setLotteryName(lotteryName);
        return lotteryVO;
    }
}
