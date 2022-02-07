package com.pipedog.mixkit.web;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IExecutor;
import com.pipedog.mixkit.kernel.ResultCallback;
import com.pipedog.mixkit.module.MethodInvoker;
import com.pipedog.mixkit.module.ModuleManager;
import com.pipedog.mixkit.parser.MessageParserManager;
import com.pipedog.mixkit.parser.IMessageParser;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 消息执行器实现（webView 接收到的消息会被传递至这里，并最终执行）
 * @author liang
 */
public class WebViewExecutor implements IExecutor {

    private class WebResultCallback implements ResultCallback {

        public String mCallbackId;

        public WebResultCallback(String callbackId) {
            mCallbackId = callbackId;
        }

        @Override
        public void invoke(Object[] response) {
            List<Object> args = Arrays.asList(response);
            invokeCallback(args, mCallbackId);
        }

    }

    private static String sBridgeName;
    private static String sFunctionName;

    private WebViewBridge mBridge;

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
    public void setBridge(IBridge bridge) {
        mBridge = (WebViewBridge)bridge;
    }

    @Override
    public boolean invokeMethod(Object metaData) {
        MessageParserManager parserManager = mBridge.getMessageParserManager();

        IMessageParser parser = parserManager.detectParser(metaData);
        if (parser == null) { return false; }

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

        IScriptEngine scriptEngine = mBridge.bridgeDelegate().getScriptEngine();
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
