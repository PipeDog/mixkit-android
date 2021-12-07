package com.pipedog.mixkit.plugin.config

class ConfigItem {

    /**
     * 包含 {@link #generateToClassName} 类的 jar 包文件
     */
    File fileContainsInitClass

    /**
     * 实现 {@link #interfaceName} 接口的类列表
     */
    ArrayList<String> classList = new ArrayList<>()

    private String interfaceName;
    private String generateToClassName;
    private String generateToMethodName;
    private String registerMethodName;

    /**
     * 初始化配置信息
     *
     * @param interfaceName 扫描实现此接口的类
     * @param generateToClassName 注册代码将会被生成到这个类中
     * @param generateToMethodName 注册代码将会被生成到这个方法中
     * @param registerMethodName 在 {@link #generateToClassName} 类中的注册方法名
     */
    ConfigItem(
            String interfaceName,
            String generateToClassName,
            String generateToMethodName,
            String registerMethodName) {
        this.interfaceName = interfaceName
        this.generateToClassName = generateToClassName
        this.generateToMethodName = generateToMethodName
        this.registerMethodName = registerMethodName

        convertIfNeeded()
    }

    private void convertIfNeeded() {
        interfaceName = convertDotToSlash(interfaceName)
        generateToClassName = convertDotToSlash(generateToClassName)
        generateToMethodName = convertDotToSlash(generateToMethodName)
        registerMethodName = convertDotToSlash(registerMethodName)
    }

    private String convertDotToSlash(String str) {
        return str ? str.replaceAll('\\.', '/').intern() : str
    }


    // GETTER METHODS

    /**
     * 扫描实现此接口的类
     */
    String getInterfaceName() {
        return interfaceName
    }

    /**
     * 注册代码将会被生成到这个类中
     */
    String getGenerateToClassName() {
        return generateToClassName
    }

    /**
     * 注册代码将会被生成到这个方法中
     */
    String getGenerateToMethodName() {
        return generateToMethodName
    }

    /**
     * 在 {@link #generateToClassName} 类中的注册方法名
     */
    String getRegisterMethodName() {
        return registerMethodName
    }


    // OVERRIDE METHODS

    @Override
    String toString() {
        return "ConfigItem{\n" +
                "\tinterfaceName = " + interfaceName + ',\n' +
                "\tgenerateToClassName = " + generateToClassName + ',\n' +
                "\tgenerateToMethodName = " + generateToMethodName + ',\n' +
                "\tregisterMethodName = " + registerMethodName + ',\n' +
                '}';
    }

}