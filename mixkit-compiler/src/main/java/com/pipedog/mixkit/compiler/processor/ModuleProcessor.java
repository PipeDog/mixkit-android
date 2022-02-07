package com.pipedog.mixkit.compiler.processor;

import com.google.gson.Gson;
import com.pipedog.mixkit.compiler.utils.Logger;
import com.pipedog.mixkit.compiler.utils.UUIDCreator;
import com.pipedog.mixkit.compiler.bean.MethodBean;
import com.pipedog.mixkit.compiler.bean.ModuleBean;
import com.pipedog.mixkit.compiler.bean.ParameterBean;
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

/**
 * 自定义 module 注解处理（相关信息会被写入到编译期生成的 java 文件中，该文件以 MixModuleProvider 开头）
 * @author liang
 */
public class ModuleProcessor {

    private final Logger mLogger;
    private final Filer mFiler;
    private final Gson mGson;
    private Map<String, ModuleBean> mModuleDataMap;
    private final TypeElement mProviderTypeElement;

    public ModuleProcessor(ProcessingEnvironment processingEnv) {
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
        Map<String, String> moduleNameMap = new HashMap<String, String>();
        for (Element moduleElement : moduleElements) {
            MixModule moduleAnnotation = moduleElement.getAnnotation(MixModule.class);
            String moduleName = moduleAnnotation.name();
            String className = moduleElement.toString();
            moduleNameMap.put(className, moduleName);
        }

        // 遍历 @MixMethod 注解，生成注解导出信息 map
        Map<String, ModuleBean> moduleDataMap = new HashMap<String, ModuleBean>();
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
            ModuleBean moduleData = moduleDataMap.get(moduleName);

            if (moduleData == null) {
                moduleData = new ModuleBean();
                moduleData.classes = new ArrayList<String>();
                moduleDataMap.put(moduleName, moduleData);
            }

            if (!moduleData.classes.contains(className)) {
                moduleData.classes.add(className);
            }

            Map<String, MethodBean> methods = moduleData.methods;
            if (methods == null) {
                methods = new HashMap<String, MethodBean>();
                moduleData.methods = methods;
            }

            MixMethod methodAnnotation = methodElement.getAnnotation(MixMethod.class);
            String exportMethodName = methodAnnotation.name();
            if (methods.containsKey(exportMethodName)) {
                throw new RuntimeException("Duplicate method annotation named `" +
                        exportMethodName + "` in a module, rename it!");
            }

            MethodBean methodBean = new MethodBean();
            methods.put(exportMethodName, methodBean);

            methodBean.className = className;
            methodBean.methodName = methodElement.getSimpleName().toString();
            List<ParameterBean> parameters = new ArrayList<ParameterBean>();
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

                ParameterBean parameterBean = new ParameterBean();
                parameterBean.name = variableElement.getSimpleName().toString();
                parameterBean.type = methodParameterType.toString();
                parameters.add(parameterBean);
            }
        }

        mModuleDataMap = new HashMap<String, ModuleBean>(moduleDataMap);
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
                    TypeSpec.classBuilder(Path.MIX_MODULE_PROVIDER_NAME + "_$_" + UUIDCreator.UUID())
                            .addSuperinterface(ClassName.get(mProviderTypeElement))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(method)
                            .build();

            JavaFile javaFile =
                    JavaFile.builder(Path.MIX_MODULE_PROVIDER_PACKAGE, typeSpec).build();
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            mLogger.info("ModuleProcessor catch exception " + e.toString());
        }
    }

}
