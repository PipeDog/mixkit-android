package com.pipedog.mixkit.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.pipedog.mixkit.plugin.config.ConfigManager
import com.pipedog.mixkit.plugin.core.RegisterTransform
import com.pipedog.mixkit.plugin.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

class LaunchPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("`mixkit-plugin` launched")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (!isApp) {
            return
        }

        Logger.init(project)

        ConfigManager configManager = new ConfigManager()
        def transform = new RegisterTransform(project)
        transform.setConfigManager(configManager)

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(transform)
    }

}