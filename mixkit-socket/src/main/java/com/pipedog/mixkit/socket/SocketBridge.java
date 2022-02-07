package com.pipedog.mixkit.socket;

import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IExecutor;
import com.pipedog.mixkit.kernel.ModuleCreator;
import com.pipedog.mixkit.parser.MessageParserManager;

public class SocketBridge implements IBridge {

    private ISocketBridgeDelegate mDelegate;
    private SocketExecutor mExecutor;
    private ModuleCreator mModuleCreator;
    private MessageParserManager mMessageParserManager;

    public SocketBridge(ISocketBridgeDelegate delegate) {
        mDelegate = delegate;

        mExecutor = new SocketExecutor();
        mExecutor.setBridge(this);

        mModuleCreator = new ModuleCreator(this);
        mMessageParserManager = MessageParserManager.defaultManager();
    }

    public ISocketBridgeDelegate bridgeDelegate() {
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
