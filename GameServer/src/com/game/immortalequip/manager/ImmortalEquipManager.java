package com.game.immortalequip.manager;


import java.util.List;

import com.data.CfgManager;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.immortalequip.script.IImmortalEquip;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by clc on 2020/2/12.
 */
public class ImmortalEquipManager {

    private static final Logger log = LogManager.getLogger(ImmortalEquipManager.class);


    //第一套30-43,第二套44-57，第三套 58-71 第四套 72-85
    public static int MinPart = 30;//最小部位

    public static int MaxPart = 85;//最大部位

    //八卦部位
    public static int baguaMinPart = 401;//八卦最小部位
    public static int baguaMaxPart = 440;//八卦最大部位

    public static int Suit_1_Start = 34;//套装1 起点

    public static int Suit_1_End = 36;//套装1 结束

    public static int Suit_2_Start = 36;//套装2 起点

    public static int Suit_2_End = 40;//套装2 结束

    public static int Suit_3_Start = 40;//套装3 起点

    public static int Suit_3_End = 44;//套装3 结束

    public static int Suit_4_Start = 30;//套装4 起点

    public static int Suit_4_End = 34;//套装4 结束

    public static int MaxSuitNum = 4;//最大套装数量


    public static int FacadePartTypeMin = 30;//外观最小部位类型

    public static int FacadePartTypeMax = 33;//外观最大部位类型

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ImmortalEquipManager manager;

        Singleton() {
            this.manager = new ImmortalEquipManager();
        }

        ImmortalEquipManager getProcessor() {
            return manager;
        }
    }

    //SoulManager
    public static ImmortalEquipManager getInstance() {
        return ImmortalEquipManager.Singleton.INSTANCE.getProcessor();
    }

    public IImmortalEquip manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ImmortalEquipBaseScript);
        if (is instanceof IImmortalEquip) {
            return (IImmortalEquip) is;
        } else {
            return null;
        }
    }


    public boolean getPartIsRight(int part){
        return part>= MinPart && part<=MaxPart;
    }

    /**
     * 判断是否仙甲
     * @return
     */
    public boolean isImmEquip(Item item) {
        final Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        return itemBean.getType() == ItemTypeConst.IMM_EQUIP;
    }

}
