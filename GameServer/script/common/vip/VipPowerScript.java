package common.vip;

import com.data.CfgManager;
import com.data.bean.Cfg_VIPTrueRecharge_Bean;
import com.data.bean.Cfg_VipPower_Bean;
import com.data.bean.Cfg_Vip_Bean;
import com.data.struct.ReadArray;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.vip.script.IVipPowerScript;
import com.game.vip.structs.VipPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.game.vip.manager.VipManager.INTEGER_BIT;

public class VipPowerScript implements IVipPowerScript {
    private final Logger logger = LogManager.getLogger(VipPowerScript.class);

    private final int DEFAULT_Discount = 10000;

    @Override
    public int getId() {
        return ScriptEnum.VipPowerScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public boolean canFree(Player player, int powerType) {
        //增加VIP宝珠状态检查
        if(!player.getVipPearl().canFree()){
            return false;
        }

        int vipLv = player.getVipLv();
        if (vipLv != 0) {
            Cfg_Vip_Bean vipBean = CfgManager.getCfg_Vip_Container().getValueByKey(vipLv);
            if (vipBean != null && vipBean.getVipPowerId().contains(powerType)) {
                return true;
            }
        }

        int rechargetLv = getRechargeLv(player);

        if (rechargetLv != 0) {
            Cfg_VIPTrueRecharge_Bean rechargeBean = CfgManager.getCfg_VIPTrueRecharge_Container().getValueByKey(rechargetLv);
            if (rechargeBean != null && rechargeBean.getTrueRewardPowerId().contains(powerType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getVipPowerValue(Player player, int powerType) {
        int num = 0;
        int vipLv = player.getVipLv();
        if (vipLv != 0) {
            Cfg_Vip_Bean vipBean = CfgManager.getCfg_Vip_Container().getValueByKey(vipLv);
            if (vipBean != null && vipBean.getVipPowerId().contains(powerType)) {
                for (ReadArray<Integer> r1 : vipBean.getVipPowerPra().getValuees()) {
                    if (r1.get(0) == powerType) {
                        num += r1.get(1);
                    }
                }
            }
        }

        //int rechargetLv = getRechargeLv(player);

        //if (rechargetLv != 0) {
        //    Cfg_VIPTrueRecharge_Bean rechargeBean = CfgManager.getCfg_VIPTrueRecharge_Container().getValueByKey(rechargetLv);
        //    if (rechargeBean != null && rechargeBean.getTrueRewardPowerId().contains(type)) {
        //        for (ReadArray<Integer> r1 : rechargeBean.getTrueRewardPowerPra().getValuees()) {
        //            if (r1.get(0) == type) {
        //                num += r1.get(1);
        //            }
        //        }
        //    }
        //}
        return num;
    }

    @Override
    public int getPriceByPurNum(int purNum, int powerType) {
        Cfg_VipPower_Bean bean = CfgManager.getCfg_VipPower_Container().getValueByKey(powerType);
        int size  = bean.getVipPowerPrice().size();
        if (purNum >= size) {
            return bean.getVipPowerPrice().get(size - 1);
        } else {
            return bean.getVipPowerPrice().get(purNum - 1);
        }
    }

    @Override
    public int getVipPurNum(Player player, int type) {
        //增加VIP宝珠状态检查
        if(!player.getVipPearl().canFree()){
            return 0;
        }
        Cfg_VipPower_Bean bean = CfgManager.getCfg_VipPower_Container().getValueByKey(type);
        if (bean == null || bean.getVipPowerPrice() == null || bean.getVipPowerPrice().size() == 0) {
            return 0;
        }
        return getVipPowerValue(player, type);
    }

    @Override
    public int getVipFreeNum(Player player, int type) {
        //增加VIP宝珠状态检查
        if(!player.getVipPearl().canFree()){
            return 0;
        }
        return getVipPowerValue(player, type);
    }

    @Override
    public int getVipDiscount(Player player, int type) {
        if (canFree(player, type)) {
            return getVipPowerValue(player, type);
        }
        return DEFAULT_Discount;
    }

    @Override
    public int getVipAddNumPrice(int purNum, int type) {
        return getPriceByPurNum(purNum, type);
    }

    @Override
    public int getDefaultDiscount() {
        return DEFAULT_Discount;
    }

    @Override
    public boolean isCanFreeFly(Player player) {
        return canFree(player, VipPower.POWER_1);
    }

    @Override
    public boolean isCanFreeStore(Player player) {
        return canFree(player, VipPower.POWER_2);
    }

    @Override
    public boolean isCanFreeRecycle(Player player) {
        return canFree(player, VipPower.POWER_3);
    }

    @Override
    public boolean isCanFreeFastBattle(Player player) {
        return canFree(player, VipPower.POWER_4);
    }

    @Override
    public boolean isCanFreeFabaoSweep(Player player) {
        return canFree(player, VipPower.POWER_5);
    }

    @Override
    public boolean isCanFreeMagicalSweep(Player player) {
        return canFree(player, VipPower.POWER_6);
    }

    @Override
    public boolean isOpenStarCopySweep(Player player) {
        return canFree(player, VipPower.POWER_7);
    }

    @Override
    public boolean isCanFreeSuoLinTai(Player player) {
        return canFree(player, VipPower.POWER_31);
    }

    @Override
    public boolean isCanFreeXinMo(Player player) {
        return canFree(player, VipPower.POWER_32);
    }

    @Override
    public boolean isCanFreeLinYun(Player player) {
        return canFree(player, VipPower.POWER_33);
    }

    private static int getRechargeLv(Player player) {
        for (int i = INTEGER_BIT; i > 0; i--) {
            if ((player.getVipRechargeReward() & (1 << (i - 1))) != 0) {
                return i;
            }
        }
        return 0;
    }


}
