package common.RechargeRebate;

import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.RechargeRebate.script.IRechargeRebate;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.db.bean.RechargeReturnBean;
import com.game.db.bean.RechargeTotalMoneyBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值返利
 */

public class RechargeRebateScript implements IScript, IRechargeRebate {

    private final Logger log = LogManager.getLogger("RechargeRebateScript");
    @Override
    public int getId() {
        return ScriptEnum.RechargeRebateScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
    @Override
    public void loadData() {

    }
    @Override
    public void rechargeRebate(Player player) {
        if (ServerConfig.getIsrebate() <= 0){
            return;
        }
        if (!Manager.rechargeRebateManager.getReturnBeanHashMap().containsKey(player.getUuid())){
            return;
        }
        RechargeReturnBean returnBean =  Manager.rechargeRebateManager.getReturnBeanHashMap().get(player.getUuid());
        if (returnBean.getReturnTime()>0 || returnBean.getRoleId()>0){
            return;
        }
        if (returnBean.getReturnGold() <= 0){
            return;
        }
        RechargeReturnBean returnBean1 = Manager.rechargeRebateManager.getRechargeReturnDao().selectByUserName(player.getUuid());
        if (returnBean1.getReturnTime()>0 || returnBean1.getRoleId()>0){
            returnBean.setReturnTime(returnBean1.getReturnTime());
            returnBean.setRoleId(returnBean1.getRoleId());
            returnBean.setCreateSid(returnBean1.getCreateSid());
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        List<Item> itemList = new ArrayList<>();
        itemList.addAll(Item.createItems(ItemCoinType.GemCoin,returnBean.getReturnGold(), false));
        String context = MessageString.Recharge_Get_Back_Double_Mail + "@_@" + returnBean.getRechargeTotalMoney() / 100 + "@_@" + returnBean.getReturnGold();
        Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                MessageString.Recharge_Get_Back_Double_Mail_Title, context, itemList, ItemChangeReason.RechargeRebateGet, actionId);
        //Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.GemCoin,returnBean.getReturnGold(), ItemChangeReason.RechargeRebateGet, IDConfigUtil.getLogId());
        log.info("玩家 playerUserID  :{}  roleID :{} 获得充值返利灵玉 :{} UserName： {}",returnBean.getUserId(),player.getId(),returnBean.getReturnGold(),returnBean.getUserName());
        returnBean.setReturnTime(TimeUtils.Time());
        returnBean.setRoleId(player.getId());
        returnBean.setCreateSid(player.getCreateServerId());
        Manager.rechargeRebateManager.getRechargeReturnDao().update(returnBean);
    }

    @Override
    public void addTotalRecharge(Player player, int totalfen) {
        if (totalfen <=0){
            return;
        }
        RechargeTotalMoneyBean bean1 = new RechargeTotalMoneyBean();
        bean1.setUserName(player.getUuid());
        bean1.setUserId(player.getUserId());
        bean1.setPlatformAccount(player.getPlatUserId());
        bean1.setRechargeTotalMoney(totalfen);
        Manager.rechargeRebateManager.getRechargeTotalMoneyDao().insert(bean1);
    }
}
