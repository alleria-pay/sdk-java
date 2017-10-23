package com.alleria.pay;

import com.alleria.util.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Created on 2017/6/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private static final Logger LOGGER = LoggerFactory.getLogger(Event.class);

    private String eventId;

    private String event;

    private Date createDate;

    private Map<String, Object> data;

    public Event() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JacksonUtil.toJSON(this);
    }
}
