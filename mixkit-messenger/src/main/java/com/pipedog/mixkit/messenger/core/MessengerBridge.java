package com.pipedog.mixkit.messenger.core;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixModuleCreator;
import com.pipedog.mixkit.parser.MixMessageParserManager;

public class MessengerBridge implements IMixBridge {

    private IMessengerBridgeDelegate mDelegate;
    private MessengerExecutor mExecutor;
    private MixModuleCreator mModuleCreator;
    private MixMessageParserManager mMessageParserManager;

    public MessengerBridge(IMessengerBridgeDelegate delegate) {
        mDelegate = delegate;

        mExecutor = new MessengerExecutor();
        mExecutor.setBridge(this);

        mModuleCreator = new MixModuleCreator(this);
        mMessageParserManager = MixMessageParserManager.defaultManager();
    }

    public IMessengerBridgeDelegate bridgeDelegate() {
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
