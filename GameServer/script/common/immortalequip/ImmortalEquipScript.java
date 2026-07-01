package common.immortalequip;

import com.data.*;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Equip_xianjia_exchange_Bean;
import com.data.bean.Cfg_Equip_xianjia_synthesis_Bean;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.immortalequip.manager.ImmortalEquipManager;
import com.game.immortalequip.script.IImmortalEquip;
import com.game.immortalequip.structs.ImmortalEquipPart;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ItemChangeAction;
import com.game.utils.MessageUtils;
import game.core.script.IScript;

import game.core.util.IDConfigUtil;
import game.message.ImmortalEquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by clc on 2020/2/12.
 */
public class ImmortalEquipScript implements IImmortalEquip, IScript {

    private static final Logger log = LogManager.getLogger(ImmortalEquipScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ImmortalEquipBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 上线初始化部位
     *
     * @param player
     */
    public void onLineInitPart(Player player) {

        int maxPartLen = ImmortalEquipManager.MaxPart - ImmortalEquipManager.MinPart;
        int baguaLen = 40;
        maxPartLen += baguaLen;
        if (player.getImmortalEquipPartLisit().size() < maxPartLen) {
            ConcurrentHashMap<Integer, ImmortalEquipPart> partlist = player.getImmortalEquipPartLisit();
            for (int i = ImmortalEquipManager.MinPart; i <= ImmortalEquipManager.MaxPart; i++) {
                if (!partlist.containsKey(i)){
                    ImmortalEquipPart immortalEquipPart = new ImmortalEquipPart();
                    immortalEquipPart.setPart(i);
                    immortalEquipPart.setEquip(null);
                    partlist.put(i, immortalEquipPart);
                }
            }
            //八卦部分
            for (int i = ImmortalEquipManager.baguaMinPart; i <= ImmortalEquipManager.baguaMaxPart; i++) {
                if (!partlist.containsKey(i)){
                    ImmortalEquipPart immortalEquipPart = new ImmortalEquipPart();
                    immortalEquipPart.setPart(i);
                    immortalEquipPart.setEquip(null);
                    partlist.put(i, immortalEquipPart);
                }
            }
            player.setImmortalEquipPartLisit(partlist);
        }

        ImmortalEquipMessage.ResOnlineInitImmortalEquip.Builder msg = ImmortalEquipMessage.ResOnlineInitImmortalEquip.newBuilder();
        for (ImmortalEquipPart immortalEquipPart : player.getImmortalEquipPartLisit().values()) {
            ImmortalEquipMessage.ImmortalEquipPart.Builder part = ImmortalEquipMessage.ImmortalEquipPart.newBuilder();
            part.setPart(immortalEquipPart.getPart());
            if (immortalEquipPart.getEquip() != null) {
                part.setEquip(Manager.backpackManager.manager().buildItemInfo(immortalEquipPart.getEquip()));
            }
            msg.addImmortalPartList(part.build());
        }
        for (Item item : player.getImmEquipItemList().values()) {
            msg.addImmBagEquip(Manager.backpackManager.manager().buildItemInfo(item).build());
        }
        MessageUtils.send_to_player(player, ImmortalEquipMessage.ResOnlineInitImmortalEquip.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }


    /**
     * 镶嵌 穿戴
     *
     * @param player
     * @param uid
     */
    public void ReqInlayImmortalEquip(Player player, long uid) {

        Equip equip = (Equip) Manager.backpackManager.manager().getItemById(player, uid);
        if (equip == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LIANQI_GEM_NEEDITEMNOTENOUGH);
            return;
        }
        Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (equip_bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, equip.getItemModelId() + "");
            return;
        }

        if (!getPartIsRight(equip_bean.getPart())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_EQUIP_PART_NOSAME);
            return;
        }

        ImmortalEquipPart immortalEquipPart = player.getImmortalEquipPartLisit().get(equip_bean.getPart());

        if (immortalEquipPart.getEquip() != null) {
            boolean isTrue = Manager.backpackManager.manager().addItem(player, immortalEquipPart.getEquip().getItemModelId(), 1, true,
                    0, ItemChangeReason.InlayImmortalEquip, immortalEquipPart.getEquip().getItemModelId());
            if (!isTrue) {
                Item itemquip = Item.createItem(immortalEquipPart.getEquip().getItemModelId(), 1, true);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, Collections.singletonList(itemquip), ItemChangeReason.InlayImmortalEquip, immortalEquipPart.getEquip().getItemModelId());
            }
        }
        Manager.backpackManager.manager().onRemoveItem(player, equip, 1, ItemChangeReason.InlayImmortalEquip, equip.getItemModelId());
        immortalEquipPart.setEquip(equip);

        ImmortalEquipMessage.ResInlayImmortalReuslt.Builder msg = ImmortalEquipMessage.ResInlayImmortalReuslt.newBuilder();
        ImmortalEquipMessage.ImmortalEquipPart.Builder part = ImmortalEquipMessage.ImmortalEquipPart.newBuilder();
        part.setPart(equip_bean.getPart());
        part.setEquip(Manager.backpackManager.manager().buildItemInfo(equip));
        msg.setImmortalPart(part.build());
        MessageUtils.send_to_player(player, ImmortalEquipMessage.ResInlayImmortalReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.ImmortalEquip);

        Manager.controlManager.operate(player, FunctionVariable.GetEquipId, equip.getItemModelId());

        setDefaultPartType(player,immortalEquipPart);
        synImmortalEquipInfoToTother(player);
        Manager.mapManager.manager().SynMeToOther(player);
        //装备BI
        Manager.biManager.getScript().biEquip(player, 1, GrowType.immortalEquip_wear.getType(), equip_bean.getPart(), equip_bean.getId(), equip_bean.getName(), equip_bean.getDiamond_Number(), equip_bean.getGrade(), equip_bean.getQuality(), 0, equip_bean.getBind(), 0, 0, 0, 0, 0, 0);
    }

    private void setDefaultPartType(Player player , ImmortalEquipPart immortalEquipPart){
        //每套长度14所以 取余
        int partType = ((immortalEquipPart.getPart()-30)%14) + 30;

        if (partType > Manager.immortalEquipManager.FacadePartTypeMax ||
                partType <  Manager.immortalEquipManager.FacadePartTypeMin ){
            return ;
        }
        if (!player.getImmortalEquipFacadeMap().containsKey(partType)){
            player.getImmortalEquipFacadeMap().put(partType, immortalEquipPart.getEquip().getItemModelId());
        }else if (player.getImmortalEquipFacadeMap().get(partType) <=0){
            player.getImmortalEquipFacadeMap().put(partType, immortalEquipPart.getEquip().getItemModelId());
        }
    }

    /**
     * 合成
     *
     * @param player
     * @param part
     */
    public void ReqCompoundImmortal(Player player, int part) {

        ImmortalEquipPart immortalEquipPart = player.getImmortalEquipPartLisit().get(part);
        if (immortalEquipPart == null) {
            log.info("immortalEquipPart null" + part);
            return;
        }
        if (immortalEquipPart.getEquip() == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.HolyPartNotEquip);
            return;
        }

        Equip partEquip = immortalEquipPart.getEquip();
        Cfg_Equip_xianjia_synthesis_Bean xianjia_synthesis_bean =
                CfgManager.getCfg_Equip_xianjia_synthesis_Container().getValueByKey(partEquip.getItemModelId());
        if (xianjia_synthesis_bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, partEquip.getItemModelId() + "");
            return;
        }

        ReadIntegerArray joinitem = xianjia_synthesis_bean.getJoin_item();

        if (joinitem == null || joinitem.size() <= 0) {
            return;
        }

        int count = getEquipNum(player, joinitem.get(0));
        if (count < joinitem.get(1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LIANQI_GEM_NEEDITEMNOTENOUGH);
            return;
        }

        if (!remImmEquip(player, joinitem.get(0), joinitem.get(1), ItemChangeReason.CompoundImmortalEquip)) {
            log.info("扣除材料失败");
            return;
        }
        Item newequip = Item.createItem(xianjia_synthesis_bean.getEquip_ID(), 1, true);
        if (newequip == null) {
            log.info("创建失败  " + xianjia_synthesis_bean.getEquip_ID());
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItem(player, newequip, ItemChangeReason.CompoundImmortalEquip, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                    , MessageString.GetAwardNotEnoughSpaceContent, Collections.singletonList(newequip), ItemChangeReason.CompoundImmortalEquip, actionId);
            return;
        }

        immortalEquipPart.setEquip(null);

        //装备BI
        Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(newequip.getItemModelId());
        Manager.biManager.getScript().biEquip(player, GrowType.immortalEquip_synthesis.getAct_type(), GrowType.immortalEquip_synthesis.getType(), equip_bean.getPart(), equip_bean.getId(), equip_bean.getName(), equip_bean.getDiamond_Number(), equip_bean.getGrade(), equip_bean.getQuality(), 0, equip_bean.getBind(), 0, 0, 0, 0, 0, 0);

        //穿上
        ReqInlayImmortalEquip(player, newequip.getId());
    }


    /**
     * 分解
     *
     * @param player
     * @param uid
     */
    public void ReqResolveImmortal(Player player, long uid) {

        Equip equip = (Equip) Manager.backpackManager.manager().getItemById(player, uid);
        if (equip == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LIANQI_GEM_NEEDITEMNOTENOUGH);
            return;
        }

        Cfg_Equip_Bean equip_bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (equip_bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, equip.getItemModelId() + "");
            return;
        }
        if (!getPartIsRight(equip_bean.getPart())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_EQUIP_PART_NOSAME);
            return;
        }
        if (equip_bean.getPrice1() == null || equip_bean.getPrice1().size() <= 0) {
            log.info("equip_bean.getPrice1() == null  " + equip_bean.getId());
            return;
        }

        int itemid = equip_bean.getPrice1().get(0).get(0);
        long num = equip_bean.getPrice1().get(0).get(1);
        List<Item> items = Item.createItems(itemid, num, true);
        if (items == null) {
            log.info("createItem fail " + itemid);
            return;
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player, equip, equip.getNum(), ItemChangeReason.ResolveImmortalEquipDec, equip.getItemModelId())) {
            log.info("onRemoveItem fail " + equip.getItemModelId());
            return;
        }
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.ResolveImmortalEquipGet, itemid);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1,
                    MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.ResolveImmortalEquipGet, itemid);
        }
    }

    private boolean getPartIsRight(int part){
        return (part>= ImmortalEquipManager.MinPart && part<=ImmortalEquipManager.MaxPart)
                || (part>= ImmortalEquipManager.baguaMinPart && part<=ImmortalEquipManager.baguaMaxPart);
    }

    /**
     * 兑换
     *
     * @param player
     * @param modelID
     */
    public void ReqExchangeImmortal(Player player, int modelID) {

        Cfg_Equip_xianjia_exchange_Bean exchange_bean = CfgManager.getCfg_Equip_xianjia_exchange_Container().getValueByKey(modelID);
        if (exchange_bean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, modelID + "");
            return;
        }
        int count = Manager.backpackManager.manager().getItemNum(player, exchange_bean.getNeeditem().get(0));
        if (count < exchange_bean.getNeeditem().get(1)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LIANQI_GEM_NEEDITEMNOTENOUGH);
            return;
        }
        int needId = exchange_bean.getNeeditem().get(0);
        if (!Manager.backpackManager.manager().onRemoveItem(player, needId, exchange_bean.getNeeditem().get(1), ItemChangeReason.ExchangeImmortalEquipDec, needId)) {
            log.info("扣除材料失败");
            return;
        }

        Item newequip = Item.createItem(exchange_bean.getEquipid(), 1, true);
        if (newequip == null) {
            log.info("创建失败  " + exchange_bean.getEquipid());
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItem(player, newequip, ItemChangeReason.ExchangeImmortalEquipGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                    , MessageString.GetAwardNotEnoughSpaceContent, Collections.singletonList(newequip), ItemChangeReason.ExchangeImmortalEquipGet, actionId);
        }
    }

    private void synImmortalEquipInfoToTother(Player player) {
        ImmortalEquipMessage.ResSynImmortalEquipInfo.Builder msg = ImmortalEquipMessage.ResSynImmortalEquipInfo.newBuilder();
        //2020/3/9临时需求普通武器切换时也要更新外观
        int weaponId = getImmFacadeForType(player,30);
        if (weaponId <= 0) {
            Equip weapon = Manager.equipManager.getEquipByType(player, 1);
            if (weapon != null) {
                weaponId = weapon.getItemModelId();
            }
        }
        msg.setPart30(weaponId);
        msg.setPart31(getImmFacadeForType(player,31));
        msg.setPart32(getImmFacadeForType(player,32));
        msg.setPart33(getImmFacadeForType(player,33));
        msg.setPlayerId(player.getId());
        MessageUtils.send_to_roundPlayer(player, ImmortalEquipMessage.ResSynImmortalEquipInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    /**
     * 切换仙甲外观
     * @param player
     * @param partType
     * @param part
     */
    public void onReqChangeImmEquipAppearance(Player player,int partType,int part){
        if (partType > Manager.immortalEquipManager.FacadePartTypeMax ||
                partType <  Manager.immortalEquipManager.FacadePartTypeMin ){
            log.error("切换部位类型错误  {}",partType);
            return;
        }

        if ( part >  Manager.immortalEquipManager.MaxPart  ||  part <Manager.immortalEquipManager.MinPart){
            log.error("切换部位错误  {}",partType);
            return;
        }

        ImmortalEquipPart immortalEquipPart  = player.getImmortalEquipPartLisit().get(part);
        int modleID = immortalEquipPart == null || immortalEquipPart.getEquip() == null?0:immortalEquipPart.getEquip().getItemModelId();
        player.getImmortalEquipFacadeMap().put(partType,modleID);
        synImmortalEquipInfoToTother(player);

    }

    public int getImmFacadeForType(Player player,int type){
        if (type > Manager.immortalEquipManager.FacadePartTypeMax ||
                type <  Manager.immortalEquipManager.FacadePartTypeMin ){
            log.error("获取类型越界  {}",type);
            return 0;
        }
        ImmortalEquipPart immortalEquipPart  =  player.getImmortalEquipPartLisit().get(type);
        int id =  immortalEquipPart != null && immortalEquipPart.getEquip() != null?immortalEquipPart.getEquip().getItemModelId():0;
        if (!player.getImmortalEquipFacadeMap().containsKey(type)){
            player.getImmortalEquipFacadeMap().put(type,id);
        }else {
            if (player.getImmortalEquipFacadeMap().get(type) <=0){
                player.getImmortalEquipFacadeMap().put(type,id);
            }
        }
        int modleID  =  player.getImmortalEquipFacadeMap().get(type);
        return modleID;
    }



    public int getXianjiaPart30(Player player) {
        ImmortalEquipPart immortalEquipPart = player.getImmortalEquipPartLisit().get(30);
        if (immortalEquipPart != null && immortalEquipPart.getEquip() != null) {
            return immortalEquipPart.getEquip().getItemModelId();
        }
        return 0;
    }

    public int getXianjiaPart31(Player player) {
        ImmortalEquipPart immortalEquipPart = player.getImmortalEquipPartLisit().get(31);
        if (immortalEquipPart != null && immortalEquipPart.getEquip() != null) {
            return immortalEquipPart.getEquip().getItemModelId();
        }
        return 0;
    }

    public int getXianjiaPart32(Player player) {
        ImmortalEquipPart immortalEquipPart = player.getImmortalEquipPartLisit().get(32);
        if (immortalEquipPart != null && immortalEquipPart.getEquip() != null) {
            return immortalEquipPart.getEquip().getItemModelId();
        }
        return 0;
    }

    public int getXianjiaPart33(Player player) {
        ImmortalEquipPart immortalEquipPart = player.getImmortalEquipPartLisit().get(33);
        if (immortalEquipPart != null && immortalEquipPart.getEquip() != null) {
            return immortalEquipPart.getEquip().getItemModelId();
        }
        return 0;
    }


    public boolean canAddImmEquipBag(Player player, List<Item> itemList) {
        if (itemList == null || itemList.size() <= 0) {
            log.error("传入的道具为空！");
            return false;
        }
        if (player.getImmEquipItemList().size() + itemList.size() > Global.Xianjia_bag_num) {
            return false;
        }
        return true;
    }

    public boolean addImmEquip(Player player, Item item, int reason) {

        if (item == null) {
            log.error("传入的道具为空！");
            return false;
        }
        if (player.getImmEquipItemList().size() > Global.Xianjia_bag_num) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return false;
        }
        player.getImmEquipItemList().put(item.getId(), item);
        sendImmEquipAdd(player, item, reason);
        return true;
    }


    private boolean remImmEquip(Player player, int itemID, int num, int reason) {
        List<Item> remItemlist = new ArrayList<>();
        for (Item item : player.getImmEquipItemList().values()) {
            if (num <= 0) {
                break;
            }
            if (item.getItemModelId() == itemID) {
                remItemlist.add(item);
                num -= 1;
            }
        }
        for (Item item : remItemlist) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, item, item.getNum(), reason, 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean delImmEquip(Player player, Item item, int reason) {

        if (item == null) {
            log.error("传入的道具为空！");
            return false;
        }
        if (player.getImmEquipItemList().containsKey(item.getId())) {
            player.getImmEquipItemList().remove(item.getId());
            sendImmEquipDel(player, item.getId(), reason);
            return true;
        }
        return false;
    }

    private void sendImmEquipAdd(Player player, Item item, int reason) {
        ImmortalEquipMessage.ResAddImmortalEquip.Builder builder = ImmortalEquipMessage.ResAddImmortalEquip.newBuilder();
        builder.addAddEquips(Manager.backpackManager.manager().buildItemInfo(item).build());
        builder.setReason(reason);
        MessageUtils.send_to_player(player, ImmortalEquipMessage.ResAddImmortalEquip.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void sendImmEquipDel(Player player, long uid, int reason) {
        ImmortalEquipMessage.ResDeleteImmortalEquip.Builder builder = ImmortalEquipMessage.ResDeleteImmortalEquip.newBuilder();
        builder.addDeleteEquipIds(uid);
        builder.setReason(reason);
        MessageUtils.send_to_player(player, ImmortalEquipMessage.ResDeleteImmortalEquip.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private int getEquipNum(Player player, int modelID) {
        int num = 0;
        for (Item item : player.getImmEquipItemList().values()) {
            if (item.getItemModelId() == modelID) {
                num += 1;
            }
        }
        return num;
    }
}
