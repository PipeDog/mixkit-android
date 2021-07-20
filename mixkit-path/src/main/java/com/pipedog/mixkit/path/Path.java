package com.pipedog.mixkit.path;

public class Path {

    // For annotation @MixModule()
    public static final String MIX_MODULE_PROVIDER_PACKAGE = "com.pipedog.mixkit.compiler.dynamic.module";
    public static final String MIX_MODULE_CLASS_NAME =
            MIX_MODULE_PROVIDER_PACKAGE + "." + "MixModule";
    public static final String MIX_MODULE_METHOD_CLASS_NAME = "com.pipedog.mixkit.compiler.MixMethod";
    public static final String MIX_MODULE_PROVIDER_METHOD = "getRegisteredModulesJson";
    public static final String MIX_MODULE_PROVIDER_NAME = "MixModuleProvider";

    // For annotation @MixMessageParser()
    public static final String MIX_PARSER_PROVIDER_PACKAGE = "com.pipedog.mixkit.compiler.dynamic.parser";
    public static final String MIX_PARSER_CLASS_NAME =
            MIX_PARSER_PROVIDER_PACKAGE + "." + "MixMessageParser";
    public static final String MIX_PARSER_PROVIDER_METHOD = "getRegisteredMessageParsersJson";
    public static final String MIX_PARSER_PROVIDER_NAME = "MixMessageParserProvider";

    // For annotations path
    // Set the follow constants to annotation `SupportedAnnotationTypes` at Class `MixExportProcessor`
    public static final String MIX_ANNOTATION_MIX_MODULE_PATH = "com.pipedog.mixkit.annotation.MixModule";
    public static final String MIX_ANNOTATION_MIX_METHOD_PATH = "com.pipedog.mixkit.annotation.MixMethod";
    public static final String MIX_ANNOTATION_MIX_MESSAGE_PARSER_PATH = "com.pipedog.mixkit.annotation.MixMessageParser";

}
