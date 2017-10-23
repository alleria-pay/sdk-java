package com.alleria.pay;

import com.alleria.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created on 2017/7/31.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayRequest.class);

    private long merchantId;

    // 商户订单号
    private String orderNo;

    // 支付类型代号
    private String channel;

    private String subject;

    private String desc;

    private long amount;

    // 商户自定义数据
    private Map<String, String> metaData;

    // 支付通道额外数据
    private ChannelExtra extra = new ChannelExtra();

    // 客户信息数据
    private ClientInfo clientInfo;

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }

    public ChannelExtra getExtra() {
        return extra;
    }

    public void setExtra(ChannelExtra extra) {
        this.extra = extra;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChannelExtra {
        private String name;

        private String phone;

        private String idCard;

        private String bankCard;

        private String expirationDate;

        private String cvn2;

        public ChannelExtra() {
        }

        public ChannelExtra(String name, String phone, String idCard, String bankCard) {
            this.name = name;
            this.phone = phone;
            this.idCard = idCard;
            this.bankCard = bankCard;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getCvn2() {
            return cvn2;
        }

        public void setCvn2(String cvn2) {
            this.cvn2 = cvn2;
        }

        @Override
        public String toString() {
            return JacksonUtil.toJSON(this);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClientInfo {

        private String userId;

        private String clientIp;

        public ClientInfo() {
        }


        public ClientInfo(String userId, String clientIp) {
            this.userId = userId;
            this.clientIp = clientIp;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        @Override
        public String toString() {
            return JacksonUtil.toJSON(this);
        }
    }

}
