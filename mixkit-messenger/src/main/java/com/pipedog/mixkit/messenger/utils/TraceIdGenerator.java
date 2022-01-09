package com.pipedog.mixkit.messenger.utils;

import java.util.Random;

public class TraceIdGenerator {

    private static final String STR_GENERATE_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 获取 traceId
     */
    public static String getTraceId() {
        return "_$_mk_traceId_$_" + getRandomString(16);
    }

    private static String getRandomString(int len) {
        String str = STR_GENERATE_POOL;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < len; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
