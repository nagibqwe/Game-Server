/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.structs;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.data.ItemChangeReason;

/**
 * 特定经验值的道具
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class ExpValueItem extends Item {

    private long expNum;

    @Override
    public long realNum() {
        return expNum; 
    }

    public long getExpNum() {
        return expNum;
    }

    public void setExpNum(long expNum) {
        this.expNum = expNum;
    }

    @Override
    public boolean use(Player player, int userNum, int index, long actionId) {
        if (!Manager.backpackManager.manager().canUse(player, this, userNum)) {
            return false;
        }

        if (Manager.backpackManager.manager().onRemoveItem(player, this, userNum, ItemChangeReason.OwnUseDec, actionId)) {
            Manager.backpackManager.manager().doEffects(player, this, userNum, actionId);
        }
        return true;
    }

    @Override
    public boolean unuse(Player player, int unUseNum, long actionId) {
        return true;
    }

    @Override
    public void release() {

    }
}
