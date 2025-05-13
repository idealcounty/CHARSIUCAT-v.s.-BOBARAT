//package com.example.SBEAM.controller;
//
//import com.alipay.easysdk.factory.Factory;
//import com.example.SBEAM.configure.AliPayConfig;
//import com.example.SBEAM.po.AliPay;
//
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.AlipayTradePagePayRequest;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("api/alipay")
//@Transactional(rollbackFor = Exception.class)
//public class AliPayController {
//
//    @Resource
//    AliPayConfig aliPayConfig;
//
//    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//    private static final String FORMAT ="JSON";
//    private static final String CHARSET ="utf-8";
//    private static final String SIGN_TYPE ="RSA2";
//
//
//    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
//    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
//        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
//                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
//        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
//        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
//                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
//                + "\"subject\":\"" + aliPay.getSubject() + "\","
//                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//        String form = "";
//        try {
//            // 调用SDK生成表单
//            form = alipayClient.pageExecute(request).getBody();
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        httpResponse.setContentType("text/html;charset=" + CHARSET);
//        // 直接将完整的表单html输出到页面
//        httpResponse.getWriter().write(form);
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();
//    }
//
//    @PostMapping("/notify")  // 注意这里必须是POST接口
//    public String payNotify(HttpServletRequest request) throws Exception {
//        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
//            System.out.println("=========支付宝异步回调========");
//
//            Map<String, String> params = new HashMap<>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (String name : requestParams.keySet()) {
//                params.put(name, request.getParameter(name));
//                // System.out.println(name + " = " + request.getParameter(name));
//            }
//
//            String tradeNo = params.get("out_trade_no");
//            String gmtPayment = params.get("gmt_payment");
//            String alipayTradeNo = params.get("trade_no");
//            // 支付宝验签
//            if (Factory.Payment.Common().verifyNotify(params)) {
//                // 验签通过
//                System.out.println("交易名称: " + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
//                // 更新订单未已支付
//
//            }
//        }
//        return "success";
//    }
//}
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
            Cart cart = cartRepository.findByUserId(order.getUserId());
            User user = userRepository.findById(order.getUserId()).get();
            for (CartItem cartItem : cart.getCartItems()) {
                Inventory inventory = new Inventory(user,cartItem.getProductId(),cartItem.getProductQuantity(),cartItem.getProductPrice());
                user.getInventories().add(inventory);
            }
            userRepository.save(user);

            if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setPayMethod("支付宝");
                ordersRepository.save(order);
                cart.getCartItems().clear();
                cartRepository.save(cart);
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
            Cart cart = cartRepository.findByUserId(order.getUserId());
            System.out.println(order.getOutTradeNo());
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
                cart.getCartItems().clear();
                cartRepository.save(cart);
            return "success";
        } else {
            System.out.println("【异步通知】验签失败");
            return "fail";
        }
    }
}


//package com.example.SBEAM.controller;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.internal.util.AlipaySignature;
//import com.alipay.api.request.AlipayTradePagePayRequest;
//import com.example.SBEAM.configure.AliPayConfig;
//import com.example.SBEAM.enums.OrderStatus;
//import com.example.SBEAM.po.Orders;
//import com.example.SBEAM.repository.OrdersRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.net.URLDecoder;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@RestController
//@RequestMapping("/alipay")
//@Transactional(rollbackFor = Exception.class)
//public class AliPayController {
//
//    @Resource
//    private AliPayConfig aliPayConfig;
//
//    @Autowired
//    private OrdersRepository ordersRepository;
//
//    // 沙箱网关地址，生产环境请替换为：https://openapi.alipay.com/gateway.do
//    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
//    private static final String FORMAT      = "JSON";
//    private static final String CHARSET     = "UTF-8";
//    private static final String SIGN_TYPE   = "RSA2";
//
//@GetMapping("/pay")
//public void pay(@RequestParam("subject")     String  subject,
//                @RequestParam("traceNo")     String  traceNo,
//                @RequestParam("totalAmount") Double  totalAmount,
//                HttpServletResponse httpResponse) throws Exception {
//
//    // 1. 构造 AlipayClient
//    AlipayClient client = new DefaultAlipayClient(
//        GATEWAY_URL,
//        aliPayConfig.getAppId(),
//        aliPayConfig.getAppPrivateKey(),
//        FORMAT,
//        CHARSET,
//        aliPayConfig.getAlipayPublicKey(),
//        SIGN_TYPE
//    );
//
//    // 2. 构造支付请求
//    AlipayTradePagePayRequest req = new AlipayTradePagePayRequest();
//    req.setNotifyUrl(aliPayConfig.getNotifyUrl());
//    req.setBizContent("{"
//        + "\"out_trade_no\":\""   + traceNo     + "\","
//        + "\"total_amount\":\""   + totalAmount + "\","
//        + "\"subject\":\""        + subject     + "\","
//        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\""
//        + "}");
//
//    // 3. 执行并拿到 HTML form
//    String form = client.pageExecute(req).getBody();
//    System.out.println("【完整 HTML 表单】\n" + form);
//
//    // 4. 从 <form action="…?sign=xxx&…"> 提取 sign
//    Pattern actionSignPattern =
//        Pattern.compile("action=\"[^\"]*?[?&]sign=([^&\"]+)");
//    Matcher ma = actionSignPattern.matcher(form);
//
//    String sdkSign = null;
//    if (ma.find()) {
//        sdkSign = java.net.URLDecoder.decode(ma.group(1), CHARSET);
//    } else {
//        System.err.println("⚠️ 提取 SDK 签名失败，请检查 action URL！");
//    }
//
//    // 5. 从 <form action="…"> 中提取 URL，并拆分参数
//    Pattern urlPattern = Pattern.compile("action=\"([^\"]+)\"");
//    Matcher um = urlPattern.matcher(form);
//    String actionUrl = um.find() ? um.group(1) : "";
//
//    String query = actionUrl.substring(actionUrl.indexOf('?') + 1);
//    String[] pairs = query.split("&");
//    Map<String, String> allParams = new HashMap<>();
//    for (String pair : pairs) {
//        String[] kv = pair.split("=", 2);
//        String k = kv[0];
//        String v = URLDecoder.decode(kv[1], CHARSET);
//        allParams.put(k, v);
//    }
//
//    // ✅ 新增：提取 biz_content 隐藏域
//    Pattern bizPattern = Pattern.compile(
//        "<input[^>]*name=[\"']biz_content[\"'][^>]*value=[\"']([^\"']+)[\"']"
//    );
//    Matcher bm = bizPattern.matcher(form);
//    if (bm.find()) {
//        String rawBizContent = bm.group(1).replace("&quot;", "\"");
//        allParams.put("biz_content", rawBizContent);
//    } else {
//        System.err.println("⚠️ 未能提取 biz_content，请检查正则！");
//    }
//
//    // 6. 删除签名相关字段
//    allParams.remove("sign");
//    allParams.remove("sign_type");
//
//    // 7. 排序并生成明文
//    String contentAll = AlipaySignature.getSignCheckContentV1(allParams);
//
//    // 8. 本地签名
//    String mySdkLikeSign = AlipaySignature.rsaSign(
//        contentAll,
//        aliPayConfig.getAppPrivateKey(),
//        CHARSET,
//        SIGN_TYPE
//    );
//
//    // 9. 打印对比结果
//    System.out.println("【SDK 签名前明文】\n" + contentAll);
//    System.out.println("【本地模拟 SDK 签名】\n" + mySdkLikeSign);
//    System.out.println("【SDK 原始签名】\n" + sdkSign);
//
//    // 10. 响应页面
//    httpResponse.setContentType("text/html;charset=" + CHARSET);
//    httpResponse.getWriter().write(form);
//    httpResponse.getWriter().flush();
//    httpResponse.getWriter().close();
//}
//
//
//    @PostMapping("/notify")
//    public String payNotify(HttpServletRequest request) throws Exception {
//        String tradeStatus = request.getParameter("trade_status");
//        if (tradeStatus != null && tradeStatus.equals("TRADE_SUCCESS")) {
//            System.out.println("========= 支付宝异步回调 （TRADE_SUCCESS） =========");
//
//            // 1. 收集回调参数
//            Map<String,String> params = new HashMap<>();
//            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
//                params.put(entry.getKey(), request.getParameter(entry.getKey()));
//            }
//
//            // 2. 验签
//            boolean signVerified = AlipaySignature.rsaCheckV1(
//                params,
//                aliPayConfig.getAlipayPublicKey(),
//                CHARSET,
//                SIGN_TYPE
//            );
//
//            if (signVerified) {
//                System.out.println("✅ 验签通过");
//
//                // 3. 业务处理：更新订单状态
//                String outTradeNo = params.get("out_trade_no");
//                Orders order = ordersRepository.findById(Integer.valueOf(outTradeNo))
//                                               .orElse(new Orders());
//                order.setOrdersId(Integer.valueOf(outTradeNo));
//                order.setOrderStatus(OrderStatus.SUCCESS);
//                ordersRepository.save(order);
//
//            } else {
//                System.err.println("❌ 验签失败，可能是公钥配置错误或参数被篡改");
//                return "failure";
//            }
//        }
//        // 注意：支付宝只识别“success”作为成功响应
//        return "success";
//    }
//}
