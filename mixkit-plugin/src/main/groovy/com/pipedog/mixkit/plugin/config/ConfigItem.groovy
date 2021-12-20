package com.pipedog.mixkit.plugin.config

/**
 * 配置项信息抽象实例
 * @author liang
 */
class ConfigItem {

    /**
     * 包含 {@link #generateToClassName} 类的 jar 包文件
     */
    File fileContainsInitClass

    /**
     * 实现 {@link #interfaceName} 接口的类列表
     */
    ArrayList<String> classList = new ArrayList<>()

    private String interfaceName
    private String generateToClassName
    private String generateToMethodName
    private String registerMethodName
    private ArrayList<String> scanPackageNames

    /**
     * 初始化配置信息
     *
     * @param interfaceName 扫描实现此接口的类
     * @param generateToClassName 注册代码将会被生成到这个类中
     * @param generateToMethodName 注册代码将会被生成到这个方法中
     * @param registerMethodName 在 {@link #generateToClassName} 类中的注册方法名
     * @param scanPackageNames 指定要扫描的包
     */
    ConfigItem(
            String interfaceName,
            String generateToClassName,
            String generateToMethodName,
            String registerMethodName,
            ArrayList<String> scanPackageNames) {
        this.interfaceName = interfaceName
        this.generateToClassName = generateToClassName
        this.generateToMethodName = generateToMethodName
        this.registerMethodName = registerMethodName
        this.scanPackageNames = scanPackageNames ?: new ArrayList<>()

        convertIfNeeded()
    }

    private void convertIfNeeded() {
        interfaceName = convertDotToSlash(interfaceName)
        generateToClassName = convertDotToSlash(generateToClassName)
        generateToMethodName = convertDotToSlash(generateToMethodName)
        registerMethodName = convertDotToSlash(registerMethodName)

        ArrayList<String> convertedScanPackageNames = new ArrayList<>()
        for (int i = 0; i < scanPackageNames.size(); i++) {
            String packageName = convertDotToSlash(scanPackageNames.get(i))
            convertedScanPackageNames.add(packageName)
        }
        scanPackageNames = convertedScanPackageNames
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

    /**
     * 指定需要扫描的 package，如果为空则默认扫描所有包
     */
    ArrayList<String> getScanPackageNames() {
        return scanPackageNames
    }


    // OVERRIDE METHODS

    @Override
    String toString() {
        return "ConfigItem{\n" +
                "\tinterfaceName = " + interfaceName + ',\n' +
                "\tgenerateToClassName = " + generateToClassName + ',\n' +
                "\tgenerateToMethodName = " + generateToMethodName + ',\n' +
                "\tregisterMethodName = " + registerMethodName + ',\n' +
                "\tscanPackageNames = " + scanPackageNames.toString() + ',\n' +
                '}';
    }

}