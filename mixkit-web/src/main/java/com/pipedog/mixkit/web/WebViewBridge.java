package com.pipedog.mixkit.web;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixModuleCreator;
import com.pipedog.mixkit.parser.MixMessageParserManager;

/**
 * native-js 交互 bridge 实现
 * @author liang
 */
public class WebViewBridge implements IMixBridge {

    private IWebViewBridgeDelegate mDelegate;
    private WebViewExecutor mExecutor;
    private MixModuleCreator mModuleCreator;
    private MixMessageParserManager mMessageParserManager;

    public WebViewBridge(IWebViewBridgeDelegate delegate) {
        mDelegate = delegate;

        mExecutor = new WebViewExecutor();
        mExecutor.setBridge(this);

        mModuleCreator = new MixModuleCreator(this);
        mMessageParserManager = MixMessageParserManager.defaultManager();
    }

    public IWebViewBridgeDelegate bridgeDelegate() {
        return mDelegate;
    }


    // OVERRIDE METHODS

    @Override
    public IMixExecutor getExecutor() {
        return mExecutor;
    }

    @Override
    public MixModuleCreator getModuleCreator() {
        return mModuleCreator;
    }

    @Override
    public MixMessageParserManager getMessageParserManager() {
        return mMessageParserManager;
    }

}
