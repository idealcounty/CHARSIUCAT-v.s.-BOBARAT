package com.example.SBEAM.controller;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.OrderVO;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.SBEAM.configure.AliPayConfig;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.po.Orders;
import com.example.SBEAM.repository.OrdersRepository;
import com.example.SBEAM.util.AlipayUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AliPayConfig aliPayConfig;
    @Autowired
    private OrdersRepository ordersRepository;

    @GetMapping("/pay")
    public void pay(@RequestParam("orderId") int orderId, HttpServletResponse response) throws Exception {
        Orders order = ordersRepository.findById(orderId).orElse(null);
        if (order == null || order.getOrderStatus() != OrderStatus.PENDING) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("订单不存在或状态异常（可能已支付、已取消）");
            return;
        }
        if (order.getOutTradeNo() == null || order.getOutTradeNo().isEmpty()) {
            String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "");
            order.setOutTradeNo(outTradeNo);
            ordersRepository.save(order);
        }
        AlipayClient alipayClient = new DefaultAlipayClient(
                aliPayConfig.getGatewayUrl(),
                aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(),
                "json", "UTF-8",
                aliPayConfig.getAlipayPublicKey(),
                "RSA2"
        );
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(aliPayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOutTradeNo());
        bizContent.put("total_amount", order.getTotalPrice().toString());
        bizContent.put("subject", "订单支付 - 编号：" + order.getOrdersId());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        alipayRequest.setBizContent(bizContent.toString());
        String form = alipayClient.pageExecute(alipayRequest).getBody();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }
    @GetMapping("/return")
    public String returnUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = String.join(",", values);
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipayUtils.verifySignature(params, aliPayConfig.getAlipayPublicKey());
        String outTradeNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");
        String totalAmount = params.get("total_amount");
        if (signVerified) {
            Orders order = ordersRepository.findByOutTradeNo(outTradeNo);
            if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setPayMethod("支付宝");
                ordersRepository.save(order);
            }
            return "支付成功，订单号：" + outTradeNo + "，支付宝交易号：" + tradeNo + "，金额：" + totalAmount;
        } else {
            Orders order = ordersRepository.findByOutTradeNo(outTradeNo);
            if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
                order.setOrderStatus(OrderStatus.FAILED);
                ordersRepository.save(order);
            }
            return "验签失败，支付未成功！";
        }
    }
    @PostMapping("/notify")
    public String notifyUrl(HttpServletRequest request) throws Exception {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = String.join(",", values); // 支持数组拼接
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8"); // 字符编码处理
            params.put(name, valueStr);
        }
        boolean signVerified = AlipayUtils.verifySignature(params, aliPayConfig.getAlipayPublicKey());
        if (signVerified) {
            String outTradeNo = params.get("out_trade_no");
            String tradeStatus = params.get("trade_status");
            String totalAmount = params.get("total_amount");
            Orders order = ordersRepository.findByOutTradeNo(outTradeNo);
            if (order == null) {
                System.out.println("【异步通知】订单不存在：" + outTradeNo);
                return "fail";
            }
            if (!totalAmount.equals(order.getTotalPrice().toString())) {
                System.out.println("【异步通知】金额不一致，订单：" + outTradeNo);
                return "fail";
            }
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                if (order.getOrderStatus() == OrderStatus.PENDING) {
                    order.setOrderStatus(OrderStatus.SUCCESS);
                    order.setPayMethod("支付宝");
                    ordersRepository.save(order);
                    System.out.println("【异步通知】订单支付成功：" + outTradeNo);
                }
            }
            return "success";
        } else {
            System.out.println("【异步通知】验签失败");
            return "fail";
        }
    }
}
