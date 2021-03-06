package com.pipedog.mixkit.messenger.core;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IExecutor;
import com.pipedog.mixkit.kernel.ResultCallback;
import com.pipedog.mixkit.messenger.interfaces.IMessage2Server;
import com.pipedog.mixkit.messenger.model.ResponseMessage;
import com.pipedog.mixkit.module.MethodInvoker;
import com.pipedog.mixkit.module.ModuleManager;
import com.pipedog.mixkit.parser.IMessageParser;
import com.pipedog.mixkit.parser.MessageParserManager;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.tool.ThreadUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MessengerExecutor implements IExecutor {

    private class MessengerResultCallback implements ResultCallback {

        protected String mTraceId;
        protected String mSourceClientId;
        protected String mTargetClientId;
        protected String mCallbackId;

        protected MessengerResultCallback(String traceId,
                                          String sourceClientId,
                                          String targetClientId,
                                          String callbackId) {
            mTraceId = traceId;
            mSourceClientId = sourceClientId;
            mTargetClientId = targetClientId;
            mCallbackId = callbackId;
        }

        @Override
        public void invoke(Object[] response) {
            List<Object> args = Arrays.asList(response);
            invokeCallback(mTraceId, mSourceClientId, mTargetClientId, mCallbackId, args);
        }

    }

    private MessengerBridge mBridge;

    public MessengerExecutor() {
        // Do nothing...
    }

    @Override
    public void setBridge(IBridge bridge) {
        mBridge = (MessengerBridge)bridge;
    }

    @Override
    public boolean invokeMethod(Object metaData) {
        MessageParserManager parserManager = mBridge.getMessageParserManager();
        IMessageParser parser = parserManager.detectParser(metaData);
        if (parser == null) {
            return false;
        }

        IMessageParser.IMessageBody body = parser.getMessageBody();
        String moduleName = body.getModuleName();
        String methodName = body.getMethodName();

        ModuleManager moduleManager = ModuleManager.defaultManager();
        MethodInvoker invoker = moduleManager.getInvoker(moduleName, methodName);

        if (invoker == null) {
            MixLogger.error("Get invoker failed, module : %s, method : %s.",
                    moduleName, methodName);
            return false;
        }

        String className = invoker.getClassName();
        Object bridgeModule = mBridge.getModuleCreator().getModule(className);

        if (bridgeModule == null) {
            MixLogger.error("Get bridge module object failed, module : %s, method : %s.",
                    moduleName, methodName);
            return false;
        }

        List<Object> arguments = body.getArguments();
        if (arguments == null) {
            arguments = new ArrayList<>();
        }

        List<Object> nativeArgs = new ArrayList<>();

        Map<String, Object> map = (Map<String, Object>)metaData;
        String traceId = (String) map.get("traceId");
        String sourceClientId = (String) map.get("sourceClientId");
        String targetClientId = (String) map.get("targetClientId");

        for (Object arg : arguments) {
            Object nativeArg = arg;

            if (nativeArg instanceof String) {
                boolean isCallbackID = ((String)arg).startsWith("_$_mk_callback_$_");
                if (isCallbackID) {
                    MessengerResultCallback callback = new MessengerResultCallback(
                            traceId, sourceClientId, targetClientId, (String)arg);
                    nativeArg = callback;
                }
            }

            nativeArgs.add(nativeArg);
        }

        return invoker.invoke(bridgeModule, nativeArgs);
    }

    public void invokeCallback(String traceId,
                               String sourceClientId,
                               String targetClientId,
                               String callbackID,
                               List<Object> response) {
        if (callbackID == null || callbackID.isEmpty()) {
            return;
        }

        List<Object> resp = response != null ? response : new ArrayList<>();

        ThreadUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                IMessage2Server caller = mBridge.bridgeDelegate().serverCaller();
                caller.response2Server(new ResponseMessage(
                        traceId, sourceClientId, targetClientId, callbackID, resp));
            }
        });
    }
    
}
