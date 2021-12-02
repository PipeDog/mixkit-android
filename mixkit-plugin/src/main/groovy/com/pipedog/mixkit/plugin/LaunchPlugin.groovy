package com.pipedog.mixkit.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class LaunchPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("配置成功 => Call apply")
    }

}