package com.pipedog.mixkit.parser;

import android.content.Context;

import com.pipedog.mixkit.launch.MixLaunchManager;
import com.pipedog.mixkit.tool.*;
import com.pipedog.mixkit.path.Path;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.Boolean;

public class MixMessageParserManager {

    private Gson mGson;
    private List<String> mParserClassNames;
    private List<Class<?>> mParserClasses;

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
        mParserClasses = new ArrayList<Class<?>>();

        String packageName = Path.MIX_PARSER_PROVIDER_PACKAGE;
        List<Class<?>> providerClasses =
                MixProviderClassLoader.getClassesWithPackageName(packageName);
        generateParserClasses(providerClasses);

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

    private void generateParserClasses(List<Class<?>> providerClasses) {
        List<String> classNames = new ArrayList<String>();

        // Load all class names
        for (Class<?> aClass : providerClasses) {
            try {
                Object provider = aClass.getConstructor().newInstance();
                Method method = aClass.getMethod(Path.MIX_PARSER_PROVIDER_METHOD);
                String parserNames = (String)method.invoke(provider);
                List<String> names = mGson.fromJson(parserNames, List.class);
                classNames.addAll(names);
            } catch (Exception e) {
                MixLogger.error("Load parse failed, e : " + e.toString());
            }
        }

        // Transform class name to Class instance
        for (String className : classNames) {
            try {
                Class aClass = Class.forName(className);
                if (aClass == null) {
                    MixLogger.error("Fetch class failed with class name " + className);
                    continue;
                }

                if (IMixMessageParser.class.isAssignableFrom(aClass) == false) {
                    MixLogger.error(String.format("Class %s does not comply with the interface %s",
                            className, IMixMessageParser.class));
                    continue;
                }

                mParserClasses.add(aClass);
            } catch (Exception e) {
                e.printStackTrace();
                MixLogger.error(String.format("catch exception : " + e.toString()));
            }
        }
    }

    private void autoCallRegisterParserProvider() {
        // Call this function in constructor
        // The code will be inserted automatically during compilation
        // The insert code will call function `registerParserProvider` here
    }

    private void registerParserProvider(String providerClassName) {
        // Load all parser class names in current provider
        try {
            Class aClass = Class.forName(providerClassName);
            Object provider = aClass.getConstructor().newInstance();
            Method method = aClass.getMethod(Path.MIX_PARSER_PROVIDER_METHOD);
            String parserNames = (String)method.invoke(provider);
            List<String> names = mGson.fromJson(parserNames, List.class);
            mParserClassNames.addAll(names);
        } catch (Exception e) {
            MixLogger.error("Load parse failed, e : " + e.toString());
        }
    }

    private void loadAllParserClasses() {
        // Transform class name to Class instance
        for (String className : mParserClassNames) {
            try {
                Class aClass = Class.forName(className);
                if (aClass == null) {
                    MixLogger.error("Fetch class failed with class name " + className);
                    continue;
                }

                if (IMixMessageParser.class.isAssignableFrom(aClass) == false) {
                    MixLogger.error(String.format("Class %s does not comply with the interface %s",
                            className, IMixMessageParser.class));
                    continue;
                }

                mParserClasses.add(aClass);
            } catch (Exception e) {
                e.printStackTrace();
                MixLogger.error(String.format("catch exception : " + e.toString()));
            }
        }
    }

}
