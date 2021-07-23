package com.pipedog.mixkit.parser;

import com.pipedog.mixkit.annotation.MixMessageParser;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@MixMessageParser(name = "BuiltinMessageParser")
public class MixBuiltinMessageParser implements IMixMessageParser {

    public class MixMessageBuiltinBody implements IMixMessageParser.IMixMessageBody {

        private String mModuleName;
        private String mMethodName;
        private List<Object> mArguments;

        public MixMessageBuiltinBody(String moduleName, String methodName, List arguments) {
            mModuleName = moduleName;
            mMethodName = methodName;
            mArguments = arguments;
        }

        @Override
        public String moduleName() {
            if (mModuleName == null) {
                return "";
            }
            return mModuleName;
        }

        @Override
        public String methodName() {
            if (mMethodName == null) {
                return "";
            }
            return mMethodName;
        }

        @Override
        public List<Object> arguments() {
            if (mArguments == null || mArguments.isEmpty()) {
                return new ArrayList<Object>();
            }
            return mArguments;
        }

    }

    private IMixMessageBody mMessageBody;

    public static boolean canParse(Object metaData) {
        if (metaData instanceof Map) { } else {
            return false;
        }

        Map map = (Map)metaData;
        boolean ret = (map.get("moduleName") != null && map.get("methodName") != null);
        return ret;
    }

    public static IMixMessageParser newParser(Object metaData) {
        return new MixBuiltinMessageParser(metaData);
    }

    private MixBuiltinMessageParser(Object metaData) {
        Map map = (Map)metaData;
        String moduleName = (String)map.get("moduleName");
        String methodName = (String)map.get("methodName");
        List arguments = (List)map.get("arguments");
        mMessageBody = new MixMessageBuiltinBody(moduleName, methodName, arguments);
    }

    public IMixMessageBody messageBody() {
        return mMessageBody;
    }

}
