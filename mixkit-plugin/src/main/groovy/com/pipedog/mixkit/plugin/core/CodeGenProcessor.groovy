package com.pipedog.mixkit.plugin.core

import com.pipedog.mixkit.plugin.config.ConfigItem
import com.pipedog.mixkit.plugin.utils.Logger
import org.apache.commons.io.IOUtils
import org.objectweb.asm.*

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * 代码生成器（编译器通过字节码插桩在指定类及方法插入代码）
 * @author liang
 * @time 2021/12/03
 */
class CodeGenProcessor {

    private ConfigItem configItem

    /**
     * 插入注册代码到指定类中
     * @param configItem 该插入动作指定配置信息
     */
    static void insertInitCodeTo(ConfigItem configItem) {
        if (configItem == null || configItem.classList.isEmpty()) {
            return
        }

        CodeGenProcessor processor = new CodeGenProcessor(configItem)
        processor.insertCodeIntoTargetClassFile()
    }

    private CodeGenProcessor(ConfigItem configItem) {
        this.configItem = configItem
    }


    // PRIVATE METHODS

    private void insertCodeIntoTargetClassFile() {
        File file = configItem.fileContainsInitClass

        if (file.getName().endsWith('.jar')) {
            genCodeIntoJarFile(file)
        } else {
            genCodeIntoClassFile(file)
        }
    }

    /**
     * 处理 Jar 包中的字节码注入
     */
    private void genCodeIntoJarFile(File jarFile) {
        if (jarFile == null) {
            return
        }

        // 输出临时 class 文件路径
        File optJar = new File(jarFile.getParentFile(), jarFile.name + ".opt")
        if (optJar.exists()) {
            optJar.delete()
        }

        JarFile file = new JarFile(jarFile)
        Enumeration enumeration = file.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = file.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)

            // 如果是需要插入代码的目标类，则执行插入代码逻辑，否
            // 则直接将 inputStream 写入到 jarOuputStream 中
            if (isTargetClass(entryName)) {
                Logger.i("generate code into : " + entryName)
                def bytes = performGenCode(inputStream)
                jarOutputStream.write(bytes)
            } else {
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
            }

            inputStream.close()
            jarOutputStream.closeEntry()
        }

        jarOutputStream.close()
        file.close()

        if (jarFile.exists()) {
            jarFile.delete()
        }

        optJar.renameTo(jarFile)
    }

    /**
     * 处理 .class 文件的字节码注入
     */
    private void genCodeIntoClassFile(File file) {
        if (file == null) {
            return
        }

        def optClass = new File(file.getParent(), file.name + ".opt")
        FileOutputStream outputStream = new FileOutputStream(optClass)
        FileInputStream inputStream = new FileInputStream(file)

        def bytes = performGenCode(inputStream)
        outputStream.write(bytes)

        inputStream.close()
        outputStream.close()

        if (file.exists()) {
            file.delete()
        }

        optClass.renameTo(file)
    }

    private boolean isTargetClass(String entryName) {
        if (entryName == null || !entryName.endsWith(".class")) {
            return false
        }

        String targetClassName = configItem.getGenerateToClassName()
        if (targetClassName == null || targetClassName.isEmpty()) {
            return false
        }

        entryName = entryName.substring(0, entryName.lastIndexOf('.'))
        boolean isTargetClass = entryName == targetClassName
        return isTargetClass
    }

    private byte[] performGenCode(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ClassVisitor cv = new MixClassVisitor(Opcodes.ASM6, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }


    // INTERNAL CLASS

    private class MixClassVisitor extends ClassVisitor {

        MixClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor)
        }

        @Override
        void visit(int version, int access, String name,
                   String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String descriptor,
                                  String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)

            // 注入代码到指定方法中（仅支持非静态方法）
            if (name == configItem.getGenerateToMethodName() && (Opcodes.ACC_STATIC & access) == 0) {
                mv = new MixMethodVisitor(Opcodes.ASM6, mv)
            }

            return mv
        }

    }

    class MixMethodVisitor extends MethodVisitor {

        MixMethodVisitor(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor)
        }

        @Override
        void visitInsn(int opcode) {
            if (!(opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                return
            }

            // 遍历实现指定接口的类（接口名可通过 configItem.getInterfaceName() 获取）
            configItem.classList.each { classname ->
                insertAutoRegisterCode(classname)
            }

            super.visitInsn(opcode)
        }

        @Override
        void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals)
        }


        // PRIVATE METHODS

        private void insertAutoRegisterCode(String classname) {
            // 加载 this
            mv.visitVarInsn(Opcodes.ALOAD, 0)

            // 用无参构造方法创建一个实例（这里指实现了指定接口的类的实例）
            mv.visitTypeInsn(Opcodes.NEW, classname)
            mv.visitInsn(Opcodes.DUP)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, classname,
                    "<init>", "()V", false)

            // 调用注册方法将新创建的实例注册到指定类中
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    configItem.getGenerateToClassName(),
                    configItem.getRegisterMethodName(),
                    "(L${configItem.getInterfaceName()};)V",
                    false
            )
        }

    }

}