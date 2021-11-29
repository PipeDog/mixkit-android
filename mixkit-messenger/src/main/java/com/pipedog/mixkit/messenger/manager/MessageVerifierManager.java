package com.pipedog.mixkit.messenger.manager;

import android.os.Message;

import com.pipedog.mixkit.messenger.interfaces.IMessageVerifier;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.List;

public class MessageVerifierManager {

    public static final int MANAGER_TYPE_SERVER = 1;
    public static final int MANAGER_TYPE_CLIENT = 2;

    private int mType;
    private List<IMessageVerifier> mVerifiers = new ArrayList<>();

    public MessageVerifierManager(int type) {
        mType = type;
        autoRegisterVerifier();
    }

    private void autoRegisterVerifier() {
        // Call this function in constructor
        // The code will be inserted automatically during compilation
        // The insert code will call function `registerVerifier` here
    }

    private void registerVerifier(IMessageVerifier verifier) {
        if (verifier == null) {
            return;
        }

        if (mType == MANAGER_TYPE_SERVER &&
                (verifier.getVerifierType() & IMessageVerifier.VERIFIER_TYPE_SERVER) == IMessageVerifier.VERIFIER_TYPE_SERVER) {
            mVerifiers.add(verifier);
        } else if (mType == MANAGER_TYPE_CLIENT &&
                (verifier.getVerifierType() & IMessageVerifier.VERIFIER_TYPE_CLIENT) == IMessageVerifier.VERIFIER_TYPE_CLIENT) {
            mVerifiers.add(verifier);
        } else { }
    }

    public boolean isValidMessage(Message message) {
        if (mVerifiers.isEmpty()) {
            return true;
        }

        for (IMessageVerifier verifier : mVerifiers) {
            if (!verifier.isValidMessage(message)) {
                return false;
            }
        }

        return true;
    }

}
