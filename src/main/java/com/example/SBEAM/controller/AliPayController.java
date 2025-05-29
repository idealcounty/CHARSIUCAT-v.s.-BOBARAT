package com.example.SBEAM.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.SBEAM.configure.AliPayConfig;
import com.example.SBEAM.enums.OrderStatus;
import com.example.SBEAM.po.*;
import com.example.SBEAM.repository.CartRepository;
import com.example.SBEAM.repository.OrdersRepository;
import com.example.SBEAM.repository.UserRepository;
import com.example.SBEAM.repository.LotteryOrderRepository;
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
@RequestMapping("/api/alipay")
public class AliPayController {

    @Autowired
    private AliPayConfig aliPayConfig;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LotteryOrderRepository lotteryOrderRepository;

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
                "RSA2");
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

    @GetMapping("/payLottery")
    public void payLottery(@RequestParam("lotteryOrderId") int lotteryOrderId, HttpServletResponse response)
            throws Exception {
        LotteryOrder lotteryOrder = lotteryOrderRepository.findById(lotteryOrderId).orElse(null);
        if (lotteryOrder == null || lotteryOrder.getOrderStatus() != OrderStatus.PENDING) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("抽奖次数订单不存在或状态异常（可能已支付、已取消）");
            return;
        }
        if (lotteryOrder.getOutTradeNo() == null || lotteryOrder.getOutTradeNo().isEmpty()) {
            String outTradeNo = "LOTTERY_" + UUID.randomUUID().toString().replaceAll("-", "");
            lotteryOrder.setOutTradeNo(outTradeNo);
            lotteryOrderRepository.save(lotteryOrder);
        }
        AlipayClient alipayClient = new DefaultAlipayClient(
                aliPayConfig.getGatewayUrl(),
                aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(),
                "json", "UTF-8",
                aliPayConfig.getAlipayPublicKey(),
                "RSA2");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(aliPayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", lotteryOrder.getOutTradeNo());
        bizContent.put("total_amount", lotteryOrder.getTotalPrice().toString());
        bizContent.put("subject", "抽奖次数购买 - " + lotteryOrder.getLotteryChancesCount() + "次");
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
            LotteryOrder lotteryOrder = lotteryOrderRepository.findByOutTradeNo(outTradeNo);

            if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
                // 处理商品订单
                processPaymentSuccess(order);
                return "支付成功，订单号：" + outTradeNo + "，支付宝交易号：" + tradeNo + "，金额：" + totalAmount;
            } else if (lotteryOrder != null && lotteryOrder.getOrderStatus() == OrderStatus.PENDING) {
                // 处理抽奖次数订单
                processLotteryPaymentSuccess(lotteryOrder);
                return "抽奖次数购买成功，订单号：" + outTradeNo + "，支付宝交易号：" + tradeNo + "，金额：" + totalAmount + "，获得抽奖次数："
                        + lotteryOrder.getLotteryChancesCount();
            }
            return "支付成功，订单号：" + outTradeNo + "，支付宝交易号：" + tradeNo + "，金额：" + totalAmount;
        } else {
            Orders order = ordersRepository.findByOutTradeNo(outTradeNo);
            LotteryOrder lotteryOrder = lotteryOrderRepository.findByOutTradeNo(outTradeNo);

            if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
                order.setOrderStatus(OrderStatus.FAILED);
                ordersRepository.save(order);
            } else if (lotteryOrder != null && lotteryOrder.getOrderStatus() == OrderStatus.PENDING) {
                lotteryOrder.setOrderStatus(OrderStatus.FAILED);
                lotteryOrderRepository.save(lotteryOrder);
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
            LotteryOrder lotteryOrder = lotteryOrderRepository.findByOutTradeNo(outTradeNo);

            if (order == null && lotteryOrder == null) {
                return "fail";
            }

            if (order != null) {
                if (!totalAmount.equals(order.getTotalPrice().toString())) {
                    return "fail";
                }
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    if (order.getOrderStatus() == OrderStatus.PENDING) {
                        // 处理商品订单支付成功逻辑
                        processPaymentSuccess(order);
                    }
                }
            } else if (lotteryOrder != null) {
                if (!totalAmount.equals(lotteryOrder.getTotalPrice().toString())) {
                    return "fail";
                }
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    if (lotteryOrder.getOrderStatus() == OrderStatus.PENDING) {
                        // 处理抽奖次数订单支付成功逻辑
                        processLotteryPaymentSuccess(lotteryOrder);
                    }
                }
            }
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * 处理支付成功的逻辑：添加库存到用户账户、更新订单状态、清空购物车
     * 使用订单状态防止重复处理
     */
    private void processPaymentSuccess(Orders order) {
        // 再次检查订单状态，防止重复处理
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            return;
        }

        Cart cart = cartRepository.findByUserId(order.getUserId());
        User user = userRepository.findById(order.getUserId()).get();

        // 添加商品到用户库存
        for (CartItem cartItem : cart.getCartItems()) {
            Inventory inventory = new Inventory(user, cartItem.getProductId(), cartItem.getProductQuantity(),
                    cartItem.getProductPrice());
            user.getInventories().add(inventory);
        }
        userRepository.save(user);

        // 更新订单状态
        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setPayMethod("支付宝");
        ordersRepository.save(order);

        // 清空购物车
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private void processLotteryPaymentSuccess(LotteryOrder lotteryOrder) {
        // 再次检查订单状态，防止重复处理
        if (lotteryOrder.getOrderStatus() != OrderStatus.PENDING) {
            return;
        }

        // 获取用户并增加抽奖次数
        User user = userRepository.findById(lotteryOrder.getUserId()).get();
        Integer currentChances = user.getLotteryChances() != null ? user.getLotteryChances() : 0;
        user.setLotteryChances(currentChances + lotteryOrder.getLotteryChancesCount());
        userRepository.save(user);

        // 更新订单状态
        lotteryOrder.setOrderStatus(OrderStatus.SUCCESS);
        lotteryOrder.setPayMethod("支付宝");
        lotteryOrderRepository.save(lotteryOrder);
    }
}
