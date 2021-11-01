package com.pipedog.mixkit.register

import org.gradle.api.Project
import org.gradle.api.logging.Logger

class RLogger {

    private static org.gradle.api.logging.Logger sLogger;

    public static void init(Project project) {
        sLogger = project.getLogger();
    }

    public static void debug(String format, Object... args) {
        String msg = new Formatter().format(format, args).toString();
        debug(msg);
    }

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

    public static void debug(String msg) {
        sLogger.info("[Mix - Register|debug] " + msg);
    }

    public static void info(String msg) {
        sLogger.info("[Mix - Register|info] " + msg);
    }

    public static void warning(String msg) {
        sLogger.warning("[Mix - Register|warn] " + msg);
    }

    public static void error(String msg) {
        sLogger.severe("[Mix - Register|error] " + msg);
    }

}
