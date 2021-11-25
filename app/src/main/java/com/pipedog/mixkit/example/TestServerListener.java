package com.pipedog.mixkit.example;

import com.pipedog.mixkit.messenger.interfaces.IServerListener;
import com.pipedog.mixkit.messenger.model.ErrorMessage;
import com.pipedog.mixkit.tool.MixLogger;


public class TestServerListener implements IServerListener {

    @Override
    public void didFailSendMessage2SourceClient(ErrorMessage errorMessage) {
        MixLogger.error(">>>> mainAppLog source : " + errorMessage.toString());
    }

    @Override
    public void didFailSendMessage2TargetClient(ErrorMessage errorMessage) {
        MixLogger.error(">>>> mainAppLog target : " + errorMessage.toString());
    }

}
