package common.immortalsoul;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Immortal_soul_attribute_Container;
import com.data.container.Cfg_Immortal_soul_core_Container;
import com.data.container.Cfg_Immortal_soul_core_att_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;

import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.immortalsoul.structs.Immortalsoul;
import com.game.immortalsoul.structs.ImmortalsoulDefine;
import com.game.immortalsoul.script.IImmortalSoul;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;

import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.data.ItemChangeReason;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.ImmortalSoulMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author admin
 */
public class ImmortalSoulScript implements IImmortalSoul, IScript {

    private static final Logger log = LogManager.getLogger(ImmortalSoulScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ImmortalSoulBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 上线初始化
     */
    @Override
    public void online(Player player) {

        //灵魄功能开启等级
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.FlySwordSprite)) {
            return;
        }
        Iterator<Immortalsoul> iter = player.getImmortalsoul().values().iterator();
        ImmortalSoulMessage.ResAllImmortalSoul.Builder msg = ImmortalSoulMessage.ResAllImmortalSoul.newBuilder();
        Immortalsoul soul = null;
        while (iter.hasNext()) {
            ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
            soul = iter.next();
            mSoul.setUid(soul.getUid());
            mSoul.setItemId(soul.getItemID());
            mSoul.setExp(soul.getExp());
            mSoul.setLevel(soul.getLevel());
            mSoul.setLocation(soul.getLocation());
            msg.addSoulBagList(mSoul);
        }
        iter = player.getEquipSouls().values().iterator();
        while (iter.hasNext()) {
            ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
            soul = iter.next();
            mSoul.setUid(soul.getUid());
            mSoul.setItemId(soul.getItemID());
            mSoul.setExp(soul.getExp());
            mSoul.setLevel(soul.getLevel());
            mSoul.setLocation(soul.getLocation());
            msg.addSoulEquipmentList(mSoul);
        }
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResAllImmortalSoul.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //发送灵魂核心信息
        ImmortalSoulMessage.ResSoulCore.Builder soulCore = ImmortalSoulMessage.ResSoulCore.newBuilder();
        for(Map.Entry<Integer, Integer> data : player.getImmortalsoulCores().entrySet()){
            ImmortalSoulMessage.SoulCoreInfo.Builder info = ImmortalSoulMessage.SoulCoreInfo.newBuilder();
            info.setType(data.getKey());
            info.setCore(data.getValue());
            soulCore.addInfo(info);
        }
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResSoulCore.MsgID.eMsgID_VALUE, soulCore.build().toByteArray());
    }

    /**
     * 合成
     */
    @Override
    public  void  compoundSoul(Player player,int itemid){

        //仙魄合成功能开启等级
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.XianPoSynthetic)) {
            return;
        }

        Cfg_Immortal_soul_attribute_Bean makeBean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(itemid);
        if(makeBean ==null){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoul_NotFindExcel,itemid+"");
            return;
        }
        //判断玩家卷层数是否达到
        ReadArray<Integer> openlev = makeBean.getExchange_conditions();
        int curLevel =  Manager.controlManager.deal().getFuncProgress(player, openlev);
        if (curLevel < openlev.get(1)){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulAchieveNeedLv,curLevel +"");
            return;
        }
        Cfg_Immortal_soul_synthesis_Bean synthesis_bean = CfgManager.getCfg_Immortal_soul_synthesis_Container().getValueByKey(itemid);
        if(synthesis_bean ==null){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoul_NotFindExcel,itemid+"");
            return;
        }
        //检测材料是否足够
        ReadIntegerArrayEs material1 = synthesis_bean.getMaterial1();
        List<Immortalsoul> needMatrList = new ArrayList<>();
        for ( int i = 0;i<material1.size();i++){
             int itemID  =   material1.get(i).get(0);
             int num = 1;
             if(material1.get(i).size() > 1){
                 num = material1.get(i).get(1);
             }
             Immortalsoul next;
             Iterator<Immortalsoul> iter = player.getImmortalsoul().values().iterator();
             while (iter.hasNext()) {
                  next = iter.next();
                  if (next.getItemID() == itemID && !needMatrList.contains(next)){
                      needMatrList.add(next);
                      num--;
                      if(num <= 0){
                          break;
                      }
                  }
             }
        }
        long actionId = IDConfigUtil.getLogId();
        ReadIntegerArrayEs material2 = synthesis_bean.getMaterial2();
        boolean isEnough = Manager.backpackManager.manager().canDeleteItemNum(player, material2.get(0).get(0),  material2.get(0).get(1));
        if (needMatrList.size() >= material1.size()-1 && isEnough){

            //检测全部材料都足够，扣材料
            List<Long> clientNeedDec = new ArrayList<>();
            int add = 0;
            for (Immortalsoul s:needMatrList){
                 add  +=(s.getCacheExp() - makeBean.getExp());
                 player.getImmortalsoul().remove(s.getUid());
                clientNeedDec.add(s.getUid());
                //删除物品的日志
                Manager.backpackManager.manager().writeItemLogAndBI(player,s.getNum(), 0, s, ItemChangeReason.ImmortalResolveDec, actionId);
            }
            if (add >0){
                //合成后返还经验
                Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.SoulCoinType_1, add,
                        ItemChangeReason.ImmortalResolveGet,actionId);
            }
            Manager.backpackManager.manager().onRemoveItem(player, material2.get(0).get(0),  material2.get(0).get(1),
                    ItemChangeReason.ImmortalcompoundgetDec, actionId);

            //合成概率计算，策划说的写死万分比
            Random rand = new Random();
            int randnum = rand.nextInt(ImmortalsoulDefine.compoundCatio) + 1;
            if (randnum <= synthesis_bean.getProbability()){
                Immortalsoul newSoul =  (Immortalsoul)Item.createItem(itemid,1,true);
                if (newSoul  != null){
                    if ( !Manager.backpackManager.manager().addItem(player,newSoul,ItemChangeReason.ImmortalexchangeGet, actionId)){
                        Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                                MessageString.System, MessageString.ImmortalSoulBagNotEnough
                                ,  Collections.singletonList(newSoul),ItemChangeReason.ImmortalexchangeGet, actionId);
                    }
                    sendCompoundResult(player,newSoul,clientNeedDec,true);
                    MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.ImmSoulcompoundSuc);
                }
            }
            else {
                sendCompoundResult(player,null,clientNeedDec,false);
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulcompoundFail);
                return;
            }
        }
        else {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulcompound_NoMaterial);
           return;
        }
    }

    private void sendCompoundResult(Player player, Immortalsoul newSoul, List<Long> clientNeedDec,boolean isSuc){

        ImmortalSoulMessage.ResCompoundSoulReuslt.Builder msg = ImmortalSoulMessage.ResCompoundSoulReuslt.newBuilder();
        ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
        msg.setIsSucceed(isSuc);
        if (isSuc){
            mSoul.setUid(newSoul.getUid());
            mSoul.setItemId(newSoul.getItemID());
            mSoul.setExp(newSoul.getCacheExp());
            mSoul.setLocation(newSoul.getLocation());
            mSoul.setLevel(newSoul.getLevel());
            msg.setSoul(mSoul);
        }
        msg.addAllDeleteUid(clientNeedDec);
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResCompoundSoulReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    public void gmAddSoul(Player player,int itemID,boolean tellClient,int reason){
        Cfg_Immortal_soul_attribute_Bean makeBean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(itemID);
        if(makeBean ==null){
            log.info("未找到该 itemid   "+ itemID);
            return;
        }
        if ( player.getImmortalsoul().size()  >= Global.Immortal_soul_backpack_max)
        {

            BagIsFullresolveImmSoul(player,itemID);
            MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.ImmBagIsFullSoResolve);
            return;
        }
        Immortalsoul newSoul = new Immortalsoul();
        newSoul.setUid( newSoul.getId());
        newSoul.setLevel(1);
        newSoul.setItemID(itemID);
        newSoul.setQuality(makeBean.getQuality());
        Cfg_Immortal_soul_exp_Bean nextLevelBean = CfgManager.getCfg_Immortal_soul_exp_Container().getValueByKey(1);
        ReadIntegerArray  needExp = getNeedExp(newSoul,nextLevelBean);
        if (needExp !=null && needExp.size()>0){
            newSoul.setExp(needExp.get(0));
            newSoul.setCacheExp(makeBean.getExp());
        }
        newSoul.setLocation(0);
        player.getImmortalsoul().put(newSoul.getUid(),newSoul);
        sendExchangeResult(player,newSoul,reason);
        if(tellClient){
            MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.GetSoulSucc,makeBean.getName());
        }
    }

    private void BagIsFullresolveImmSoul(Player player,int itemID)
    {
        Cfg_Immortal_soul_attribute_Bean makeBean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(itemID);
        if(makeBean ==null){
            log.info("未找到该 itemid   "+ itemID);
            return;
        }
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.SoulCoinType_1, makeBean.getExp(), ItemChangeReason.ImmortalResolveGet, itemID);
    }

    /**
     * 兑换
     */
    @Override
    public  void  exchangeSoul(Player player,int itemid,int num){

        //仙魄兑换
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.XianPoExchange)) {
            return;
        }
        if (num > 50) {//策划要求最大兑换不能超过50，且 喊写死不配置表
            log.error("兑换超过最大数量上限 " + num );
            return;
        }
        Cfg_Immortal_soul_attribute_Bean makeBean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(itemid);
        if(makeBean ==null){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoul_NotFindExcel,itemid+"");
            return;
        }
        //判断玩家卷层数是否达到
        ReadArray<Integer> openlev = makeBean.getExchange_conditions();
        if (openlev.size()<0) {
            log.info("所有获取所需爬塔层数没有配置,不能兑换" );
            return;
        }
        int curLevel =  Manager.controlManager.deal().getFuncProgress(player,openlev);
        if (curLevel < openlev.get(1)){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulAchieveNeedLv,curLevel +"");
            return;
        }
        if ( makeBean.getExchange_Consumption().size()<2) {
            log.info("该物品不能兑换   "+ itemid);
            return;
        }
        int needNum = makeBean.getExchange_Consumption().get(1) * num;
        if (Manager.currencyManager.manager().canDecItemCoin(player, needNum, makeBean.getExchange_Consumption().get(0))) {
            Manager.currencyManager.manager().onDecItemCoin(player,   needNum,
                    ItemChangeReason.ImmortalResolveDec, itemid, makeBean.getExchange_Consumption().get(0));
            List<Item> itemList = Item.createItems(itemid, num, true);
            long actionId = IDConfigUtil.getLogId();
            if ( !Manager.backpackManager.manager().addItems(player,itemList,ItemChangeReason.ImmortalexchangeGet, actionId)){
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System
                        , MessageString.ImmortalSoulBagNotEnough, itemList,ItemChangeReason.ImmortalexchangeGet, actionId);
            }
            MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.ExchangeSoulSucc, ServerStr.getChatTableName(makeBean.getName()));
        }
        else {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulExchangeFail);
        }
    }

    private void sendExchangeResult(Player player,Immortalsoul newSoul,int reason){
        ImmortalSoulMessage.ResExchangeSoulReuslt.Builder msg = ImmortalSoulMessage.ResExchangeSoulReuslt.newBuilder();
        ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
        msg.setIsSucceed(true);
        mSoul.setUid(newSoul.getUid());
        mSoul.setItemId(newSoul.getItemID());
        mSoul.setExp(newSoul.getCacheExp());
        mSoul.setLocation(newSoul.getLocation());
        mSoul.setLevel(newSoul.getLevel());
        msg.setSoul(mSoul);
        msg.setReason(reason);
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResExchangeSoulReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 拆解
     * @param player
     * @param uid
     */
    public void onDismountingSoul(Player player,long uid){

        if (!player.getImmortalsoul().containsKey(uid)){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulResolveFail,uid +"");
            return;
        }
        Immortalsoul soul =  player.getImmortalsoul().get(uid);
        long actionId = IDConfigUtil.getLogId();
        Cfg_Immortal_soul_synthesis_Bean bean = CfgManager.getCfg_Immortal_soul_synthesis_Container().getValueByKey(soul.getItemID());
        if (bean == null){
            log.info("Cfg_Immortal_soul_synthesis_Bean is null :   "+ soul.getItemID());
            return;
        }
        List<Item> items_1 = new ArrayList<>();
        int count = 0;
        for (ReadArray<Integer> readArray :  bean.getMaterial1().getValuees()){
            Immortalsoul item =  (Immortalsoul)createSoul( readArray.get(0));
            if (count == 0){
                item.setLevel( soul.getLevel() );
            }
            items_1.add(item);
            count++;
        }
        List<Item> items_2 = Item.createItems(bean.getMaterial2());

        if ( !Manager.backpackManager.manager().addItems(player,items_1,ItemChangeReason.ImmortalDismountingGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.ImmortalSoulBagNotEnough
                    ,  items_1,ItemChangeReason.ImmortalDismountingGet, actionId);
        }

        if ( !Manager.backpackManager.manager().addItems(player,items_2,ItemChangeReason.ImmortalDismountingGet, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell
                    ,  items_2,ItemChangeReason.ImmortalDismountingGet, actionId);
        }

        player.getImmortalsoul().remove(uid);
        ImmortalSoulMessage.ResDismountingSoulReuslt.Builder msg = ImmortalSoulMessage.ResDismountingSoulReuslt.newBuilder();
        msg.setIsSucceed(true);
        msg.setUid(uid);
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResDismountingSoulReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 分解
     */
    @Override
    public  void  resolveSoul(Player player,List<Long> uids){

        //仙魄分解
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.XianPoDecomposition)) {
            return;
        }
        if (uids.size()<=0){
            log.info("分解长度不能为   "+ uids.size());
            return;
        }
        for (Long uid:uids){
            if (!player.getImmortalsoul().containsKey(uid)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulResolveFail,uid +"");
                return;
            }
        }
        long actionId = IDConfigUtil.getLogId();
        int addAllExp = 0;
        for (Long uid:uids){
            Immortalsoul item = player.getImmortalsoul().get(uid);
            int Exp = item.getCacheExp();
            addAllExp +=Exp;
            player.getImmortalsoul().remove(uid);
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            if (model == null) {
                continue;
            }
            //道具删除日志
            Manager.backpackManager.manager().writeItemLogAndBI(player,item.getNum(), 0, item, ItemChangeReason.ImmortalResolveDec, actionId);
        }
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.SoulCoinType_1, addAllExp, ItemChangeReason.ImmortalResolveGet, actionId);
        MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.ResolveSoulSucc,addAllExp +"");
        sendResolveResult(player,uids);
    }
    private void sendResolveResult(Player player, List<Long> uids){
        ImmortalSoulMessage.ResResolveSoulReuslt.Builder msg = ImmortalSoulMessage.ResResolveSoulReuslt.newBuilder();
        msg.setIsSucceed(true);
        msg.addAllUids(uids);
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResResolveSoulReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 升级
     */
 @Override
  public  void  soulLevelUp(Player player,int pos){

     //if (pos>ImmortalsoulDefine.Location_Max){
     //    MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulLevelUpFail_notFind,pos +"");
     //    return;
     //}
     if( !player.getEquipSouls().containsKey(pos)) {
         MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulLevelUpFail_notFind,pos +"");
         return;
     }
     Immortalsoul soulLevelup = player.getEquipSouls().get(pos);
     ReadIntegerArray needExp = null;
     if (soulLevelup.getExp() > 0){
         if (Manager.currencyManager.manager().canDecItemCoin(player,  soulLevelup.getExp(), ItemCoinType.SoulCoinType_1)) {
             Manager.currencyManager.manager().onDecItemCoin(player,   soulLevelup.getExp(),
                     ItemChangeReason.ImmortalLevelUpDec, soulLevelup.getUid(), ItemCoinType.SoulCoinType_1);

             soulLevelup.setLevel(soulLevelup.getLevel()+1);
             Cfg_Immortal_soul_exp_Bean nextLevelBean = CfgManager.getCfg_Immortal_soul_exp_Container().getValueByKey(soulLevelup.getLevel());
             needExp = getNeedExp(soulLevelup,nextLevelBean);
             Cfg_Immortal_soul_attribute_Bean makeBean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soulLevelup.getItemID());
             if ( needExp.get(0) > 0){
                 soulLevelup.setExp(needExp.get(0));
             }else {
                 soulLevelup.setExp(0);
             }
             soulLevelup.setCacheExp(makeBean.getExp() + needExp.get(1));
             player.getEquipSouls().put(pos,soulLevelup);
             //发消息
             sendLevelUpResult(player,soulLevelup);

             updateCore(player, pos);

             Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Immortalsoul, PlayerAttributeType.EQUIP);
             Manager.controlManager.operate(player, FunctionVariable.ImmortalSou, 0);


             RoleGrowLog.create(player, GrowType.soul_level_up, 0, pos, soulLevelup.getLevel(), soulLevelup.getLevel() + 1, null);
         }else {
             MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulLevelUpFail_ExpNotEnough,soulLevelup.getExp() +"");
             Manager.backpackManager.manager().sendItemNotEnough(player, ItemCoinType.SoulCoinType_1);
             return;
         }
     }else {
         MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulLevelUpFail_LevelMax,soulLevelup.getLevel() +"");
         return;
     }
  }
  private void sendLevelUpResult(Player player,Immortalsoul soulLevelup){
      ImmortalSoulMessage.ResUpSoulReuslt.Builder msg = ImmortalSoulMessage.ResUpSoulReuslt.newBuilder();
      ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
      msg.setIsSucceed(true);
      mSoul.setUid(soulLevelup.getUid());
      mSoul.setItemId(soulLevelup.getItemID());
      mSoul.setExp(soulLevelup.getCacheExp());
      mSoul.setLevel(soulLevelup.getLevel());
      mSoul.setLocation(soulLevelup.getLocation());
      msg.setSoul(mSoul);
      MessageUtils.send_to_player(player, ImmortalSoulMessage.ResUpSoulReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
  }


    /**
     * 脱下仙魄
     */
  @Override
  public  void  getoffSoul(Player player,int pos){
      if ( player.getImmortalsoul().size()  >= Global.Immortal_soul_backpack_max) {
          MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmortalSoulBagNotEnough);
          return;
      }
      Immortalsoul putoff =  player.getEquipSouls().get(pos);
      if (putoff==null){
          MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulGetoffFail_NoSoul,pos +"");
          return;
      }
      putoff.setLocation(0);
      player.getImmortalsoul().put(putoff.getUid(),putoff);
      player.getEquipSouls().remove(pos);

      //发消息
      sendGetOffResult(player,putoff);
      Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Immortalsoul,PlayerAttributeType.EQUIP);
      Manager.controlManager.operate(player, FunctionVariable.ImmortalSou, 0);
  }
  private  void sendGetOffResult(Player player, Immortalsoul putoff){
      //发消息
      ImmortalSoulMessage.ResGetOffReuslt.Builder msg = ImmortalSoulMessage.ResGetOffReuslt.newBuilder();
      ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
      msg.setIsSucceed(true);
      mSoul.setUid(putoff.getUid());
      mSoul.setItemId(putoff.getItemID());
      mSoul.setExp(putoff.getExp());
      mSoul.setLevel(putoff.getLevel());
      mSoul.setLocation(putoff.getLocation());
      msg.setSoul(mSoul);
      MessageUtils.send_to_player(player, ImmortalSoulMessage.ResGetOffReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());
  }

    /**
     * 镶嵌
     */
    @Override
    public void reqInlaySoul(Player player, long uid, int pos) {

        //仙魄分解
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.XianPoInlay)) {
            return;
        }
        Immortalsoul soul =  player.getImmortalsoul().get(uid);
        boolean isTrue =  checkIsInPackage(player, soul,uid);
        isTrue = checkGridIsOpen(isTrue,player ,pos);
        isTrue =  checkIsExclusive(isTrue,player, soul,pos);
        //isTrue = checkType(isTrue,player, soul,pos);
        if (isTrue){
            updateInlay(player,soul,pos);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Immortalsoul,PlayerAttributeType.EQUIP);
            Manager.controlManager.operate(player, FunctionVariable.ImmortalSou, 0);
        }
    }

  // boolean checkType(boolean isTrue,Player player,Immortalsoul soul, int pos)
  // {
  //     if (!isTrue)
  //         return false;
  //     Cfg_Immortal_soul_attribute_Bean bean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soul.getItemID());
  //     boolean isCanInlay = true;
  //     for (Immortalsoul  im : player.getEquipSouls().values()) {
  //         Cfg_Immortal_soul_attribute_Bean bean_Type = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(im.getItemID());
  //         if (bean_Type!=null) {
  //             if (im.getLocation() == pos){
  //                continue;
  //             }
  //             if (bean_Type.getType2() == bean.getType2()) {
  //                 MessageUtils.notify_player(player, Notify.ERROR, MessageString.InlaySoulTheSameType,im.getLocation() +""  ,bean.getType2()+"" );
  //                 isCanInlay = false;
  //                 break;
  //             }
  //         }
  //     }
  //     return isCanInlay;
  // }

    //判断是否互斥
    private boolean checkIsExclusive(boolean isTrue,Player player,Immortalsoul soul, int pos){
        if (!isTrue)
            return false;

        int gridType =   getGridType(pos);
        Cfg_Immortal_soul_attribute_Bean bean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(soul.getItemID());
        if (bean.getExclusive_ID() <=0){
            log.info("经验仙魄不能镶嵌   "+bean.getId());
            return false;
        }
        Iterator<Immortalsoul> iter = player.getEquipSouls().values().iterator();
        while (iter.hasNext()) {
            Immortalsoul immortalsoul = iter.next();
            if (getGridType(immortalsoul.getLocation()) != gridType)
                continue;
            if (immortalsoul.getLocation() == pos)
                continue;

            if (immortalsoul.getType() == soul.getType()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulInlayFall_Exclusive, immortalsoul.getItemID() + "");
                return false;
            }
        }
        return true;
    }
    private boolean checkIsInPackage(Player player,Immortalsoul soul,long uid){
        if (soul == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulInlayFall_NotFind,uid +"");
            return false;
        }
        return  true;
    }

    private boolean checkGridIsOpen(boolean isTrue,Player player,int pos){
        if (!isTrue)
            return false;
        //判断格子是否开放
        Cfg_Immortal_soul_iattice_Bean iattice = CfgManager.getCfg_Immortal_soul_iattice_Container().getValueByKey(pos);
        if (iattice!=null){
            ReadArray<Integer> openlev = iattice.getCondition().get(0);
            int curLevel =  Manager.controlManager.deal().getFuncProgress(player,openlev);
            if (curLevel < openlev.get(1)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulInlayFall_GridNotOpen,curLevel +"");
                return false;
            }
        }else{
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulInlayFall_GridNotFind,pos +"");
            return false;
        }
        return true;
    }

    //发送镶嵌结果给客户端
    private void updateInlay(Player player,Immortalsoul soul, int pos){
        Immortalsoul putoff =  player.getEquipSouls().get(pos);
        player.getImmortalsoul().remove(soul.getUid());
        ArrayList<Immortalsoul> result = new ArrayList<>();
        if (putoff !=null){
            player.getEquipSouls().remove(pos);
            putoff.setLocation(0);
            player.getImmortalsoul().put(putoff.getUid(),putoff);
            result.add(putoff);
        }
        soul.setLocation(pos);
        player.getEquipSouls().put(pos,soul);
        result.add(soul);

        ImmortalSoulMessage.ResInlaySoulReuslt.Builder msg = ImmortalSoulMessage.ResInlaySoulReuslt.newBuilder();
        msg.setIsSucceed(true);
        for (Immortalsoul il:result){
            ImmortalSoulMessage.immortalSoul.Builder mSoul = ImmortalSoulMessage.immortalSoul.newBuilder();
            mSoul.setUid(il.getUid());
            mSoul.setItemId(il.getItemID());
            mSoul.setExp(il.getExp());
            mSoul.setLevel(il.getLevel());
            mSoul.setLocation(il.getLocation());
            msg.addSoulInlayList(mSoul);
        }
        MessageUtils.send_to_player(player, ImmortalSoulMessage.ResInlaySoulReuslt.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        updateCore(player, pos);

        RoleGrowLog.create(player, GrowType.soul_wear, 0, pos, putoff == null ? 0 : putoff.getItemID(), soul.getItemID(), null);
    }

    /**
     * 灵魂核心刷新
     * @param player
     * @param pos
     */
    private void updateCore(Player player, int pos) {
        int type = pos/100;
        Integer core = player.getImmortalsoulCores().get(type);
        if(core == null || core == 0){//解锁判断
            int[] stars = new int[7];
            int[] qualitys = new int[7];
            int totalLevel = 0;
            for(int i = 1; i <= 7; i++){
                Immortalsoul soul =  player.getEquipSouls().get(type*100 + i);
                if(soul != null){
                    totalLevel += soul.getLevel();
                    Cfg_Immortal_soul_attribute_Bean cfg_att = Cfg_Immortal_soul_attribute_Container.GetInstance().getValueByKey(soul.getItemModelId());
                    if(cfg_att == null){
                        log.error("Cfg_Immortal_soul_attribute_Bean no valid id:{}", soul.getItemModelId());
                        return;
                    }
                    stars[i-1] = cfg_att.getStar();
                    qualitys[i-1] = cfg_att.getQuality();
                }else{
                    stars[i-1] = -1;
                    qualitys[i-1] = 0;
                }
            }
            //当前核心
            Cfg_Immortal_soul_core_Bean cfg_core = Cfg_Immortal_soul_core_Container.GetInstance().getValueByKey(type);
            ReadIntegerArrayEs es = cfg_core.getStar();
            if(es != null && es.getValuees() != null){
                for(ReadArray<Integer> a : es.getValuees()){
                    if(a != null && a.size() == 2){
                        int star = a.get(1);
                        int num = a.get(0);
                        int count= getNum(star, stars);
                        if(count < num){
                            return;
                        }
                    }
                }
            }
            ReadIntegerArrayEs esqs = cfg_core.getQuality();
            if(esqs != null && esqs.getValuees() != null){
                for(ReadArray<Integer> a : esqs.getValuees()){
                    if(a != null && a.size() == 2){
                        int q = a.get(1);
                        int num = a.get(0);
                        int count= getNum(q, qualitys);
                        if(count < num){
                            return;
                        }
                    }
                }
            }
            //解锁成功
            coreLevelUp(player, type, totalLevel);
        }else{
            //核心升级判断
            int totalLevel = 0;
            for(int i = 1; i <= 7; i++) {
                Immortalsoul soul = player.getEquipSouls().get(type * 100 + i);
                if (soul != null) {
                    totalLevel += soul.getLevel();
                }
            }
            coreLevelUp(player, type, totalLevel);
        }

    }

    private void coreLevelUp(Player player, Integer type, int totalLevel) {
        int id = 0;
        Integer core = player.getImmortalsoulCores().get(type);
        if(core != null){
            id = core;
        }else{
            core = 0;
        }
        int upType = 0;
        if(id == 0){
            upType = 1;
        }else{
            upType = 2;
        }
        for(Cfg_Immortal_soul_core_att_Bean cc : Cfg_Immortal_soul_core_att_Container.GetInstance().getValuees()){
            if(cc.getCore_id() == type && cc.getAll_level() <= totalLevel && cc.getID() > id){
                id = cc.getID();
            }
        }
        if(id > core){
            if(upType == 1){
                log.info("player:{} 灵魂核心解锁成功 id:{}", player, id);
            }else{
                log.info("player:{} 灵魂核心升级成功 id:{}", player, id);
            }
            player.getImmortalsoulCores().put(type, id);
            //发送升级消息
            ImmortalSoulMessage.ResSoulCoreUpdate.Builder res = ImmortalSoulMessage.ResSoulCoreUpdate.newBuilder();
            ImmortalSoulMessage.SoulCoreInfo.Builder info = ImmortalSoulMessage.SoulCoreInfo.newBuilder();
            info.setCore(id);
            info.setType(type);
            res.setInfo(info);
            res.setType(upType);
            MessageUtils.send_to_player(player, ImmortalSoulMessage.ResSoulCoreUpdate.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    private int getNum(int v, int[] stars) {
        int count = 0;
        for(int s : stars){
            if(s >= v){
                count++;
            }
        }
        return count;
    }

    private ReadIntegerArray getNeedExp(Immortalsoul soulLevelup,Cfg_Immortal_soul_exp_Bean nextLevelBean){
        if (nextLevelBean == null)
            return null;
        switch (soulLevelup.getQuality()) {
            case ImmortalsoulDefine.q_blue:
                return  nextLevelBean.getBlue_exp();
            case ImmortalsoulDefine.q_volet:
                return nextLevelBean.getViolet_exp();
            case ImmortalsoulDefine.q_golden:
                return nextLevelBean.getGolden_exp();
            case ImmortalsoulDefine.q_red:
                return nextLevelBean.getGules_exp();
        }
        return  null;
    }

    public  int getAllOnEquipLevel(Player player)
    {
        int allLevel = 0;
        for (Immortalsoul Im : player.getEquipSouls().values()) {
            allLevel += Im.getLevel();
        }
        return allLevel;
    }

    @Override
    public String getImmortalSoulName(Integer id) {
        Cfg_Immortal_soul_attribute_Bean iattice = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(id);
        if (iattice == null) {
            return "";
        }
        return "2&_" +  iattice.getName();
    }


    public Item createSoul(int itemID){
        Cfg_Immortal_soul_attribute_Bean makeBean = CfgManager.getCfg_Immortal_soul_attribute_Container().getValueByKey(itemID);
        if(makeBean ==null){
            log.info("未找到该 itemid   "+ itemID);
            return null;
        }
        Immortalsoul newSoul = new Immortalsoul();
        newSoul.setUid( newSoul.getId());
        newSoul.setLevel(1);
        newSoul.setItemID(itemID);
        newSoul.setItemModelId(itemID);
        newSoul.setNum(1);
        newSoul.setType(makeBean.getExclusive_ID());
        newSoul.setQuality(makeBean.getQuality());
        Cfg_Immortal_soul_exp_Bean nextLevelBean = CfgManager.getCfg_Immortal_soul_exp_Container().getValueByKey(1);
        ReadIntegerArray  needExp = getNeedExp(newSoul,nextLevelBean);
        if (needExp !=null && needExp.size()>0){
            newSoul.setExp(needExp.get(0));
            log.error("createSoul  exp  {} ",needExp.get(0));
            newSoul.setCacheExp(makeBean.getExp());
        }
        newSoul.setLocation(0);
        return newSoul;
    }

   public boolean canAddSoulBag(Player player,List<Item> itemList){
       if (itemList == null || itemList.size() <= 0) {
           log.error("传入的道具为空！");
           return false;
       }
       if ( player.getImmortalsoul().size() + itemList.size()  > Global.Immortal_soul_backpack_max) {
           return false;
       }

       return true;
   }

   public  boolean addSoul(Player player,Item item,int reason){
       if (item == null) {
           log.error("传入的道具为空！");
           return false;
       }
       if (!(item instanceof Immortalsoul)){
           return false;
       }
       Immortalsoul immortalsoul =   (Immortalsoul)item;

       if ( player.getImmortalsoul().size()  >= Global.Immortal_soul_backpack_max) {
           MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.ImmBagIsFullSoResolve);
           return false;
       }

       player.getImmortalsoul().put(immortalsoul.getUid(),immortalsoul);
       sendExchangeResult(player,immortalsoul,reason);
       Manager.backpackManager.manager().writeItemLogAndBI(player,0, item.getNum(), item, reason, IDConfigUtil.getLogId());
       return true;
   }

   private int getGridType(int gridID){
       return gridID/100;
   }

}
