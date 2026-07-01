package com.game.luckydraw.manager;


import com.game.luckydraw.script.ILuckyDrawWeekScript;
import com.game.luckydraw.structs.LuckyDrawWeekData;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author gaozhaoguang
 * @desc 幸运抽奖的管理器
 * @date Created on 2020/8/20 11:53
 **/
public class LuckyDrawManager {

    /**
     * 日志对象
     */
    public final Logger log = LogManager.getLogger(LuckyDrawManager.class);

    /**
     * 一周福利抽奖数据
     */
    private final LuckyDrawWeekData weekData = new LuckyDrawWeekData();

    public LuckyDrawWeekData getWeekData() {
        return weekData;
    }


    /**
     * 用枚举来实现单例
     */
    public enum Singleton {

        INSTANCE;
        LuckyDrawManager manager;

        Singleton() {
            this.manager = new LuckyDrawManager();
        }

        LuckyDrawManager getProcessor() {
            return manager;
        }
    }

    //LuckyDrawManager
    public static LuckyDrawManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 获取处理脚本
     * @return
     */
    public ILuckyDrawWeekScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.LuckyDrawWeekScript);
        if (is instanceof ILuckyDrawWeekScript) {
            return (ILuckyDrawWeekScript) is;
        } else {
            log.error("没有找到具体的实例！");
            return null;
        }
    }

}
