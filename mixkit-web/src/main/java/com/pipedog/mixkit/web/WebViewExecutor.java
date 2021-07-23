package com.pipedog.mixkit.web;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixResultCallback;
import com.pipedog.mixkit.module.MixMethodInvoker;
import com.pipedog.mixkit.module.MixModuleManager;
import com.pipedog.mixkit.parser.MixMessageParserManager;
import com.pipedog.mixkit.parser.IMixMessageParser;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebViewExecutor implements IMixExecutor {

    private class WebResultCallback implements MixResultCallback {

        public String mCallbackId;

        public WebResultCallback(String callbackId) {
            mCallbackId = callbackId;
        }

        @Override
        public void invoke(Object[] arguments) {
            List<Object> args = new ArrayList<Object>(Arrays.asList(arguments));
            invokeCallback(args, mCallbackId);
        }

    }

    private static String sBridgeName;
    private static String sFunctionName;

    private MixWebViewBridge mBridge;

    public WebViewExecutor() {
        if (sBridgeName == null) {
            registerCallbackScript("NativeModules", "invokeCallback");
        }
    }

    public static void registerCallbackScript(String bridgeName, String funcName) {
        sBridgeName = bridgeName;
        sFunctionName = funcName;
    }

    @Override
    public void setBridge(IMixBridge bridge) {
        mBridge = (MixWebViewBridge)bridge;
    }

    @Override
    public boolean invokeMethod(Object metaData) {
        MixMessageParserManager parserManager = mBridge.messageParserManager();

        IMixMessageParser parser = parserManager.detectParser(metaData);
        if (parser == null) { return false; }

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
            arguments = new ArrayList<Object>();
        }

        List<Object> nativeArgs = new ArrayList<Object>();

        for (Object arg : arguments) {
            Object nativeArg = arg;

            if (nativeArg instanceof String) {
                boolean isCallbackID = ((String)arg).startsWith("_$_mk_callback_$_");
                if (isCallbackID) {
                    WebResultCallback callback = new WebResultCallback((String)arg);
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

        List<Object> jsArgs = new ArrayList<Object>();
        jsArgs.add(callbackID);

        if (arguments == null || arguments.isEmpty()) {
            arguments = new ArrayList<>();
        }
        jsArgs.add(arguments);

        IMixScriptEngine scriptEngine = mBridge.bridgeDelegate().scriptEngine();
        scriptEngine.invokeMethod(sBridgeName, sFunctionName, jsArgs.toArray(), new ScriptCallback() {
            @Override
            public void onReceiveValue(String value) {
                MixLogger.info("js return value : %s", value);
            }

            @Override
            public void onReceiveError(String error) {
                MixLogger.error("invoke js failed : %s", error);
            }
        });
    }

}
