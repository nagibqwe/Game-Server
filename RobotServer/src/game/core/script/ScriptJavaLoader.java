/**
 * @date 2014/3/26 18:55
 * @author ChenLong
 */
package game.core.script;

import game.core.command.CommandThreadFactory;
import game.core.exception.ReinitializedException;
import game.core.exception.UninitializedException;
import game.core.util.TimeUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * <b>脚本加载类</b>
 *
 * @author ChenLong
 */
public class ScriptJavaLoader
{
    private static final Logger log = LogManager.getLogger(ScriptJavaLoader.class);
    private final Map<Integer, ScriptBean> scripts = new ConcurrentHashMap<>();
    private String classpath; // javac classpath
    private String javaFilePath; // .java文件路径
    private String classFilePath; // javac编译输出路径，内部ClassLoader查找脚本class文件路径
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1, new CommandThreadFactory(ScriptJavaLoader.class.getSimpleName() + "_timer"));
    private boolean hasInitialized = false; // 是否已经初始化过, 即调用过init

    private ScriptJavaLoader()
    {
        classpath = new String();
        javaFilePath = new String();
        classFilePath = new String();
    }

    public synchronized void initialize(String javaFilePath, String classFilePath)
    {
        if (hasInitialized)
            throw new ReinitializedException("reinitialize JSLoader");
        this.javaFilePath = javaFilePath;
        this.classFilePath = classFilePath;
        buildClassPath();
        //scanModifyScriptFile();
        hasInitialized = true;
    }

    /**
     * 加载脚本
     *
     * @param scriptNames
     */
    public void loadScript(Map<Integer, String> scriptNames)
    {
        if (!hasInitialized)
            throw new UninitializedException("uninitialize JSLoader");
        loadScript(scriptNames, false);
    }

    public void reloadScript(Map<Integer, String> scriptNames)
    {
        if (!hasInitialized)
            throw new UninitializedException("uninitialize JSLoader");
        loadScript(scriptNames, true);
    }

    /**
     * 调用脚本
     *
     * @param scriptId
     * @param arg
     * @return
     * @throws ScriptNotFoundException 脚本不存在异常
     * @throws UninitializedException 未初始化异常
     */
    public Object call(int scriptId, Object arg) throws ScriptNotFoundException, UninitializedException
    {
        if (!hasInitialized)
            throw new UninitializedException("uninitialize JSLoader");
        IScript script = getScript(scriptId);
        if (script == null)
            throw new ScriptNotFoundException("cannot find script, scriptId = " + scriptId);
        return script.call(scriptId, arg);
    }

    public Object callmothed(int scriptId, String method, Object paras) throws ScriptNotFoundException, UninitializedException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        if (!hasInitialized)
            throw new UninitializedException("uninitialize JSLoader");
        IScript script = getScript(scriptId);
        if (script == null)
            throw new ScriptNotFoundException("cannot find script, scriptId = " + scriptId);

        Method fun = script.getClass().getDeclaredMethod(method, Object.class);
        return fun.invoke(script, paras);

    }

    public static ScriptJavaLoader getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    private ScriptBean getScriptBean(int scriptId)
    {
        return scripts.get(scriptId);
    }

    private IScript getScript(int scriptId)
    {
        ScriptBean bean = getScriptBean(scriptId);
        return (bean != null) ? bean.getScript() : null;
    }

    /**
     * 判断是否正确设置ScriptLoader的相关path
     *
     * @return
     */
    private boolean hasAvailablePath()
    {
        return !classpath.isEmpty() && !javaFilePath.isEmpty() && !classFilePath.isEmpty();
    }

    private void buildClassPath()
    {
        File file = new File(classFilePath);
        if (!file.exists())
            file.mkdirs();
        log.info(classFilePath + " lastModifed: [" + DateFormatUtils.format(file.lastModified(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()) + "]");
        StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) this.getClass().getClassLoader()).getURLs())
        {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        sb.append(classFilePath);
        this.classpath = sb.toString();
        log.info("Build scriptLoader classpath: [" + this.classpath + "]");
    }

    /**
     * 加载脚本 如果脚本class文件不存在则找相应的.java源文件编译后加载
     *
     * @param scriptNames
     * @param force 是否强制重新编译.java后加载
     */
    private void loadScript(Map<Integer, String> scriptNames, boolean force)
    {
        if (!hasAvailablePath())
            throw new NullPointerException();

        for (Map.Entry entry : scriptNames.entrySet())
        {
            int scriptId = ((Integer) entry.getKey());
            String scriptName = (String) entry.getValue();

            ScriptBean scriptBean = scripts.get(scriptId); // 判断重新加载脚本
            if (scriptBean != null)
                log.warn("Reload script, scriptId = " + scriptId + " oldName = " + scriptBean.getName() + " newName = " + scriptName);
            else
                scriptBean = new ScriptBean();

            if (!force)
            {
                try
                {
                    Class<?> clazz = Class.forName(scriptName); // 判断AppClassLoader是否能找到该脚本class文件
                    if (clazz != null)
                    {
                        scriptBean.setId(scriptId)
                                .setName(scriptName)
                                .setScript((IScript) clazz.newInstance())
                                .setClassFileTimestamp(0)
                                .setJavaFileTimestamp(0);
                        scripts.put(scriptId, scriptBean);
                        continue;
                    }
                }
                catch (ClassNotFoundException | IllegalAccessException | InstantiationException e)
                {
                    log.warn("AppClassLoader not fond class [" + scriptName + "]");
                }
            }

            try
            {
                String fullJavaFilePath = javaFilePath + "/" + scriptName.replace('.', '/') + ".java";
                log.info("Read java file [" + fullJavaFilePath + "]");
                File file = new File(fullJavaFilePath);
                if (file.isFile() && file.canRead())
                {
                    InputStream inStream = new FileInputStream(fullJavaFilePath);
                    byte[] bytes = new byte[(int) file.length()];
                    int len = inStream.read(bytes);
                    Class<?> clazz = javaCodeToObject(scriptName, new String(bytes, "UTF-8"));
                    if (clazz != null)
                    {
                        IScript script = (IScript) clazz.newInstance();
                        if (scriptId == script.getId())
                        {
                            scriptBean.setId(scriptId)
                                    .setName(scriptName)
                                    .setScript(script)
                                    .setJavaFileTimestamp(file.lastModified())
                                    .setClassFileTimestamp(TimeUtils.Time());
                            scripts.put(scriptId, scriptBean);
                        }
                        else
                        {
                            log.error("scriptId = " + scriptId + ", script.getId() = " + script.getId());
                        }
                    }
                }
                else
                {
                    log.error(file.getPath() + " cannot read");
                }
            }
            catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e)
            {
                log.error("IOException | ClassNotFoundException | IllegalAccessException | InstantiationException", e);
            }
            catch (Throwable t)
            {
                log.error("Throwable", t);
            }
        }
    }

    /**
     * 扫描已加载的脚本.class和.java时间戳 如果发现.java已更新则重新编译后加载该脚本 TODO 该方法未测试
     */
    private void scanModifyScriptFile()
    {
        scheduledExecutor.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Map<Integer, ScriptBean> scriptBeans = new HashMap<>();
                    synchronized (scripts)
                    {
                        scriptBeans.putAll(scripts);
                    }
                    Map<Integer, String> recompileScripts = new HashMap<>();
                    for (Map.Entry entry : scripts.entrySet())
                    {
                        int scriptId = (Integer) entry.getKey();
                        ScriptBean scriptBean = (ScriptBean) entry.getValue();

                        String fullJavaFilePath = javaFilePath + "/" + scriptBean.getName().replace('.', '/') + ".java";
                        log.info("scanner open file [" + fullJavaFilePath + "]");
                        File file = new File(fullJavaFilePath);
                        if (file.lastModified() > scriptBean.getClassFileTimestamp())
                        {
                            recompileScripts.put(scriptId, scriptBean.getName());
                        }
                    }
                    loadScript(recompileScripts, true);
                    System.out.println("in scanModifyScriptFile " + TimeUtils.Time() / 1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private Class<?> javaCodeToObject(String name, String code) throws IllegalAccessException, IOException, ClassNotFoundException
    {
        boolean reload = false;
        try
        {
            Class<?> c = Class.forName(name);
            reload = (c != null);
        }
        catch (ClassNotFoundException e)
        {
            log.info("load new script [" + name + "]");
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        JavaFileObject jfile = new JavaSourceFromString(name, code);
        List<JavaFileObject> jfiles = new ArrayList<>();
        jfiles.add(jfile);
        List<String> options = new ArrayList<>();

        // 重新加载类需要用单独的类加载器
        ScriptClassLoader loader = new ScriptClassLoader();

        options.add("-encoding");
        options.add("UTF-8");
        options.add("-classpath");
        options.add(this.classpath);
        options.add("-d");
        options.add(classFilePath); // javac编译结果输出到classFilePath目录中

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
        boolean success = task.call();
        fileManager.close();
        if (success)
        {
            if (reload)
            {
                try
                {
                    return loader.loadScriptClass(name);
                }
                catch (ClassNotFoundException e)
                {
                    log.error("ClassNotFoundException", e);
                }
            }
            else
            {
                return Class.forName(name);
            }
        }
        else
        {
            String error = "";
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics())
            {
                error = error + compilePrint(diagnostic);
            }
            log.error( error);
        }
        return null;
    }

    private class JavaSourceFromString extends SimpleJavaFileObject
    {
        private String code;

        public JavaSourceFromString(String name, String code)
        {
            super(URI.create("string:///" + name.replace('.', '/')
                    + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors)
        {
            return code;
        }
    }

    private String compilePrint(Diagnostic<?> diagnostic)
    {
        StringBuilder res = new StringBuilder();
        res.append("Code:[").append(diagnostic.getCode()).append("]\n");
        res.append("Kind:[").append(diagnostic.getKind()).append("]\n");
        res.append("Position:[").append(diagnostic.getPosition()).append("]\n");
        res.append("Start Position:[").append(diagnostic.getStartPosition()).append("]\n");
        res.append("End Position:[").append(diagnostic.getEndPosition()).append("]\n");
        res.append("Source:[").append(diagnostic.getSource()).append("]\n");
        res.append("Message:[").append(diagnostic.getMessage(null)).append("]\n");
        res.append("LineNumber:[").append(diagnostic.getLineNumber()).append("]\n");
        res.append("ColumnNumber:[").append(diagnostic.getColumnNumber()).append("]\n");
        return res.toString();
    }

    private class ScriptClassLoader extends ClassLoader
    {
        /**
         * 脚本字节码加载器 注意： 1 为了实现重新加载功能, 此处需打破JVM类加载器双亲委托模型,
         * 否则始终由AppClassLoader加载将无法支持重新加载功能 2 暂不支持脚本内部类
         *
         * @param className 类全名
         * @return
         * @throws ClassNotFoundException
         */
        public Class<?> loadScriptClass(String className) throws ClassNotFoundException
        {
            try
            {
                String classFileName = classFilePath + "/" + className.replace('.', '/') + ".class";
                byte[] bytes = loadClassData(classFileName);
                Class<?> clazz = this.defineClass(className, bytes, 0, bytes.length);

//                // 加载static/非static内部类
//                Class<?>[] dc = clazz.getDeclaredClasses();
//                if (dc.length > 0)
//                {
//                    for (Class<?> innClazz : dc)
//                    {
//                        String innClassName = innClazz.getName();
//                        String x = innClassName.replace('$', '.');
//                        String innClassFileName = classFilePath + "/" + innClassName.replace('.', '/') + ".class";
//                        log.info("load inner className [" + innClassName + "]"
//                                + ", classFilePath [" + innClassFileName + "]");
//                        byte[] innClassBytes = loadClassData(innClassFileName);
//                        try
//                        {
//                            this.defineClass(innClassName, innClassBytes, 0, innClassBytes.length);
//                        }
//                        catch (Throwable t)
//                        {
//                            log.error("load innerClass Exception", t);
//                        }
//                    }
//                }
//
//                // 加载匿名内部类(只加载50个, hack ... )
//                for (int i = 0; i < 50; ++i)
//                {
//                    String anInnerClassName = className + "$" + i;
//                    String anInnClassFilePath = classFilePath + "/" + anInnerClassName.replace('.', '/') + ".class";
//                    File file = new File(anInnClassFilePath);
//                    if (file.exists())
//                    {
//                        log.info("load anonymous inner className [" + anInnerClassName + "]"
//                                + ", classFilePath [" + anInnClassFilePath + "]");
//                        byte[] anInnClassBytes = loadClassData(anInnClassFilePath);
//                        try
//                        {
//                            this.defineClass(null, anInnClassBytes, 0, anInnClassBytes.length);
//                        }
//                        catch (Throwable t)
//                        {
//                            log.error("load anInnerClass Exception", t);
//                        }
//                    }
//                }
                return clazz;
            }
            catch (IOException e)
            {
                log.info(e, e);
                throw new ClassNotFoundException(className);
            }
        }

        /**
         * 读取字节码
         *
         * @param name
         * @return
         * @throws FileNotFoundException
         * @throws IOException
         */
        private byte[] loadClassData(String classFileName) throws FileNotFoundException, IOException
        {
            int readCount = 0;
            //String classFileName = classFilePath + "/" + name.replace('.', '/') + ".class";
            FileInputStream in = null;
            ByteArrayOutputStream buffer = null;
            try
            {
                in = new FileInputStream(classFileName);
                buffer = new ByteArrayOutputStream();
                while ((readCount = in.read()) != -1)
                {
                    buffer.write(readCount);
                }
                return buffer.toByteArray();
            }
            finally
            {
                try
                {
                    if (in != null)
                        in.close();
                    if (buffer != null)
                        buffer.close();
                }
                catch (IOException e)
                {
                    log.info(e, e);
                }
            }
        }
    }

    private enum Singleton
    {
        INSTANCE;
        ScriptJavaLoader loader;

        Singleton()
        {
            this.loader = new ScriptJavaLoader();
        }

        ScriptJavaLoader getProcessor()
        {
            return loader;
        }
    }
}
