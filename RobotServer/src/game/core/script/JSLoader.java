/**
 * @date 2014/5/12
 * @author ChenLong
 */
package game.core.script;

import game.core.exception.ReinitializedException;
import game.core.exception.UninitializedException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * JavaScript脚本支持类
 *
 * @author ChenLong
 */
public class JSLoader
{
    private static final Logger log = LogManager.getLogger(JSLoader.class);
    private boolean hasInitialized = false;
    private String jsPath; // js文件所在顶层目录
    private ScriptEngine engine;

    private JSLoader()
    {
    }

    /**
     * 初始化JSLoader
     *
     * @param jsPath js文件所在顶层目录
     * @throws FileNotFoundException
     * @throws ScriptException
     */
    public synchronized void initialize(String jsPath) throws FileNotFoundException, ScriptException
    {
        if (hasInitialized)
            throw new ReinitializedException("reinitialize JSLoader");
        this.jsPath = jsPath;
        initializeEngine();
        hasInitialized = true;
    }

    private void initializeEngine() throws FileNotFoundException, ScriptException
    {
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
        engine.put("engine", engine); // engine全局变量
        engine.put("jsPath", jsPath); // jsPath全局变量
        { // 加载global.js
            Reader reader = new FileReader(jsPath + "/global.js");
            engine.eval(reader);
        }
        {
            // 其他需要在java层加载的js脚本
            // ...
        }
    }

    /**
     * 调用JS脚本
     *
     * @param arg
     * @return
     * @throws UninitializedException 未初始化异常
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
    public Object callMain(Object arg) throws UninitializedException, ScriptException, NoSuchMethodException
    {
        return call("main", arg);
    }

    /**
     * 调用JS脚本函数
     *
     * @param functionName 函数名
     * @param arg 参数
     * @return
     * @throws UninitializedException
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public Object call(String functionName, Object arg) throws UninitializedException, ScriptException, NoSuchMethodException
    {
        hasInitialized();
        Invocable inv = (Invocable) engine;
        return inv.invokeFunction(functionName, arg);
    }

    private void hasInitialized()
    {
        if (!hasInitialized)
            throw new UninitializedException("uninitialized JSLoader");
    }

    public static JSLoader getInstance()
    {
        return Singleton.INSTANCE.getLoader();
    }

    private enum Singleton
    {
        INSTANCE;

        JSLoader loader;

        Singleton()
        {
            this.loader = new JSLoader();
        }

        JSLoader getLoader()
        {
            return loader;
        }
    }
}
