package com.pipedog.mixkit.parser;

import android.content.Context;

import com.pipedog.mixkit.compiler.provider.IMixMessageParserProvider;
import com.pipedog.mixkit.launch.MixLaunchManager;
import com.pipedog.mixkit.tool.*;
import com.pipedog.mixkit.path.Path;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.Boolean;

public class MixMessageParserManager {

    private Gson mGson;
    private List<String> mParserClassNames;
    private Set<Class<?>> mParserClasses;

    private volatile static MixMessageParserManager sDefaultManager;

    public static MixMessageParserManager defaultManager() {
        if (sDefaultManager == null) {
            synchronized (MixMessageParserManager.class) {
                if (sDefaultManager == null) {
                    sDefaultManager = new MixMessageParserManager();
                }
            }
        }
        return sDefaultManager;
    }

    private MixMessageParserManager() {
        mGson = new Gson();
        mParserClassNames = new ArrayList<String>();
        mParserClasses = new HashSet<Class<?>>();

        autoCallRegisterParserProvider();
        loadAllParserClasses();
    }

    public IMixMessageParser detectParser(Object metaData) {
        if (metaData == null || mParserClasses == null || mParserClasses.isEmpty()) {
            return null;
        }

        for (Class aClass : mParserClasses) {
            try {
                Method canParseMethod = aClass.getMethod("canParse", Object.class);
                Boolean ret = (Boolean)canParseMethod.invoke(aClass, metaData);
                if (Boolean.FALSE.equals(ret)) { continue; }

                Method newParserMethod = aClass.getMethod("newParser", Object.class);
                IMixMessageParser parser = (IMixMessageParser)newParserMethod.invoke(aClass, metaData);
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

    private void registerParserProvider(IMixMessageParserProvider provider) {
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

            if (IMixMessageParser.class.isAssignableFrom(aClass) == false) {
                MixLogger.error(String.format("Class %s does not comply with the interface %s",
                        parserClassName, IMixMessageParser.class));
                return;
            }

            mParserClasses.add(aClass);
        } catch (Exception e) {
            e.printStackTrace();
            MixLogger.error(String.format("catch exception : " + e.toString()));
        }
    }

}
