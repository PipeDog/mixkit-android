package com.pipedog.messenger.clientdemo;

import android.os.Message;

import com.pipedog.mixkit.messenger.interfaces.IMessageVerifier;
import com.pipedog.mixkit.tool.MixLogger;

public class TestMessageVerifier implements IMessageVerifier {

    @Override
    public int getVerifierType() {
        return IMessageVerifier.VERIFIER_TYPE_SERVER | IMessageVerifier.VERIFIER_TYPE_CLIENT;
    }

    @Override
    public boolean isValidMessage(Message message) {
        MixLogger.error(">>>>>>>>>>>>>>> call isValidMessage in TestMessageVerifier");
        return true;
    }

}
