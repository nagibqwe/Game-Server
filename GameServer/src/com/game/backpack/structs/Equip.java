/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backpack.structs;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Changejob_Bean;
import com.data.bean.Cfg_Equip_Bean;
import com.game.backpack.script.IItemUse;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.TimeUtils;

/**
 * @author Administrator
 */
public class Equip extends Item {

    /**
     *套装ID
     */
    private int suitId;

    @Override
    public boolean use(Player player, int useNum, int index, long actionId) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EquipItemBaseScript);
        if (is instanceof IItemUse) {
            return ((IItemUse) is).useItem(player, this, useNum, actionId, index > 0);
        }
        return false;
    }

    @Override
    public boolean unuse(Player player, int unUseNum, long actionId) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.EquipItemBaseScript);
        if (is instanceof IItemUse) {
            return ((IItemUse) is).unUseItem(player, this, unUseNum, actionId);
        }
        return false;
    }

    public int getSuitId() {
        return suitId;
    }

    public void setSuitId(int suitId) {
        this.suitId = suitId;
    }


    @Override
    public Equip clone(){
        try {
            Equip eq = (Equip) super.clone();
            return eq;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    
    /**
     * 判断是否有足够的属性点穿戴
     *
     * @return
     */
    public boolean canEquip(Player player) {
        Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(this.getItemModelId());
        if (model == null) {
            return false;
        }
        return canEquip(player, model);
    }

    public boolean canEquip(Player player, Cfg_Equip_Bean model) {
        /**穿戴的要求
        * 1，等级
        * 2，职业
        * 3,失效时间
        * 4，转职要求
         */

        if (!model.getGender().contains(9)&&!model.getGender().contains((int)player.getCareer())) {
            return false;
        }
        if (player.getLevel() < model.getLevel()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EquipWearLevelNot);
            return false;
        }
//        if (player.getStateVip().getLv() < model.getStatelevel()) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EquipWearStateVipNot);
//            return false;
//        }

        if(player.getXsGrade() < model.getClasslevel()){
            Cfg_Changejob_Bean jobean = CfgManager.getCfg_Changejob_Container().getValueByKey(model.getClasslevel());
            if (jobean != null) {
                String job = ServerStr.getChatTableName(jobean.getChangejob_name());
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_EQUIP_WARFIALECLASEE,jobean.getChangejob_condition(), job);
            }
            return false;
        }

//        Cfg_Changejob_Bean jobean = CfgManager.getCfg_Changejob_Container().getValueByKey(model.getClasslevel());
//        if (jobean != null) {
//            if(jobean.getGenderClass()>player.getGradeClass()){
//                return false;
//            }
//        }
        return !(this.getLosttime() > 0 && TimeUtils.Time() > this.getLosttime());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Equip{");
        builder.append("excelId=");
        builder.append(getItemModelId());
        builder.append(",suitId=");
        builder.append(suitId);
        builder.append('}');
        return builder.toString();
    }

    @Override
    public void release() {

    }
}
