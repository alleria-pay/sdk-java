package com.alleria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2017/7/31.
 */
public class AppResult {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppResult.class);

    protected String message;

    protected boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
