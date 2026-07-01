package com.game.task.manager;

import com.data.bean.Cfg_Task_gender_Bean;
import com.data.struct.ReadLongArrayEs;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.IConquerTask;
import com.game.task.script.IDailyTask;
import com.game.task.script.IGenderTask;
import com.game.task.script.ITaskDeal;
import com.game.task.structs.TaskCondition;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务管理
 */
public class TaskManager extends CommandProcessor {

    private static final Logger log = LogManager.getLogger(TaskManager.class);
    /**
     * 1(任务类型：日常任务), 60_110(60级-110级), [10001,100002](可以接的任务的Id)
     */
    private final ConcurrentHashMap<Integer, Map<TaskCondition, List<Integer>>> canReceiveTasks = new ConcurrentHashMap<>();
    /**
     * 奖励计算值
     */
    private final ConcurrentHashMap<String, ReadLongArrayEs> rewardStr = new ConcurrentHashMap<>();
    /**
     * key 为 职业
     */
    private final ConcurrentHashMap<Integer, List<Cfg_Task_gender_Bean>> genderTaskCache = new ConcurrentHashMap<>();

    public TaskManager() {
        super("任务线程");
    }

    public ConcurrentHashMap<Integer, Map<TaskCondition, List<Integer>>> getCanReceiveTasks() {
        return canReceiveTasks;
    }

    public ConcurrentHashMap<String, ReadLongArrayEs> getRewardStr() {
        return rewardStr;
    }

    public ConcurrentHashMap<Integer, List<Cfg_Task_gender_Bean>> getGenderTaskCache() {
        return genderTaskCache;
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     */
    @Override
    public void writeError(String message) {
        log.error(message);
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     * @param t       产生错误的异常类
     */
    @Override
    public void writeError(String message, Throwable t) {
        log.error(message, t);
    }

    private enum Singleton {

        INSTANCE;
        TaskManager manager;

        Singleton() {
            this.manager = new TaskManager();
        }

        TaskManager getProcessor() {
            return manager;
        }
    }

    public static TaskManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ITaskDeal deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TaskManagerBaseScript);
        if (is instanceof ITaskDeal) {
            return (ITaskDeal) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IConquerTask guild() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        if (is instanceof IConquerTask) {
            return (IConquerTask) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
