package com.game.yed;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.yed.scripts.YedMethodScript;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ConcurrentHashMap;

final public class YedMgr{

    public static final String AIFILEPATH_PREFIX = "YedAi" + File.separator + "config";
    public static final String AIFILE_EXTENSION = ".txt";

    private final static Logger log = LogManager.getLogger("AILOG");

    // 每个ai的update里面检查这个字段是否一样,不一样则自己重新加载一次
    volatile public int reloadIndex = 0;
    // 每个ai的update里面检查这个字段是否一样,不一样则清空缓存的函数方法
    volatile public int cleanFuncsIndex = 0;

    private final ConcurrentHashMap<String, byte[]> loadedfiles = new ConcurrentHashMap<>();

    private YedMgr() {
        Init();
    }

    public static YedMgr getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public Logger getAiLogger() {
        return log;
    }

    /**
     * 清空缓存,等要再用到的时候重新加载
     * 这个函数多线程不安全
     */
    public void ReLoad() {
        loadedfiles.clear();
        reloadIndex++;

        System.out.println("重新加载完成,使用原来ai的对象重新创建后自动应用新的AI");
    }

    private void Init() {
        Yed.loader = new Yed.YedLoader() {
            @Override
            public boolean Invoke(String name, Yed.OnLoaded onloaded) {
                return YedLoader(name, onloaded);
            }
        };
    }

    public boolean IsExist(String _name){
        String filepath = getFilePath(_name);
        File f = new File(filepath);
        boolean ret = f.exists();
        return ret;
    }

    boolean YedLoader(String _name, Yed.OnLoaded _onloader) {
        return Load(_name, _onloader);
    }

    private boolean Load(String _name, Yed.OnLoaded _onloader) {
        if(!TimeUtils.isShowAiLog()){
            SetLogFlag("error");
        }
        byte[] retbytes = null;

        synchronized (loadedfiles) {
            if (loadedfiles.containsKey(_name)) {
                retbytes = loadedfiles.get(_name);
            } else {
                // 弄个临时的,减少锁时间
                retbytes = LoadFromFile(_name);
                loadedfiles.put(_name, retbytes);
            }
        }
        if (retbytes == null) {
            return false;
        }
        if (_onloader != null) {
            try {
                _onloader.Invoke(_name, retbytes);
            } catch (Exception e) {
                log.fatal(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    private String getFilePath(String _name){

        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir"));
        pathBuilder.append(File.separator);
        pathBuilder.append(AIFILEPATH_PREFIX);
        pathBuilder.append(File.separator);
        pathBuilder.append(_name);
        pathBuilder.append(AIFILE_EXTENSION);
        final String filepath = pathBuilder.toString();
        return filepath;
    }

    private byte[] LoadFromFile(String _name) {
        String filepath = getFilePath(_name);
//				String filepath = "G:/code/7shanmen/Server/GameServer/YedAi/config/HelpRobot.txt";
        System.out.println("cur dir:" + filepath);
        File f = new File(filepath);
        long flen = f.length();
        if (flen > Integer.MAX_VALUE) {
            String str = "加载ai：" + _name + "失败了，flen太大:" + flen;
            System.out.println(str);
            log.error(str);
            return null;
        }
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(f);
            byte[] retbytes = new byte[(int) flen];
            int ret = fstream.read(retbytes);
            if (ret != flen) {
                String str = "加载ai：" + _name + "失败了，ret和flen不符:" + flen + " ret:" + ret;
                System.out.println(str);
                log.error(str);
                return null;
            }
            fstream.close();
            return retbytes;
        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
        }
        return null;
    }

    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        YedMgr manager;

        Singleton() {
            this.manager = new YedMgr();
        }

        YedMgr getProcessor() {
            return manager;
        }
    }

    public synchronized void SetLogFlag(String loglevel) {
        log.trace(String.format("ai debug SetLogFlag val:%s", loglevel));
        Configurator.setLevel(log.getName(), Level.toLevel(loglevel));
    }

    public YedMethodScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.YedAiCommonScript);
        if (is instanceof YedMethodScript) {
            return (YedMethodScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

}
