package com.pipedog.mixkit.plugin

import com.android.build.gradle.AppPlugin
import com.pipedog.mixkit.plugin.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

class LaunchPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("配置成功 => Call apply")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (!isApp) {
            return
        }

        Logger.init(project)


    }

}