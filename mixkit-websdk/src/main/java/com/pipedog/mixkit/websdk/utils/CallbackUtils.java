package com.pipedog.mixkit.websdk.utils;

import com.pipedog.mixkit.kernel.ResultCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liang
 * @time 2022/06/08
 * @desc callback 回调工具
 */
public class CallbackUtils {

    /**
     * 状态码 - 成功唯一标识
     */
    public static final int CALLBACK_CODE_UNIQUE_SUCCESS = 0;

    /**
     * 状态码 - 通用失败状态（如果需要表示特殊的业务含义，另行定义，这里不提供）
     */
    public static final int CALLBACK_CODE_COMMON_ERROR = 100;


    /**
     * 执行回调，并传递响应数据
     * @param callback 回调对象
     * @param code 状态码，0 表示成功，其他都是失败
     * @param message 信息描述
     * @param data 具体数据
     */
    public static void invoke(ResultCallback callback, int code, String message, Map<String, Object> data) {
        if (callback == null) {
            return;
        }

        Object[] response = getResponseResult(code, message, data);
        callback.invoke(response);
    }

    /**
     * 创建固定格式的响应数据体
     * @param code 状态码，0 表示成功，其他都是失败
     * @param message 信息描述
     * @param data 具体数据
     */
    public static Object[] getResponseResult(int code, String message, Map<String, Object> data) {
        Map<String, Object> resp = getResponse(code, message, data);
        Object[] args = new Object[]{ resp };
        return args;
    }

    /**
     * 创建固定格式的响应数据体
     * @param code 状态码，0 表示成功，其他都是失败
     * @param message 信息描述
     * @param data 具体数据
     */
    public static Map<String, Object> getResponse(int code, String message, Map<String, Object> data) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", (Integer) code);
        resp.put("message", message);
        resp.put("data", data != null ? data : new HashMap<>());
        return resp;
    }

}
