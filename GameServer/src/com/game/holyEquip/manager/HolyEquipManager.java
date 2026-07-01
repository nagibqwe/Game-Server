package com.game.holyEquip.manager;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.holyEquip.script.IHolyEquipScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/10/25.
 */
public class HolyEquipManager {


    public final Logger log = LogManager.getLogger(HolyEquipManager.class);

    public static HolyEquipManager getInstance() {
        return HolyEquipManager.Singleton.INSTANCE.getProcessor();
    }

    public IHolyEquipScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HolyEquipScript);
        if (is instanceof IHolyEquipScript) {
            return (IHolyEquipScript) is;
        } else {
            log.error("IHolyEquipScript 没有找到具体的实例！");
            return null;
        }
    }

    /**
     * 玩家圣装系统战斗力
     */
    private ConcurrentHashMap<Long, Integer> fightPowerMap = new ConcurrentHashMap<Long, Integer>();

    public ConcurrentHashMap<Long, Integer> getFightPowerMap() {
        return fightPowerMap;
    }

    public void setFightPowerMap(ConcurrentHashMap<Long, Integer> fightPowerMap) {
        this.fightPowerMap = fightPowerMap;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        HolyEquipManager processor;

        Singleton() {
            this.processor = new HolyEquipManager();
        }

        HolyEquipManager getProcessor() {
            return processor;
        }
    }

    /**
     * 获取圣装背包格子
     * @param player
     * @return
     */
    public int getEmptyGridNum(Player player) {
        return Global.Equip_Holy_bag - player.getHolyEquipBaseInfo().getHolyEquipItemList().size();
    }
    /**
     * 判断是否圣装
     * @return
     */
    public boolean isHolyEquipOrItem(Item item) {
        final Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        return itemBean.getType() == ItemTypeConst.HolyEuiqp;
    }
}
