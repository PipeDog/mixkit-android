package com.pipedog.mixkit.tool;

import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.tool.NumberUtils;
import com.pipedog.mixkit.tool.StringUtils;

/**
 * 数据类型转换工具
 * @author liang
 */
public class TypeConverter {

    public static byte toByte(Object arg) {
        String str = arg.toString();
        if (StringUtils.isBlank(str)) {
            return (byte)0;
        }

        if (StringUtils.contains(str, ".")) {
            double d = toDouble(arg);
            return (byte)d;
        }

        return NumberUtils.toByte(arg.toString());
    }

    public static int toInt(Object arg) {
        String str = arg.toString();
        if (StringUtils.isBlank(str)) {
            return (int)0;
        }

        if (StringUtils.contains(str, ".")) {
            double d = toDouble(arg);
            return (int)d;
        }

        return NumberUtils.toInt(arg.toString());
    }

    public static short toShort(Object arg) {
        String str = arg.toString();
        if (StringUtils.isBlank(str)) {
            return (short)0;
        }

        if (StringUtils.contains(str, ".")) {
            double d = toDouble(arg);
            return (short)d;
        }

        return NumberUtils.toShort(arg.toString());
    }

    public static long toLong(Object arg) {
        String str = arg.toString();
        if (StringUtils.isBlank(str)) {
            return (long)0;
        }

        if (StringUtils.contains(str, ".")) {
            double d = toDouble(arg);
            return (long)d;
        }

        return NumberUtils.toLong(arg.toString());
    }

    public static float toFloat(Object arg) {
        return NumberUtils.toFloat(arg.toString());
    }

    public static double toDouble(Object arg) {
        return NumberUtils.toDouble(arg.toString());
    }

    public static boolean toBoolean(Object arg) {
        try {
            return Boolean.parseBoolean(arg.toString());
        } catch (Exception e) {
            MixLogger.error("Convert arg `%s` to boolean failed!", arg.toString());
            return false;
        }
    }

    public static char toChar(Object arg) {
        try {
            return arg.toString().charAt(0);
        } catch (Exception e) {
            MixLogger.error("Convert arg `%s` to char failed!", arg.toString());
            return (char)0;
        }
    }

    public static String toString(Object arg) {
        if (arg == null) {
            return null;
        }
        return arg.toString();
    }

}
