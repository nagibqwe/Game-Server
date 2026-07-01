package com.game.crazyweek.manager;

import com.game.crazyweek.script.ICrazyWeekScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 狂欢周管理器 
 */
public class CrazyWeekManager {

    private static final Logger LOGGER = LogManager.getLogger(CrazyWeekManager.class);
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        /**
         * 一个枚举元素就是一个实例
         */
        INSTANCE;

        CrazyWeekManager processor;

        Singleton() {
            this.processor = new CrazyWeekManager();
        }

        CrazyWeekManager getProcessor() {
            return processor;
        }
    }

    public static CrazyWeekManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    /**
     * 获取处理脚本
     * @return
     */
    public ICrazyWeekScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrazyWeekScript);
        if (is instanceof ICrazyWeekScript) {
            return (ICrazyWeekScript) is;
        } else {
            LOGGER.error("没有找到具体的实例！");
            return null;
        }
    }

    //周功能管理
    private ConcurrentHashMap<Integer, int[]> weekDayFuncMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer,int[]> getWeekDayFuncMap() {
        return weekDayFuncMap;
    }

    //已经开启的功能列表
    private Set<Integer> openedFuncs  =  new HashSet<>();
    public Set<Integer> getOpenedFuncs() {
        return openedFuncs;
    }
}
