package game.core.util;

import game.core.script.JavaSourceFromString;
import game.core.script.ScriptClassLoader;

import javax.tools.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    /**
     *
     * @param name 文件名
     * @param code 文件数据
     * @param classpath javac classpath
     * @param classFilePath javac编译输出路径，内部ClassLoader查找脚本class文件路径
     * @return
     * @throws Exception
     */
    public static Class<?> javaCodeToObject(String name, String code, String classpath, String classFilePath) throws Exception {
        boolean reload;
        Class<?> c = Class.forName(name);
        reload = (c != null);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        ScriptClassLoader loader;
        boolean success;
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            JavaFileObject jfile = new JavaSourceFromString(name, code);
            List<JavaFileObject> jfiles = new ArrayList<>();
            jfiles.add(jfile);
            List<String> options = new ArrayList<>();
            // 重新加载类需要用单独的类加载器
            loader = new ScriptClassLoader();
            options.add("-encoding");
            options.add("UTF-8");
            options.add("-classpath");
            options.add(classpath);
            options.add("-d");
            options.add(classFilePath); // javac编译结果输出到classFilePath目录中
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
            success = task.call();
        }
        if (!success) {
            return null;
        }

        if (reload) {
            return loader.loadScriptClass(classFilePath, name);
        } else {
            return Class.forName(name);
        }

    }

    /**
     * 判断c是否继承接口szInterface
     *
     * @param c
     * @param szInterface
     * @return
     */
    public static boolean isInterface(Class<?> c, Class<?> szInterface) {
        Class<?>[] face = c.getInterfaces();
        for (int i = 0, j = face.length; i < j; i++) {
            if (face[i].isAssignableFrom(szInterface)) {
                return true;
            } else {
                Class<?>[] face1 = face[i].getInterfaces();
                for (Class<?> face11 : face1) {
                    if (face11.isAssignableFrom(szInterface)) {
                        return true;
                    } else if (isInterface(face11, szInterface)) {
                        return true;
                    }
                }
            }
        }
        if (null != c.getSuperclass()) {
            return isInterface(c.getSuperclass(), szInterface);
        }
        return false;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param <T>
     * @param packageName
     * @param parentClass
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> Set<Class<T>> getSubClasses(String packageName, Class<T> parentClass) throws IOException, ClassNotFoundException {
        //第一个class类的集合
        Set<Class<T>> classes = new LinkedHashSet<>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

        //循环迭代下去
        while (dirs.hasMoreElements()) {
            //获取下一个元素
            URL url = dirs.nextElement();
            //得到协议的名称
            String protocol = url.getProtocol();
            if (protocol == null) {
                continue;
            }
            //如果是以文件的形式保存在服务器上
            switch (protocol) {
                case "file":
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes, parentClass);
                    break;
                case "jar":
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    getClasssFromJarFile(jar, packageDirName, classes, parentClass);
                    break;
            }

        }

        return classes;
    }

    /**
     * 从jar文件中读取指定目录下面的所有的class文件
     *
     * @param jarFile jar文件存放的位置
     * @param filePaht 指定的文件目录
     * @param classes
     * @param parentClass
     */
    public static <T> void getClasssFromJarFile(JarFile jarFile, String filePaht, Set<Class<T>> classes, Class<T> parentClass) throws IOException, ClassNotFoundException {
        List<JarEntry> jarEntryList = new ArrayList<>();
        Enumeration<JarEntry> ee = jarFile.entries();
        while (ee.hasMoreElements()) {
            JarEntry entry = ee.nextElement();
            // 过滤我们出满足我们需求的东西
            if (entry.getName().startsWith(filePaht) && entry.getName().endsWith(".class")) {
                jarEntryList.add(entry);
            }
        }
        for (JarEntry entry : jarEntryList) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - 6);
            Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (parentClass.isAssignableFrom(loadClass) && !parentClass.equals(loadClass)) {
                @SuppressWarnings("unchecked")
                Class<T> result = (Class<T>) loadClass;
                classes.add(result);
            }
        }

    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param <T>
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     * @param parentClass
     * @throws ClassNotFoundException
     */
    public static <T> void findAndAddClassesInPackageByFile(String packageName,
            String packagePath, final boolean recursive, Set<Class<T>> classes, Class<T> parentClass) throws ClassNotFoundException {
        // 获取此包的目录 建立一个File  
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回  
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");  
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录  
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)  
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件  
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes, parentClass);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                // 添加到集合中去
                // classes.add(Class.forName(packageName + '.' +
                // className));
                // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                if (parentClass.isAssignableFrom(loadClass) && !parentClass.equals(loadClass)) {
                    @SuppressWarnings("unchecked")
                    Class<T> result = (Class<T>) loadClass;
                    classes.add(result);
                }

            }
        }
    }

}
