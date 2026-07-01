package common.item;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Equip_suit_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.script.IItemUse;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.equip.struct.EquipDefine;
import com.game.equip.struct.EquipPart;
import com.game.hook.manager.PlayerHookManager;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.EquipMessage;
import game.message.EquipMessage.ResEquipChange;
import game.message.GodWeaponMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
public class EquipItemScript implements IScript, IItemUse {

    private static final Logger log = LogManager.getLogger(EquipItemScript.class);

    @Override
    public int getId() {
        return ScriptEnum.EquipItemBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean useItem(Player player, Item aThis, int useNum, long actionId, boolean otherOpt) {
        if (!(aThis instanceof Equip)) {
            log.error("穿戴装备 转换错误：" + aThis.getItemModelId());
            return false;
        }
        Equip equip = (Equip) aThis;
        Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (model == null) {
            log.error("穿戴装备 错误的装备：" + equip.getItemModelId());
            sendWearFaliedInfo(player, -1);
            return false;
        }
        if (!equip.canEquip(player, model)) {
            return false;
        }

        int equipPart = model.getPart();
        if (!isRightEquipPart(equipPart)) {
            log.error("穿戴装备 错误的装备部位,装备：" + equip.getItemModelId());
            sendWearFaliedInfo(player, -1);
            return false;
        }

        Equip weared = Manager.equipManager.getEquipByType(player, equipPart);
        if (weared == null) {
            return wear(player, model, equip, equipPart, actionId);
        } else {
            return wearAndInherit(player, model, equip, weared, equipPart, actionId, otherOpt);
        }
    }

    private boolean wear(Player player, Cfg_Equip_Bean equipBean, Equip equip, int equipPart, long actionId){
        Manager.backpackManager.manager().removeItemByCellId(player, equip.getGridId(), ItemChangeReason.WearEquipDec, actionId);
        //穿上装备,gridId表示装备栏位从0开始,0-7对应7个部位
        equip.setGridId(equipPart);
        EquipPart part = player.getEquipParts().get(equipPart);
        part.setEquip(equip);

        //通知客户端
        sendWearSuccessInfo(player, equip);

        //2020/3/9临时需求普通武器切换时也要更新外观
        if (equipPart == 1) {
            sendWeaponMessage(player,equip);
        }

        if (equipBean.getGrade() >= 6) {
            Manager.rankListManager.deal().updateEquipStarNum(player);
        }
        Manager.rankListManager.deal().updateEquipAllStar(player);

        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);

        handleEquipChange(player);

        Manager.controlManager.operate(player, FunctionVariable.New_sever_growup_wear_equip, 0);

        Manager.playerHookManager.deal().checkPlayerRateChange(player, PlayerHookManager.ShouZhuo);

        //装备BI
        Manager.biManager.getScript().biEquip(player, 1, 1, equipBean.getPart(), equip.getItemModelId(), equipBean.getName(), equipBean.getDiamond_Number(), equipBean.getGrade(), equipBean.getQuality(), part.getLevel(), equipBean.getBind(), equip.getSuitId(), 0, 0, 0, 0, 0);
        return true;
    }

    private boolean wearAndInherit(Player player, Cfg_Equip_Bean equipBean, Equip equip, Equip weared, int equipPart, long actionId, boolean otherOpt) {
        Manager.equipManager.deal().equipSuitInheritance(player, weared, equip);
        weared.setGridId(equip.getGridId());
        Manager.backpackManager.manager().removeItemByCellId(player, equip.getGridId(), ItemChangeReason.WearEquipDec, actionId);

        Cfg_Equip_Bean wearedBean = CfgManager.getCfg_Equip_Container().getValueByKey(weared.getItemModelId());
        if (wearedBean == null) {
            log.error("原来身上穿戴的装备错误：" + equip.getItemModelId());
            sendWearFaliedInfo(player, -1);
            return false;
        }
        if(equipBean.getQuality()==EquipDefine.EquipQuality_Red&&equipBean.getDiamond_Number()<wearedBean.getDiamond_Number()&&
                wearedBean.getInherit_equip_id()>0&&otherOpt){
            equip = (Equip)Item.createItem(wearedBean.getInherit_equip_id(), 1, true);
        }else{
            Manager.backpackManager.manager().addItem(player, weared, ItemChangeReason.UnWearEquipGet, actionId);
        }

        //穿上装备,gridId表示装备栏位从0开始,0-7对应7个部位
        equip.setGridId(equipPart);
        EquipPart part = player.getEquipParts().get(equipPart);
        part.setEquip(equip);

        //通知客户端
        sendWearSuccessInfo(player, equip);

        //2020/3/9临时需求普通武器切换时也要更新外观
        if (equipPart == 1) {
            sendWeaponMessage(player,equip);
        }

        if (equipBean.getGrade() >= 6) {
            Manager.rankListManager.deal().updateEquipStarNum(player);
        }
        Manager.rankListManager.deal().updateEquipAllStar(player);

        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP,PlayerAttributeType.GEM);

        handleEquipChange(player);

        Manager.controlManager.operate(player, FunctionVariable.New_sever_growup_wear_equip, 0);

        Manager.playerHookManager.deal().checkPlayerRateChange(player, PlayerHookManager.ShouZhuo);

        //装备BI
        Manager.biManager.getScript().biEquip(player, 1, 1, equipBean.getPart(), equip.getItemModelId(), equipBean.getName(), equipBean.getDiamond_Number(), equipBean.getGrade(), equipBean.getQuality(), part.getLevel(), equipBean.getBind(), equip.getSuitId(), 0, 0, 0, 0, 0);
        return true;
    }

    @Override
    public boolean unUseItem(Player player, Item aThis, int useNum, long actionId) {
        if (!(aThis instanceof Equip)) {
            log.error("卸下装备 转换错误：" + aThis.getItemModelId());
            return false;
        }
        Equip equip = (Equip) aThis;
        Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (model == null) {
            log.error("卸下装备 错误的装备：" + equip.getItemModelId());
            sendUnWearFailedInfo(player, EquipDefine.UnWearFailed_WrongEquip);
            return false;
        }
        //穿戴部位
        int equipPart = equip.getGridId();
        if (!Manager.backpackManager.manager().onHasAddSpace(player, equip)) {
            sendUnWearFailedInfo(player, EquipDefine.UnWearFaile_NoBagCell);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return false;
        }
        equip.setGridId(-1);
        if (!Manager.backpackManager.manager().addItem(player, equip, ItemChangeReason.UnWearEquipGet, IDConfigUtil.getLogId())) {
            //加入包裹失败
            sendUnWearFailedInfo(player, EquipDefine.UnWearFaile_NoBagCell);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemToBagFailed);
            return false;
        }
        player.getEquipParts().get(equipPart).setEquip(null);

        if (equip.getSuitId() > 0) {
            Cfg_Equip_suit_Bean suitbean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(equip.getSuitId());
            List<Item> items = new ArrayList<>();
            Cfg_Equip_Bean eb = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            for (ReadArray<Integer> item : suitbean.getNeed_items().getValuees()) {
                if (eb.getPart() == item.get(0)) {
                    items.add(Item.createItem(item.get(1), item.get(2), true));
                }
            }
            if (suitbean.getParent_ID() > 0) {
                Cfg_Equip_suit_Bean parentSuitbean = CfgManager.getCfg_Equip_suit_Container().getValueByKey(suitbean.getParent_ID());
                if (parentSuitbean != null) {
                    for (ReadArray<Integer> item : parentSuitbean.getNeed_items().getValuees()) {
                        if (eb.getPart() == item.get(0)) {
                            items.add(Item.createItem(item.get(1), item.get(2), true));
                        }
                    }
                }
            }
            if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
                Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.UnWearEquipGet, equip.getId());
            } else {
                Manager.taskManager.deal().sendRewardByMail(player, items, ItemChangeReason.UnWearEquipGet, equip.getId());
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.BAGISSPACETOMAIL);//"背包格子不足"
            }
            equip.setSuitId(0);
            Manager.backpackManager.manager().sendItemChange(player, ItemChangeReason.UnWearEquipGet, equip);
        }

        sendUnWearSuccessInfo(player, equip.getId());
        if (equipPart == EquipDefine.EquipPart_Weapon || equipPart == EquipDefine.EquipPart_Breastplate) {
            ResEquipChange.Builder msg = ResEquipChange.newBuilder();
            msg.setEquipType(equipPart);
            msg.setRoleId(player.getId());
            msg.setModeId(0);
            msg.setStar(Manager.equipManager.getOneEquipStar(player, equipPart));
            MessageUtils.send_to_roundPlayer(player, ResEquipChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());

//            Manager.rankListManager.syncRankPlayer(player);
        }

        handleEquipChange(player);
        if (model.getGrade() >= 6) {
            Manager.rankListManager.deal().updateEquipStarNum(player);
        }
        Manager.rankListManager.deal().updateEquipAllStar(player);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);

        MapUtils.synPlayerEquipMinStar(player);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);
        return true;
    }

    //发送穿戴装备失败消息
    private void sendWearFaliedInfo(Player player, int reason) {
        EquipMessage.ResEquipWearFailed.Builder msg = EquipMessage.ResEquipWearFailed.newBuilder();
        msg.setResult(reason);
        MessageUtils.send_to_player(player, EquipMessage.ResEquipWearFailed.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //判断是否是正确的装备部位
    private boolean isRightEquipPart(int equipPart) {
        return equipPart >= 0 && equipPart < EquipDefine.EquipPart_Num;
    }

    //发送穿戴装备成功消息
    private void sendWearSuccessInfo(Player player, Equip equip) {
        EquipMessage.ResEquipWearSuccess.Builder msg = EquipMessage.ResEquipWearSuccess.newBuilder();
        msg.setEquiped(Manager.backpackManager.manager().buildItemInfo(equip));//.buildItemInfo());
        MessageUtils.send_to_player(player, EquipMessage.ResEquipWearSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //卸下装备失败消息
    private void sendUnWearFailedInfo(Player player, int reason) {
        EquipMessage.ResEquipUnWearFailed.Builder msg = EquipMessage.ResEquipUnWearFailed.newBuilder();
        msg.setResult(reason);
        MessageUtils.send_to_player(player, EquipMessage.ResEquipUnWearFailed.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //卸下装备成功消息
    private void sendUnWearSuccessInfo(Player player, long equipId) {
        EquipMessage.ResEquipUnWearSuccess.Builder msg = EquipMessage.ResEquipUnWearSuccess.newBuilder();
        msg.setEquipId(equipId);
        MessageUtils.send_to_player(player, EquipMessage.ResEquipUnWearSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void handleEquipChange(Player player) {
        Manager.controlManager.operate(player, FunctionVariable.WornEquip, 0);
        Manager.controlManager.operate(player, FunctionVariable.EquipPositionQuality, 0);
        Manager.controlManager.operate(player, FunctionVariable.OrderEquip, 0);
        Manager.controlManager.operate(player, FunctionVariable.EquipStrengthenLevelMax, 0);
        Manager.controlManager.operate(player, FunctionVariable.EquipSuitNum, 0);
        Manager.controlManager.operate(player, FunctionVariable.EquipSuitLevel, 0);
        Manager.controlManager.operate(player, FunctionVariable.WornEquipPower, 0);
        Manager.controlManager.operate(player, FunctionVariable.ComposeEquip, 0);
        Manager.shopManager.limitShop().refresh(player);
    }

    private void sendWeaponMessage(Player player,Equip equip){
        int xianjiaPart30 = Manager.immortalEquipManager.manager().getImmFacadeForType(player,30);
        GodWeaponMessage.ResSceneGodWeaponEquipPart.Builder msg = GodWeaponMessage.ResSceneGodWeaponEquipPart.newBuilder();
        if (xianjiaPart30 <= 0) {
            msg.setModelId(equip.getItemModelId());
        } else {
            msg.setModelId(xianjiaPart30);
        }
        msg.setPart(1);
        msg.setPlayerid(player.getId());
        MessageUtils.send_to_roundPlayer(player, GodWeaponMessage.ResSceneGodWeaponEquipPart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        MessageUtils.send_to_player(player, GodWeaponMessage.ResSceneGodWeaponEquipPart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
