package com.pipedog.mixkit.web;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IExecutor;
import com.pipedog.mixkit.kernel.ModuleCreator;
import com.pipedog.mixkit.parser.MessageParserManager;

/**
 * native-js 交互 bridge 实现
 * @author liang
 */
public class WebViewBridge implements IBridge {

    private IWebViewBridgeDelegate mDelegate;
    private WebViewExecutor mExecutor;
    private ModuleCreator mModuleCreator;
    private MessageParserManager mMessageParserManager;

    public WebViewBridge(IWebViewBridgeDelegate delegate) {
        mDelegate = delegate;

        mExecutor = new WebViewExecutor();
        mExecutor.setBridge(this);

        mModuleCreator = new ModuleCreator(this);
        mMessageParserManager = MessageParserManager.defaultManager();
    }

    public IWebViewBridgeDelegate bridgeDelegate() {
        return mDelegate;
    }


    // OVERRIDE METHODS

    @Override
    public IExecutor getExecutor() {
        return mExecutor;
    }

    @Override
    public ModuleCreator getModuleCreator() {
        return mModuleCreator;
    }

    @Override
    public MessageParserManager getMessageParserManager() {
        return mMessageParserManager;
    }

}
