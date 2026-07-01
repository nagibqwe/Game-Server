package com.game.equip.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.EquipMessage;

public interface IGemScript extends IScript {

    /**
     * 初始化宝石信息
     */
    void initGemInfo(Player player);

    /**
     * 检查宝石孔位开启
     */
    void checkOpenState(Player player);

    /**
     * 请求宝石镶嵌
     */
    void onReqInlay(Player player, EquipMessage.ReqInlay mess);

    /**
     * 请求宝石快速精炼
     */
    void onReqQuickRefineGem(Player player, EquipMessage.ReqQuickRefineGem mess);

    /**
     * 请求宝石智能精炼
     */
    void onReqAutoRefineGem(Player player, EquipMessage.ReqAutoRefineGem mess);

    /**
     * 请求宝石升级
     */
    void onReqUpGradeGem(Player player, EquipMessage.ReqUpGradeGem mess);

}
