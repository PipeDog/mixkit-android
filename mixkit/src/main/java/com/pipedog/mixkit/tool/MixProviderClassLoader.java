package com.pipedog.mixkit.tool;

import android.content.Context;

import com.pipedog.mixkit.launch.Mix;
import com.pipedog.mixkit.launch.MixOptions;
import com.pipedog.mixkit.tool.*;
import com.pipedog.mixkit.path.Path;

import java.util.List;
import java.util.ArrayList;

public class MixProviderClassLoader {

    public static List<Class<?>> getClassesWithPackageName(String packageName) {
        List<Class<?>> providerClasses = new ArrayList<Class<?>>();

        // Get android application context
        Context context = null;
        try {
            MixOptions options = Mix.options();
            context = options.context;
        } catch (Exception e) {
            MixLogger.error("Catch exception : ", e.toString());
            return providerClasses;
        }

        // Get files in package
        List<String> providerClassNames = null;
        try {
            providerClassNames = ClassUtils.getFileNameByPackageName(context, packageName);
        } catch (Exception e) {
            MixLogger.error("fetch provider failed, exception : %s", e.toString());
            return providerClasses;
        }

        if (providerClassNames == null || providerClassNames.isEmpty()) {
            MixLogger.info("Dynamic loader class is null!");
            return providerClasses;
        }

        for (String className : providerClassNames) {
            try {
                Class aClass = Class.forName(className);
                if (aClass == null) {
                    MixLogger.error("Fetch class failed with class name " + className);
                    continue;
                }

                providerClasses.add(aClass);
            } catch (Exception e) {
                MixLogger.error("Load parse failed, e : " + e.toString());
            }
        }

        return providerClasses;
    }

}
