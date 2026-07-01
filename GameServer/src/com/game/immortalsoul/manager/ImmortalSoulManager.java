package com.game.immortalsoul.manager;


import java.util.List;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.immortalsoul.script.IImmortalSoul;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by clc on 2019/7/5. cxl
 */
public class ImmortalSoulManager {

    private static final Logger log = LogManager.getLogger(ImmortalSoulManager.class);


    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ImmortalSoulManager manager;

        Singleton() {
            this.manager = new ImmortalSoulManager();
        }

        ImmortalSoulManager getProcessor() {
            return manager;
        }
    }

    //SoulManager
    public static ImmortalSoulManager getInstance() {
        return ImmortalSoulManager.Singleton.INSTANCE.getProcessor();
    }

    public IImmortalSoul manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ImmortalSoulBaseScript);
        if (is instanceof IImmortalSoul) {
            return (IImmortalSoul) is;
        } else {
            return null;
        }
    }

    //上线
    public void online(Player player) {
        manager().online(player);
    }
    //镶嵌
    public  void  reqInlaySoul(Player player,long uid,int pos)
    {
        manager().reqInlaySoul( player, uid, pos);
    }
    //脱下
    public  void  getoffSoul(Player player,int pos){
        manager().getoffSoul(player,pos);
    }

    //升级
    public  void  soulLevelUp(Player player,int pos)
    {
        manager().soulLevelUp(player,pos);
    }
    //兑换
    public  void  exchangeSoul(Player player,int itemid,int num){
        manager().exchangeSoul(player,itemid, num);
    }

    //合成
    public  void  compoundSoul(Player player,int itemid){
        manager().compoundSoul(player,itemid);
    }

    //分解
    public  void  resolveSoul(Player player,List<Long> uids){
        manager().resolveSoul(player,uids);
    }

    //GM添加仙魄
    public  void  gmAddSoul(Player player,int itemID,boolean tellClient,int reason){
        manager().gmAddSoul(player,itemID,tellClient,reason);
    }

    //获得所有装备上的仙魄
    public  int  getAllOnEquipLevel(Player player){return manager().getAllOnEquipLevel(player);}

    //获取仙魄名字
    public  String  getImmortalSoulName(Integer id){return manager().getImmortalSoulName(id);}

    /**
     * 判断是否灵魄
     * @return
     */
    public boolean isSoulItem(Item item) {
        final Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        return itemBean.getType() == ItemTypeConst.SoulItem;
    }

}
