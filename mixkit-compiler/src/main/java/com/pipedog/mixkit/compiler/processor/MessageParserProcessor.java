package com.pipedog.mixkit.compiler.processor;

import com.google.gson.Gson;
import com.pipedog.mixkit.compiler.utils.Logger;
import com.pipedog.mixkit.compiler.utils.UUIDCreator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

import com.pipedog.mixkit.annotation.MixMessageParser;
import com.pipedog.mixkit.path.Path;

/**
 * 消息解析器注解处理（相关信息会被写入到编译期生成的 java 文件中，该文件以 MixMessageParserProvider 开头）
 * @author liang
 */
public class MessageParserProcessor {

    private final Logger mLogger;
    private final Filer mFiler;
    private final Gson mGson;
    private final List<String> mParserClasses;
    private final TypeElement mProviderTypeElement;

    public MessageParserProcessor(ProcessingEnvironment processingEnv) {
        mLogger = new Logger(processingEnv.getMessager());
        mFiler = processingEnv.getFiler();
        mGson = new Gson();
        mParserClasses = new ArrayList<>();

        Elements elements = processingEnv.getElementUtils();
        mProviderTypeElement = elements.getTypeElement(Path.MIX_PARSER_PROVIDER_INTERFACE);
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

        createMixMessageParserProviderClass();
    }

    private void createMixMessageParserProviderClass() {
        MethodSpec method = MethodSpec.methodBuilder(Path.MIX_PARSER_PROVIDER_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .build();

        method = method.toBuilder()
                .addStatement("return $S", mGson.toJson(mParserClasses))
                .build();

        try {
            TypeSpec typeSpec =
                    TypeSpec.classBuilder(Path.MIX_PARSER_PROVIDER_NAME + "_$_" + UUIDCreator.UUID())
                            .addSuperinterface(ClassName.get(mProviderTypeElement))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(method)
                            .build();

            JavaFile javaFile =
                    JavaFile.builder(Path.MIX_PARSER_PROVIDER_PACKAGE,
                            typeSpec).build();
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
            mLogger.info("MessageParserProcessor catch exception " + e.toString());
        }
    }

}
