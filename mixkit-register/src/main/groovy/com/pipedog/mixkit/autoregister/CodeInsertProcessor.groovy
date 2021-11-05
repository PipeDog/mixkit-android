package com.pipedog.mixkit.autoregister

import org.apache.commons.io.IOUtils
import org.objectweb.asm.*

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class CodeInsertProcessor {

    RegisterInfo mRegisterInfo

    private CodeInsertProcessor(RegisterInfo registerInfo) {
        this.mRegisterInfo = registerInfo
    }

    public static void insertInitCodeTo(RegisterInfo registerInfo) {
        if (registerInfo == null || registerInfo.classList.isEmpty()) {
            return
        }

        CodeInsertProcessor processor = new CodeInsertProcessor(registerInfo)
        processor.insertCodeIntoTargetClassFile()
    }

    private void insertCodeIntoTargetClassFile() {
        File file = mRegisterInfo.fileContainsInitClass

        if (file.getName().endsWith('.jar')) {
            generateCodeIntoJarFile(file)
        } else {
            generateCodeIntoClassFile(file)
        }
    }

    // 处理 jar 包中的 class 代码注入
    private File generateCodeIntoJarFile(File jarFile) {
        if (jarFile == null) {
            return null
        }

        def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
        if (optJar.exists()) {
            optJar.delete()
        }

        def file = new JarFile(jarFile)
        Enumeration enumeration = file.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = file.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)

            if (isInitClass(entryName)) {
                Logger.i('generate code into:' + entryName)

                def bytes = doGenerateCode(inputStream)
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
        return jarFile
    }

    boolean isInitClass(String entryName) {
        if (entryName == null || !entryName.endsWith(".class")) {
            return false
        }

        String initClassName = mRegisterInfo.initClassName
        if (initClassName == null || initClassName.isEmpty()) {
            return false
        }

        entryName = entryName.substring(0, entryName.lastIndexOf('.'))
        return initClassName == entryName
    }

    /**
     * 处理 class 的注入
     * @param file class文件
     * @return 修改后的字节码文件内容
     */
    private byte[] generateCodeIntoClassFile(File file) {
        def optClass = new File(file.getParent(), file.name + ".opt")
        FileOutputStream outputStream = new FileOutputStream(optClass)
        FileInputStream inputStream = new FileInputStream(file)

        def bytes = doGenerateCode(inputStream)
        outputStream.write(bytes)

        inputStream.close()
        outputStream.close()

        if (file.exists()) {
            file.delete()
        }

        optClass.renameTo(file)
        return bytes
    }

    private byte[] doGenerateCode(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ClassVisitor cv = new MyClassVisitor(Opcodes.ASM6, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

    class MyClassVisitor extends ClassVisitor {

        MyClassVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        void visit(int version, int access, String name, String signature,
                   String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc,
                                  String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
            if (name == mRegisterInfo.initMethodName) {
                // 注入代码到指定的方法之中
                boolean _static = (access & Opcodes.ACC_STATIC) > 0
                mv = new MyMethodVisitor(Opcodes.ASM6, mv, _static)
            }
            return mv
        }
    }

    class MyMethodVisitor extends MethodVisitor {
        boolean _static;

        MyMethodVisitor(int api, MethodVisitor mv, boolean _static) {
            super(api, mv)
            this._static = _static;
        }

        @Override
        void visitInsn(int opcode) {
            if (!(opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                return
            }

            mRegisterInfo.classList.each { name ->
                if (!_static) {
                    // 加载 this
                    mv.visitVarInsn(Opcodes.ALOAD, 0)
                }
                // 用无参构造方法创建一个组件实例
                mv.visitTypeInsn(Opcodes.NEW, name)
                mv.visitInsn(Opcodes.DUP)
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", "()V", false)
                // 调用注册方法将组件实例注册到组件库中
                if (_static) {
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC
                            , mRegisterInfo.registerClassName
                            , mRegisterInfo.registerMethodName
                            , "(L${mRegisterInfo.interfaceName};)V"
                            , false)
                } else {
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL
                            , mRegisterInfo.registerClassName
                            , mRegisterInfo.registerMethodName
                            , "(L${mRegisterInfo.interfaceName};)V"
                            , false)
                }
            }

            super.visitInsn(opcode)
        }

        @Override
        void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(maxStack + 4, maxLocals)
        }
    }

}