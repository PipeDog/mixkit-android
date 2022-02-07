package com.pipedog.mixkit.parser;

import com.pipedog.mixkit.compiler.provider.IMessageParserProvider;
import com.pipedog.mixkit.tool.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.Boolean;

/**
 * 消息解析管理器（负责根据消息体原始数据匹配生成相应解析器实例）
 * @author liang
 */
public class MessageParserManager {

    private Gson mGson;
    private List<String> mParserClassNames;
    private Set<Class<?>> mParserClasses;

    private volatile static MessageParserManager sDefaultManager;

    public static MessageParserManager defaultManager() {
        if (sDefaultManager == null) {
            synchronized (MessageParserManager.class) {
                if (sDefaultManager == null) {
                    sDefaultManager = new MessageParserManager();
                }
            }
        }
        return sDefaultManager;
    }

    private MessageParserManager() {
        mGson = new Gson();
        mParserClassNames = new ArrayList<String>();
        mParserClasses = new HashSet<Class<?>>();

        autoCallRegisterParserProvider();
        loadAllParserClasses();
    }

    public IMessageParser detectParser(Object metaData) {
        if (metaData == null || mParserClasses == null || mParserClasses.isEmpty()) {
            return null;
        }

        for (Class aClass : mParserClasses) {
            try {
                Method canParseMethod = aClass.getMethod("canParse", Object.class);
                Boolean ret = (Boolean)canParseMethod.invoke(aClass, metaData);
                if (Boolean.FALSE.equals(ret)) { continue; }

                Method newParserMethod = aClass.getMethod("newParser", Object.class);
                IMessageParser parser = (IMessageParser)newParserMethod.invoke(aClass, metaData);
                return parser;
            } catch (Exception e) {
                e.printStackTrace();
                MixLogger.info("catch exception : " + e.toString());
            }
        }

        return null;
    }

    private void autoCallRegisterParserProvider() {
        // Call this function in constructor
        // The code will be inserted automatically during compilation
        // The insert code will call function `registerParserProvider` here
    }

    private void registerParserProvider(IMessageParserProvider provider) {
        // Load all parser class names in current provider
        try {
            String parserNames = provider.getRegisteredMessageParsersJson();
            List<String> names = mGson.fromJson(parserNames, List.class);
            mParserClassNames.addAll(names);
        } catch (Exception e) {
            MixLogger.error("Load parse failed, e : " + e.toString());
        }
    }

    private void loadAllParserClasses() {
        for (String className : mParserClassNames) {
            loadParserClass(className);
        }
    }

    private void loadParserClass(String parserClassName) {
        // Transform class name to Class instance
        try {
            Class aClass = Class.forName(parserClassName);
            if (aClass == null) {
                MixLogger.error("Fetch class failed with class name " + parserClassName);
                return;
            }

            if (IMessageParser.class.isAssignableFrom(aClass) == false) {
                MixLogger.error(String.format("Class %s does not comply with the interface %s",
                        parserClassName, IMessageParser.class));
                return;
            }

            mParserClasses.add(aClass);
        } catch (Exception e) {
            e.printStackTrace();
            MixLogger.error(String.format("catch exception : " + e.toString()));
        }
    }

}
