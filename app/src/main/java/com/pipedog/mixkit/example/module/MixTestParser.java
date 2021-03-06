package com.pipedog.mixkit.example.module;

import com.pipedog.mixkit.annotation.MixMessageParser;
import com.pipedog.mixkit.parser.IMessageParser;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@MixMessageParser(name = "MKTestParser")
public class MixTestParser implements IMessageParser {

    public class TestMixMessageBuiltinBody implements IMessageBody {

        private String moduleName;
        private String methodName;
        private List<Object> arguments;

        public TestMixMessageBuiltinBody(String moduleName, String methodName, List arguments) {
            this.moduleName = moduleName;
            this.methodName = methodName;
            this.arguments = arguments;
        }

        @Override
        public String getModuleName() {
            return moduleName;
        }

        @Override
        public String getMethodName() {
            return methodName;
        }

        @Override
        public List<Object> getArguments() {
            if (arguments == null || arguments.isEmpty()) {
                return new ArrayList<Object>();
            }
            return arguments;
        }

    }

    private IMessageBody mMessageBody;

    public static boolean canParse(Object metaData) {
        if (metaData instanceof Map) { } else {
            return false;
        }

        Map map = (Map)metaData;
        boolean ret = (map.get("testModuleName") != null && map.get("testMethodName") != null);
        return ret;
    }

    public static IMessageParser newParser(Object metaData) {
        return new MixTestParser(metaData);
    }

    private MixTestParser(Object metaData) {
        Map map = (Map)metaData;
        String moduleName = (String)map.get("testModuleName");
        String methodName = (String)map.get("testMethodName");
        List arguments = (List)map.get("arguments");
        mMessageBody = new MixTestParser.TestMixMessageBuiltinBody(moduleName, methodName, arguments);
    }

    public IMessageBody getMessageBody() {
        return mMessageBody;
    }

}
