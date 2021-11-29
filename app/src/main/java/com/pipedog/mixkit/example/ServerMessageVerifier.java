package com.pipedog.mixkit.example;

import android.os.Message;

import com.pipedog.mixkit.messenger.interfaces.IMessageVerifier;
import com.pipedog.mixkit.tool.MixLogger;

public class ServerMessageVerifier implements IMessageVerifier {

    @Override
    public int getVerifierType() {
        return IMessageVerifier.VERIFIER_TYPE_SERVER;
    }

    @Override
    public boolean isValidMessage(Message message) {
        MixLogger.info(">>>>>>>>>>>>>>>>>>>>> call isValidMessage >>>>>>>>>>>>>>>");
        return true;
    }

}
