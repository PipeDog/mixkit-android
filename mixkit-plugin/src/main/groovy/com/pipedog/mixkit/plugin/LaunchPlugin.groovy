package com.pipedog.mixkit.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.pipedog.mixkit.plugin.config.ConfigManager
import com.pipedog.mixkit.plugin.core.RegisterTransform
import com.pipedog.mixkit.plugin.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * mixkit-plugin 插件入口
 * @author liang
 */
class LaunchPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "mixPluginConfig"

    @Override
    void apply(Project project) {
        println("`mixkit-plugin` launched")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (!isApp) {
            return
        }

        Logger.init(project)

        project.extensions.create(EXTENSION_NAME, ConfigManager)

        def transform = new RegisterTransform(project)
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(transform)

        // 配置参数完成回调
        project.afterEvaluate {
            ConfigManager configManager = project.extensions.findByName(EXTENSION_NAME) as ConfigManager
            configManager.mergeConfig()
            transform.setConfigManager(configManager)
        }
    }

}