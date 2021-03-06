package com.pipedog.mixkit.compiler.processor;

import java.util.Set;

import com.pipedog.mixkit.path.Path;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 注解处理器（编译期执行）
 * @author liang
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        // com.pipedog.mixkit.annotation.MixModule
        Path.MIX_ANNOTATION_MIX_MODULE_PATH,
        // com.pipedog.mixkit.annotation.MixMethod
        Path.MIX_ANNOTATION_MIX_METHOD_PATH,
        // com.pipedog.mixkit.annotation.MixMessageParser
        Path.MIX_ANNOTATION_MIX_MESSAGE_PARSER_PATH,
})

public class ExportProcessor extends AbstractProcessor {

    private ModuleProcessor mModuleProcessor;
    private MessageParserProcessor mMessageParserProcessor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mModuleProcessor = new ModuleProcessor(processingEnv);
        mMessageParserProcessor = new MessageParserProcessor(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        mModuleProcessor.process(annotations, roundEnvironment);
        mMessageParserProcessor.process(annotations, roundEnvironment);
        return true;
    }

}