package common.shop;


import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Free_newshop_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.shop.script.INewFreeShop;
import com.game.shop.structs.FreeShop;
import com.game.shop.structs.NewFreeShop;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.shopMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 新零元购
 */
public class NewFreeShopScript implements INewFreeShop {

    private final Logger log = LogManager.getLogger(NewFreeShopScript.class);

    private  int MaxRecordNum = 20;

    @Override
    public int getId() {
        return ScriptEnum.NewFreeShopScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onlineInit(Player player) {
        sendFreeShopListToClient(player);
    }

    @Override
    public void onReqFreeShop(Player player, int id, int type) {

        if (type == 1){
            buyFreeShop(player,id);
        }else if (type == 2){
            getReward(player,id);
        }
    }
    private void buyFreeShop(Player player,int id){
        if (player.getNewFreeShopMap().containsKey(id)) {
            return;
        }

        Cfg_Free_newshop_Bean bean = CfgManager.getCfg_Free_newshop_Container().getValueByKey(id);
        if (bean == null){
            log.error("Cfg_Free_newshop_Bean is null {}",id);
            return;
        }
        if (TimeUtils.getOpenServerDay()  <   bean.getOpenTime()) {
            log.error("开服天数未达到 {} {}",TimeUtils.getOpenServerDay(),bean.getOpenTime());
            return;
        }


        int type = bean.getPrice().get(0);
        int needMoney = bean.getPrice().get(1);
        if (type  <= 0){
            return;
        }
        if (!Manager.currencyManager.manager().canDecItemCoin(player,  needMoney, type)){
            return;
        }
        Manager.currencyManager.manager().onDecItemCoin(player, needMoney, ItemChangeReason.FreeShopCost, id,type);

        List<Item> items = Item.createItems(player.getCareer(),bean.getItem(), 1);
        long actionId = IDConfigUtil.getLogId();
        if(!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.NewFreeShopReward, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System
                    , MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.NewFreeShopReward, actionId);
        }

        long now = TimeUtils.Time();
        NewFreeShop freeShop = new NewFreeShop();
        freeShop.setGet(false);
        freeShop.setBuyTime(now);
        freeShop.setId(id);
        player.getNewFreeShopMap().put(id,freeShop);
        onSyncFreeShopResult(player,freeShop,1);


        //只保留二十条
        if ( ServerParamUtil.newFreeShopBuyInfos.size() >= MaxRecordNum){
            ServerParamUtil.newFreeShopBuyInfos.remove(0);
        }
        ServerParamUtil.newFreeShopBuyInfos.add(buildBuyInfoRecord(player,id,false));
        ServerParamUtil.saveNewFreeShopBuyInfosRecord();
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.NewFREE_BUY, ItemChangeReason.NewFreeShopReward, id);

    }
    private void getReward(Player player,int id){
        if (!player.getNewFreeShopMap().containsKey(id)) {
            return;
        }
        NewFreeShop shop =  player.getNewFreeShopMap().get(id);
        if (shop.isGet() ){
            return;
        }
        Cfg_Free_newshop_Bean bean = CfgManager.getCfg_Free_newshop_Container().getValueByKey(id);
        if (bean == null){
            log.error("Cfg_Free_newshop_Bean is null {}",id);
            return;
        }

        if (TimeUtils.getOpenServerDay()  <   bean.getTime()) {
            log.error("开服天数未达到 {} {}",TimeUtils.getOpenServerDay(),bean.getTime());
            return;
        }

        int type = bean.getPrice().get(0);
        int returnMoney = bean.getPrice().get(1);
        Manager.currencyManager.manager().onAddItemCoin(player,type,returnMoney,ItemChangeReason.NewFreeShopReward,IDConfigUtil.getLogId());

        shop.setGet(true);
        onSyncFreeShopResult(player,shop,2);

        //只保留二十条
        if ( ServerParamUtil.newFreeShopBuyInfos.size() >= MaxRecordNum){
            ServerParamUtil.newFreeShopBuyInfos.remove(0);
        }
        ServerParamUtil.newFreeShopBuyInfos.add(buildBuyInfoRecord(player,id,true));
        ServerParamUtil.saveNewFreeShopBuyInfosRecord();
    }

    public void refresh(Player player){

    }
    private void onSyncFreeShopResult(Player player ,NewFreeShop shop,  int type){
        shopMessage.SyncBuyNewFreeShopResult.Builder msg = shopMessage.SyncBuyNewFreeShopResult.newBuilder();
        msg.setType(type);
        shopMessage.FreeShopData.Builder shopdata = shopMessage.FreeShopData.newBuilder();
        shopdata.setIsGet(shop.isGet());
        shopdata.setBuyTime(shop.getBuyTime());
        shopdata.setId(shop.getId());
        msg.setBuyData(shopdata.build());
        MessageUtils.send_to_player(player, shopMessage.SyncBuyNewFreeShopResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    public void sendFreeShopListToClient(Player player){
        ConcurrentHashMap<Integer, NewFreeShop> freeShopList =  player.getNewFreeShopMap();
        shopMessage.SyncOnlineInitNewFreeShop.Builder msg = shopMessage.SyncOnlineInitNewFreeShop.newBuilder();
        shopMessage.FreeShopData.Builder shopdata = null;
        for (Map.Entry<Integer,NewFreeShop>shopEntry: freeShopList.entrySet()){
            NewFreeShop freeShop = shopEntry.getValue();
            shopdata = shopMessage.FreeShopData.newBuilder();
            shopdata.setBuyTime(freeShop.getBuyTime());
            shopdata.setId(freeShop.getId());
            shopdata.setIsGet(freeShop.isGet());
            msg.addZeroBuyList(shopdata.build());
        }
        MessageUtils.send_to_player(player, shopMessage.SyncOnlineInitNewFreeShop.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    public void onReqOpenNewFreeShop(Player player){
        sendFreeShopBuyInfoToClient(player);
    }

    private void sendFreeShopBuyInfoToClient(Player player){
        List<NewFreeShop> buyinfos =  ServerParamUtil.newFreeShopBuyInfos;
        shopMessage.ResOpenNewFreeShopResult.Builder msg = shopMessage.ResOpenNewFreeShopResult.newBuilder();
        shopMessage.BuyInfo.Builder builder;
        for (int i = buyinfos.size() - 1;i >=0; i--){
            NewFreeShop data =  buyinfos.get(i);
            if (data  == null){
                break;
            }
            builder =  shopMessage.BuyInfo.newBuilder();
            builder.setId(data.getId());
            builder.setType(data.isGet() ? 2:1);
            builder.setName(data.getName());
            msg.addBuyInfos(builder.build());
        }
        MessageUtils.send_to_player(player, shopMessage.ResOpenNewFreeShopResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private NewFreeShop buildBuyInfoRecord(Player player,int id,boolean isget){

        NewFreeShop freeShopRecord = new NewFreeShop();
        freeShopRecord.setGet(isget);
        freeShopRecord.setId(id);
        freeShopRecord.setName(player.getName());
        return freeShopRecord;
    }
}
