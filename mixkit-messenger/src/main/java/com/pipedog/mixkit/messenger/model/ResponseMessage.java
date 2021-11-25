package com.pipedog.mixkit.messenger.model;

import java.io.Serializable;
import java.util.List;

/**
 * 响应消息数据封装类
 * @author liang
 * @time 2021/11/25
 */
public class ResponseMessage implements Serializable {

    private String sourceClientId;
    private String targetClientId;
    private String callbackId;
    private List<Object> response;

    public ResponseMessage(String sourceClientId,
                           String targetClientId,
                           String callbackId,
                           List<Object> response) {
        this.sourceClientId = sourceClientId;
        this.targetClientId = targetClientId;
        this.callbackId = callbackId;
        this.response = response;
    }


    // GETTER、SETTER METHODS

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

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public List<Object> getResponse() {
        return response;
    }

    public void setResponse(List<Object> response) {
        this.response = response;
    }


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "sourceClientId='" + sourceClientId + '\'' +
                ", targetClientId='" + targetClientId + '\'' +
                ", callbackId='" + callbackId + '\'' +
                ", response=" + response +
                '}';
    }

}
