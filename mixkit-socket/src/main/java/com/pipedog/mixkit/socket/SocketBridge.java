package com.pipedog.mixkit.socket;

import com.pipedog.mixkit.kernel.IMixBridge;
import com.pipedog.mixkit.kernel.IMixExecutor;
import com.pipedog.mixkit.kernel.MixModuleCreator;
import com.pipedog.mixkit.parser.MixMessageParserManager;

public class SocketBridge implements IMixBridge {

    private ISocketBridgeDelegate mDelegate;
    private SocketExecutor mExecutor;
    private MixModuleCreator mModuleCreator;
    private MixMessageParserManager mMessageParserManager;

    public SocketBridge(ISocketBridgeDelegate delegate) {
        mDelegate = delegate;

        mExecutor = new SocketExecutor();
        mExecutor.setBridge(this);

        mModuleCreator = new MixModuleCreator(this);
        mMessageParserManager = MixMessageParserManager.defaultManager();
    }

    public ISocketBridgeDelegate bridgeDelegate() {
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
