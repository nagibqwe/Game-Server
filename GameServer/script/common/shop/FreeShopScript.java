package common.shop;

import com.data.*;
import com.data.bean.Cfg_Free_shop_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.recharge.script.IRechargeReward;
import com.game.script.structs.ScriptEnum;
import com.game.shop.script.IFreeShop;
import com.game.shop.structs.FreeShop;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.shopMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2020/6/1.
 */
public class FreeShopScript implements IFreeShop,IRechargeReward
{

    private final Logger log = LogManager.getLogger(LimitShopScript.class);
    public int getId() {
        return ScriptEnum.FreeShopScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    public boolean canReward(Player player, int goodId) {

        ConcurrentHashMap<Integer, FreeShop> freeShopList  = player.getFreeShopMap();
        if (!freeShopList.containsKey(goodId))
            return true;
        return false;
    }

    @Override
    public void afterReward(Player player, String orderId, int goodId){
        buyFreeShop(player,goodId,true);
    }


    public void onlineInit(Player player){
        ConcurrentHashMap<Integer, FreeShop> freeShopList =  player.getFreeShopMap();
        for (Map.Entry<Integer,FreeShop> freeShopEntry :freeShopList.entrySet()){
            FreeShop shop =  freeShopEntry.getValue();

            if (shop.isOverdue())
                continue;
            updateFreeShopState(player,shop,shop.isGet());
        }
        sendFreeShopListToClient(player);
    }

    private void updateFreeShopState(Player player, FreeShop shop,boolean isget) {
        long now   =   TimeUtils.Time();
        int curday =   TimeUtils.getCurDayByTime(now);
        long lastGetTime = shop.getLastGetRewardTime();
        int lastGetDay =   TimeUtils.getCurDayByTime(lastGetTime);
        if (curday  == lastGetDay)
             return;
        if (curday > lastGetDay){
            int overDay =  curday - lastGetDay;
            if (overDay< 2){
                shop.setGet(false);
                return;
            }
            shop.setGet(false);
            if (overDay > 1){
                Cfg_Free_shop_Bean bean = CfgManager.getCfg_Free_shop_Container().getValueByKey(shop.getId());
                if (bean == null){
                    log.error("配置表不存在 Cfg_Free_shop_Bean   "  + shop.getId());
                    return;
                }
                List<Item> itemList = new ArrayList<>();
                for (int i = 0;i<overDay-1;i++){
                    Item item = Item.createItemCoin( bean.getRewardDaily().get(0),bean.getRewardDaily().get(1));
                    itemList.add(item);
                    shop.setRewardTimes(shop.getRewardTimes()+1);
                    if (shop.getRewardTimes() >= bean.getTime()){
                        shop.setOverdue(true);
                        shop.setGet(true);
                        break;
                    }
                }
                if (itemList.size() > 0){
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                            MessageString.System, MessageString.Free_Shop_Rewad_Mail_Title, MessageString.Free_Shop_Rewad_Mail_Notice, itemList, ItemChangeReason.FreeShopCost);
                    shop.setLastGetRewardTime(now);
                }
            }
        }
    }


    public void  onReqFreeShop(Player player,int id,int type){
        if (type == 1){
            buyFreeShop(player,id,false);
        }else if(type == 2){
            getReward(player,id);
        }
    }

    private void buyFreeShop(Player player,int id,boolean isCallBack){
        if (player.getFreeShopMap().containsKey(id)) {
            return;
        }
       if (TimeUtils.getOpenServerDay() >  Global.Free_Shop_End_Time) {
           return;
       }
        Cfg_Free_shop_Bean bean = CfgManager.getCfg_Free_shop_Container().getValueByKey(id);
        if (bean == null)
            return;
        int type = bean.getPrice().get(0);
        int needMoney = bean.getPrice().get(1);
        if (!isCallBack){
            if (type  <= 0)
                return;
            if (!Manager.currencyManager.manager().canDecItemCoin(player,  needMoney, type))
                return;
            Manager.currencyManager.manager().onDecItemCoin(player, needMoney, ItemChangeReason.FreeShopCost, id,type);
        }


        List<Item> items = Item.createItems(player.getCareer(),bean.getItem(), 1);
        long actionId = IDConfigUtil.getLogId();
        if(!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FreeShopReward, actionId)){
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System
                    , MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.FreeShopReward, actionId);
        }

        long now = TimeUtils.Time();
        FreeShop freeShop = new FreeShop();
        freeShop.setGet(true);
        freeShop.setLastGetRewardTime(now);
        freeShop.setBuyTime(now);
        freeShop.setId(id);
        freeShop.setRewardTimes(0);
        freeShop.setOverdue(false);
        player.getFreeShopMap().put(id,freeShop);

        onSyncFreeShopResult(player,freeShop,1);

        Manager.controlManager.operate(player, FunctionVariable.Free_shop_total,  bean.getId());
        //记录bi数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FREE_BUY, ItemChangeReason.FreeShopReward, id);
    }

    private void getReward(Player player,int id){
        if (!player.getFreeShopMap().containsKey(id)) {
            return;
        }
        FreeShop shop =  player.getFreeShopMap().get(id);
        if (shop.isGet() || shop.isOverdue()){
            return;
        }
        Cfg_Free_shop_Bean bean = CfgManager.getCfg_Free_shop_Container().getValueByKey(id);
        if (bean == null)
            return;

        if (shop.getRewardTimes() >=  bean.getTime())
            return;

        Item item = Item.createItemCoin(bean.getRewardDaily().get(0), bean.getRewardDaily().get(1));
        if (!Manager.backpackManager.manager().onHasAddSpace(player, item))
            return;
        Manager.backpackManager.manager().addItem(player, item, ItemChangeReason.FreeShopReward, id);

        shop.setGet(true);
        shop.setLastGetRewardTime(TimeUtils.Time());
        shop.setRewardTimes(shop.getRewardTimes()+1);

        if (shop.getRewardTimes() >= bean.getTime())
            shop.setOverdue(true);

        onSyncFreeShopResult(player,shop,2);
    }
    private void onSyncFreeShopResult(Player player ,FreeShop shop,  int type){
        shopMessage.SyncFreeShopResult.Builder msg = shopMessage.SyncFreeShopResult.newBuilder();
        msg.setType(type);
        shopMessage.FreeShopData.Builder shopdata = shopMessage.FreeShopData.newBuilder();
        shopdata.setIsGet(shop.isGet());
        shopdata.setBuyTime(shop.getBuyTime());
        shopdata.setId(shop.getId());
        msg.setBuyData(shopdata.build());
        MessageUtils.send_to_player(player, shopMessage.SyncFreeShopResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void refresh(Player player){

        ConcurrentHashMap<Integer, FreeShop> freeShopList =  player.getFreeShopMap();
        long now = TimeUtils.Time();
        for (Map.Entry<Integer, FreeShop> shopMap: freeShopList.entrySet()){
            Cfg_Free_shop_Bean bean = CfgManager.getCfg_Free_shop_Container().getValueByKey(shopMap.getKey());
            if (bean == null)
                continue;

            FreeShop shop = shopMap.getValue();
            if (shop.getRewardTimes() >= bean.getTime()){
                shop.setOverdue(true);
                shop.setGet(true);
                continue;
            }
            if (!shop.isGet()){
                List<Item> itemList = new ArrayList<>();
                Item item = Item.createItemCoin(bean.getRewardDaily().get(0), bean.getRewardDaily().get(1));
                itemList.add(item);
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                        MessageString.System, MessageString.Free_Shop_Rewad_Mail_Title, MessageString.Free_Shop_Rewad_Mail_Notice, itemList, ItemChangeReason.FreeShopCost);

                shop.setRewardTimes(shop.getRewardTimes()+1);
                shop.setLastGetRewardTime(now);
            }
            if (shop.getRewardTimes() >=bean.getTime() ){
                shop.setOverdue(true);
                shop.setGet(true);
            }else {
                shop.setGet(false);
            }
        }
        sendFreeShopListToClient(player);
    }

    public void sendFreeShopListToClient(Player player){
        ConcurrentHashMap<Integer, FreeShop> freeShopList =  player.getFreeShopMap();
        shopMessage.SyncOnlineInitFreeShop.Builder msg = shopMessage.SyncOnlineInitFreeShop.newBuilder();
        shopMessage.FreeShopData.Builder shopdata = null;
        for (Map.Entry<Integer,FreeShop>shopEntry: freeShopList.entrySet()){
            FreeShop freeShop = shopEntry.getValue();
            shopdata = shopMessage.FreeShopData.newBuilder();
            shopdata.setBuyTime(freeShop.getBuyTime());
            shopdata.setId(freeShop.getId());
            shopdata.setIsGet(freeShop.isGet());
            msg.addZeroBuyList(shopdata.build());
        }
        MessageUtils.send_to_player(player, shopMessage.SyncOnlineInitFreeShop.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
