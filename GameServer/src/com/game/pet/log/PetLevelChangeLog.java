package com.game.pet.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 宠物升级日志
 */
public class PetLevelChangeLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("PetLevelChangeLog");

    private int level;            //当前等级
    private long exp;             //当前经验
    private long addExp;          //本次操作增加的经验
    private String eatEquips;     //本次吞噬的装备配置表id

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

    @Log(logField = "level", fieldType = "int", index = "0")
    public int getLevel() {
        return level;
    }

    @Log(logField = "exp", fieldType = "bigint", index = "0")
    public long getExp() {
        return exp;
    }

    @Log(logField = "addExp", fieldType = "bigint", index = "0")
    public long getAddExp() {
        return addExp;
    }

    @Log(logField = "eatEquips", fieldType = "text", index = "0")
    public String getEatEquips() {
        return eatEquips;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    public void setEatEquips(String eatEquips) {
        this.eatEquips = eatEquips;
    }
}
