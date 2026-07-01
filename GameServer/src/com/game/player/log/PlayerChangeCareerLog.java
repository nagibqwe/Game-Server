/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class PlayerChangeCareerLog extends CommonLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    public void setPlayer(Player p) {
        setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    private int oldCareer;
    private int newCareer;
    private int itemId;
    private long actionId;

    @Log(fieldType = "int", index = "0", logField = "oldCareer")
    public int getOldCareer() {
        return oldCareer;
    }

    public void setOldCareer(int oldCareer) {
        this.oldCareer = oldCareer;
    }

    @Log(fieldType = "int", index = "0", logField = "newCareer")
    public int getNewCareer() {
        return newCareer;
    }

    public void setNewCareer(int newCareer) {
        this.newCareer = newCareer;
    }

    @Log(fieldType = "int", index = "0", logField = "itemId")
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "actionId")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public void logToFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
