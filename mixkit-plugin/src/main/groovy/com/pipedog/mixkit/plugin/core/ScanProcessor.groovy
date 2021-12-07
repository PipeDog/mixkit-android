package com.pipedog.mixkit.plugin.core

import com.pipedog.mixkit.plugin.config.ConfigItem
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.regex.Pattern

/**
 * 代码扫描器（Jar 包及 .class 文件扫描）
 * @author liang
 * @time 2021/12/03
 */
class ScanProcessor {

    private ArrayList<ConfigItem> configItems;

    ScanProcessor(ArrayList<ConfigItem> configItems) {
        this.configItems = configItems
    }

    /**
     * 扫描 Jar 包
     * @param jarFile 来源 Jar 包文件
     * @param destFile transform 后的目标 Jar 包文件
     */
    boolean scanJar(File jarFile, File destFile) {
        if (!jarFile) {
            return false
        }

        def srcFilePath = jarFile.absolutePath
        def file = new JarFile(jarFile)
        Enumeration enumeration = file.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()

            // support 包不扫描
            if (entryName.startsWith("android/support")) {
                break
            }

            checkTargetClass(entryName, destFile, srcFilePath)

            // 是否需要过滤这个类
            if (!shouldProcessClass(entryName)) {
                continue
            }

            InputStream inputStream = file.getInputStream(jarEntry)
            scanClass(inputStream, jarFile.absolutePath)
            inputStream.close()
        }

        if (file != null) {
            file.close()
        }

        return true
    }

    /**
     * 检查此 entryName 是否是被注入注册代码的类，如果是则记录此文件（class 文件或 jar 包文件），用于后续的注册代码注入
     */
    private boolean checkTargetClass(String entryName, File destFile) {
        return checkTargetClass(entryName, destFile, "")
    }


    private boolean checkTargetClass(String entryName, File destFile, String srcFilePath) {
        if (entryName == null || !entryName.endsWith(".class")) {
            return false
        }

        entryName = entryName.substring(0, entryName.lastIndexOf("."))
        def found = false

        configItems.each {item ->
            if (item.getGenerateToClassName() != entryName) {
                return
            }

            item.fileContainsInitClass = destFile
            if (!destFile.name.endsWith(".jar")) {
                return
            }

            found = true
        }

        return found
    }

    /**
     * 是否需要处理该文件
     * @param entryName entry 名称
     *
     *  文件夹中的文件，eg：
     *      com/pipedog/mixkit/App.class
     *      com/pipedog/mixkit/BuildConfig.class
     *      com/pipedog/mixkit/R$attr.class
     *      com/pipedog/mixkit/R.class
     *
     *  jar 包中的文件，eg：
     *      android/support/v4/BuildConfig.class
     *      com/lib/pipedog/common/util/UITools.class
     * */
    private boolean shouldProcessClass(String entryName) {
        if (entryName == null || !entryName.endsWith(".class")) {
            return false
        }

        entryName = entryName.substring(0, entryName.lastIndexOf("."))
        def len = configItems.size()

        for (int i = 0; i < len; i++) {
            if (shouldProcessClass(entryName, configItems.get(i))) {
                return true
            }
        }

        return false
    }

    private static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    private static boolean shouldProcessClass(String entryName, ConfigItem configItem) {
        // TODO:
        // 如果存在指定扫描范围，则判断 entryName 是否在该 package 路径下，如果是则 return true，不是则 return false
        // 如果没有指定扫描范围，则默认返回 true
        // return entryName != null && entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
        return true
    }

    /**
     * 处理 class 的注入
     * @param file class 文件
     */
    private boolean scanClass(File file) {
        return scanClass(file.newInputStream(), file.absolutePath)
    }

    /**
     * refer hack class when object init
     */
    private boolean scanClass(InputStream inputStream, String filePath) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM6, cw, filePath)

        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        inputStream.close()
        return cv.found
    }

    // INTERNAL CLASS

    private class ScanClassVisitor extends ClassVisitor {

        private String filePath
        private boolean found = false

        ScanClassVisitor(int api, ClassVisitor cv, String filePath) {
            super(api, cv)
            this.filePath = filePath
        }


        // OVERRIDE METHODS
        @Override
        void visit(int version, int access, String name,
                   String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)

            // 抽象类、接口、非 public 等类无法调用其无参构造方法
            if (is(access, Opcodes.ACC_ABSTRACT) ||
                is(access, Opcodes.ACC_INTERFACE) ||
                !is(access, Opcodes.ACC_PUBLIC)) {
                return
            }

            configItems.each { ConfigItem configItem ->

                if (!shouldProcessClass(name, configItem)) {
                    return
                }

                if (configItem.getInterfaceName() && interfaces != null) {
                    interfaces.each {itName ->
                        if (itName != configItem.getInterfaceName()) {
                            return
                        }

                        // 收集实现了指定 interface 的类
                        configItem.classList.add(name)
                        found = true
                    }
                }

            }

        }


        // PRIVATE METHODS

        private boolean is(int access, int flag) {
            return (access & flag) == flag
        }

        private boolean isFound() {
            return found
        }

    }

}