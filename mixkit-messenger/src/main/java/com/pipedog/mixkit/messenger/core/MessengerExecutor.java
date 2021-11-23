package com.pipedog.mixkit.messenger.core;

import android.content.Context;
import android.os.Parcelable;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.MessengerEngine;
import com.pipedog.mixkit.messenger.constants.MessageKeyword;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.module.MixMethodInvoker;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.parser.IMixMessageParser;
import com.pipedog.mixkit.parser.MixMessageParserManager;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessengerExecutor implements IMixExecutor {

    private class MessengerResultCallback implements MixResultCallback {

        protected String mSourceClientId;
        protected String mTargetClientId;
        protected String mCallbackId;

        protected MessengerResultCallback(String sourceClientId,
                                          String targetClientId,
                                          String callbackId) {
            mSourceClientId = sourceClientId;
            mTargetClientId = targetClientId;
            mCallbackId = callbackId;
        }

        @Override
        public void invoke(Object[] response) {
            List<Object> args = Arrays.asList(response);
            invokeCallback(mSourceClientId, mTargetClientId, mCallbackId, args);
        }

    }

    private MessengerBridge mBridge;

    public MessengerExecutor() {
        // Do nothing...
    }

    @Override
    public void setBridge(IMixBridge bridge) {
        mBridge = (MessengerBridge)bridge;
    }

    @Override
    public boolean invokeMethod(Object metaData) {
        MixMessageParserManager parserManager = mBridge.messageParserManager();
        IMixMessageParser parser = parserManager.detectParser(metaData);
        if (parser == null) {
            return false;
        }

        IMixMessageParser.IMixMessageBody body = parser.messageBody();
        String moduleName = body.moduleName();
        String methodName = body.methodName();

        MixModuleManager moduleManager = MixModuleManager.defaultManager();
        MixMethodInvoker invoker = moduleManager.getInvoker(moduleName, methodName);

        if (invoker == null) {
            MixLogger.error("Get invoker failed, module : %s, method : %s.",
                    moduleName, methodName);
            return false;
        }

        String className = invoker.getClassName();
        Object bridgeModule = mBridge.moduleCreator().getModule(className);

        if (bridgeModule == null) {
            MixLogger.error("Get bridge module object failed, module : %s, method : %s.",
                    moduleName, methodName);
            return false;
        }

        List<Object> arguments = body.arguments();
        if (arguments == null) {
            arguments = new ArrayList<>();
        }

        List<Object> nativeArgs = new ArrayList<>();

        Map<String, Object> map = (Map<String, Object>)metaData;
        String sourceClientId = (String) map.get(MessageKeyword.KEY_SOURCE_CLIENT_ID);
        String targetClientId = (String) map.get(MessageKeyword.KEY_TARGET_CLIENT_ID);

        for (Object arg : arguments) {
            Object nativeArg = arg;

            if (nativeArg instanceof String) {
                boolean isCallbackID = ((String)arg).startsWith("_$_mk_callback_$_");
                if (isCallbackID) {
                    MessengerResultCallback callback = new MessengerResultCallback(
                            sourceClientId, targetClientId, (String)arg);
                    nativeArg = callback;
                }
            }

            nativeArgs.add(nativeArg);
        }

        return invoker.invoke(bridgeModule, nativeArgs);
    }

    public void invokeCallback(String sourceClientId,
                               String targetClientId,
                               String callbackID,
                               List<Object> response) {
        if (callbackID == null || callbackID.isEmpty()) {
            return;
        }

        if (response == null) {
            response = new ArrayList<>();
        }

        IMessage2Server caller = mBridge.bridgeDelegate().serverCaller();
        caller.response2Server(sourceClientId, targetClientId, callbackID, response);
    }
    
}
