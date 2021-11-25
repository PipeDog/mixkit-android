package com.pipedog.mixkit.messenger.server;

import com.pipedog.mixkit.messenger.interfaces.IServerListener;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.tool.MixLogger;

/**
 * 服务监听器（内置日志打印功能）
 * @author liang
 * @time 2021/11/25
 */
public class LogServerListener implements IServerListener {

    @Override
    public void didFailSendMessage2SourceClient(ErrorMessage errorMessage) {
        MixLogger.error(errorMessage.toString());
    }

    @Override
    public void didFailSendMessage2TargetClient(ErrorMessage errorMessage) {
        MixLogger.error(errorMessage.toString());
    }

}
