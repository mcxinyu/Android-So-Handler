package com.imf.plugin.so

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author <a href=mailto:mcxinyu@foxmail.com>yuefeng</a> in 2023/6/6.
 */
class LoadLibraryVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM7, classVisitor) {
    val TARGET_FLAG = "java/lang/System"
    val LOAD_LIBRARY = "loadLibrary"
    val LOAD = "load"
    val SO_LOAD_HOOK = "com/imf/so/SoLoadHook"
    val SO_LOAD_INTERFACES = "com/imf/so/SoLoadProxy"
    val ANNOTATION = "Lcom/imf/so/KeepSystemLoadLib;"
    var isClassSkip = false
    private var className: String? = null

    /**
     * 访问类头部信息
     *
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        if (!interfaces.isNullOrEmpty()) {
            for (it in interfaces) {
                isClassSkip = SO_LOAD_INTERFACES == it
                if (isClassSkip) {
                    break
                }
            }
        }
        className = name
    }

    /**
     * 访问类的注解
     *
     * @param descriptor 注解类的类描述
     * @param visible runtime 时期注解是否可以被访问
     * @return 返回一个注解值访问器
     */
    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        if (!visible && !isClassSkip && ANNOTATION == descriptor) {
            isClassSkip = true
            // println "类${className} isClassSkip = ${isClassSkip} ,注解信息 descriptor=${descriptor} , visible=$visible"
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        // 这里我们修改了MethodVisitor再返回，即修改了这个方法
        return if (isClassSkip) methodVisitor else object : MethodVisitor(Opcodes.ASM7, methodVisitor) {
            private var isMethodSkip = false

            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                if (!visible) {
                    isMethodSkip = ANNOTATION == descriptor
                }
                return super.visitAnnotation(descriptor, visible)
            }

            override fun visitMethodInsn(
                opcode: Int,
                owner: String,
                name: String,
                descriptor: String,
                isInterface: Boolean
            ) {
                // 覆盖方法调用的过程
                val newOwner = if (!isMethodSkip && opcode == Opcodes.INVOKESTATIC
                    && TARGET_FLAG == owner
                    && (LOAD_LIBRARY == name || LOAD == name)
                ) SO_LOAD_HOOK
                else owner
                super.visitMethodInsn(opcode, newOwner, name, descriptor, isInterface)
            }
        }
    }
}