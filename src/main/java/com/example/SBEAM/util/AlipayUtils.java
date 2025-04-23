package com.example.SBEAM.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import java.util.Map;

public class AlipayUtils {
    /**
     * 使用支付宝公钥校验签名
     *
     * @param content 签名原始数据（按支付宝文档拼接）
     * @param sign 支付宝传来的签名
     * @param alipayPublicKey 支付宝公钥
     * @return 验签结果 true 表示成功
     */
    public static boolean verifySignature(Map<String, String> params, String alipayPublicKey) {
        try {
            // 支付宝使用 RSA2（推荐）或 RSA
            return AlipaySignature.rsaCheckV1(
                    params,
                    alipayPublicKey,
                    "UTF-8",
                    "RSA2" // 如果你配置了 RSA 就填 "RSA"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
