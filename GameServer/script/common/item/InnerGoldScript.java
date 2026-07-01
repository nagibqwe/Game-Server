package common.item;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.script.IItemUse;
import com.game.backpack.structs.Item;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author admin
 */
public class InnerGoldScript implements IScript, IItemUse {

    private static final Logger log = LogManager.getLogger(InnerGoldScript.class);

    @Override
    public int getId() {
//        return 1001;
        return ScriptEnum.InnerGoldBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
//
//    @Override
//    public Object call(int scriptId, Object arg) {
//        ScriptParams params = (ScriptParams) arg;
//        Player player = params.GetPlayer();
//        Item item = params.getItem();
//        int num = params.GetOtherIntByType("num");
//        long actionId = params.GetOtherLongByType("actionId");
//        String funcName = params.GetFuncName();
//        if (funcName.equals(ScriptFuncNamesEnum.Item_Use.GetValue())) {
//            return use(player, item, num, actionId);
//        }
//        return false;
//    }

    @Override
    public boolean useItem(Player player, Item item, int useNum, long actionId, boolean otherOpt) {
        if (!ServerConfig.isTestServer()) {
            return false;
        }

        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (model == null) {
            return false;
        }

        if (!Manager.backpackManager.manager().onRemoveItem(player, item, useNum, ItemChangeReason.OwnUseDec, actionId)) {
            log.error("在使用内部元宝充值道具时出现扣除失败了------------------" + item.toString());
            return false;
        }

        ReadIntegerArrayEs effect = model.getEffect_num();
        if (effect.isEmpty()) {
            return false;
        }
        for (ReadArray<Integer> aii : effect.getValuees()) {
            if (28 != aii.get(0)) {
                log.error("元宝充值卡内部充值，配置数据错误：" + effect.toString());
                return false;
            }
            try {
                int num = (int)(aii.get(1) * useNum);

                Manager.currencyManager.manager().onAddGold(player, num, ItemChangeReason.UserItemCardGetGoldGet, actionId);
                double f = num / 10.0;
                //写数据库log
                log.info("使用道具ID：" + model.getName() + "(" + model.getId() + ") 数量：(" + useNum + "),充值成功：充值的金额：" + f + " , 元宝数据:" + num);
                return true;
            } catch (NumberFormatException e) {
                log.error(e, e);
            }
        }
        return false;
    }

    @Override
    public boolean unUseItem(Player player, Item aThis, int useNum, long actionId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
