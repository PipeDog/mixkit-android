package com.pipedog.mixkit.messenger.model;

import com.pipedog.mixkit.module.MixModuleData;

import java.io.Serializable;
import java.util.Map;

/**
 * 注册客户端数据封装类
 * @author liang
 * @time 2021/11/25
 */
public class RegisterClientMessage implements Serializable {

    private String traceId;
    private String sourceClientId;
    private Map<String, MixModuleData> moduleDataMap;

    public RegisterClientMessage(String traceId,
                                 String sourceClientId,
                                 Map<String, MixModuleData> moduleDataMap) {
        this.traceId = traceId;
        this.sourceClientId = sourceClientId;
        this.moduleDataMap = moduleDataMap;
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

    public Map<String, MixModuleData> getModuleDataMap() {
        return moduleDataMap;
    }

    public void setModuleDataMap(Map<String, MixModuleData> moduleDataMap) {
        this.moduleDataMap = moduleDataMap;
    }


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "RegisterClientMessage{" +
                "traceId='" + traceId + '\'' +
                ", sourceClientId='" + sourceClientId + '\'' +
                ", moduleDataMap=" + moduleDataMap +
                '}';
    }

}
