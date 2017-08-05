package com.alleria.util;

import com.alleria.AppRequest;
import com.alleria.AppResponse;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/7/31.
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(
                    new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            LOGGER.debug(message);
                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    public static AppResponse post(String url, AppRequest appRequest) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                appRequest.json());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            return AppResponse.from(body);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("连接异常");
        }
    }

}
