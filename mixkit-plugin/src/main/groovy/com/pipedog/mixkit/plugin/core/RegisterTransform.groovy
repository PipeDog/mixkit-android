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
import com.pipedog.mixkit.plugin.utils.PerfMonitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class RegisterTransform extends Transform {

    private Project project;
    private ConfigManager configManager;

    RegisterTransform(Project project) {
        this.project = project
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
        scanClasses(transformInvocation)
        insertCode()
    }


    // PRIVATE METHODS

    private void scanClasses(TransformInvocation transformInvocation) {
        PerfMonitor.startPerf("Trans|Scan")

        ScanProcessor scanProcessor = new ScanProcessor(configManager.getConfigItems())
        TransformOutputProvider outputProvider = transformInvocation.outputProvider

        // ??????????????????
        transformInvocation.inputs.each { TransformInput input ->

            // ?????? Jar
            input.jarInputs.each { JarInput jarInput ->
                scanJarFile(jarInput, outputProvider, scanProcessor)
            }

            // ????????????
            input.directoryInputs.each { DirectoryInput directoryInput ->
                // ?????????????????????
                File dest = outputProvider.getContentLocation(
                        directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                String root = directoryInput.file.absolutePath

                if (!root.endsWith(File.separator)) {
                    root += File.separator
                }

                // ??????????????????????????????
                directoryInput.file.eachFileRecurse { File file ->
                    scanClassFile(file, root, dest, scanProcessor)
                }

                FileUtils.copyDirectory(directoryInput.file, dest)
            }

        }

        PerfMonitor.endPerf("Trans|Scan")
    }

    private void insertCode() {
        PerfMonitor.startPerf("Trans|GenCode")

        configManager.configItems.each { ConfigItem item ->
            if (!item.fileContainsInitClass) {
                Logger.e("The specified register class not found:" + item.getGenerateToClassName())
                return
            }

            if (item.classList.isEmpty()) {
                Logger.w("No class implements found for interface:" + item.getInterfaceName())
                return
            }

            item.classList.each {
                Logger.i(it)
            }
            CodeGenProcessor.insertInitCodeTo(item)
        }

        PerfMonitor.endPerf("Trans|GenCode")
    }

    private void scanJarFile(JarInput jarInput,
                             TransformOutputProvider outputProvider,
                             ScanProcessor scanProcessor) {
        PerfMonitor.startPerf("Trans|Scan|Jar")

        // ??????????????????
        File src = jarInput.file

        // ?????? Jar ??????????????????????????????????????????????????????
        File dest = getDestFile(jarInput, outputProvider)
        scanProcessor.scanJar(src, dest)

        // Copy Jar ????????? transform ??????
        FileUtils.copyFile(src, dest)

        PerfMonitor.endPerf("Trans|Scan|Jar")
    }

    private void scanClassFile(File file, String rootPath, File dest, ScanProcessor scanProcessor) {
        def path = file.absolutePath.replace(rootPath, "")
        if (!file.isFile()) {
            return
        }

        def entryName = path
        boolean isLeftSlash = File.separator == '/'
        if (!isLeftSlash) {
            entryName = entryName.replaceAll("\\\\", "/")
        }

        scanProcessor.checkTargetClass(
                entryName, new File(dest.absolutePath + File.separator + path))
        if (scanProcessor.shouldProcessClass(entryName)) {
            PerfMonitor.startPerf("Trans|Scan|Class")
            scanProcessor.scanClass(file)
            PerfMonitor.endPerf("Trans|Scan|Class")
        }
    }

    private File getDestFile(JarInput jarInput, TransformOutputProvider outputProvider) {
        def destName = jarInput.name

        // ??????????????????????????????????????????????????????
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4)
        }

        // ??????????????????
        File dest = outputProvider.getContentLocation(
                destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        return dest
    }

}