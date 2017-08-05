package com.alleria;

import com.alleria.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017/7/31.
 */
public class AppResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppResponse.class);

    private String encryptKey;

    private String encryptData;

    private String signature;

    private String message;

    public static AppResponse from(String json) {
        return JacksonUtil.toObject(json, AppResponse.class);
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
