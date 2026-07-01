package com.game.openserverac.manager;

import com.game.manager.Manager;
import com.game.openserverac.scripts.IOpenServerAc;
import com.game.openserverac.scripts.IV4Rebate;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class OpenServerAcManager {

    private static final Logger logger = LogManager.getLogger(OpenServerAcManager.class);

    /**
     * 等级达人
     */
    public static final int Ac_Level_Talent = 1;

    //TODO 策划改为仙甲达人
//
//    /**
//     * 强化达人
//     */
//    public static final int Ac_Str_Talent = 2;
    public static final int Ac_EquipStar_Talent = 2;


    //TODO 暂时废弃
    /**
     * 充值达人
     */
    public static final int Ac_Recharge_Talent = 3;

    //TODO  策划 4法宝106
    /**
     * 宝石达人
     */
    public static final int Ac_Gem_Talent = 4;


    //TODO 5 修改为宠物排行
    /**
     * 洗练达人
     */
    public static final int Ac_Wash_Talent = 5;

    //TODO 宝石114 策划调整
    /**
     * 战力达人
     */
    public static final int Ac_Fight_Talent = 6;

    //TODO 战力102 策划调整 20210917欧阳帆
    /**
     * 灵体战力达人 - 118
     */
    public static final int Ac_Sprit_Talent = 7;

    /**
     * 坐骑达人 - 103
     */
    public static final int Ac_Horse_Talent = 8;

    /**
     * 未达成
     */
    public static final int State_0 = 0;

    /**
     * 可领取
     */
    public static final int State_1 = 1;

    /**
     * 已领取
     */
    public static final int State_2 = 2;


    public IOpenServerAc deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.OpenServerAcBaseScript);
        if (is instanceof IOpenServerAc) {
            return (IOpenServerAc) is;
        } else {
            logger.error("IOpenServerAc接口脚本错误");
            return null;
        }
    }
    public IV4Rebate v4Rebate(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.V4RebateScript);
        if (is instanceof IV4Rebate) {
            return (IV4Rebate) is;
        } else {
            logger.error("IV4Rebate接口脚本错误");
            return null;
        }
    }

    private enum Singleton {
        INSTANCE;
        OpenServerAcManager manager;
        Singleton() {
            this.manager = new OpenServerAcManager();
        }
        OpenServerAcManager getProcessor() {
            return manager;
        }
    }

    public static OpenServerAcManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
