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

    private String sourceClientId;
    private Map<String, MixModuleData> moduleDataMap;

    public RegisterClientMessage(String sourceClientId,
                                 Map<String, MixModuleData> moduleDataMap) {
        this.sourceClientId = sourceClientId;
        this.moduleDataMap = moduleDataMap;
    }


    // GETTER、SETTER METHODS

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
                "sourceClientId='" + sourceClientId + '\'' +
                ", moduleDataMap=" + moduleDataMap +
                '}';
    }

}
