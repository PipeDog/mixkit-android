package com.pipedog.mixkit.messenger.model;

import java.io.Serializable;
import java.util.List;

/**
 * 请求消息数据封装类
 * @author liang
 * @time 2021/11/25
 */
public class RequestMessage implements Serializable {

    private String traceId;
    private String sourceClientId;
    private String targetClientId;
    private String moduleName;
    private String methodName;
    private List<Object> arguments;

    public RequestMessage(String traceId,
                          String sourceClientId,
                          String targetClientId,
                          String moduleName,
                          String methodName,
                          List<Object> arguments) {
        this.traceId = traceId;
        this.sourceClientId = sourceClientId;
        this.targetClientId = targetClientId;
        this.moduleName = moduleName;
        this.methodName = methodName;
        this.arguments = arguments;
    }


    // GETTER、SETTER METHODS

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "RequestMessage{" +
                "traceId='" + traceId + '\'' +
                ", sourceClientId='" + sourceClientId + '\'' +
                ", targetClientId='" + targetClientId + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", arguments=" + arguments +
                '}';
    }

}
