package com.alleria;

import com.alleria.util.CryptoUtil;
import com.alleria.util.JacksonUtil;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

import static com.alleria.util.CryptoUtil.*;

/**
 * Created on 2017/7/31.
 */
public abstract class Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);

    private String baseUrl;

    private String appId;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public Action(String baseUrl, String appId, PublicKey publicKey, PrivateKey privateKey) {
        this.baseUrl = baseUrl;
        this.appId = appId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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


    protected AppRequest encrypt(Object request) {
        AppRequest appRequest = new AppRequest();
        String data = JacksonUtil.toJSON(request);
        LOGGER.debug("data:{}", data);
        byte[] key = CryptoUtil.initAESKey();

        String encryptData = Base64.encodeBase64String(AESEncrypt(data.getBytes(Charsets.UTF_8), key, AES, AES_PADDING, null));
        LOGGER.debug("encrypt data:{}", encryptData);

        String encryptKey = Base64.encodeBase64String(RSAEncrypt(key, publicKey, LENGTH_2048, RSA_PADDING));
        LOGGER.debug("encrypt key:{}", encryptKey);

        String sign = CryptoUtil.sign(privateKey, data, MD5withRSA);
        LOGGER.debug("sign:{}", sign);

        appRequest.setAppId(appId);
        appRequest.setEncryptData(encryptData);
        appRequest.setEncryptKey(encryptKey);
        appRequest.setSignature(sign);
        return appRequest;
    }

    protected <T extends AppResult> T decrypt(AppResponse response, Class<T> tClass) {
        if (StringUtils.isEmpty(response.getSignature())) {
            throw new RuntimeException(response.getMessage());
        }

        byte[] AESKey = CryptoUtil.RSADecrypt(Base64.decodeBase64(response.getEncryptKey()), privateKey, LENGTH_2048, RSA_PADDING);

        // 解密正文
        String data = new String(CryptoUtil.AESDecrypt(Base64.decodeBase64(response.getEncryptData()), AESKey, AES, AES_PADDING, null), Charsets.UTF_8);
        // 签名验证
        boolean res = CryptoUtil.verifyData(data, response.getSignature(), publicKey, MD5withRSA);

        LOGGER.debug("data:{}", data);
        LOGGER.debug("verify:{}", res);

        if (!res) {
            throw new RuntimeException("签名校验错误");
        }
        T t = JacksonUtil.toObject(data, tClass);
        if (t == null) {
            throw new RuntimeException("处理错误");
        }
        if (!t.isSuccess()) {
            throw new RuntimeException(t.getMessage());
        }
        return t;
    }

}
