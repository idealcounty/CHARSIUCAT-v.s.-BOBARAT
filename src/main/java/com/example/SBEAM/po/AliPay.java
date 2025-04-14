package com.example.SBEAM.po;

import lombok.Data;
@Data
public class AliPay {
    private String traceNo;// 我们自己生成的订单编号
    private double totalAmount;// 订单的总金额
    private String subject;// 支付的名称
    private String alipayTraceNo;
}