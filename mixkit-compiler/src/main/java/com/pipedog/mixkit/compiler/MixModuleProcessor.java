package com.pipedog.mixkit.compiler;

import com.google.gson.Gson;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.path.Path;

public class MixModuleProcessor {

    private final Logger mLogger;
    private final Filer mFiler;
    private final Gson mGson;
    private List<MixModuleBean> mModuleDataList;

    public MixModuleProcessor(ProcessingEnvironment processingEnv) {
        mLogger = new Logger(processingEnv.getMessager());
        mFiler = processingEnv.getFiler();
        mGson = new Gson();

        mLogger.info("MixModuleProcessor init");
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        Set<? extends Element> moduleElements = roundEnvironment.getElementsAnnotatedWith(MixModule.class);
        Set<? extends Element> methodElements = roundEnvironment.getElementsAnnotatedWith(MixMethod.class);

        mLogger.info("[Module] moduleElements.size = " + moduleElements.size());
        mLogger.info("[Module] methodElements.size = " + methodElements.size());

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

        mLogger.info("moduleNames = " + mGson.toJson(moduleNameMap));

        // 遍历 @MixMethod 注解，生成注解导出信息 map
        HashMap<String, MixModuleBean> moduleDataMap = new HashMap<String, MixModuleBean>();
        for (Element methodElement : methodElements) {
            String className = methodElement.getEnclosingElement().toString();
            if (className == null || className.length() == 0) { continue; }
            if (!moduleNameMap.containsKey(className)) { continue; }

            String moduleName = moduleNameMap.get(className);
            MixModuleBean moduleData = moduleDataMap.get(moduleName);

            if (moduleData == null) {
                moduleData = new MixModuleBean();
                moduleData.moduleName = moduleName;
                moduleData.className = className;
                moduleDataMap.put(moduleName, moduleData);
            }

            Map<String, String> methods = moduleData.methods;
            if (methods == null) {
                methods = new HashMap<String, String>();
                moduleData.methods = methods;
            }

            MixMethod methodAnnotation = methodElement.getAnnotation(MixMethod.class);
            String exportMethodName = methodAnnotation.name();
            String nativeMethodName = methodElement.getSimpleName().toString();
            methods.put(exportMethodName, nativeMethodName);
        }

        mLogger.info("moduleDataMap = " + mGson.toJson(moduleDataMap));

        mModuleDataList = new ArrayList<MixModuleBean>(moduleDataMap.values());
        createMixModuleLoaderClass();
    }

    private void createMixModuleLoaderClass() {
        MethodSpec method = MethodSpec.methodBuilder(Path.MIX_MODULE_PROVIDER_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .build();

        method = method.toBuilder()
                .addStatement("return $S", mGson.toJson(mModuleDataList))
                .build();

        try {
            TypeSpec typeSpec =
                    TypeSpec.classBuilder(Path.MIX_MODULE_PROVIDER_NAME + "_$_" + MixUUID.UUID())
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
