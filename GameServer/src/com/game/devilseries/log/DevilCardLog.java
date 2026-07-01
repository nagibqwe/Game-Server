package com.game.devilseries.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 魔魂日志
 * @Auther: gouzhongliang
 * @Date: 2021/5/17 14:20
 */
public class DevilCardLog extends CommonLogBean {

    private static final Logger log = LogManager.getLogger(DevilCardLog.class);

    private int campId;

    private int cardId;

    private int type;

    private int afterLevel;

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(fieldType = "int", index = "0", logField = "campId")
    public int getCampId() {
        return campId;
    }

    public void setCampId(int campId) {
        this.campId = campId;
    }

    @Log(fieldType = "int", index = "0", logField = "cardId")
    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @Log(fieldType = "int", index = "0", logField = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(fieldType = "int", index = "0", logField = "afterLevel")
    public int getAfterLevel() {
        return afterLevel;
    }

    public void setAfterLevel(int afterLevel) {
        this.afterLevel = afterLevel;
    }

}
