package com.game.peak.manager;

import com.game.db.bean.PeakBean;
import com.game.manager.Manager;
import com.game.peak.script.IPeakPk;
import com.game.peak.timer.PeakTimer;
import com.game.script.structs.ScriptEnum;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2020/10/14 14:14
 * @Auth ZUncle
 */
public class PeakManager extends CommandProcessor {

    final Logger logger = LogManager.getLogger(PeakManager.class);

    //匹配心跳
    final PeakTimer timer = new PeakTimer();
    //巅峰竞技排名数据
    final List<PeakBean> ranks = new ArrayList<>();
    //巅峰数据
    final ConcurrentHashMap<Long, PeakBean> peaks = new ConcurrentHashMap<>();
    //匹配队列
    private ConcurrentHashMap<Long, PeakBean> match ;
    //巅峰准备地图
    private long waitId;


    public PeakManager() {
        super(PeakManager.class.getSimpleName());
    }

    public ConcurrentHashMap<Long, PeakBean> getMatch() {
        return match;
    }

    public void setMatch(ConcurrentHashMap<Long, PeakBean> match) {
        this.match = match;
    }

    public List<PeakBean> getRanks() {
        return ranks;
    }

    public ConcurrentHashMap<Long, PeakBean> getPeaks() {
        return peaks;
    }

    public PeakTimer getTimer() {
        return timer;
    }

    public long getWaitId() {
        return waitId;
    }

    public void setWaitId(long waitId) {
        this.waitId = waitId;
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     */
    @Override
    public void writeError(String message) {
        logger.error("PeakManager error" + message, message);
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     * @param t       产生错误的异常类
     */
    @Override
    public void writeError(String message, Throwable t) {
        logger.error(message, t);
    }


    public IPeakPk deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PeakPkScript);
        if (is == null) {
            logger.error("未找到脚本PeakPkScript={}", ScriptEnum.PeakPkScript);
            return null;
        }
        return (IPeakPk) is;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        PeakManager manager;

        Singleton() {
            this.manager = new PeakManager();
        }

        PeakManager getProcessor() {
            return manager;
        }
    }

    public static PeakManager getInstance() {
        return PeakManager.Singleton.INSTANCE.getProcessor();
    }

}
