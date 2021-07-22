package com.pipedog.mixkit.module;

import com.pipedog.mixkit.tool.MixLogger;

import java.lang.Class;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MixMethodInvoker {

    private static final int NOT_FOUND = -1;

    private static Map<String, Class<?>> sClassMap;
    private MixModuleMethod mModuleMethod;
    private Method mMethod;

    public MixMethodInvoker(MixModuleMethod method) {
        mModuleMethod = method;
    }

    public boolean invoke(Object module, List<Object> parameters) {
        try {
            prepareContextIfNeeded();

            if (mModuleMethod.parameters.size() > 0 &&
                (parameters == null || parameters.size() != mModuleMethod.parameters.size())) {
                MixLogger.error("Invoke method failed, invalid parameters count, class : %s, " +
                        "method : %s, parameters : %s!", mModuleMethod.className,
                        mModuleMethod.methodName, parameters.toString());
                return false;
            }

            Object[] parameterArray = convertParameters(parameters);
            mMethod.invoke(module, parameterArray);
        } catch (Exception e) {
            MixLogger.error("Invoke method failed, class : `%s`, method : `%s`, parameters : %s " +
                            "execption : %s!", mModuleMethod.className, mModuleMethod.methodName,
                            parameters.toString(), e.toString());
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

    private void prepareContextIfNeeded() {
        if (mMethod != null) { return; }

        try {
            Class cls = Class.forName(mModuleMethod.className);

            int size = mModuleMethod.parameters.size();
            Class<?>[] parameterTypes = new Class<?>[size];
            for (int i = 0; i < size; i++) {
                MixMethodParameter parameter = mModuleMethod.parameters.get(i);
                Class parameterClass = getClass(parameter.type);
                parameterTypes[i] = parameterClass;
            }

            Method method = cls.getMethod(mModuleMethod.methodName, parameterTypes);
            method.setAccessible(true);
            mMethod = method;
        } catch (Exception e) {
            MixLogger.error("Found method failed, class : `%s`, method : `%s`, exception : %s!",
                    mModuleMethod.className, mModuleMethod.methodName, e.toString());
        }
    }

    private Object[] convertParameters(List<Object> parameters) {
        Object[] parameterArray = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            MixMethodParameter parameter = mModuleMethod.parameters.get(i);
            String declaredType = parameter.type;
            Object from = parameters.get(i);

            if (declaredType.equals("byte")) {
                byte to = MixTypeConverter.toByte(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("short")) {
                short to = MixTypeConverter.toShort(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("int")) {
                int to = MixTypeConverter.toInt(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("long")) {
                long to = MixTypeConverter.toLong(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("float")) {
                float to = MixTypeConverter.toFloat(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("double")) {
                double to = MixTypeConverter.toDouble(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("boolean")) {
                boolean to = MixTypeConverter.toBoolean(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("char")) {
                char to = MixTypeConverter.toChar(from);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.String")) {
                String to = MixTypeConverter.toString(from);
                parameterArray[i] = to;
            } else {
                parameterArray[i] = from;
                MixLogger.info("Invoker: get declaredType is `%s`, parameter class is `%s`",
                        declaredType, from.getClass().toString());
            }
        }

        return parameterArray;
    }

    private static Class getClass(String className) {
        if (sClassMap == null) {
            sClassMap = new HashMap<String, Class<?>>();
            sClassMap.put("byte", byte.class);
            sClassMap.put("short", short.class);
            sClassMap.put("int", int.class);
            sClassMap.put("long", long.class);
            sClassMap.put("float", float.class);
            sClassMap.put("double", double.class);
            sClassMap.put("boolean", boolean.class);
            sClassMap.put("char", char.class);
        }

        // Remove the generic suffix, like the follow case:
        //      before : java.util.List<java.lang.Object>
        //      after  : java.util.List
        int index = className.indexOf("<");
        if (index != NOT_FOUND) {
            String tmp = className;
            className = tmp.substring(0, index);
            MixLogger.info("Invoker: transform class name from `%s` to `%s`.",
                    tmp, className);
        }

        Class cls = sClassMap.get(className);
        if (cls == null) {
            try {
                cls = Class.forName(className);
                sClassMap.put(className, cls);
            } catch (Exception e) {
                MixLogger.error("Invoker: get class failed, class name is `%s`, exception : %s!",
                        className, e.toString());
                return null;
            }
        }

        return cls;
    }

}
