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
    private ArrayList<String> superClasses
    private String generateToClassName
    private String generateToMethodName
    private String registerMethodName
    private ArrayList<String> scanPackageNames
    private boolean containsSuperClasses

    /**
     * 初始化配置信息
     *
     * @param interfaceName 扫描实现此接口的类
     * @param generateToClassName 注册代码将会被生成到这个类中
     * @param generateToMethodName 注册代码将会被生成到这个方法中
     * @param registerMethodName 在 {@link #generateToClassName} 类中的注册方法名
     * @param scanPackageNames 指定要扫描的包
     * @param containsSuperClasses 最终命中的类是否包含所指定的父类
     */
    ConfigItem(
            String interfaceName,
            ArrayList<String> superClasses,
            String generateToClassName,
            String generateToMethodName,
            String registerMethodName,
            ArrayList<String> scanPackageNames,
            boolean containsSuperClasses) {
        this.interfaceName = interfaceName
        this.superClasses = superClasses ?: new ArrayList<>()
        this.generateToClassName = generateToClassName

        // 如果不配置，则默认插入到 static 块中
        this.generateToMethodName = generateToMethodName
        if (this.generateToMethodName == null || this.generateToMethodName.length() == 0) {
            this.generateToMethodName = "<clinit>"
        }

        this.registerMethodName = registerMethodName
        this.scanPackageNames = scanPackageNames ?: new ArrayList<>()
        this.containsSuperClasses = containsSuperClasses

        convertIfNeeded()
    }

    private void convertIfNeeded() {
        interfaceName = convertDotToSlash(interfaceName)
        superClasses = convertedList(superClasses)
        generateToClassName = convertDotToSlash(generateToClassName)
        generateToMethodName = convertDotToSlash(generateToMethodName)
        registerMethodName = convertDotToSlash(registerMethodName)
        scanPackageNames = convertedList(scanPackageNames)
    }

    private String convertDotToSlash(String str) {
        return str ? str.replaceAll('\\.', '/').intern() : str
    }

    private ArrayList<String> convertedList(ArrayList<String> sourceList) {
        if (sourceList == null) {
            return new ArrayList<String>()
        }

        ArrayList<String> convertedList = new ArrayList<>()
        for (int i = 0; i < sourceList.size(); i++) {
            String convertedArg = convertDotToSlash(sourceList.get(i))
            convertedList.add(convertedArg)
        }
        return convertedList
    }


    // GETTER METHODS

    /**
     * 扫描实现此接口的类
     */
    String getInterfaceName() {
        return interfaceName
    }

    /**
     * 扫描继承自以下父类的 java 类
     */
    ArrayList<String> getSuperClasses() {
        return superClasses
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

    /**
     * 最终命中的类是否包含所指定的父类
     */
    boolean getContainsSuperClasses() {
        return containsSuperClasses
    }


    // OVERRIDE METHODS

    @Override
    String toString() {
        return "ConfigItem{\n" +
                "\tinterfaceName = " + interfaceName + ',\n' +
                "\tsuperClasses = " + superClasses.toString() + ',\n' +
                "\tgenerateToClassName = " + generateToClassName + ',\n' +
                "\tgenerateToMethodName = " + generateToMethodName + ',\n' +
                "\tregisterMethodName = " + registerMethodName + ',\n' +
                "\tscanPackageNames = " + scanPackageNames.toString() + ',\n' +
                "\tcontainsSuperClasses = " + containsSuperClasses.toString() + ',\n' +
                '}';
    }

}