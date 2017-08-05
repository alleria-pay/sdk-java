package com.alleria;

import com.alleria.pay.ConfirmRequest;
import com.alleria.pay.PayOrder;
import com.alleria.pay.PayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created on 2017/7/31.
 */
public class SdkTest {

    private static final String appId = "替换为测试资料中的AppId";
    private static final String agencyPrivateKey = "替换为测试资料中的商户私钥";
    private static final String platformPublicKey = "替换为测试资料中的平台公钥";
    private static final String baseUrl = "替换为测试资料中的平台测试URL";
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkTest.class);
    private Sdk sdk = new Sdk()
            .setBaseUrl(baseUrl)
            .setAppId(appId)
            .setPrivateKeyStr(agencyPrivateKey)
            .setPublicKeyStr(platformPublicKey);

    @org.junit.Test
    public void createPay() throws Exception {
        PayRequest request = new PayRequest();
        request.setMerchantId(10025153461L);
        request.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        request.setChannel("express_d0");
        request.setSubject("测试订单");
        request.setDesc("测试描述");
        request.setAmount(1060);
        request.setClientIp("127.0.0.1");
        PayRequest.ChannelExtra extra = new PayRequest.ChannelExtra("张三", "18671040587", "130201199006010429", "6225092387218372");
        extra.setExpirationDate("0422");
        extra.setCvn2("159");
        request.setExtra(extra);
        PayOrder order = sdk.pay().create(request);
        LOGGER.info(String.valueOf(order.getOrderNo()));

    }

    @org.junit.Test
    public void confirmPay() throws Exception {
        ConfirmRequest request = new ConfirmRequest();
        request.setOrderNo(348318168287727962L);
        request.setMerchantOrderNo("1501468474670181009");
        request.setSmsCode("123456");
        request.setPassword("123456");
        PayOrder order = sdk.pay().confirm(request);
        LOGGER.info(String.valueOf(order.getOrderNo()));

    }

    @org.junit.Test
    public void queryOrder() throws Exception {
        ConfirmRequest request = new ConfirmRequest();
        request.setOrderNo(348318168287727962L);
        request.setMerchantOrderNo("1501468474670181009");
        PayOrder order = sdk.pay().query(request);
        LOGGER.info(String.valueOf(order.getOrderNo()));

    }

}