package com.csh.plugin

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
import com.csh.asm.LifecycleClassVisitor
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

public class LifeCycleTransform extends Transform {

    @Override
    String getName() {
        return "LifeCycleTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }

    /**
     * 表示当前 Transform 是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        Collection<TransformInput> inps = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider

        for (TransformInput ti : inps) {
            for (DirectoryInput di: ti.directoryInputs) {
                File dir = di.file
                if (dir) {
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File f ->
                        // 对class文件读取解析
                        ClassReader reader = new ClassReader(f.bytes)
                        // 对class文件的写入
                        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
                        // 访问class文件的相应内容
                        ClassVisitor visitor = new LifecycleClassVisitor(writer)
                        // 依次调用ClassVisitor接口的各个方法
                        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
                        // toByteArray方法最终修改字节码并以byte数组形式返回
                        byte[] bytes = writer.toByteArray()
                        // 通过文件流写入方式覆盖掉原先的内容，实现class文件的改写
                        FileOutputStream fos = new FileOutputStream(f.path)
                        fos.write(bytes)
                        fos.close()

                    }
                }
                def dest = outputProvider.getContentLocation(di.name, di.contentTypes, di.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(di.file, dest)

            }

            // 由于从Android Gradle插件3.6.0-alpha01开始，不再生成R.java，并且将R片段与其他源分开编译为R.jar
            // 所以要把R.jar复制过来
            ti.jarInputs.each { JarInput jarInput ->
                File file = jarInput.file
                def dest = outputProvider.getContentLocation(jarInput.name,
                        jarInput.contentTypes,
                        jarInput.scopes, Format.JAR)
                FileUtils.copyFile(file, dest)
            }

        }

    }
}