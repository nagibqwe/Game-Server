package com.game.vip.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Vip_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.vip.script.IVipPearlScript;
import com.game.vip.script.IVipPowerScript;
import com.game.vip.script.IVipScript;
import com.game.vip.structs.VipPower;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VipManager {
    private static final Logger log = LogManager.getLogger(VipManager.class);

    public static final int INTEGER_BIT = 31;

    private enum Singleton {
        INSTANCE;
        VipManager manager;

        Singleton() {
            this.manager = new VipManager();
        }

        VipManager getProcessor() {
            return manager;
        }
    }

    public static VipManager getInstance() {
        return VipManager.Singleton.INSTANCE.getProcessor();
    }

    public IVipScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.VipBaseScript);
        if (is instanceof IVipScript) {
            return (IVipScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IVipPowerScript power() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.VipPowerScript);
        if (is instanceof IVipPowerScript) {
            return (IVipPowerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public IVipPearlScript pearl() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.VipPearlScript);
        if (is instanceof IVipPearlScript) {
            return (IVipPearlScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public static int getVipExpRate(Player player) {
        int vipLv = player.getVipLv();
        Cfg_Vip_Bean bean = CfgManager.getCfg_Vip_Container().getValueByKey(vipLv);
        if (bean == null || bean.getVipPowerPra().isEmpty()) {
            return 0;
        }
        for (int i = 0; i < bean.getVipPowerPra().size(); i++) {
            if (bean.getVipPowerPra().get(i).get(0) == VipPower.POWER_8) {
                return bean.getVipPowerPra().get(i).get(2);
            }
        }
        return 0;
    }

}
