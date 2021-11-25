package com.pipedog.mixkit.messenger.model;

import java.io.Serializable;

/**
 * 错误消息数据封装类
 * @author liang
 * @time 2021/11/25
 */
public class ErrorMessage implements Serializable {

    private int code;
    private String message;
    private String sourceClientId;
    private String targetClientId;

    public ErrorMessage(int code,
                        String message,
                        String sourceClientId,
                        String targetClientId) {
        this.code = code;
        this.message = message;
        this.sourceClientId = sourceClientId;
        this.targetClientId = targetClientId;
    }


    // GETTER、SETTER METHODS

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceClientId() {
        return sourceClientId;
    }

    public void setSourceClientId(String sourceClientId) {
        this.sourceClientId = sourceClientId;
    }

    public String getTargetClientId() {
        return targetClientId;
    }

    public void setTargetClientId(String targetClientId) {
        this.targetClientId = targetClientId;
    }


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", sourceClientId='" + sourceClientId + '\'' +
                ", targetClientId='" + targetClientId + '\'' +
                '}';
    }

}
