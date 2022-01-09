package com.pipedog.mixkit.compiler.utils;

import java.lang.StringBuilder;
import java.util.Random;

public class MixUUID {

    public static String UUID() {
        return UUID(16);
    }

    public static String UUID(int len) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(chars.length());
            builder.append(chars.charAt(number));
        }
        return builder.toString();
    }

}
