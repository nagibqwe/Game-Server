package com.game.manager;

import com.game.yed.Yed;
import com.game.yed.YedAI;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ConcurrentHashMap;

public class YedMgr {

    public static final String AIFILEPATH_PREFIX = "config" + File.separator + "YedAi" + File.separator;
    public static final String AIFILE_EXTENSION = ".txt";

    private final static Logger log = LogManager.getLogger("AILOG");

    // 每个ai的update里面检查这个字段是否一样,不一样则自己重新加载一次
    volatile public int reloadIndex = 0;

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
     * 清空缓存,等要再用到的时候重新加载 这个函数多线程不安全
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

    boolean YedLoader(String _name, Yed.OnLoaded _onloader) {
        return Load(_name, _onloader);
    }

    private boolean Load(String _name, Yed.OnLoaded _onloader) {
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
                log.info(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    private byte[] LoadFromFile(String _name) {

        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir"));
        pathBuilder.append(File.separator);
        pathBuilder.append(AIFILEPATH_PREFIX);
        pathBuilder.append(File.separator);
        pathBuilder.append(_name);
        pathBuilder.append(AIFILE_EXTENSION);
        String filepath = pathBuilder.toString();
//				String filepath = "G:/code/7shanmen/Server/GameServer/YedAi/config/HelpRobot.txt";
        System.out.println("cur dir:" + pathBuilder.toString());
        File f = new File(filepath);
        long flen = f.length();
        if (flen > Integer.MAX_VALUE) {
            System.out.println("加载ai：" + _name + "失败了，flen太大:" + flen);
            return null;
        }
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(f);
            byte[] retbytes = new byte[(int) flen];
            int ret = fstream.read(retbytes);
            if (ret != flen) {
                System.out.println("加载ai：" + _name + "失败了，ret和flen不符:" + flen + " ret:" + ret);
                return null;
            }
            fstream.close();
            return retbytes;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return null;
    }

    public YedAI GetAi(String ainame, Object host) throws CloneNotSupportedException {
        YedAI ret = new YedAI(host);
        ret.Load(ainame, false);
        return ret;
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

    public synchronized void Sa_SetLogFlag(YedAI ai, String loglevel) {
        log.trace(String.format("ai debug SetLogFlag val:%s", loglevel));
        Configurator.setLevel(log.getName(), Level.toLevel(loglevel));
    }
}
