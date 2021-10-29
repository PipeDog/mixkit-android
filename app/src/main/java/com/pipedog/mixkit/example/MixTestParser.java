package com.pipedog.mixkit.example;

import com.pipedog.mixkit.annotation.MixMessageParser;
import com.pipedog.mixkit.parser.IMixMessageParser;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@MixMessageParser(name = "MKTestParser")
public class MixTestParser implements IMixMessageParser {

    public class TestMixMessageBuiltinBody implements IMixMessageParser.IMixMessageBody {

        private String moduleName;
        private String methodName;
        private List<Object> arguments;

        public TestMixMessageBuiltinBody(String moduleName, String methodName, List arguments) {
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
        boolean ret = (map.get("testModuleName") != null && map.get("testMethodName") != null);
        return ret;
    }

    public static IMixMessageParser newParser(Object metaData) {
        return new MixTestParser(metaData);
    }

    private MixTestParser(Object metaData) {
        Map map = (Map)metaData;
        String moduleName = (String)map.get("testModuleName");
        String methodName = (String)map.get("testMethodName");
        List arguments = (List)map.get("arguments");
        mMessageBody = new MixTestParser.TestMixMessageBuiltinBody(moduleName, methodName, arguments);
    }

    public IMixMessageBody messageBody() {
        return mMessageBody;
    }

}
