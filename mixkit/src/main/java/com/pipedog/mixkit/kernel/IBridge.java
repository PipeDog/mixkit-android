package com.pipedog.mixkit.kernel;

import com.pipedog.mixkit.parser.MessageParserManager;

/**
 * bridge 接口抽象
 * @author liang
 */
public interface IBridge {

    /**
     * 获取执行器
     */
    IExecutor getExecutor();

    /**
     * 获取 module 构造器
     */
    ModuleCreator getModuleCreator();

    /**
     * 获取消息解析器管理实例
     */
    MessageParserManager getMessageParserManager();

}
