package com.pipedog.mixkit.autoregister

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * 自动注册插件入口
 * @author billy.qi
 * @since 17/3/14 17:35
 */
public class RegisterPlugin implements Plugin<Project> {
    public static final String EXT_NAME = 'autoregister'

    @Override
    public void apply(Project project) {
        /**
         * 注册transform接口
         */
        def isApp = project.plugins.hasPlugin(AppPlugin)
        project.extensions.create(EXT_NAME, AutoRegisterConfig)

        project.logger.warn(">>>>>>>> 111111 start apply!!!!")

        if (isApp) {
            project.logger.warn(">>>>>>>> 22222222 start apply!!!!")

            println 'project(' + project.name + ') apply auto-register plugin'

            println '>>>>>>>>>>>>> *************** TTTTTTTTTTTTTT'

            def android = project.extensions.getByType(AppExtension)
            def transformImpl = new RegisterTransform(project)
            android.registerTransform(transformImpl)
            project.afterEvaluate {
                println '>>>>>>>>> aaaaaaaaaa'
                init(project, transformImpl)//此处要先于transformImpl.transform方法执行
                println '>>>>>>>>> bbbbbbbb'
            }
        }
    }

    static void init(Project project, RegisterTransform transformImpl) {
        println '>>>>>>>>> cccccccccc'

        AutoRegisterConfig config = project.extensions.findByName(EXT_NAME) as AutoRegisterConfig
        config.project = project
        config.convertConfig()
        transformImpl.config = config
    }

}
