package com.pipedog.mixkit.parser;

import java.util.List;

public interface IMixMessageParser {

    interface IMixMessageBody {
        public String moduleName();
        public String methodName();
        public List<Object> arguments();
    }

    public static boolean canParse(Object metaData) {
        return false;
    }
    public static IMixMessageParser newParser(Object metaData) {
        return null;
    }
    public IMixMessageBody messageBody();

}
