package com.pipedog.mixkit.web;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixModuleCreator;
import com.pipedog.mixkit.parser.MixMessageParserManager;

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

    @Override
    public IMixExecutor executor() {
        return mExecutor;
    }

    @Override
    public MixModuleCreator moduleCreator() {
        return mModuleCreator;
    }

    @Override
    public MixMessageParserManager messageParserManager() {
        return mMessageParserManager;
    }

}
