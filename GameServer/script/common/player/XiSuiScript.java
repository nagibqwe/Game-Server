package common.player;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Item_Bean;
import com.data.bean.Cfg_State_xisui_Bean;
import com.data.bean.Cfg_State_xisui_acupoint_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.script.IXiSuiScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @explain: desc
 * @time Created on 2020/2/3 18:33.
 * @author: tc
 */
public class XiSuiScript implements IXiSuiScript {

    private static final Logger log = LogManager.getLogger(XiSuiScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.XiSuiScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 请求洗髓升级
     *
     * @param player
     * @param free   true免费洗髓（消耗道具），false收费洗髓（消耗货币）
     */
    @Override
    public void onReqXiSui(Player player, boolean free) {
        int lvl = player.getXsLevel();
        Cfg_State_xisui_acupoint_Bean bean = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(lvl == 0 ? lvl + 1 : lvl);
        if (bean == null) return;
        if (!checkCondition(player, bean.getGroup())) return;

        // 下一级配置
        Cfg_State_xisui_acupoint_Bean beanNext = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(lvl + 1);
        if (beanNext == null) return;
        boolean ret = false;
        if (beanNext.getItem_cost().size() == 0 && beanNext.getCoin_cost().size() == 0) {
            // 突破
            player.setXsLevel(lvl + 1);
            refreshProp(player);
            ret = true;
        } else {
            if (!free)
                ret = coinUp(player, beanNext);
            else
                ret = itemUp(player, beanNext);
        }

        if (ret) sync(player);

        Manager.controlManager.operate(player, FunctionVariable.XisuiAccomplished, 1);
    }

    /**
     * 该玩家是否需要
     *
     * @param player
     * @param itemId
     * @return
     */
    @Override
    public boolean isNeed(Player player, int itemId) {
        int lvl = player.getXsLevel();
        Cfg_State_xisui_acupoint_Bean bean = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(lvl == 0 ? lvl + 1 : lvl);
        if (bean == null) return false;
        if (bean.getItem_cost().size() == 0) {
            bean = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(lvl + 1);
        }

        if (bean == null) return false;
        return bean.getItem_cost().get(0) == itemId;
    }

    /**
     * 使用道具
     *
     * @param player
     * @param itemModuleId
     * @param num
     * @param reason
     * @param action
     */
    @Override
    public void useItem(Player player, int itemModuleId, int num, int reason, long action) {
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemModuleId);
        if (itemBean == null) return;
        if (itemBean.getType() != ItemTypeConst.XiSui) return;

        for (int i = 0; i < num; i++) {
            List<Item> dropItems = getDropItems(player, itemBean, num);
            if (dropItems == null || dropItems.size() == 0) continue;
            if (!Manager.backpackManager.manager().addItems(player, dropItems, reason, action))
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                        MessageString.System, MessageString.System, MessageString.Task_Content, dropItems, reason);
        }
    }

    /**
     * 获取洗髓包的掉落
     *
     * @param player
     * @param itemModuleId
     * @param num
     * @return
     */
    @Override
    public List<Item> getDropItems(Player player, int itemModuleId, int num) {
        List<Item> items = new ArrayList<>();
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemModuleId);
        if (itemBean == null) return items;
        return getDropItems(player, itemBean, num);
    }

    /**
     * 获取洗髓包的掉落
     *
     * @param player
     * @param bean
     * @param num
     * @return
     */
    @Override
    public List<Item> getDropItems(Player player, Cfg_Item_Bean bean, int num) {
        List<Item> items = new ArrayList<>();
        if (bean == null) return items;
        if (bean.getType() != ItemTypeConst.XiSui) return items;

        Map<Integer, List<Integer>> dropMap = new HashMap<>();
        for (int i = 0; i < num; i++) {
            List<List<Integer>> dropList = Manager.dropManager.deal().getItemDrops(player, bean.getUes_gift());
            if (dropList == null || dropList.isEmpty()) continue;

            for (List<Integer> itemObj : dropList) {
                if (dropMap.containsKey(itemObj.get(0))) {
                    dropMap.get(itemObj.get(0)).set(1, itemObj.get(1) + dropMap.get(itemObj.get(0)).get(1));
                } else {
                    dropMap.put(itemObj.get(0), itemObj);
                }
            }
        }
        if (dropMap.size() == 0) return items;
        return Item.createItems(dropMap.values(), 1);
    }

    @Override
    public int calcOneKeyCoinNum(Player player, int curGenderClass) {
        if (!checkCondition(player, curGenderClass)) {
            log.error("洗髓条件不满足 player={}", player);
            return -1;
        }
        int needCoin = 0;
        for (Cfg_State_xisui_acupoint_Bean acupointBean : CfgManager.getCfg_State_xisui_acupoint_Container().getValuees()) {
            if (acupointBean.getGroup() == curGenderClass && acupointBean.getId() > player.getXsLevel()) {
                if (acupointBean.getCoin_cost().size() == 2) {
                    needCoin += acupointBean.getCoin_cost().get(1);
                }
            }
        }
        return needCoin;
    }

    /**
     * 一键洗髓完成
     *
     * @param player
     * @param genderClass
     */
    @Override
    public void oneKeySucess(Player player, int genderClass) {
        int level = player.getXsLevel();
        for (Cfg_State_xisui_acupoint_Bean acupointBean : CfgManager.getCfg_State_xisui_acupoint_Container().getValuees()) {
            if (acupointBean.getGroup() == genderClass) {
                level = Math.max(level, acupointBean.getId());
            }
        }
        player.setXsLevel(level == 0 ? 0 : level + 1);
        sync(player);
        log.error("一键洗髓等级提升 level={} player={}", level, player);
    }

    /**
     * 重新刷新洗髓属性，用于已经初始化属性之后的调用
     *
     * @param player
     */
    private void refreshProp(Player player) {
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.XiSui);
    }

    private void sync(Player player) {
        PlayerMessage.SyncXiSuiData.Builder builder = PlayerMessage.SyncXiSuiData.newBuilder();
        builder.setRoleID(player.getId());
        builder.setXsLvl(player.getXsLevel());
        MessageUtils.send_to_player(player, PlayerMessage.SyncXiSuiData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private boolean coinUp(Player player, Cfg_State_xisui_acupoint_Bean bean) {
        int lvl = player.getXsLevel();
        int newLvl = bean.getId();
        int group = 0;
        if (lvl == 0)
            group = bean.getGroup();
        else {
            Cfg_State_xisui_acupoint_Bean old = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(lvl);
            if (old != null)
                group = old.getGroup();
        }

        if (group == 0) return false;
        int coinType = bean.getCoin_cost().get(0);
        int coinNum = bean.getCoin_cost().get(1);

        boolean isComplete = false;
        while (true) {
            Cfg_State_xisui_acupoint_Bean next = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(newLvl + 1);
            if (next == null) break;
            if (next.getCoin_cost().size() > 0 && coinType != next.getCoin_cost().get(0)) return false;
            if (next.getGroup() != group) {
                newLvl++;
                isComplete = true;
                break;
            }
            newLvl++;
            coinNum += next.getCoin_cost().get(1);
        }

        if (!isComplete) return false;

        if (!Manager.currencyManager.manager().canDecItemCoin(player, coinNum, coinType))
            return false;

        // 扣钱
        Manager.currencyManager.manager().onDecItemCoin(player, coinNum, ItemChangeReason.XiSuiDec, lvl, coinType);
        player.setXsLevel(newLvl);
        refreshProp(player);
        return true;
    }

    private boolean itemUp(Player player, Cfg_State_xisui_acupoint_Bean bean) {
        // 判断道具是否足够
        int itemID = bean.getItem_cost().get(0);
        int num = bean.getItem_cost().get(1);

        if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemID, num)) return false;

        // 扣道具
        Manager.backpackManager.manager().onRemoveItem(player, itemID, num, ItemChangeReason.XiSuiDec, player.getXsLevel());
        Cfg_State_xisui_acupoint_Bean nextBean = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(bean.getId() + 1);
        if (nextBean != null && nextBean.getGroup() != bean.getGroup()) {
            //第一阶段最后一星完成直接进入第二阶段0星
            player.setXsLevel(nextBean.getId());
        } else {
            player.setXsLevel(bean.getId());
        }
        refreshProp(player);
        return true;
    }

    private boolean checkCondition(Player player, int group) {
        Cfg_State_xisui_Bean bean = CfgManager.getCfg_State_xisui_Container().getValueByKey(group);
        if (bean == null) {
            return true;
        }

        for (ReadArray<Integer> is : bean.getCondition().getValuees()) {
            if (is.size() != 2)
                return false;

            if (is.get(0) == 1) {
                // 等级条件
                if (player.getLevel() < is.get(1))
                    return false;
            } else if (is.get(0) == 2) {
                // 境界条件
                if (player.getStateVip().getLv() < is.get(1))
                    return false;
            } else
                return false;
        }

        return true;
    }
}
