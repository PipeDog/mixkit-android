package com.pipedog.mixkit.socket;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.module.MixMethodInvoker;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.parser.IMixMessageParser;
import com.pipedog.mixkit.parser.MixMessageParserManager;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketExecutor implements IMixExecutor {

    private class SocketResultCallback implements MixResultCallback {

        public String mCallbackId;

        public SocketResultCallback(String callbackId) {
            mCallbackId = callbackId;
        }

        @Override
        public void invoke(Object[] response) {
            List<Object> args = Arrays.asList(response);
            invokeCallback(args, mCallbackId);
        }

    }

    private SocketBridge mBridge;

    public SocketExecutor() {
        // Do nothing...
    }

    @Override
    public void setBridge(IMixBridge bridge) {
        mBridge = (SocketBridge)bridge;
    }

    @Override
    public boolean invokeMethod(Object metaData) {
        MixMessageParserManager parserManager = mBridge.getMessageParserManager();
        IMixMessageParser parser = parserManager.detectParser(metaData);
        if (parser == null) { return false; }

        IMixMessageParser.IMixMessageBody body = parser.messageBody();
        String moduleName = body.getModuleName();
        String methodName = body.getMethodName();

        MixModuleManager moduleManager = MixModuleManager.defaultManager();
        MixMethodInvoker invoker = moduleManager.getInvoker(moduleName, methodName);

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
            arguments = new ArrayList<Object>();
        }

        List<Object> nativeArgs = new ArrayList<Object>();

        for (Object arg : arguments) {
            Object nativeArg = arg;

            if (nativeArg instanceof String) {
                boolean isCallbackID = ((String)arg).startsWith("_$_mk_callback_$_");
                if (isCallbackID) {
                    SocketResultCallback callback = new SocketResultCallback((String)arg);
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

        List<Object> callbackArgs = new ArrayList<Object>();
        callbackArgs.add(callbackID);

        if (arguments == null || arguments.isEmpty()) {
            arguments = new ArrayList<>();
        }
        callbackArgs.add(arguments);

        ISocketEngine socketEngine = mBridge.bridgeDelegate().socketEngine();
        socketEngine.sendData(callbackArgs.toArray(), new SocketCallback() {
            @Override
            public void onReceiveFailure(Object error) {
                if (error == null) {
                    return;
                }
                MixLogger.error("invoke socket failed : %s", error.toString());
            }
        });
    }

}

