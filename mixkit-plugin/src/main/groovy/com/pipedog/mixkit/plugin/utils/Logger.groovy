package com.pipedog.mixkit.plugin.utils

import org.gradle.api.Project

class Logger {

    static org.gradle.api.logging.Logger sLogger

    static void init(Project project) {
        sLogger = project.getLogger()
    }

    static void i(String info) {
        if (null != info && null != sLogger) {
            sLogger.info("Mix:Register == " + info)
        }
    }

    static void e(String error) {
        if (null != error && null != sLogger) {
            sLogger.error("Mix:Register == " + error)
        }
    }

    static void w(String warning) {
        if (null != warning && null != sLogger) {
            sLogger.warn("Mix:Register == " + warning)
        }
    }

}