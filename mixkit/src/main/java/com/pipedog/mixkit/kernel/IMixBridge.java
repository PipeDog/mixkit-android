package com.pipedog.mixkit.kernel;

import com.pipedog.mixkit.parser.MixMessageParserManager;

/**
 * bridge 接口抽象
 * @author liang
 */
public interface IMixBridge {

    /**
     * 获取执行器
     */
    public IMixExecutor getExecutor();

    /**
     * 获取 module 构造器
     */
    public MixModuleCreator getModuleCreator();

    /**
     * 获取消息解析器管理实例
     */
    public MixMessageParserManager getMessageParserManager();

}
