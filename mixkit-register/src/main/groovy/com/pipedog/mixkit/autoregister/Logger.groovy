package com.pipedog.mixkit.autoregister

import org.gradle.api.Project

class Logger {

    static org.gradle.api.logging.Logger logger

    static void make(Project project) {
        logger = project.getLogger()
    }

    static void i(String info) {
        if (null != info && null != logger) {
            logger.info("Mix:Register == " + info)
        }
    }

    static void e(String error) {
        if (null != error && null != logger) {
            logger.error("Mix:Register == " + error)
        }
    }

    static void w(String warning) {
        if (null != warning && null != logger) {
            logger.warn("Mix:Register == " + warning)
        }
    }

}