package com.pipedog.mixkit.module;

import com.pipedog.mixkit.tool.MixLogger;

import java.lang.Class;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public class MixMethodInvoker {

    private MixModuleMethod mModuleMethod;
    private Method mMethod;

    public MixMethodInvoker(MixModuleMethod method) {
        mModuleMethod = method;
    }

    public boolean invoke(Object module, List<Object> parameters) {
        try {
            prepareMethodIfNeeded();
            mMethod.invoke(module, parameters);
        } catch (Exception e) {
            MixLogger.error("Invoke method failed, class : `%s`, method : `%s`!",
                    mModuleMethod.className, mModuleMethod.methodName);
            return false;
        }

        return true;
    }

    public String getClassName() {
        return mModuleMethod.className;
    }

    public String getMethodName() {
        return mModuleMethod.methodName;
    }

    private void prepareMethodIfNeeded() {
        if (mMethod != null) { return; }

        try {
            Class cls = Class.forName(mModuleMethod.className);

            int size = mModuleMethod.parameters.size();
            Class<?>[] parameterTypes = new Class<?>[size];
            for (int i = 0; i < size; i++) {
                MixMethodParameter parameter = mModuleMethod.parameters.get(i);
                Class parameterClass = Class.forName(parameter.type);
                parameterTypes[i] = parameterClass;
            }

            Method method = cls.getMethod(mModuleMethod.methodName, parameterTypes);
            method.setAccessible(true);
            mMethod = method;
        } catch (Exception e) {
            MixLogger.error("Found method failed, class : `%s`, method : `%s`!",
                    mModuleMethod.className, mModuleMethod.methodName);
        }
    }

}
