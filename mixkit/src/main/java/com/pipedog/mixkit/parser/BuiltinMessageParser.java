package com.pipedog.mixkit.parser;

import com.pipedog.mixkit.annotation.MixMessageParser;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 内置默认数据解析器
 * @author liang
 */
@MixMessageParser(name = "BuiltinMessageParser")
public class BuiltinMessageParser implements IMessageParser {

    public class MixMessageBuiltinBody implements IMessageBody {

        private String mModuleName;
        private String mMethodName;
        private List<Object> mArguments;

        public MixMessageBuiltinBody(String moduleName, String methodName, List arguments) {
            mModuleName = moduleName;
            mMethodName = methodName;
            mArguments = arguments;
        }

        @Override
        public String getModuleName() {
            if (mModuleName == null) {
                return "";
            }
            return mModuleName;
        }

        @Override
        public String getMethodName() {
            if (mMethodName == null) {
                return "";
            }
            return mMethodName;
        }

        @Override
        public List<Object> getArguments() {
            if (mArguments == null || mArguments.isEmpty()) {
                return new ArrayList<Object>();
            }
            return mArguments;
        }

    }

    private IMessageBody mMessageBody;

    public static boolean canParse(Object metaData) {
        if (metaData instanceof Map) { } else {
            return false;
        }

        Map map = (Map)metaData;
        boolean ret = (map.get("moduleName") != null && map.get("methodName") != null);
        return ret;
    }

    public static IMessageParser newParser(Object metaData) {
        return new BuiltinMessageParser(metaData);
    }

    private BuiltinMessageParser(Object metaData) {
        Map map = (Map)metaData;
        String moduleName = (String)map.get("moduleName");
        String methodName = (String)map.get("methodName");
        List arguments = (List)map.get("arguments");
        mMessageBody = new MixMessageBuiltinBody(moduleName, methodName, arguments);
    }

    public IMessageBody getMessageBody() {
        return mMessageBody;
    }

}
