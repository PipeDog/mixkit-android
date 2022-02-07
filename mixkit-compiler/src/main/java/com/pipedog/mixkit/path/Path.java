package com.pipedog.mixkit.path;

/**
 * 文件路径定义
 * @author liang
 */
public class Path {

    // For annotation @MixModule()
    public static final String MIX_MODULE_PROVIDER_PACKAGE = "com.pipedog.mixkit.compiler.dynamic.module";
    public static final String MIX_MODULE_PROVIDER_METHOD = "getRegisteredModulesJson";
    public static final String MIX_MODULE_PROVIDER_NAME = "ModuleProvider";
    public static final String MIX_MODULE_PROVIDER_INTERFACE = "com.pipedog.mixkit.compiler.provider.IModuleProvider";

    // For annotation @MixMessageParser()
    public static final String MIX_PARSER_PROVIDER_PACKAGE = "com.pipedog.mixkit.compiler.dynamic.parser";
    public static final String MIX_PARSER_PROVIDER_METHOD = "getRegisteredMessageParsersJson";
    public static final String MIX_PARSER_PROVIDER_NAME = "MessageParserProvider";
    public static final String MIX_PARSER_PROVIDER_INTERFACE = "com.pipedog.mixkit.compiler.provider.IMessageParserProvider";

    // For annotations path
    // Set the follow constants to annotation `SupportedAnnotationTypes` at Class `ExportProcessor`
    public static final String MIX_ANNOTATION_MIX_MODULE_PATH = "com.pipedog.mixkit.annotation.MixModule";
    public static final String MIX_ANNOTATION_MIX_METHOD_PATH = "com.pipedog.mixkit.annotation.MixMethod";
    public static final String MIX_ANNOTATION_MIX_MESSAGE_PARSER_PATH = "com.pipedog.mixkit.annotation.MixMessageParser";

}
