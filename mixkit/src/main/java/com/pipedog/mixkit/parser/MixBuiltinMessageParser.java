package com.pipedog.mixkit.parser;

import com.pipedog.mixkit.annotation.MixMessageParser;
import com.pipedog.mixkit.tool.MixLogger;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@MixMessageParser(name = "BuiltinMessageParser")
public class MixBuiltinMessageParser implements IMixMessageParser {

    public class MixMessageBuiltinBody implements IMixMessageParser.IMixMessageBody {

        private String moduleName;
        private String methodName;
        private List<Object> arguments;

        public MixMessageBuiltinBody(String moduleName, String methodName, List arguments) {
            this.moduleName = moduleName;
            this.methodName = methodName;
            this.arguments = arguments;
        }

        @Override
        public String moduleName() {
            return moduleName;
        }

        @Override
        public String methodName() {
            return methodName;
        }

        @Override
        public List<Object> arguments() {
            if (arguments == null || arguments.isEmpty()) {
                return new ArrayList<Object>();
            }
            return arguments;
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
