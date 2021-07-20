package com.pipedog.mixkit.web;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixModuleCreator;
import com.pipedog.mixkit.parser.MixMessageParserManager;

public class MixWebViewBridge implements IMixBridge {

    private IMixWebViewBridgeDelegate mDelegate;
    private WebViewExecutor mExecutor;
    private MixModuleCreator mModuleCreator;
    private MixMessageParserManager mMessageParserManager;

    public MixWebViewBridge(IMixWebViewBridgeDelegate delegate) {
        mDelegate = delegate;
        mExecutor = new WebViewExecutor();
        mModuleCreator = new MixModuleCreator(this);
        mMessageParserManager = MixMessageParserManager.defaultManager();
    }

    public IMixWebViewBridgeDelegate bridgeDelegate() {
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
