package com.pipedog.mixkit.compiler;

import com.google.gson.Gson;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

import com.pipedog.mixkit.annotation.MixMessageParser;
import com.pipedog.mixkit.path.Path;

public class MixMessageParserProcessor {

    private final Logger mLogger;
    private final Filer mFiler;
    private final Gson mGson;
    private final List<String> mParserClasses;

    public MixMessageParserProcessor(ProcessingEnvironment processingEnv) {
        mLogger = new Logger(processingEnv.getMessager());
        mFiler = processingEnv.getFiler();
        mGson = new Gson();
        mParserClasses = new ArrayList<>();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        Set<? extends Element> parserElements =
                roundEnvironment.getElementsAnnotatedWith(MixMessageParser.class);
        if (parserElements == null || parserElements.isEmpty()) { return false; }

        processParsers(parserElements);
        return true;
    }

    private void processParsers(Set<? extends Element> parserElements) {
        for (Element parserElement : parserElements) {
            String className = parserElement.toString();
            mParserClasses.add(className);
        }

        createMixMessageParserLoaderClass();
    }

    private void createMixMessageParserLoaderClass() {
        MethodSpec method = MethodSpec.methodBuilder(Path.MIX_PARSER_PROVIDER_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .build();

        method = method.toBuilder()
                .addStatement("return $S", mGson.toJson(mParserClasses))
                .build();

        try {
            TypeSpec typeSpec =
                    TypeSpec.classBuilder(Path.MIX_PARSER_PROVIDER_NAME + "_$_" + MixUUID.UUID())
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(method)
                            .build();

            JavaFile javaFile =
                    JavaFile.builder(Path.MIX_PARSER_PROVIDER_PACKAGE,
                            typeSpec).build();
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            mLogger.info("MixMessageParserProcessor catch exception " + e.toString());
        }
    }

}
