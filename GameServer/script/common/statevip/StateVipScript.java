package common.statevip;

import com.data.*;
import com.data.bean.Cfg_State_Bean;
import com.data.bean.Cfg_State_power_Bean;
import com.game.backpack.structs.Item;
import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.statevip.script.IStateVip;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.message.StateVipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lw
 */

public class StateVipScript implements IScript, IStateVip {

    private static final Logger logger = LogManager.getLogger("StateVipScript");

    @Override
    public void initStateVip(Player player) {
        player.getStateVips().clear();
        int key = (player.getStateVip().getLv() + 1) * 100;
        while (true) {
            key++;
            Cfg_State_Bean stateBean = CfgManager.getCfg_State_Container().getValueByKey(key);
            if (stateBean != null) {
                if (Manager.controlManager.deal().checkFuncProgress(player, stateBean.getCondition())) {
                    player.getStateVips().put(stateBean.getId(), true);
                } else {
                    player.getStateVips().put(stateBean.getId(), false);
                }
                continue;
            }
            break;
        }
        reqStateVip(player);
    }

//    @Override
//    public void reqReward(Player player, int id) {
//        StateVipMessage.ResGetReward.Builder builder = StateVipMessage.ResGetReward.newBuilder();
//        Cfg_State_Bean bean = CfgManager.getCfg_State_Container().getValueByKey(id);
//        if (bean == null) {
//            logger.error("境界任务不存在:" + id);
//            return;
//        }
//
//        boolean isReward = player.getStateVips().getOrDefault(id, true);
//        if (isReward) {
//            return;
//        }
//
//        if (!Manager.controlManager.deal().checkFuncProgress(player,  bean.getCondition())) {
//            return;
//        }
//
//        player.getStateVips().put(id, true);
//        List<Item> items = Item.createItems(bean.getReward().get(0), bean.getReward().get(1), true);
//        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
//            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.StateVipReward, id);
//        } else {
//            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items);
//        }
//        builder.setId(id);
//        MessageUtils.send_to_player(player, StateVipMessage.ResGetReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
//
//        Manager.countManager.addCount(player, BaseCountType.StateTask, player.getStateVip().getLv(), Count.RefreshType.CountType_Forever,1);
//        Manager.controlManager.operate(player, FunctionVariable.StateTask, 1);
//
//        int progress = Manager.controlManager.deal().getFuncProgress(player, bean.getCondition());
//        Manager.biManager.getScript().biRealm(player,1,2,progress,bean.getId(),progress+"/"+bean.getCondition().get(bean.getCondition().size()-1));
//    }

    @Override
    public void reqStateVip(Player player) {
        StateVipMessage.ResStateVip.Builder builder = StateVipMessage.ResStateVip.newBuilder();
        Iterator iterator = player.getStateVips().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = (Map.Entry<Integer, Boolean>) iterator.next();
            Cfg_State_Bean bean = CfgManager.getCfg_State_Container().getValueByKey(entry.getKey());
            if (bean == null) {
                logger.error("境界任务不存在:" + entry.getKey());
                iterator.remove();
                continue;
            }
            StateVipMessage.stateVip.Builder stateVip = StateVipMessage.stateVip.newBuilder();
            stateVip.setId(entry.getKey());
            stateVip.setStatus(entry.getValue());
            stateVip.setProgress(Manager.controlManager.deal().getFuncProgress(player, bean.getCondition()));
            builder.addStateVips(stateVip);
        }
        MessageUtils.send_to_player(player, StateVipMessage.ResStateVip.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqStateVipUp(Player player) {
        int oldStateLv = player.getStateVip().getLv();
        Iterator iterator = player.getStateVips().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = (Map.Entry<Integer, Boolean>) iterator.next();
            if (!entry.getValue()) {
                return;
            }
        }

        Cfg_State_power_Bean bean = CfgManager.getCfg_State_power_Container().getValueByKey(oldStateLv + 1);
        if (bean == null) {
            return;
        }

        player.getStateVips().clear();

        List<Item> items = Item.createItems(bean.getReward());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.StateVipUpRewardGet, oldStateLv + 1);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.StateVipUpRewardGet);
        }

        //4399bi
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        if (guild != null && guild.getChairMan().getId() == player.getId()) {
            Manager.biManager.get4399Script().updateGuild(guild);
        }

        StateVipMessage.ResStateVipBroadcast.Builder builder1 = StateVipMessage.ResStateVipBroadcast.newBuilder();
        StateVipMessage.ResStateVip.Builder builder = StateVipMessage.ResStateVip.newBuilder();
        int key = (oldStateLv + 1 + 1) * 100;
        while (true) {
            key++;
            Cfg_State_Bean stateBean = CfgManager.getCfg_State_Container().getValueByKey(key);
            if (stateBean != null) {
                StateVipMessage.stateVip.Builder stateVip = StateVipMessage.stateVip.newBuilder();
                stateVip.setId(stateBean.getId());
                stateVip.setProgress(Manager.controlManager.deal().getFuncProgress(player, stateBean.getCondition()));
                if (Manager.controlManager.deal().checkFuncProgress(player, stateBean.getCondition())) {
                    player.getStateVips().put(stateBean.getId(), true);
                    stateVip.setStatus(true);

                } else {
                    player.getStateVips().put(stateBean.getId(), false);
                    stateVip.setStatus(false);
                }
                builder.addStateVips(stateVip);
                continue;
            }
            break;
        }
        player.getStateVip().setLv(oldStateLv + 1);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StateVip);
        Manager.controlManager.operate(player, FunctionVariable.StateLevel, player.getStateVip().getLv());
        builder1.setRoleID(player.getId());
        builder1.setStateVip(player.getStateVip().getLv());

        MessageUtils.send_to_player(player, StateVipMessage.ResStateVip.MsgID.eMsgID_VALUE,
                builder.build().toByteArray());
        MessageUtils.send_to_roundPlayer(player, StateVipMessage.ResStateVipBroadcast.MsgID.eMsgID_VALUE,
                builder1.build().toByteArray(), true);
        Manager.shopManager.limitShop().refresh(player);

        //激活化形
        Manager.huaxinFlySwordManager.deal().onReqUseHuxin(player, bean.getFly_sword_modele() ,1,false);
    }

    @Override
    public boolean gmStateVipUp(Player player, int lv) {
        player.getStateVips().clear();
        Cfg_State_power_Bean bean = CfgManager.getCfg_State_power_Container().getValueByKey(lv);
        if (bean == null) {
            return false;
        }

        List<Item> items = Item.createItems(bean.getReward());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.StateVipUpRewardGet, lv);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.StateVipUpRewardGet);
        }


        StateVipMessage.ResStateVipBroadcast.Builder builder1 = StateVipMessage.
                ResStateVipBroadcast.newBuilder();
        StateVipMessage.ResStateVip.Builder builder = StateVipMessage.
                ResStateVip.newBuilder();
        int key = (lv + 1) * 100;
        while (true) {
            key++;
            Cfg_State_Bean stateBean =CfgManager.getCfg_State_Container().getValueByKey(key);
            if (stateBean != null) {
                StateVipMessage.stateVip.Builder stateVip = StateVipMessage.stateVip.newBuilder();
                stateVip.setId(stateBean.getId());
                stateVip.setProgress(Manager.controlManager.deal().getFuncProgress(player, stateBean.getCondition()));
                if (Manager.controlManager.deal().checkFuncProgress(player, stateBean.getCondition())) {
                    player.getStateVips().put(stateBean.getId(), true);
                    stateVip.setStatus(true);
                } else {
                    player.getStateVips().put(stateBean.getId(), false);
                    stateVip.setStatus(false);
                }
                builder.addStateVips(stateVip);
                continue;
            }
            break;
        }
        player.getStateVip().setLv(lv);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StateVip);
        Manager.controlManager.operate(player, FunctionVariable.StateLevel,  player.getStateVip().getLv());
        builder1.setRoleID(player.getId());
        builder1.setStateVip(player.getStateVip().getLv());

        MessageUtils.send_to_player(player, StateVipMessage.ResStateVip.MsgID.eMsgID_VALUE,
                builder.build().toByteArray());
        MessageUtils.send_to_roundPlayer(player, StateVipMessage.ResStateVipBroadcast.MsgID.eMsgID_VALUE,
                builder1.build().toByteArray(), true);
        Manager.shopManager.limitShop().refresh(player);

        //激活化形
        Manager.huaxinFlySwordManager.deal().onReqUseHuxin(player, bean.getFly_sword_modele() ,1,false);
        return true;
    }

    @Override
    public void operateStateVip(Player player, int type) {
        Iterator iterator = player.getStateVips().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> entry = (Map.Entry<Integer, Boolean>) iterator.next();
            if (entry.getValue()) {
                continue;
            }

            Cfg_State_Bean bean = CfgManager.getCfg_State_Container().getValueByKey(entry.getKey());
            if (bean == null) {
                iterator.remove();
                continue;
            }

            if (bean.getCondition().get(0) != type) {
                continue;
            }

            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())) {
                entry.setValue(true);
            }

            StateVipMessage.ResStateVipProgress.Builder builder = StateVipMessage.ResStateVipProgress.newBuilder();
            builder.setId(bean.getId());
            int progress = Manager.controlManager.deal().getFuncProgress(player, bean.getCondition());
            builder.setProgress(progress);
            MessageUtils.send_to_player(player, StateVipMessage.ResStateVipProgress.MsgID.eMsgID_VALUE, builder.build().toByteArray());

//            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())) {
//                Manager.biManager.getScript().biRealm(player,1,1,progress,bean.getId(),progress+"/"+bean.getCondition().get(bean.getCondition().size()-1));
//            }
        }
    }


    @Override
    public int getId() {
        return ScriptEnum.StateLevelBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


}
