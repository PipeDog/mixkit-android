package com.pipedog.mixkit.parser;

public interface IMixMessageParserManager {
    IMixMessageParser detectParser(Object metaData);
}
