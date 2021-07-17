package com.pipedog.mixkit.parser;

import android.content.Context;

import com.pipedog.mixkit.launch.Mix;
import com.pipedog.mixkit.launch.MixOptions;
import com.pipedog.mixkit.tool.*;
import com.pipedog.mixkit.path.Path;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.lang.Boolean;

public class MixMessageParserManager implements IMixMessageParserManager {

    private Gson mGson;
    private List<Class<?>> mParserClasses;
    private volatile static IMixMessageParserManager defaultManager;

    public static IMixMessageParserManager defaultManager() {
        if (defaultManager == null) {
            synchronized (MixMessageParserManager.class) {
                if (defaultManager == null) {
                    defaultManager = new MixMessageParserManager();
                }
            }
        }
        return defaultManager;
    }

    private MixMessageParserManager() {
        mGson = new Gson();
        mParserClasses = new ArrayList<>();

        // Load all classes from package "com.pipedog.mixkit.compiler.dynamic.parser"
        MixLogger.info("init parser manager");

        Context context = null;
        try {
            MixOptions options = Mix.options();
            context = options.context;
        } catch (Exception e) {
            MixLogger.error("Catch exception : ", e.toString());
            return;
        }

        MixLogger.info("get context success!");

        List<String> loaderClassNames = null;
        try {
            String packageName = Path.MIX_PARSER_PROVIDER_PACKAGE;
            loaderClassNames = ClassUtils.getFileNameByPackageName(context, packageName);
        } catch (Exception e) {
            MixLogger.error("fetch provider failed, exception : %s", e.toString());
            return;
        }

        MixLogger.info("get class names from package finished, class names = %s", mGson.toJson(loaderClassNames));

        if (loaderClassNames == null || loaderClassNames.isEmpty()) {
            MixLogger.info("Dynamic loader class is null!");
            return;
        }

        List<Class<?>> loaderClasses = new ArrayList<>();
        for (String className : loaderClassNames) {
            MixLogger.info("class name = %s", className);

            try {
                Class aClass = Class.forName(className);
                if (aClass == null) {
                    MixLogger.error("Fetch class failed with class name " + className);
                    continue;
                }

                loaderClasses.add(aClass);

                MixLogger.info("add class to classes set");
            } catch (Exception e) {
                MixLogger.error("Load parse failed, e : " + e.toString());
            }
        }

        MixLogger.info("loaderClasses : %d", loaderClasses.size());

        List<String> classNames = new ArrayList<String>();

        // Load all class names
        for (Class<?> aClass : loaderClasses) {
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

        MixLogger.info("parser classes : %d", mParserClasses.size());
    }

    @Override
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

}
