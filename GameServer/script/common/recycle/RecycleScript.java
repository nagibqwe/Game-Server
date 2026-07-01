package common.recycle;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Equip_Bean;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recycle.script.IRecycleScript;
import com.game.script.structs.ScriptEnum;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.message.RecycleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class RecycleScript implements IRecycleScript, IScript {
    private static final Logger log = LogManager.getLogger(RecycleScript.class);

    public static final int auto_quality = 6;
    public static final int auto_star = 1;
    public static final int autoMinLevel = 65;

    @Override
    public int getId() {
        return ScriptEnum.RecycleBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqRecycle(Player player, RecycleMessage.ReqRecycle messInfo) {
        List<Equip> list = getEquips(player, messInfo.getItemIdList());
        if (list.isEmpty()) {
            //策划要求没有装备的主动熔炼也算熔炼次数
            Manager.countManager.addVariant(player, VariantType.RecycleNum, 1);
            Manager.controlManager.operate(player, FunctionVariable.EquipSmelt, 1);
            return;
        }
        recycle(player, list);
    }

    @Override
    public void onReqSetAuto(Player player, RecycleMessage.ReqSetAuto messInfo) {
        if (player.isAutoRecycle() == messInfo.getIsOpen()) {
            return;
        }
        //VIP宝珠激活检查
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        //VIP等级检查
        if(!Manager.vipManager.power().isCanFreeRecycle(player)){
            return;
        }
        //角色等级检查
        if(player.getLevel()<autoMinLevel){
            return;
        }
        setAutoRecycle(player,messInfo.getIsOpen());
    }

    @Override
    public void setAutoRecycle(Player player, boolean isOpen) {
        player.setAutoRecycle(isOpen);
        sendResSetAuto(player);
    }

    private void sendResSetAuto(Player player){
        RecycleMessage.ResSetAuto.Builder msg = RecycleMessage.ResSetAuto.newBuilder();
        msg.setIsOpen(player.isAutoRecycle());
        MessageUtils.send_to_player(player, RecycleMessage.ResSetAuto.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void online(Player player) {
        sendResSetAuto(player);
    }

    private void recycle(Player player, List<Equip> equipList) {
        int getNum = 0;
        long actionId = IDConfigUtil.getLogId();
        Set<Long> deleteList = new HashSet<>();
        Set<Long> changeList = new HashSet<>();
        int agateNum = 0;
        for (Equip equip : equipList) {
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (bean == null) {
                log.error("回收炉不能回收的物品，原因：Cfg_Equip_Bean找不到数据");
                continue;
            }

            if (!Manager.equipManager.deal().removeEquipNotNotic(player, equip, ItemChangeReason.RecycleDec, actionId, deleteList, changeList)) {
                log.error(String.format("玩家{%s}从背包放入仓库错误!移除道具{%s}错误!!!", TaskHelp.getPlayerInfo(player), equip.getItemModelId()));
                continue;
            }
            getNum += bean.getSeal_anima();
            agateNum += bean.getMagic_bowl_solve();
        }
        if (deleteList.size() != 0) {
            Manager.backpackManager.manager().sendItemDeleteList(player, ItemChangeReason.RecycleDec, deleteList);
        }
        for (Long changeId : changeList) {
            Item item = Manager.backpackManager.manager().getItemById(player, changeId);
            if (item == null) {
                log.error("回收炉同步客户端物品失败，原因：背包里找不到" + changeId);
                continue;
            }
            Manager.backpackManager.manager().sendItemChange(player, ItemChangeReason.RecycleDec, item);
        }

//        Manager.vipManager.deal().addAgatePoint(player, agateNum, 1);
        Manager.countManager.addVariant(player, VariantType.RecycleNum, 1);
        Manager.controlManager.operate(player, FunctionVariable.EquipSmelt, 1);

        if (getNum < 1) {
            log.error("回收炉回收的装备没有给灵晶！：" + JsonUtils.toJSONString(deleteList) + ";" + JsonUtils.toJSONString(deleteList));
        } else {
            //VIP特权生效则增加额外熔炼奖励
            if (Manager.vipManager.power().canFree(player, VipPower.POWER_38)){
                getNum += getNum * (Manager.vipManager.power().getVipPowerValue(player, VipPower.POWER_38)/10000f);
            }

            if (!Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.DeftCrystal, getNum, ItemChangeReason.RecycleGet, actionId)) {
                log.error("回收炉回收装备没有给灵晶，玩家无法加上灵晶，玩家 = " + player.getId() + "，应得灵晶数量 = " + getNum);
                return;
            }
        }
        RecycleMessage.ResRecycle.Builder msg = RecycleMessage.ResRecycle.newBuilder();
        msg.setNum(getNum);
        MessageUtils.send_to_player(player, RecycleMessage.ResRecycle.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void autoRecycle(Player player, Equip equip) {
        //VIP等级检查
        if(!Manager.vipManager.power().isCanFreeRecycle(player)){
            return;
        }
        //角色等级检查
        if(player.getLevel()<autoMinLevel){
            return;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (bean == null) {
            log.error("回收炉不能回收的物品，原因：Cfg_Equip_Bean找不到数据");
            return;
        }
        if (bean.getQuality() >= auto_quality) {
            return;
        }
        if (bean.getDiamond_Number() > auto_star) {
            return;
        }
//        if (bean.getPart() == 3 || bean.getPart() == 7) {//排除的项链和手镯
//            return;
//        }

        //2021.11.11新增需求, 检查要熔炼的装备和玩家身上的装备做评分对比
        Equip pEquip = Manager.equipManager.getEquipByType(player, bean.getPart());
        if(pEquip != null){
            Cfg_Equip_Bean pBean = CfgManager.getCfg_Equip_Container().getValueByKey(pEquip.getItemModelId());
            if (pBean != null && bean.getScore()>pBean.getScore()) {
                return;
            }
        }

        long actionId = IDConfigUtil.getLogId();
        if (!Manager.equipManager.deal().removeEquip(player, equip, ItemChangeReason.RecycleDec, actionId)) {
            log.error(String.format("玩家{%s}从背包放入仓库错误!移除道具{%s}错误!!!", TaskHelp.getPlayerInfo(player), equip.getItemModelId()));
            return;
        }
        int getNum = bean.getSeal_anima();

        Manager.countManager.addVariant(player, VariantType.RecycleNum, 1);
        Manager.controlManager.operate(player, FunctionVariable.EquipSmelt, 1);
//        Manager.vipManager.deal().addAgatePoint(player, bean.getMagic_bowl_solve(), 1);

        if (getNum < 1) {
            log.error("回收炉回收的装备没有给灵晶！id="+ equip.getId()+",modelId="+equip.getId());
        } else {
            //VIP特权生效则增加额外熔炼奖励
            if (Manager.vipManager.power().canFree(player, VipPower.POWER_38)){
                getNum += getNum * (Manager.vipManager.power().getVipPowerValue(player, VipPower.POWER_38)/10000f);
            }

            if (!Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.DeftCrystal, getNum, ItemChangeReason.RecycleGet, actionId)) {
                log.error("回收炉回收装备没有给灵晶，玩家无法加上灵晶，玩家 = " + player.getId() + "，应得灵晶数量 = " + getNum);
                return;
            }
        }
//        Manager.chatManager.sendSystemStrToPlayer(player, String.format("自动熔炼金色1星以下装备，获得%d灵晶",getNum));
        RecycleMessage.ResRecycle.Builder msg = RecycleMessage.ResRecycle.newBuilder();
        msg.setNum(getNum);
        MessageUtils.send_to_player(player, RecycleMessage.ResRecycle.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private List<Equip> getEquips(Player player, List<Long> equipIds) {
        List<Equip> list = new ArrayList<>();
        Iterator<Long> ite = equipIds.iterator();
        while (ite.hasNext()) {
            long id = ite.next();
            Item item = Manager.backpackManager.manager().getItemById(player, id);
            if (item == null) {
                log.error("回收炉不能回收的物品，原因：背包里找不到");
                continue;
            }
            if (!(item instanceof Equip)) {
                log.error("回收炉不能回收的物品，原因：不是装备");
                continue;
            }
            list.add((Equip) item);
        }
        return list;
    }
}
