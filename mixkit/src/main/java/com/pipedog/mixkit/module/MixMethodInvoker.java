package com.pipedog.mixkit.module;

import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.tool.MixTypeConverter;

import java.lang.Class;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 方法调用功能抽象（自定义 module 中的函数最终在这里被调用）
 * @author liang
 */
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
        if (parameters == null || parameters.isEmpty()) {
            return new Object[0];
        }

        Object[] parameterArray = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            MixMethodParameter parameter = mModuleMethod.parameters.get(i);
            String declaredType = parameter.type;
            Object from = parameters.get(i);

            // Basic data types
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
            }

            // Basic data types wrapper classes
            else if (declaredType.equals("java.lang.Byte")) {
                byte b = MixTypeConverter.toByte(from);
                Byte to = new Byte(b);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Short")) {
                short s = MixTypeConverter.toShort(from);
                Short to = new Short(s);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Integer")) {
                int bi = MixTypeConverter.toInt(from);
                Integer to = new Integer(bi);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Long")) {
                long l = MixTypeConverter.toLong(from);
                Long to = new Long(l);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Float")) {
                float f = MixTypeConverter.toFloat(from);
                Float to = new Float(f);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Double")) {
                double d = MixTypeConverter.toDouble(from);
                Double to = new Double(d);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Boolean")) {
                boolean b = MixTypeConverter.toBoolean(from);
                Boolean to = new Boolean(b);
                parameterArray[i] = to;
            } else if (declaredType.equals("java.lang.Character")) {
                char c = MixTypeConverter.toChar(from);
                Character to = new Character(c);
                parameterArray[i] = to;
            }

            // Other types
            else if (declaredType.equals("java.lang.String")) {
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

            // Basic data types
            sClassMap.put("byte", byte.class);
            sClassMap.put("short", short.class);
            sClassMap.put("int", int.class);
            sClassMap.put("long", long.class);
            sClassMap.put("float", float.class);
            sClassMap.put("double", double.class);
            sClassMap.put("boolean", boolean.class);
            sClassMap.put("char", char.class);

            // Basic data types wrapper classes
            sClassMap.put("java.lang.Byte", Byte.class);
            sClassMap.put("java.lang.Short", Short.class);
            sClassMap.put("java.lang.Integer", Integer.class);
            sClassMap.put("java.lang.Long", Long.class);
            sClassMap.put("java.lang.Float", Float.class);
            sClassMap.put("java.lang.Double", Double.class);
            sClassMap.put("java.lang.Boolean", Boolean.class);
            sClassMap.put("java.lang.Character", Character.class);
        }

        Class cls = sClassMap.get(className);
        if (cls != null) {
            return cls;
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

        cls = sClassMap.get(className);
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
