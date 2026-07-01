package com.game.equip.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 宝石镶嵌日志
 */
public class GemInlayLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("GemInlayLog");

    private int part;               //部位id,0~7
    private int type;               //类型：1宝石 2仙玉
    private int gemIndex;           //宝石位置，从0开始
    private int operateType;        //操作类型：0开启孔位，1宝石镶嵌，2宝石替换，3宝石升级, 4宝石卸下
    private int lastId;             //操作前宝石或仙玉的id，-1表示孔位未开启
    private int nowId;              //宝石或仙玉的id，0表示孔位开启
    private long actionId;          //关联ID

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Log(logField = "part", fieldType = "tinyint", index = "0")
    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    @Log(logField = "type", fieldType = "tinyint", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "gemIndex", fieldType = "tinyint", index = "0")
    public int getGemIndex() {
        return gemIndex;
    }

    public void setGemIndex(int gemIndex) {
        this.gemIndex = gemIndex;
    }

    @Log(logField = "operateType", fieldType = "tinyint", index = "0")
    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    @Log(logField = "lastId", fieldType = "int", index = "0")
    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    @Log(logField = "nowId", fieldType = "int", index = "0")
    public int getNowId() {
        return nowId;
    }

    public void setNowId(int nowId) {
        this.nowId = nowId;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
