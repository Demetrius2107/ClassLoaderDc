package org.hua.classloader.compiler;

import org.hua.classloader.core.ChameleonSrcCode;
import org.hua.classloader.pipeline.ChainContext;
import org.hua.classloader.pipeline.ChainProcess;

import javax.tools.*;

import java.io.*;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.ResourceUtils.toURI;

/**
 * @description: 编译源代码并保存至内存,可惜类无法主动卸载,不然 remove 的时候可以卸载，避免占用内存
 * @Author: Xhy
 * @CreateTime: 2024-03-15 15:04
 */
public class MemoryCompiler implements Compiler, ChainProcess<ChameleonSrcCode> {

    /**
     * 编译指定java源代码
     * @param javaSrc java源代码
     * @return 返回类的全限定名和编译后的class字节码字节数组的映射
     */
    public Map<String, byte[]> compile(String javaSrc) throws IOException {
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(javaSrc);
        if (matcher.find()) {
            return compile(matcher.group(1) + ".java", javaSrc);
        }
        return null;
    }

    /**
     * 编译指定java源代码
     * @param javaName java文件名
     * @param javaSrc java源码内容
     * @return 返回类的全限定名和编译后的class字节码字节数组的映射
     */
    public Map<String, byte[]> compile(String javaName, String javaSrc) throws IOException {

        // 获取 java 编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        // 使用内存管理 java 文件,这里就不用将载入的 java 源代码存入磁盘，而是基于内存了
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)){
            // 编译
            JavaFileObject javaFileObject = manager.makeStringSource(javaName, javaSrc);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            if (task.call()) {
                return manager.getClassBytes();
            }
        }
        return null;
    }



    @Override
    public void process(ChainContext<ChameleonSrcCode> context) {
        try {
            final Map<String, byte[]> classMap = compile(context.getProcessModel().getJavaSrc());
            context.getProcessModel().setClassMap(classMap);
        } catch (IOException e) {
            context.setNeedBreak(true);
            context.setException(e);
        }
    }


    final static class MemoryJavaFileManager extends ForwardingJavaFileManager {

        private final static String EXT = ".java";
        private Map<String, byte[]> classBytes;

        public MemoryJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
            classBytes = new HashMap<String, byte[]>();
        }

        public Map<String, byte[]> getClassBytes() {
            return classBytes;
        }

        public void close() throws IOException {
            classBytes = new HashMap<String, byte[]>();
        }

        public void flush() throws IOException {
        }

        private static class StringInputBuffer extends SimpleJavaFileObject {
            final String code;

            StringInputBuffer(String name, String code) {
                super(toURI(name), Kind.SOURCE);
                this.code = code;
            }

            public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
                return CharBuffer.wrap(code);
            }

            public Reader openReader() {
                return new StringReader(code);
            }
        }

        private class ClassOutputBuffer extends SimpleJavaFileObject {
            private String name;

            ClassOutputBuffer(String name) {
                super(toURI(name), Kind.CLASS);
                this.name = name;
            }

            public OutputStream openOutputStream() {
                return new FilterOutputStream(new ByteArrayOutputStream()) {
                    public void close() throws IOException {
                        out.close();
                        ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                        classBytes.put(name, bos.toByteArray());
                    }
                };
            }
        }

        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location,
                                                   String className,
                                                   JavaFileObject.Kind kind,
                                                   FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                return new ClassOutputBuffer(className);
            } else {
                return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        }

        static JavaFileObject makeStringSource(String name, String code) {
            return new StringInputBuffer(name, code);
        }

        static URI toURI(String name) {
            File file = new File(name);
            if (file.exists()) {
                return file.toURI();
            } else {
                try {
                    final StringBuilder newUri = new StringBuilder();
                    newUri.append("mfm:///");
                    newUri.append(name.replace('.', '/'));
                    if (name.endsWith(EXT)) {
                        newUri.replace(newUri.length() - EXT.length(), newUri.length(), EXT);
                    }
                    return URI.create(newUri.toString());
                } catch (Exception exp) {
                    return URI.create("mfm:///com/sun/script/java/java_source");
                }
            }
        }
    }
}
