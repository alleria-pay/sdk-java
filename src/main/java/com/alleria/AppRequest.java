package com.alleria;

import com.alleria.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017/7/31.
 */
public class AppRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRequest.class);

    private String appId;

    private String signature;

    private String encryptKey;

    private String encryptData;

    public AppRequest() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getEncryptData() {
        return encryptData;
    }

    public void setEncryptData(String encryptData) {
        this.encryptData = encryptData;
    }

    public String json() {
        return JacksonUtil.toJSON(this);
    }

}
