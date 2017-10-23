package com.alleria.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created on 2014/9/18.
 */
public abstract class JacksonUtil {
    protected static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

    public static <T> String toJSON(T t) {
        String json = "";
        try {
            json = mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            LOGGER.error("An unexpected error occurred.", e);
        }
        return json;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("An unexpected error occurred.", e);
            return null;
        }
    }

    public static <T> T toObject(Map<String, Object> data, Class<T> clz) {
        try {
            return mapper.convertValue(data, clz);
        } catch (IllegalArgumentException e) {
            LOGGER.error("An unexpected error occurred.", e);
            return null;
        }
    }

}
