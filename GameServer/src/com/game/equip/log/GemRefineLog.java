package com.game.equip.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 宝石精炼日志
 */
public class GemRefineLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("GemRefineLog");

    private int part;               //部位id,0~7
    private int operateType;        //操作类型：0快速精炼 1智能精炼
    private int lastLevel;          //精炼前等级
    private int nowLevel;           //精炼后等级
    private long lastExp;           //精炼前经验
    private long nowExp;            //精炼后经验
    private long addExp;            //增加的经验
    private long actionId;          //操作ID

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

    @Log(logField = "part", fieldType = "int", index = "0")
    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    @Log(logField = "operateType", fieldType = "tinyint", index = "0")
    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    @Log(logField = "lastLevel", fieldType = "int", index = "0")
    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    @Log(logField = "nowLevel", fieldType = "int", index = "0")
    public int getNowLevel() {
        return nowLevel;
    }

    public void setNowLevel(int nowLevel) {
        this.nowLevel = nowLevel;
    }

    @Log(logField = "lastExp", fieldType = "int", index = "0")
    public long getLastExp() {
        return lastExp;
    }

    public void setLastExp(long lastExp) {
        this.lastExp = lastExp;
    }

    @Log(logField = "nowExp", fieldType = "int", index = "0")
    public long getNowExp() {
        return nowExp;
    }

    public void setNowExp(long nowExp) {
        this.nowExp = nowExp;
    }

    @Log(logField = "addExp", fieldType = "int", index = "0")
    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
