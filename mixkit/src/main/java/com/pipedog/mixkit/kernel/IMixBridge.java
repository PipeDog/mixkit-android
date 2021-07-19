package com.pipedog.mixkit.kernel;

import com.pipedog.mixkit.parser.MixMessageParserManager;

public interface IMixBridge {
    public IMixExecutor executor();
    public MixModuleCreator moduleCreator();
    public MixMessageParserManager messageParserManager();
}
