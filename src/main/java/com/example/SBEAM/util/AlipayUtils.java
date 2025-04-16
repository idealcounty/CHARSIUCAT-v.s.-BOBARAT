package com.example.SBEAM.util;

import com.alipay.api.internal.util.AlipaySignature;

import java.util.Map;

public class AlipayUtils {
    public static boolean verifySignature(Map<String, String> params, String alipayPublicKey) {
        try {
            return AlipaySignature.rsaCheckV1(
                    params,
                    alipayPublicKey,
                    "UTF-8",
                    "RSA2"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
