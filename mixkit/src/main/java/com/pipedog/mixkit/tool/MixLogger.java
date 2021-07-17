package com.pipedog.mixkit.tool;

import java.util.Formatter;
import java.util.logging.Logger;

public class MixLogger {

    private static final Logger mLogger = Logger.getLogger("MixLogger");

    public static void info(String format, Object... args) {
        String msg = new Formatter().format(format, args).toString();
        info(msg);
    }

    public static void warning(String format, Object... args) {
        String msg = new Formatter().format(format, args).toString();
        warning(msg);
    }

    public static void error(String format, Object... args) {
        String msg = new Formatter().format(format, args).toString();
        error(msg);
    }

    public static void info(String msg) {
        mLogger.info("[Mix|info] " + msg);
    }

    public static void warning(String msg) {
        mLogger.warning("[Mix|warning] " + msg);
    }

    public static void error(String msg) {
        mLogger.severe("[Mix|error] " + msg);
    }

}
