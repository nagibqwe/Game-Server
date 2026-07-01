package com.game.marriage.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @Desc TODO
 * @Date 2020/8/14 17:59
 * @Auth ZUncle
 */
public class MarryLoveLockLog extends BaseLogBean {

    long playerId;      //玩家ID
    int stage;          //阶
    int grade;          //级
    int exp;            //经验
    int opt;            //1=解锁, 2=强化

    @Log(fieldType = "bigint", logField = "playerId", index = "1")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(fieldType = "int", logField = "stage", index = "2")
    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Log(fieldType = "int", logField = "grade", index = "3")
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Log(fieldType = "int", logField = "exp", index = "0")
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    @Log(fieldType = "int", logField = "opt", index = "0")
    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    /**
     * 日志多长时间建一次表
     *
     * @return
     */
    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
