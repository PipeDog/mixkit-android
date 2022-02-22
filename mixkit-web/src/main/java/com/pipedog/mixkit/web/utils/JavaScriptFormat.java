package com.pipedog.mixkit.web.utils;

import com.google.gson.Gson;
import com.pipedog.mixkit.tool.JsonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * js 脚本格式化工具
 * @author liang
 * @time 2022/02/22
 */
public class JavaScriptFormat {

    private static Gson sGson = JsonUtils.getMapGson();

    /**
     * 格式化 js 脚本
     * @param method js 方法名
     * @param arguments 参数列表
     * @return 格式化后的 js 脚本语句
     */
    public static String formatScript(String method, Object[] arguments) {
        return formatScript(null, method, arguments);
    }

    /**
     * 格式化 js 脚本
     * @param module module 变量名
     * @param method js 方法名
     * @param arguments 参数列表
     * @return 格式化后的 js 脚本语句
     */
    public static String formatScript(String module, String method, Object[] arguments) {
        if (method == null || method.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (module != null && !module.isEmpty()) {
            sb.append(module);
            sb.append(".");
        }

        if (arguments == null) {
            arguments = new Object[]{};
        }

        sb.append(method);

        // Avoid calling undefined JS methods
        String methodName = sb.toString();
        String prefix = String.format("if (%1$s && (typeof %2$s == 'function')) {", methodName, methodName);
        String suffix = "}";

        sb.append("(");

        int numberOfArguments = arguments.length;
        for (int i = 0; i < numberOfArguments; i++) {
            Object obj = arguments[i];

            if (obj == null) {
                sb.append("null");
            } else if (obj instanceof Arrays || obj instanceof List || obj instanceof Map) {
                String argument = sGson.toJson(obj);
                sb.append(argument);
            } else if (obj instanceof String) {
                String argument = String.format("'%s'", obj);
                sb.append(argument);
            } else if (obj instanceof Short ||
                    obj instanceof Integer ||
                    obj instanceof Long ||
                    obj instanceof Float ||
                    obj instanceof Double ||
                    obj instanceof Boolean ||
                    obj instanceof Character ||
                    obj instanceof CharSequence ||
                    obj instanceof char[]) {
                sb.append(obj);
            } else {
                String argument = sGson.toJson(obj);
                sb.append(argument);
            }

            if (i != numberOfArguments - 1) {
                sb.append(", ");
            }
        }

        sb.append(");");

        String script = prefix + sb.toString() + suffix;
        return script;
    }

}
