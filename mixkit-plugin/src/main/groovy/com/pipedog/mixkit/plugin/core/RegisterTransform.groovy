package com.pipedog.mixkit.plugin.core

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.pipedog.mixkit.plugin.config.ConfigItem
import com.pipedog.mixkit.plugin.config.ConfigManager
import com.pipedog.mixkit.plugin.utils.Logger
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class RegisterTransform extends Transform {

    private ConfigManager configManager;

    RegisterTransform(Project project) {

    }

    void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager
    }


    // OVERRIDE METHODS

    @Override
    String getName() {
        return "mixkit-plugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {

        boolean isLeftSlash = File.separator == '/'
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()

        inputs.each { TransformInput input ->

            // 扫描搜有 Jar 包
            input.jarInputs.each { JarInput jarInput ->
                // 使用 JarFile 进行解压
                Enumeration<JarEntry> enumeration = new JarFile(jarInput.file).entries()

                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement()
                    scanJarFile(outputProvider, entry)
                }
            }

            // 扫描所有 .class 文件
            input.directoryInputs.each { DirectoryInput directoryInput ->

                File dest = outputProvider.getContentLocation(
                        directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                String root = directoryInput.file.absolutePath

                if (!root.endsWith(File.separator)) {
                    root += File.separator
                }

                directoryInput.file.eachFileRecurse { File file ->
                    def path = file.absolutePath.replace(root, '')
                    if (!isLeftSlash) {
                        path = path.replaceAll("\\\\", "/")
                    }
                    if (file.isFile()) {
                        scanClassFile(outputProvider, file, path)
                    }
                }

                // Copy to dest
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }


//        config.list.each { ext ->
//            if (ext.fileContainsInitClass) {
//                Logger.i("insert register code to file:" + ext.fileContainsInitClass.absolutePath)
//                if (ext.classList.isEmpty()) {
//                    Logger.e("No class implements found for interface:" + ext.interfaceName)
//                } else {
//                    ext.classList.each {
//                        Logger.i(it)
//                    }
//                    CodeInsertProcessor.insertInitCodeTo(ext)
//                }
//            } else {
//                Logger.e("The specified register class not found:" + ext.registerClassName)
//            }
//        }

        ArrayList<ConfigItem> configItems = configManager.getConfigItems()
        configItems.each { ConfigItem item ->
            // 没有找到指定的代码注入的目标类
            if (item.fileContainsInitClass == null) {
                Logger.e("The specified register class not found:" + item.getGenerateToClassName())
                return
            }

            // 没有找到接口的实现类
            if (item.classList.isEmpty()) {
                Logger.e("No class implements found for interface:" + item.getInterfaceName())
                return
            }

            // 打印实现该指定接口的类
            item.classList.each {
                Logger.i(it)
            }

            // TODO:
            // 插入代码
            // CodeInsertProcessor.insertInitCodeTo(item)
        }
    }


    // PRIVATE METHODS

    private void scanJarFile(TransformOutputProvider outputProvider, JarEntry jarEntry) {

    }

    private void scanClassFile(TransformOutputProvider outputProvider, File file, String filePath) {

    }

//                    if(file.isFile() && ScanUtil.shouldProcessClass(path)){
//                        ScanUtil.scanClass(file)
//                    }
//
//
////                    static boolean shouldProcessClass(String entryName) {
////                        return entryName != null && entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
////                    }


//    /**
//     * scan jar file
//     * @param jarFile All jar files that are compiled into apk
//     * @param destFile dest file after this transform
//     */
//    static void scanJar(File jarFile, File destFile) {
//        if (jarFile) {
//            def file = new JarFile(jarFile)
//            Enumeration enumeration = file.entries()
//            while (enumeration.hasMoreElements()) {
//                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
//                String entryName = jarEntry.getName()
//                if (entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)) {
//                    InputStream inputStream = file.getInputStream(jarEntry)
//                    scanClass(inputStream)
//                    inputStream.close()
//                } else if (ScanSetting.GENERATE_TO_CLASS_FILE_NAME == entryName) {
//                    // mark this jar file contains LogisticsCenter.class
//                    // After the scan is complete, we will generate register code into this file
//                    RegisterTransform.fileContainsInitClass = destFile
//                }
//            }
//            file.close()
//        }
//    }
//
//    static boolean shouldProcessPreDexJar(String path) {
//        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
//    }
//
//    static boolean shouldProcessClass(String entryName) {
//        return entryName != null && entryName.startsWith(ScanSetting.ROUTER_CLASS_PACKAGE_NAME)
//    }
//
//    /**
//     * scan class file
//     * @param class file
//     */
//    static void scanClass(File file) {
//        scanClass(new FileInputStream(file))
//    }
//
//    static void scanClass(InputStream inputStream) {
//        ClassReader cr = new ClassReader(inputStream)
//        ClassWriter cw = new ClassWriter(cr, 0)
//        ScanClassVisitor cv = new ScanClassVisitor(Opcodes.ASM5, cw)
//        cr.accept(cv, ClassReader.EXPAND_FRAMES)
//        inputStream.close()
//    }
//
//    static class ScanClassVisitor extends ClassVisitor {
//
//        ScanClassVisitor(int api, ClassVisitor cv) {
//            super(api, cv)
//        }
//
//        void visit(int version, int access, String name, String signature,
//                   String superName, String[] interfaces) {
//            super.visit(version, access, name, signature, superName, interfaces)
//            RegisterTransform.registerList.each { ext ->
//                if (ext.interfaceName && interfaces != null) {
//                    interfaces.each { itName ->
//                        if (itName == ext.interfaceName) {
//                            //fix repeated inject init code when Multi-channel packaging
//                            if (!ext.classList.contains(name)) {
//                                ext.classList.add(name)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

}