package com.game.skill.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * 技能日志
 */
public class SkillLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("SkillLog");

    private int skillId;            //技能id
    private int level;              //等级
    private int action;             //-1移除、0学习、1升级
    private String consume;         //消耗
    private long actionId;          //关联id

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(fieldType = "int", logField = "skillId", index = "0")
    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    @Log(fieldType = "int", logField = "level", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(fieldType = "int", logField = "action", index = "0")
    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Log(fieldType = "TEXT", logField = "consume", index = "0")
    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

}
