package game.core.script;

import game.core.util.ClassUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptException;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ScriptAbstractManager {
    private static final Logger logger = LogManager.getLogger(ScriptAbstractManager.class);
    protected final ConcurrentHashMap<Integer, ScriptBean> scripts = new ConcurrentHashMap<>();
    private String classpath;
    private String javaFilePath;
    private String classFilePath;
    private String packageName;
    private boolean hasInitialized = false;

    /**
     * 初始化脚本管理器
     *
     * @param javaFilePath java脚本所在目录
     * @param classFilePath java脚本编译后class所在目录
     * @param packageName 初始化package下的脚本
     * @throws FileNotFoundException
     * @throws ScriptException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public final synchronized void initialize(String javaFilePath, String classFilePath, String packageName) throws FileNotFoundException, ScriptException, IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        initialize(javaFilePath, classFilePath, packageName, false);
    }

    /**
     * 初始化脚本管理器
     *
     * @param javaFilePath java脚本所在目录
     * @param classFilePath java脚本编译后class所在目录
     * @param packageName 初始化package下的脚本
     * @param isDebug 是否是debug版本，debug版本会启动timer，每秒自动编译最新脚本
     * @throws FileNotFoundException
     * @throws ScriptException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    public final synchronized void initialize(String javaFilePath, String classFilePath, String packageName, boolean isDebug) throws FileNotFoundException, ScriptException, IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        if (hasInitialized) {
            return;
        }
        this.javaFilePath = javaFilePath;
        this.classFilePath = classFilePath;
        this.packageName = packageName;
        buildClassPath();
        initScript(isDebug);
        hasInitialized = true;
        if (isDebug) {
            timerCheckReload();
        }
    }

    /**
     * 启动一个timer，每秒自动编译最新脚本，开发版本时才调用
     */
    private void timerCheckReload() {
        new Timer("ScriptCheckReload-Timer").schedule(new TimerTask() {
            private final HashMap<String, ScriptBean> checkScripts = new HashMap<>();

            @Override
            public void run() {
                try {
                    check();
                } catch (Exception e) {

                }
            }

            private void check() {
                    if (!hasInitialized) {
                        return;
                    }
                    if (checkScripts.isEmpty()) {
                        Iterator<Map.Entry<Integer, ScriptBean>> itt = scripts.entrySet().iterator();
                        while (itt.hasNext()) {
                            Map.Entry<Integer, ScriptBean> entry = itt.next();
                            checkScripts.put(entry.getValue().getName(), entry.getValue());
                        }
                        return;
                    }
                    File dir = new File(javaFilePath + "/" + packageName);
                    checkScript(dir, packageName);
            }

            /**
             * 检查脚本
             */
            private void checkScript(File dir, String pakckageName) {
                for (File f : dir.listFiles()) {
                    if (f.isDirectory()) {
                        if (pakckageName.isEmpty()) {
                            checkScript(f, f.getName());
                        } else {
                            checkScript(f, pakckageName + "." + f.getName());
                        }
                        continue;
                    }
                    if (f.isFile() && f.canRead()) {
                        String className;
                        if (pakckageName.isEmpty()) {
                            className = f.getName();
                        } else {
                            className = pakckageName + "." + f.getName();
                        }
                        className = className.replace(".java", "");
                        ScriptBean scriptBean = checkScripts.get(className);
                        if (scriptBean == null) {
                            scriptBean = new ScriptBean();
                            scriptBean.setName(className);
                            if (makeScript(scriptBean, f, false)) {
                                scripts.put(scriptBean.getId(), scriptBean);
                                checkScripts.put(className, scriptBean);
                                logger.error("热更新加载 脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName());
                            }
                            continue;
                        }
                        if (scriptBean.getJavaFileTimestamp() == 0) {
                            scriptBean.setJavaFileTimestamp(f.lastModified());
                            continue;
                        }
                        if (f.lastModified() == scriptBean.getJavaFileTimestamp()) {
                            continue;
                        }
                        if (makeScript(scriptBean, f, false)) {
                            logger.info("热更新reload 脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName());
                        }
                    }
                }
            }

        }, 1000, 1000);
    }

    /**
     * 获取脚
     *
     * @param scriptId
     * @return
     * @throws Exception
     */
    public IScript GetScriptClass(int scriptId){
        ScriptBean scriptBean = scripts.get(scriptId);
        if (scriptBean == null) {
            logger.error("cannot find scriptBean, scriptId = " + scriptId);
            return null;
        }

        IScript script = scriptBean.getScript();
        if (script == null) {
            logger.error("脚本ID：" + scriptId + ", 没有找到具体的实现类，请注意！");
        }
        return script;
    }

    /**
     * 调用脚本，默认使用脚本call函数
     *
     * @param scriptId 脚本id
     * @param args 参数列表
     * @return
     * @throws Exception 脚本异常
     */
    public final Object call(int scriptId, Object... args){
        ScriptBean scriptBean = scripts.get(scriptId);
        if (scriptBean == null) {
            logger.error("cannot find scriptBean, scriptId = " + scriptId);
        }
        IScript script = scriptBean.getScript();
        if (script == null) {
            logger.error("脚本ID：" + scriptId + ", 没有找到具体的实现类，请注意！");
        }
        return script.call(args);
    }

    /**
     * 调用脚本，使用公共的method函数
     *
     * @param scriptId 脚本id
     * @param method public方法名
     * @param args 参数列表
     * @return
     * @throws Exception 脚本异常
     */
    public final Object callMethod(int scriptId, String method, Object args) throws Exception {
        ScriptBean scriptBean = scripts.get(scriptId);
        if (scriptBean == null) {
            throw new Exception("cannot find scriptBean, scriptId = " + scriptId);
        }
        IScript script = scriptBean.getScript();
        if (script == null) {
            throw new Exception("cannot find script, scriptId = " + scriptId);
        }
        Method fun = script.getClass().getDeclaredMethod(method, Object.class);
        return fun.invoke(script, args);
    }

    /**
     * 重新加载已存在的脚本
     *
     * @param scriptId
     * @return
     * @throws FileNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public final synchronized boolean reload(int scriptId) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ScriptBean scriptBean = scripts.get(scriptId);
        if (scriptBean == null) {
            logger.info("脚本id：" + scriptId + " 脚本刷新失败，脚本不存在");
            return false;
        }
        if (makeScript(scriptBean)) {
            logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + " 脚本刷新成功");
            return true;
        }
        logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + " 脚本刷新失败，生成scriptBean失败");
        return false;
    }

    /**
     * 加载新脚本
     *
     * @param scriptId 脚本id
     * @param className 脚本名
     * @return
     * @throws FileNotFoundException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public final synchronized boolean loadNew(int scriptId, String className) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ScriptBean scriptBean = scripts.get(scriptId);
        if (scriptBean != null) {
            logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + " 加载新脚本失败，脚本已存在");
            return false;
        }
        scriptBean = new ScriptBean();
        scriptBean.setName(className);
        if (makeScript(scriptBean)) {
            logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + " 脚本新增成功");
            scripts.put(scriptId, scriptBean);
            return true;
        }
        logger.info("脚本id：" + scriptId + " 脚本名：" + className + " 加载新脚本失败，生成scriptBean失败");
        return false;
    }

    /**
     * 生成脚本数据（scriptBean已设置脚本名）
     *
     * @param scriptBean
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    private boolean makeScript(ScriptBean scriptBean) {
        try {
            String fullJavaFilePath = javaFilePath + "/" + scriptBean.getName().replace('.', '/') + ".java";
            File f = new File(fullJavaFilePath);
            return makeScript(scriptBean, f, true);
        } catch (Exception e) {
            logger.error(e, e);
        }
        return false;
    }

    /**
     * 生成脚本数据（scriptBean已设置脚本名）
     *
     * @param scriptBean
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    private boolean makeScript(ScriptBean scriptBean, File f, boolean isNeedLog) {
        try {
            logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + ";fullJavaFilePath:" + f.getAbsolutePath() + " begin makeScript");
            if (f.isFile() && f.canRead()) {
                Class<?> clazz;
                try (InputStream inStream = new FileInputStream(f)) {
                    byte[] bytes = new byte[(int) f.length()];
                    inStream.read(bytes);
                    clazz = ClassUtil.javaCodeToObject(scriptBean.getName(), new String(bytes, "UTF-8"), classpath, classFilePath);
                }
                if (clazz == null) {
                    return false;
                }

                if (!ClassUtil.isInterface(clazz, IScript.class)) {
                    return false;
                }

                @SuppressWarnings("unchecked")
                IScript script = (IScript) clazz.newInstance();
                scriptBean.setId(script.getId())
                        .setScript(script)
                        .setJavaFileTimestamp(f.lastModified())
                        .setClassFileTimestamp(System.currentTimeMillis());
                return true;

            }
        } catch (Exception e) {
            if (isNeedLog) {
                logger.error("makes script failed;id:" + scriptBean.getId() + ";name:" + scriptBean.getName(), e);
            }
        }
        return false;
    }

    /**
     * 初始化packageName下的脚本到map中
     *
     * @param isDebug
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void initScript(boolean isDebug) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<Class<IScript>> scriptList = ClassUtil.getSubClasses(packageName, IScript.class);
        for (Class<IScript> cls : scriptList) {
            IScript script = cls.newInstance();
            ScriptBean scriptBean = new ScriptBean();
            scriptBean.setName(script.getClass().getName());
            scriptBean.setId(script.getId())
                    .setScript(script)
                    .setJavaFileTimestamp(0)
                    .setClassFileTimestamp(System.currentTimeMillis());
            if (!isDebug) {
                //正式版需使用最新的代码编译成的脚本
                String fullJavaFilePath = javaFilePath + "/" + scriptBean.getName().replace('.', '/') + ".java";
                File f = new File(fullJavaFilePath);
                if (f.isFile() && f.canRead()) {
                    if(makeScript(scriptBean, f, true)){
                        logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + " init reload success");
                    }else{
                        logger.error("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName() + " init reload failed");
                        System.exit(-1);
                    }
                }
            }
            if (scripts.containsKey(scriptBean.getId())) {
                logger.error("脚本id：" + scriptBean.getId() + "重复；脚本名1：" + scriptBean.getName() + ";脚本id2：" + scripts.get(scriptBean.getId()).getName());
                System.exit(-1);
            }

            scripts.put(scriptBean.getId(), scriptBean);
            logger.info("脚本id：" + scriptBean.getId() + " 脚本名：" + scriptBean.getName());
        }
    }

    private void buildClassPath() {
        File file = new File(classFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) this.getClass().getClassLoader()).getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        sb.append(classFilePath);
        this.classpath = sb.toString();
        logger.info("Build scriptLoader classpath: [" + this.classpath + "]");
    }
}
