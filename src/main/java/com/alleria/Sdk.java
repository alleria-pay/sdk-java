package com.alleria;

import com.alleria.pay.Pay;
import com.alleria.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created on 2017/7/31.
 */
public class Sdk {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sdk.class);

    private String baseUrl;

    private String appId;

    private String publicKeyStr;

    private String privateKeyStr;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public Sdk() {
    }

    public Sdk(String baseUrl, String appId, String publicKeyStr, String privateKeyStr) {
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.publicKeyStr = publicKeyStr;
        this.privateKeyStr = privateKeyStr;
        this.publicKey = CryptoUtil.getPublicKey(publicKeyStr);
        this.privateKey = CryptoUtil.getPrivateKey(privateKeyStr);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Sdk setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public Sdk setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getPublicKeyStr() {
        return publicKeyStr;
    }

    public Sdk setPublicKeyStr(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
        this.publicKey = CryptoUtil.getPublicKey(publicKeyStr);
        return this;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    public Sdk setPrivateKeyStr(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
        this.privateKey = CryptoUtil.getPrivateKey(privateKeyStr);
        return this;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public Pay pay() {
        return new Pay(baseUrl, appId, publicKey, privateKey);
    }
}
