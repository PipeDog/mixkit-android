package com.pipedog.mixkit.messenger.interfaces;

import android.os.Parcelable;

import java.util.Map;

/**
 * 消息回调接口
 * @author liang
 * @time 2021/11/22
 */
public interface IMessageCallback<T> {

    /**
     * 通知回调
     * @param error 错误信息（泛型，外部自己约定类型）
     * @param result 响应结果数据实体
     */
    void callback(T error, Map<String, Object> result);

}
