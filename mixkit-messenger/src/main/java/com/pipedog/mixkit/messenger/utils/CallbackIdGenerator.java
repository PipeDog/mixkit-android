package com.pipedog.mixkit.messenger.utils;

import java.util.Random;

/**
 * 回调 ID 生成器
 * @author liang
 * @time 2021/11/22
 */
public class CallbackIdGenerator {

    private static final String STR_GENERATE_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 获取回调 ID
     */
    public static String getCallbackId() {
        return "_$_mk_callback_$_" + getRandomString(16);
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
