package com.game.equip.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 装备出售日志
 */
public class EquipSellLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("EquipSellLog");

    private String ids;             //装备出售的Id
    private String equipInfo;       //出售装备详情
    private long actionId;          //关联ID

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

    @Log(fieldType = "text", logField = "ids", index = "0")
    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Log(fieldType = "text", logField = "equipInfo", index = "0")
    public String getEquipInfo() {
        return equipInfo;
    }

    public void setEquipInfo(String equipInfo) {
        this.equipInfo = equipInfo;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

}
