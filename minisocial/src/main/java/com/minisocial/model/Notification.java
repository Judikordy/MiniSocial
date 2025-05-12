package com.minisocial.model;

public class Notification {
    private String eventType;
    private Long userId;
    private String message;
    private String timestamp;
    private Object additionalData;

    public Notification(String eventType, Long userId, String message, String timestamp, Object additionalData) {
        this.eventType = eventType;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.additionalData = additionalData;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Object additionalData) {
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "eventType='" + eventType + '\'' +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", additionalData=" + additionalData +
                '}';
    }
}
