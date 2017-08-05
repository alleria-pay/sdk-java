package com.alleria.pay;

import com.alleria.Action;
import com.alleria.AppRequest;
import com.alleria.AppResponse;
import com.alleria.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created on 2017/7/31.
 */
public class Pay extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pay.class);

    private String CREATE = getBaseUrl() + "/payment";
    private String CONFIRM = getBaseUrl() + "/payment/confirm";
    private String QUERY = getBaseUrl() + "/payment/query";

    public Pay(String baseUrl, String appId, PublicKey publicKey, PrivateKey privateKey) {
        super(baseUrl, appId, publicKey, privateKey);
    }

    public PayOrder create(PayRequest request) {
        AppRequest appRequest = encrypt(request);
        AppResponse response = HttpUtil.post(CREATE, appRequest);
        return decrypt(response, PayOrder.class);
    }

    public PayOrder confirm(ConfirmRequest request) {
        AppRequest appRequest = encrypt(request);
        AppResponse response = HttpUtil.post(CONFIRM, appRequest);
        return decrypt(response, PayOrder.class);
    }

    public PayOrder query(ConfirmRequest request) {
        AppRequest appRequest = encrypt(request);
        AppResponse response = HttpUtil.post(QUERY, appRequest);
        return decrypt(response, PayOrder.class);
    }

}
