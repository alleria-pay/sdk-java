package com.alleria.pay;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017/7/31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmRequest.class);

    private long orderNo;

    private String merchantOrderNo;

    private String smsCode;

    private String password;

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
