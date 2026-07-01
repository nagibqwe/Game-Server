package game.core.script;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ScriptClassLoader extends ClassLoader {

    /**
     * 脚本字节码加载器 注意： 1 为了实现重新加载功能, 此处需打破JVM类加载器双亲委托模型,
     * 否则始终由AppClassLoader加载将无法支持重新加载功能 2 暂不支持脚本内部类
     *
     * @param classFilePath 路径
     * @param className 类全名
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Class<?> loadScriptClass(String classFilePath, String className) throws ClassNotFoundException, IOException {
        String classFileName = classFilePath.replace('.', '/') + "/" + className.replace('.', '/') + ".class";
        byte[] bytes = loadClassData(classFileName);
        Class<?> clazz = this.defineClass(className, bytes, 0, bytes.length);
        return clazz;
    }

    /**
     * 读取字节码
     *
     * @param classFileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private byte[] loadClassData(String classFileName) throws FileNotFoundException, IOException {
        int readCount;
        FileInputStream in = null;
        ByteArrayOutputStream buffer = null;
        try {
            in = new FileInputStream(classFileName);
            buffer = new ByteArrayOutputStream();
            while ((readCount = in.read()) != -1) {
                buffer.write(readCount);
            }
            return buffer.toByteArray();
        } finally {
            if (in != null) {
                in.close();
            }
            if (buffer != null) {
                buffer.close();
            }
        }
    }

}
