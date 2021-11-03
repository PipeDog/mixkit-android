package com.pipedog.mixkit.autoregister

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

// 自动注册插件入口
public class RegisterPlugin implements Plugin<Project> {
    public static final String EXT_NAME = 'autoregister'

    @Override
    public void apply(Project project) {

        // 注册 transform 接口
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) { return }

        project.extensions.create(EXT_NAME, AutoRegisterConfig)

        println 'project(' + project.name + ') apply auto-register plugin'

        def android = project.extensions.getByType(AppExtension)
        def transformImpl = new RegisterTransform(project)
        android.registerTransform(transformImpl)

        project.afterEvaluate {
            // 此处要先于 transformImpl.transform 方法执行
            init(project, transformImpl)
        }
    }

    static void init(Project project, RegisterTransform transformImpl) {
        AutoRegisterConfig config = project.extensions.findByName(EXT_NAME) as AutoRegisterConfig
        config.project = project
        config.convertConfig()
        transformImpl.config = config
    }

}
