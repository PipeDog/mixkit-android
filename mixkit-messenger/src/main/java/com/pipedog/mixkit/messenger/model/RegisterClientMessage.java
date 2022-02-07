package com.pipedog.mixkit.messenger.model;

import com.pipedog.mixkit.module.ModuleData;

import java.io.Serializable;
import java.util.Map;

/**
 * 注册客户端数据封装类
 * @author liang
 * @time 2021/11/25
 */
public class RegisterClientMessage implements Serializable {

    private String sourceClientId;
    private Map<String, ModuleData> moduleDataMap;

    public RegisterClientMessage(String sourceClientId,
                                 Map<String, ModuleData> moduleDataMap) {
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

    public Map<String, ModuleData> getModuleDataMap() {
        return moduleDataMap;
    }

    public void setModuleDataMap(Map<String, ModuleData> moduleDataMap) {
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
