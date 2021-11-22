package com.pipedog.mixkit.messenger.core;

import android.os.Parcelable;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.interfaces.IMessageCallback;
import com.pipedog.mixkit.module.MixMethodInvoker;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.parser.IMixMessageParser;
import com.pipedog.mixkit.parser.MixMessageParserManager;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MessengerExecutor implements IMixExecutor {

    private class MessengerResultCallback implements MixResultCallback {

        protected String mCallbackId;

        protected MessengerResultCallback(String callbackId) {
            mCallbackId = callbackId;
        }

        @Override
        public void invoke(Object[] arguments) {
            List<Object> args = Arrays.asList(arguments);
            invokeCallback(args, mCallbackId);
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

        for (Object arg : arguments) {
            Object nativeArg = arg;

            if (nativeArg instanceof String) {
                boolean isCallbackID = ((String)arg).startsWith("_$_mk_callback_$_");
                if (isCallbackID) {
                    MessengerResultCallback callback = new MessengerResultCallback((String)arg);
                    nativeArg = callback;
                }
            }

            nativeArgs.add(nativeArg);
        }

        return invoker.invoke(bridgeModule, nativeArgs);
    }

    private void invokeCallback(List<Object> arguments, String callbackID) {
        if (callbackID == null || callbackID.isEmpty()) {
            return;
        }

        if (arguments == null || arguments.isEmpty()) {
            arguments = new ArrayList<>();
        }

        Map<String, Parcelable> result = arguments.size() > 0 ? arguments.get(0) : null;

        IMessage2Server caller = mBridge.bridgeDelegate().serverCaller();
        caller.response2Server(callbackID, result, new IMessageCallback() {
            @Override
            public void callback(Object error, Map result) {
                if (error == null) {
                    return;
                }
                MixLogger.error("invoke socket failed : %s", error.toString());
            }
        });
    }
    
}