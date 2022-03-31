package com.pipedog.mixkit.messenger.core;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IExecutor;
import com.pipedog.mixkit.module.ModuleCreator;
import com.pipedog.mixkit.parser.MessageParserManager;

public class MessengerBridge implements IBridge {

    private IMessengerBridgeDelegate mDelegate;
    private MessengerExecutor mExecutor;
    private ModuleCreator mModuleCreator;
    private MessageParserManager mMessageParserManager;

    public MessengerBridge(IMessengerBridgeDelegate delegate) {
        mDelegate = delegate;

        mExecutor = new MessengerExecutor();
        mExecutor.setBridge(this);

        mModuleCreator = new ModuleCreator(this);
        mMessageParserManager = MessageParserManager.defaultManager();
    }

    public IMessengerBridgeDelegate bridgeDelegate() {
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
