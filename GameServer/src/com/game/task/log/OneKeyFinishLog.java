package com.game.task.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zcd on 2018/2/28.
 */
public class OneKeyFinishLog  extends TaskLogBean {

    private static final Logger logger = LogManager.getLogger(OneKeyFinishLog.class);

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    /**
     * 任务类型
     */
    private int taskType;

    /**
     * 任务详细信息
     */
    private String taskDetailInfo;

    /**
     * 奖励信息
     */
    private String rewardInfo;

    /**
     * 一键完成花费的money
     */
    private String costCoin;

    @Log(logField = "taskType", fieldType = "int", index = "0")
    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    @Log(logField = "taskDetailInfo", fieldType = "varchar(500)", index = "0")
    public String getTaskDetailInfo() {
        return taskDetailInfo;
    }

    public void setTaskDetailInfo(String taskDetailInfo) {
        this.taskDetailInfo = taskDetailInfo;
    }

    @Log(logField = "rewardInfo", fieldType = "varchar(500)", index = "0")
    public String getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(String rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    @Log(logField = "costCoin", fieldType = "varchar(100)", index = "0")
    public String getCostCoin() {
        return costCoin;
    }

    public void setCostCoin(String costCoin) {
        this.costCoin = costCoin;
    }
}
