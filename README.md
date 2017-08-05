# java sdk

## 使用示例

见`src\test\java\com\alleria\SdkTest`

### 初始化

```
private Sdk sdk = new Sdk()
        .setBaseUrl("Api地址")
        .setAppId(商户AppId)
        .setPrivateKeyStr("商户私钥")
        .setPublicKeyStr("平台公钥");
```

### 发起交易

```
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
```

### 交易确认

`快捷交易时需要发送短信验证码进行确认`

```
ConfirmRequest request = new ConfirmRequest();
request.setOrderNo(348318168287727962L);
request.setMerchantOrderNo("1501468474670181009");
request.setSmsCode("123456");
request.setPassword("123456");
PayOrder order = sdk.pay().confirm(request);
```

### 交易订单查询

```
ConfirmRequest request = new ConfirmRequest();
request.setOrderNo(348318168287727962L);
request.setMerchantOrderNo("1501468474670181009");
PayOrder order = sdk.pay().query(request);
```

