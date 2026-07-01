package com.game.bi.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;


public class UiBiLog extends CommonLogBean {

    private long uiID;

    private long time;

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.WEEK;
    }

    @Log(logField = "uiID", fieldType = "bigint", index = "0")
    public long getUiID() {
        return uiID;
    }

    public void setUiID(long uiID) {
        this.uiID = uiID;
    }

    @Log(logField = "time", fieldType = "bigint", index = "0")
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
