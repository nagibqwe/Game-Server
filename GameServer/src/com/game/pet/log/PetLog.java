package com.game.pet.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 宠物激活强化日志
 */
public class PetLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("PetLog");

    private int actionType;       //操作动作，1：激活，2：强化
    private int petId;            //操作的宠物模型Id
    private int stage;            //宠物的阶数

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

    @Log(logField = "actionType", fieldType = "int", index = "0")
    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    @Log(logField = "petId", fieldType = "int", index = "0")
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    @Log(logField = "stage", fieldType = "int", index = "0")
    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
