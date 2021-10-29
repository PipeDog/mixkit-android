package com.pipedog.mixkit.compiler.processor;

import com.google.gson.Gson;
import com.pipedog.mixkit.compiler.utils.Logger;
import com.pipedog.mixkit.compiler.utils.MixUUID;
import com.pipedog.mixkit.compiler.bean.MixMethodBean;
import com.pipedog.mixkit.compiler.bean.MixModuleBean;
import com.pipedog.mixkit.compiler.bean.MixParameterBean;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.path.Path;

public class MixModuleProcessor {

    private final Logger mLogger;
    private final Filer mFiler;
    private final Gson mGson;
    private Map<String, MixModuleBean> mModuleDataMap;
    private final TypeElement mProviderTypeElement;

    public MixModuleProcessor(ProcessingEnvironment processingEnv) {
        mLogger = new Logger(processingEnv.getMessager());
        mFiler = processingEnv.getFiler();
        mGson = new Gson();

        Elements elements = processingEnv.getElementUtils();
        mProviderTypeElement = elements.getTypeElement(Path.MIX_MODULE_PROVIDER_INTERFACE);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        Set<? extends Element> moduleElements =
                roundEnvironment.getElementsAnnotatedWith(MixModule.class);
        Set<? extends Element> methodElements =
                roundEnvironment.getElementsAnnotatedWith(MixMethod.class);

        if (moduleElements == null || moduleElements.isEmpty()) { return false; }
        if (methodElements == null || methodElements.isEmpty()) { return false; }

        processModules(moduleElements, methodElements);
        return true;
    }

    private void processModules(Set<? extends Element> moduleElements,
                                Set<? extends Element> methodElements) {
        // 遍历 @MixModule 注解，生成 module 名称 map
        HashMap<String, String> moduleNameMap = new HashMap<String, String>();
        for (Element moduleElement : moduleElements) {
            MixModule moduleAnnotation = moduleElement.getAnnotation(MixModule.class);
            String moduleName = moduleAnnotation.name();
            String className = moduleElement.toString();
            moduleNameMap.put(className, moduleName);
        }

        // 遍历 @MixMethod 注解，生成注解导出信息 map
        HashMap<String, MixModuleBean> moduleDataMap = new HashMap<String, MixModuleBean>();
        for (Element methodElement : methodElements) {
            if (methodElement instanceof ExecutableElement) { } else {
                mLogger.info("[Error] annotation failed in class named " +
                        methodElement.getEnclosingElement().toString());
                continue;
            }

            String className = methodElement.getEnclosingElement().toString();
            if (className == null || className.length() == 0) { continue; }
            if (!moduleNameMap.containsKey(className)) { continue; }

            String moduleName = moduleNameMap.get(className);
            MixModuleBean moduleData = moduleDataMap.get(moduleName);

            if (moduleData == null) {
                moduleData = new MixModuleBean();
                moduleData.className = className;
                moduleDataMap.put(moduleName, moduleData);
            }

            Map<String, MixMethodBean> methods = moduleData.methods;
            if (methods == null) {
                methods = new HashMap<String, MixMethodBean>();
                moduleData.methods = methods;
            }

            MixMethod methodAnnotation = methodElement.getAnnotation(MixMethod.class);
            String exportMethodName = methodAnnotation.name();

            MixMethodBean methodBean = new MixMethodBean();
            methods.put(exportMethodName, methodBean);

            methodBean.className = className;
            methodBean.methodName = methodElement.getSimpleName().toString();
            List<MixParameterBean> parameters = new ArrayList<MixParameterBean>();
            methodBean.parameters = parameters;

            // Generate parameters
            ExecutableElement executableElement = (ExecutableElement)methodElement;
            List<? extends VariableElement> variableElements = executableElement.getParameters();
            for (VariableElement variableElement : variableElements) {
                TypeMirror methodParameterType = variableElement.asType();
                if (methodParameterType instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) methodParameterType;
                    methodParameterType = typeVariable.getUpperBound();
                }

                MixParameterBean parameterBean = new MixParameterBean();
                parameterBean.name = variableElement.getSimpleName().toString();
                parameterBean.type = methodParameterType.toString();
                parameters.add(parameterBean);
            }
        }

        mModuleDataMap = new HashMap<String, MixModuleBean>(moduleDataMap);
        createMixModuleProviderClass();
    }

    private void createMixModuleProviderClass() {
        MethodSpec method = MethodSpec.methodBuilder(Path.MIX_MODULE_PROVIDER_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .build();

        method = method.toBuilder()
                .addStatement("return $S", mGson.toJson(mModuleDataMap))
                .build();

        try {
            TypeSpec typeSpec =
                    TypeSpec.classBuilder(Path.MIX_MODULE_PROVIDER_NAME + "_$_" + MixUUID.UUID())
                            .addSuperinterface(ClassName.get(mProviderTypeElement))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(method)
                            .build();

            JavaFile javaFile =
                    JavaFile.builder(Path.MIX_MODULE_PROVIDER_PACKAGE, typeSpec).build();
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            mLogger.info("MixModuleProcessor catch exception " + e.toString());
        }
    }

}
