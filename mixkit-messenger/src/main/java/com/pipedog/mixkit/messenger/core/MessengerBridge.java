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
