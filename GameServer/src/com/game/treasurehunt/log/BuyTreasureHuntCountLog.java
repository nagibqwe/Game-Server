package com.game.treasurehunt.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 购买寻宝次数
 */
public class BuyTreasureHuntCountLog extends CommonLogBean {

    private long actionId;      //唯一id
    private int buyTimes;       //购买次数
    private int type;           // 寻宝类型 1、机缘寻宝....
    private int afterTimes;     //购买后次数
    private int beforeTimes;    //购买前次数
    private int modelId;        //购买所增加的道具模板id

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "aftertimes", fieldType = "int", index = "0")
    public int getAfterTimes() {
        return afterTimes;
    }

    public void setAfterTimes(int afterTimes) {
        this.afterTimes = afterTimes;
    }

    @Log(logField = "beforetimes", fieldType = "int", index = "0")
    public int getBeforeTimes() {
        return beforeTimes;
    }

    public void setBeforeTimes(int beforeTimes) {
        this.beforeTimes = beforeTimes;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    private static final Logger logger = LogManager.getLogger("GWQualityOrLevelUpLog");

    @Log(logField = "buytimes", fieldType = "int", index = "0")
    public int getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(int buyTimes) {
        this.buyTimes = buyTimes;
    }
}
