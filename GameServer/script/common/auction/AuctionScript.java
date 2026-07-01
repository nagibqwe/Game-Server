package common.auction;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.auction.logs.AuctionLog;
import com.game.auction.scripts.IAuctionScript;
import com.game.auction.structs.AuctionInfo;
import com.game.auction.structs.AuctionRecord;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.holyEquip.struct.HolyEquipItem;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.vip.structs.VipPower;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import game.message.AuctionMessage;
import game.message.AuctionMessage.ResAuctionInfo;
import game.message.AuctionMessage.ResAuctionInfoList;
import game.message.AuctionMessage.ResPersonAuctionRecordList;
import game.message.AuctionMessage.ResWorldAuctionRecordList;
import game.message.EquipMessage;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther lw
 * @create 2019-10-08 15:47
 */
public class AuctionScript implements IAuctionScript {


    //1上架2竞拍3竞拍成交4一口价成交5超时下架6转至世界拍卖7主动下架
    final int Action_UPGoods = 1;
    final int Action_Do = 2;
    final int Action_Ok = 3;
    final int Action_OneKey = 4;
    final int Action_TimeOutDown = 5;
    final int Action_GoWorldAuction = 6;
    final int Action_SelfDown = 7;
    final int Action_MailBack = 8; //8邮件退还


    private static final Logger logger = LogManager.getLogger(AuctionScript.class);


    @Override
    public void auctionOnline(Player player) {
        AuctionMessage.ResAuctionPur.Builder msg = AuctionMessage.ResAuctionPur.newBuilder();
        //取消假拍卖品
//        msg.setIsPur(Manager.countManager.getBooleanCountValue(player, BooleanForever.AuctionPur));
        msg.setIsPur(true);
        MessageUtils.send_to_player(player, AuctionMessage.ResAuctionPur.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void auctionList(Player player) {
        Iterator iterator = Manager.auctionManager.getAuctions().entrySet().iterator();
        ResAuctionInfoList.Builder msg = ResAuctionInfoList.newBuilder();
        while (iterator.hasNext()) {
            Map.Entry<Long, AuctionInfo> entry = (Map.Entry<Long, AuctionInfo>) iterator.next();
            msg.addAuctionInfoList(buildAuctionInfo(entry.getValue()));
        }
        MessageUtils.send_to_player(player, ResAuctionInfoList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void auctionRecordList(Player player) {
        ResPersonAuctionRecordList.Builder msgPerson = ResPersonAuctionRecordList.newBuilder();
        for (AuctionRecord record : player.getAuctionRecords()) {
            msgPerson.addRecords(buildAuctionRecord(record));
        }
        MessageUtils.send_to_player(player, ResPersonAuctionRecordList.MsgID.eMsgID_VALUE, msgPerson.build().toByteArray());

        ResWorldAuctionRecordList.Builder msgWorld = ResWorldAuctionRecordList.newBuilder();
        for (AuctionRecord record : ServerParamUtil.auctionRecords) {
            msgWorld.addRecords(buildAuctionRecord(record));
        }
        MessageUtils.send_to_player(player, ResWorldAuctionRecordList.MsgID.eMsgID_VALUE, msgWorld.build().toByteArray());
    }

    @Override
    public void auction(Player player, long auctionId, int price) {
        ResAuctionInfo.Builder msg = ResAuctionInfo.newBuilder();
        AuctionInfo auctionInfo = Manager.auctionManager.getAuctions().get(auctionId);

        //竞拍不存在
        if (auctionInfo == null) {
            auctionInfo = new AuctionInfo();
            auctionInfo.setId(auctionId);
            msg.setRes(2);
            msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
            MessageUtils.send_to_player(player, ResAuctionInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        if (auctionInfo.getItem() instanceof HolyEquipItem) {
            int needLevel = Global.Holy_Trade_VIP_Limit.get(1);
            if (player.getVipLv() < needLevel) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.PERSONBOSS_VIPLEVELNOTZU);
                return;
            }
        }

        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
        if (bean == null) {
            return;
        }
        //魔魂物品必须vip等级4
        if(bean.getType() == ItemTypeConst.DEVIL_CARD){
            if(player.getVipLv() < Global.DevilSoul_Trade_VIP_Limit.get(1)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Devil_Trade_VIP_Limit_Buy_Title, Global.DevilSoul_Trade_VIP_Limit.get(1));
                return;
            }
        }
        //只能一口价
        if (bean.getAuction_single_price() == 0) {
            return;
        }
        //密码交易只能一口价
        if(StringUtils.isNotEmpty(auctionInfo.getPassword())){
            logger.warn("player:{} auctionId：{} 密码交易只能一口价",player.getId(), auctionId);
            return;
        }

        //没有仙盟不能仙盟拍卖
        if (auctionInfo.getGuildId() != 0 && player.getGuildId() != auctionInfo.getGuildId()) {
            return;
        }

        int Auction_countdown = auctionInfo.getGuildId() > 0 ? Global.Guild_auction_countdown : Global.Auction_countdown;
        //还在倒计时中不能拍卖
        if (bean.getAuction_countdown() == 1 && (auctionInfo.getTime() + Auction_countdown * 1000 > TimeUtils.Time())) {
            return;
        }

        //判断是否上一个竞拍者是否是自己
        if (player.getId() == auctionInfo.getRoleId()) {
            msg.setRes(3);
            msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        //计算最大价格
        int maxPrice = bean.getAuction_max_price() * auctionInfo.getItem().getNum();
        //计算当前新的竞拍价格
        int curPrice = auctionInfo.getPrice();
        if (auctionInfo.getRoleId() != 0) {
            if (bean.getAuction_single_type() > 0) {
                curPrice += (int) Math.floor(curPrice * (bean.getAuction_single_price() / 10000.0f));
            } else {
                curPrice += bean.getAuction_single_price() * auctionInfo.getItem().getNum();
            }
        }

        //如果当前价格大于最大价格,那么就让他等于最大价格
        if (bean.getAuction_max_price() > 0 && curPrice > maxPrice) {
            curPrice = maxPrice;
        }

        //判断客户端发送的竞拍价格和服务器计算的价格是否相同
        if (curPrice != price) {
            msg.setRes(1);
            msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        if (!Manager.currencyManager.manager().onDecItemCoin(player, curPrice, ItemChangeReason.AuctionPriceDec, auctionId, bean.getAuction_use_coin())) {
            return;
        }

        String mailCoinName = Manager.backpackManager.manager().getName(bean.getAuction_use_coin());

        if (auctionInfo.getRoleId() != 0) {
            List<Item> backItems = Item.createItems(bean.getAuction_use_coin(), auctionInfo.getPrice(), false);
            String failContent = MessageString.AuctionFailureMail + "@_@" + Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId()) + "@_@" + player.getName()
                    + "@_@" + mailCoinName + "@_@" + mailCoinName;
            Manager.mailManager.sendMailToPlayer(auctionInfo.getRoleId(), MailType.AuctionMail, MessageString.Auction,
                    MessageString.AuctionFailureMailTitle, failContent, backItems,ItemChangeReason.AuctionFailureGet, auctionId);
            for (Item item : backItems) {
                auctionBI(auctionInfo.getId(), player.getId(), item, auctionInfo.getPrice(), Action_MailBack, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
            }
        }

        if (bean.getAuction_max_price() > 0 && curPrice >= maxPrice) {
            //当前价格大于等于设置的最大价格,那就直接竞拍完成
            String successContent = MessageString.AuctionSuccessfulMail + "@_@" + curPrice + "@_@" + Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId())
                    + "@_@" + mailCoinName;
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.AuctionMail, MessageString.Auction,
                    MessageString.AuctionSuccessfulMailTitle, successContent, getItems(auctionInfo.getItem(), true),ItemChangeReason.AuctionSuccessfulGet, auctionId);

            //todo 未考虑系统分红
            Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);

            if (auctionInfo.getOwnId() > 10000000) {
                Player ownPlayer = Manager.playerManager.getPlayer(auctionInfo.getOwnId());
                if (ownPlayer != null) {
                    sendSuccessMail(ownPlayer, auctionInfo, curPrice);
                    buildPersonRecord(ownPlayer, auctionInfo.getItem().getItemModelId(), curPrice, auctionInfo.getItem().getNum(), 1);
                    if(ownPlayer.isOnline()){
                        AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
                        builder.setId(auctionId);
                        builder.setOwnId(auctionInfo.getOwnId());
                        MessageUtils.send_to_player(ownPlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                    }
                }
            } else if (auctionInfo.getGuildId() > 0) {
                sendGuildAuction(auctionInfo, price);
            }
            buildPersonRecord(player, auctionInfo.getItem().getItemModelId(), curPrice, auctionInfo.getItem().getNum(), 0);
            buildWorldRecord(auctionInfo.getItem().getItemModelId(), curPrice, auctionInfo.getItem().getNum());

            Manager.auctionManager.getAuctions().remove(auctionId);

            AuctionMessage.ResAuctionInfoPur.Builder msg1 = AuctionMessage.ResAuctionInfoPur.newBuilder();
            msg1.setRes(0);
            msg1.setAuctionId(auctionId);
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoPur.MsgID.eMsgID_VALUE, msg1.build().toByteArray());
            buildAuctionLog(auctionInfo, player.getId(), 8);
            //拍卖行BI
            auctionBI(auctionInfo.getId(), player, bean, auctionInfo.getItem(), auctionInfo.getPrice(), Action_Ok, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());

            AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
            builder.setId(auctionId);
            builder.setOwnId(auctionInfo.getOwnId());
            for (long roleId : auctionInfo.getRoleIds()) {
                if (roleId == player.getId()) {
                    continue;
                }
                Player otherPlayer = Manager.playerManager.getPlayer(roleId);
                if (otherPlayer == null || !otherPlayer.isOnline()) {
                    continue;
                }
                MessageUtils.send_to_player(otherPlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }

        } else {
            int lastMinTime = Global.Auction_AddTime.get(0) * 1000;
            int addTime = Global.Auction_AddTime.get(1);
            long nowTime = TimeUtils.Time();
            long auctionTime = auctionInfo.getTime();
            int Auction_time = getOverTime(auctionInfo);
            if (((auctionTime + Auction_time * 1000) - nowTime) <= lastMinTime) {
                auctionInfo.setTime(auctionTime + addTime * 1000);
            }
            auctionInfo.setPrice(curPrice);
            auctionInfo.setRoleId(player.getId());
            auctionInfo.getRoleIds().add(player.getId());
            Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_UPDATE, SaveServer.UPDATE);
            msg.setRes(0);
            msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            buildAuctionLog(auctionInfo, player.getId(), 2);
            //拍卖行BI
            auctionBI(auctionInfo.getId(), player, bean, auctionInfo.getItem(), auctionInfo.getPrice(), Action_Do, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());

            AuctionMessage.ResAuctionUpdate.Builder builder = AuctionMessage.ResAuctionUpdate.newBuilder();
            builder.setAuctionInfo(buildAuctionInfo(auctionInfo));

            for (long roleId : auctionInfo.getRoleIds()) {
                if (roleId == player.getId()) {
                    continue;
                }

                Player otherPlayer = Manager.playerManager.getPlayer(roleId);
                if (otherPlayer == null || !otherPlayer.isOnline()) {
                    continue;
                }
                MessageUtils.send_to_player(otherPlayer, AuctionMessage.ResAuctionUpdate.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }
    }

    public void auctionActivityPut(List<Long> roleIDlist, List<Item> putItemList, int dailyID, long guildID) {

        if (putItemList.size() <= 0)
            return;
        if (roleIDlist.size() <= 0) {
            logger.info("分红玩家列表不能为空");
            return;
        }

        if (guildID <= 0) {
            logger.info("仙盟ID不能为 0");
            return;
        }

        for (Item item : putItemList) {
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            if (bean == null) {
                continue;
            }

            if (bean.getAuction_max_price() == 0) {
                continue;
            }
            AuctionInfo auctionInfo = new AuctionInfo();
            auctionInfo.setId(IDConfigUtil.getId());
            auctionInfo.setTime(TimeUtils.Time());
            auctionInfo.setOwnId(dailyID);
            auctionInfo.setItem(item);
            auctionInfo.setPrice(bean.getAuction_min_price() * item.getNum());
            auctionInfo.setGuildId(guildID);
            Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_INSERT, SaveServer.INSERT);
            Manager.auctionManager.getAuctions().put(auctionInfo.getId(), auctionInfo);
            buildAuctionLog(auctionInfo, dailyID, 1);
            ServerParamUtil.auctionRoleIDMap.put(auctionInfo.getId(), roleIDlist);
            //拍卖行BI
            activityAuctionBI(auctionInfo.getId(), auctionInfo.getOwnId(), item, auctionInfo.getPrice(), Action_UPGoods, 3, auctionInfo.getGuildId());
        }
        ServerParamUtil.saveAuctionRoleID();
    }


    @Override
    public void auctionPut(Player player, long itemUid, int num, int type, String password, int price) {
        if (num <= 0) {
            return;
        }

        if (getAuctionPutNum(player) >= Global.Trade_maxrecord) {
            return;
        }

        Item curItem = Manager.backpackManager.manager().getItemById(player, itemUid);
        if (curItem == null) {
            return;
        }
        if (curItem instanceof HolyEquipItem) {
            int needLevel = Global.Holy_Trade_VIP_Limit.get(0);
            if (player.getVipLv() < needLevel) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.PERSONBOSS_VIPLEVELNOTZU);
                return;
            }
        }

        if (curItem.isBind()) {
            return;
        }

        if (curItem.getNum() < num) {
            return;
        }

        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(curItem.getItemModelId());
        if (bean == null) {
            return;
        }

        //判断魔魂物品需要的vip等级
        if(bean.getType() == ItemTypeConst.DEVIL_CARD){
            if(player.getVipLv() < Global.DevilSoul_Trade_VIP_Limit.get(0)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Devil_Trade_VIP_Limit_Push_Title, Global.DevilSoul_Trade_VIP_Limit.get(1));
                return;
            }
        }

        if (bean.getAuction_max_price() == 0) {
            return;
        }

        if (type == 1 && player.getGuildId() == 0) {
            return;
        }

        //密码交易判断
        boolean isPassword = false;
        if(!StringUtils.isEmpty(password)){
            password = password.trim();
            if(password.length() != 6 || !NumberUtils.isDigits(password)){
                logger.warn("player:{} 密码上架密码异常 password:{}", player.getId(), password);
                return;
            }
            isPassword = true;
            //价格不能大于最大
            if(bean.getAuction_max_price() <= 0 || price < 2 || price > bean.getAuction_max_price() * num){
                logger.warn("player:{} 密码上架价格异常 itemId:{} price:{}", player.getId(), bean.getId(), price);
                return;
            }
        }else{
            //自定义价格
            if(bean.getAuction_price_type() == 1){
                if(price < 2 || price > bean.getAuction_max_price() * num){
                    logger.warn("player:{} 价格异常 itemId:{} price:{}", player.getId(), bean.getId(), price);
                    return;
                }
            }
        }

        Item item;
        try {
            item = curItem.clone();
            item.setId(IDConfigUtil.getId());
            item.setNum(num);
            item.setGridId(0);
        } catch (Exception ex) {
            logger.error(ex, ex);
            return;
        }

        if (!Manager.backpackManager.manager().onRemoveItem(player, curItem, num, ItemChangeReason.AuctionPutDec, IDConfigUtil.getId())) {
            return;
        }

        AuctionInfo auctionInfo = new AuctionInfo();
        auctionInfo.setId(IDConfigUtil.getId());
        auctionInfo.setTime(TimeUtils.Time());
        auctionInfo.setOwnId(player.getId());
        auctionInfo.setItem(item);
        if(isPassword){
            auctionInfo.setPrice(price);
            auctionInfo.setPassword(password);
        }else{
            if(bean.getAuction_price_type() == 1){
                auctionInfo.setPrice(price);
            }else{
                auctionInfo.setPrice(bean.getAuction_min_price() * item.getNum());
            }
        }
        if (type == 1) {
            auctionInfo.setGuildId(player.getGuildId());
        }
        Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_INSERT, SaveServer.INSERT);
        Manager.auctionManager.getAuctions().put(auctionInfo.getId(), auctionInfo);
        AuctionMessage.ResAuctionInfoPutSuccess.Builder msg = AuctionMessage.ResAuctionInfoPutSuccess.newBuilder();
        msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
        MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoPutSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        buildAuctionLog(auctionInfo, player.getId(), 1);

        if (bean.getType() == ItemTypeConst.EQUIP) {
            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            if (equipBean != null) {
                long key = equipBean.getGrade() * 100000 + equipBean.getQuality() * 1000 + equipBean.getPart();
                Manager.countManager.addCount(player, BaseCountType.AuctionPut, key, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.AuchtionSell, 1);
            }
        }else if(bean.getType() == ItemTypeConst.DEVIL_CARD){
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.AuchtionDevilCardSell, Count.RefreshType.CountType_Forever, 1);
        }
        Manager.controlManager.operate(player, FunctionVariable.Auchtion_Any_Shelf, 1);
        if(type == 1){
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.Guild_auction_company_business_frequency, Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.operate(player, FunctionVariable.Guild_auction_company_business_frequency, 1);
        }
        //拍卖行BI
        auctionBI(auctionInfo.getId(), player, bean, item, auctionInfo.getPrice(), Action_UPGoods, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
    }

    @Override
    public void auctionOut(Player player, long auctionId) {
        AuctionMessage.ResAuctionInfoOut.Builder msg = AuctionMessage.ResAuctionInfoOut.newBuilder();
        AuctionInfo auctionInfo = Manager.auctionManager.getAuctions().get(auctionId);
        if (auctionInfo == null) {
            auctionInfo = new AuctionInfo();
            auctionInfo.setId(auctionId);
            msg.setRes(2);
            msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
        if (bean == null) {
            return;
        }
        if (player.getId() != auctionInfo.getOwnId()) {
            return;
        }
        if (auctionInfo.getRoleId() != 0) {
            msg.setRes(1);
            msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        msg.setRes(0);
        msg.setAuctionInfo(buildAuctionInfo(auctionInfo));
        Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
        Manager.auctionManager.getAuctions().remove(auctionId);
        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.AuctionMail, MessageString.Auction,
                MessageString.AuctionOutMailTitle, MessageString.AuctionOutMail, getItems(auctionInfo.getItem(), false),ItemChangeReason.AuctionOutGet, auctionId);
        MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        buildAuctionLog(auctionInfo, player.getId(), 4);
        //拍卖行BI
        auctionBI(auctionInfo.getId(), player, bean, auctionInfo.getItem(), auctionInfo.getPrice(), Action_SelfDown, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
    }

    @Override
    public void auctionPur(Player player, long auctionId, String password) {
        AuctionMessage.ResAuctionInfoPur.Builder msg = AuctionMessage.ResAuctionInfoPur.newBuilder();
        //去除假拍卖功能
//        if (auctionId == 0) {
//            int itemId = 0;
//            for (ReadArray<Integer> l : Global.Trade_First_Buy_Item.getValuees()) {
//                if (l.get(0) == player.getCareer()) {
//                    itemId = l.get(1);
//                }
//            }
//
//            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(itemId);
//            if (bean == null) {
//                return;
//            }
//
//            int price = bean.getAuction_max_price();
//            if (!Manager.currencyManager.manager().onDecItemCoin(player, price, ItemChangeReason.AuctionPurDec, auctionId, ItemCoinType.GoldCoin)) {
//                return;
//            }
//
//            String itemName = Manager.backpackManager.manager().getName(itemId);
//            String successContent = MessageString.AuctionSuccessfulMail + "@_@" + price + "@_@" + itemName;
//            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.AuctionMail, MessageString.Auction,
//                    MessageString.AuctionSuccessfulMailTitle, successContent, Item.createItems(itemId, 1, true),ItemChangeReason.AuctionSuccessfulGet, auctionId);
//
//            msg.setAuctionId(0);
//            msg.setRes(0);
//            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoPur.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//
//            Manager.countManager.setBooleanCountValue(player, BooleanForever.AuctionPur, true);
//            AuctionMessage.ResAuctionPur.Builder msg1 = AuctionMessage.ResAuctionPur.newBuilder();
//            msg1.setIsPur(true);
//            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionPur.MsgID.eMsgID_VALUE, msg1.build().toByteArray());
//
//            buildWorldRecord(itemId, price, 1);
//            buildPersonRecord(player, itemId, price, 1, 0);
//
//            if (bean.getType() == ItemTypeConst.EQUIP) {
//                Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(bean.getId());
//                if (equipBean != null) {
//                    long key = equipBean.getGrade() * 100000 + equipBean.getQuality() * 1000 + equipBean.getPart();
//                    Manager.countManager.addCount(player, BaseCountType.AuctionBuy, key, Count.RefreshType.CountType_Forever, 1);
//                    Manager.controlManager.operate(player, FunctionVariable.AuchtionBuy, 1);
//                }
//            }else if(bean.getType() == ItemTypeConst.DEVIL_CARD){
//                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.AuchtionDevilCardBuy, Count.RefreshType.CountType_Forever, 1);
//            }
//            return;
//        }

        msg.setAuctionId(auctionId);
        AuctionInfo auctionInfo = Manager.auctionManager.getAuctions().get(auctionId);
        if (auctionInfo == null) {
            msg.setRes(1);
            MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoPur.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        if (auctionInfo.getItem() instanceof HolyEquipItem) {
            int needLevel = Global.Holy_Trade_VIP_Limit.get(1);
            if (player.getVipLv() < needLevel) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.PERSONBOSS_VIPLEVELNOTZU);
                return;
            }
        }
        boolean isPassword = false;
        if(StringUtils.isNotEmpty(auctionInfo.getPassword())){
            isPassword = true;
            if(!auctionInfo.getPassword().equals(password)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MARKETSECRETERROR);
                return;
            }
        }

        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
        if (bean == null) {
            return;
        }
        //魔魂物品必须vip等级4
        if(bean.getType() == ItemTypeConst.DEVIL_CARD){
            if(player.getVipLv() < Global.DevilSoul_Trade_VIP_Limit.get(1)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Devil_Trade_VIP_Limit_Buy_Title, Global.DevilSoul_Trade_VIP_Limit.get(1));
                return;
            }
        }
        if (bean.getAuction_max_price() == -1) {
            return;
        }

        if (auctionInfo.getGuildId() != 0 && player.getGuildId() != auctionInfo.getGuildId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_Turn_Off_Auction_Fail);
            return;
        }

        int Auction_countdown = auctionInfo.getGuildId() > 0 ? Global.Guild_auction_countdown : Global.Auction_countdown;
        //非密码交易需要有倒计时
        if (!isPassword && bean.getAuction_countdown() == 1 && (auctionInfo.getTime() + Auction_countdown * 1000 > TimeUtils.Time())) {
            return;
        }

        int price;
        if(isPassword){
            price = auctionInfo.getPrice();
        }else{
            if(bean.getAuction_price_type() == 1){
                price = auctionInfo.getPrice();
            }else{
                price = bean.getAuction_max_price() * auctionInfo.getItem().getNum();
            }
        }
        if (!Manager.currencyManager.manager().onDecItemCoin(player, price, ItemChangeReason.AuctionPurDec, auctionId, bean.getAuction_use_coin())) {
            return;
        }

        //拍卖成功处理
        auctionSuccess(auctionInfo, player);

        //给购买者发送邮件
        String itemName = Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId());
        String mailCoinName = Manager.backpackManager.manager().getName(bean.getAuction_use_coin());
        String successContent = MessageString.AuctionSuccessfulMail + "@_@" + price + "@_@" + itemName + "@_@" + mailCoinName;
        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.AuctionMail, MessageString.Auction,
                MessageString.AuctionSuccessfulMailTitle, successContent, getItems(auctionInfo.getItem(), true),ItemChangeReason.AuctionSuccessfulGet, auctionId);

        msg.setRes(0);
        MessageUtils.send_to_player(player, AuctionMessage.ResAuctionInfoPur.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        buildAuctionLog(auctionInfo, player.getId(), 3);

        if (bean.getType() == ItemTypeConst.EQUIP) {
            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(bean.getId());
            if (equipBean != null) {
                long key = equipBean.getGrade() * 100000 + equipBean.getQuality() * 1000 + equipBean.getPart();
                Manager.countManager.addCount(player, BaseCountType.AuctionBuy, key, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.AuchtionBuy, 1);
            }
        }else if(bean.getType() == ItemTypeConst.DEVIL_CARD){
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.AuchtionDevilCardBuy, Count.RefreshType.CountType_Forever, 1);
        }

        //拍卖行BI
        auctionBI(auctionInfo.getId(), player, bean, auctionInfo.getItem(), auctionInfo.getPrice(), Action_OneKey, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());

        AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
        builder.setId(auctionId);
        builder.setOwnId(auctionInfo.getOwnId());
        for (long roleId : auctionInfo.getRoleIds()) {
            if (roleId == player.getId()) {
                continue;
            }
            Player otherPlayer = Manager.playerManager.getPlayer(roleId);
            if (otherPlayer == null || !otherPlayer.isOnline()) {
                continue;
            }
            MessageUtils.send_to_player(otherPlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    private void auctionSuccess(AuctionInfo auctionInfo, Player player) {

        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
        String itemName = Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId());
        int price;
        if(StringUtils.isNotEmpty(auctionInfo.getPassword())){
            price = auctionInfo.getPrice();
        }else{
            if(bean.getAuction_price_type() == 1){
                price = auctionInfo.getPrice();
            }else{
                price = bean.getAuction_max_price() * auctionInfo.getItem().getNum();
            }
        }
        //退给别人
        if (auctionInfo.getRoleId() != 0) {
            List<Item> backItems = Item.createItems(bean.getAuction_use_coin(), auctionInfo.getPrice(), false);
            String mailCoinName = Manager.backpackManager.manager().getName(bean.getAuction_use_coin());
            String failContent = MessageString.AuctionFailureMail + "@_@" + itemName + "@_@" + player.getName()
                    + "@_@" + mailCoinName + "@_@" + mailCoinName;;
            Manager.mailManager.sendMailToPlayer(auctionInfo.getRoleId(), MailType.AuctionMail, MessageString.Auction,
                    MessageString.AuctionFailureMailTitle, failContent, backItems,ItemChangeReason.AuctionFailureGet, auctionInfo.getId());
        }

        Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
        buildWorldRecord(auctionInfo.getItem().getItemModelId(), price, auctionInfo.getItem().getNum());
        buildPersonRecord(player, auctionInfo.getItem().getItemModelId(), price, auctionInfo.getItem().getNum(), 0);

        if (auctionInfo.getOwnId() > 10000000) {
            Player ownPlayer = Manager.playerManager.getPlayer(auctionInfo.getOwnId());
            if (ownPlayer != null) {
                sendSuccessMail(ownPlayer, auctionInfo, price);
                buildPersonRecord(ownPlayer, auctionInfo.getItem().getItemModelId(), price, auctionInfo.getItem().getNum(), 1);
                if(ownPlayer.isOnline()){
                    AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
                    builder.setId(auctionInfo.getId());
                    builder.setOwnId(auctionInfo.getOwnId());
                    MessageUtils.send_to_player(ownPlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                }
            }
        } else if (auctionInfo.getGuildId() > 0) {
            sendGuildAuction(auctionInfo, price);
        }
        Manager.auctionManager.getAuctions().remove(auctionInfo.getId());
    }

    private void sendSuccessMail(Player player, AuctionInfo auctionInfo, int price) {
        int vipPer = Manager.vipManager.power().getVipDiscount(player, VipPower.POWER_27);
        int per = auctionInfo.getGuildId() > 0 ? Global.Guild_auction_earnings_tariff : Global.AuctionTax;
        int decPrice = price * per / 100;
        decPrice = decPrice == 0 ? 1: decPrice;
        int finalDec = decPrice * vipPer / 10000;
        int vipDec = decPrice - finalDec;
        String itemName = Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId());

        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
        List<Item> ownItems = Item.createItems(bean.getAuction_use_coin(), price - finalDec, false);
        String content = "";
        String mailCoinName = Manager.backpackManager.manager().getName(bean.getAuction_use_coin());
        if (vipDec > 0) {
            content = MessageString.SaleSuccessfulMail + "@_@" + (price - finalDec) + "@_@" + itemName + "@_@" + decPrice + "@_@" + vipDec
                    + "@_@" + mailCoinName + "@_@" + mailCoinName + "@_@" + mailCoinName;
        } else {
            content = MessageString.SaleSuccessfulMailNoXiuShen + "@_@" + (price - finalDec) + "@_@" + itemName + "@_@" + decPrice
                    + "@_@" + mailCoinName + "@_@" + mailCoinName;
        }
        Manager.mailManager.sendMailToPlayer(auctionInfo.getOwnId(), MailType.AuctionMail, MessageString.Auction,
                MessageString.SaleSuccessfulMailTitle, content, ownItems,ItemChangeReason.SaleSuccessfulGet, auctionInfo.getId());
    }

    private void sendGuildAuction(AuctionInfo auctionInfo, int price) {
        if (CfgManager.getCfg_Daily_Container().getValueByKey((int) auctionInfo.getOwnId()) != null) {
            if (ServerParamUtil.auctionRoleIDMap.containsKey(auctionInfo.getId())) {
                List<Long> roleList = ServerParamUtil.auctionRoleIDMap.get(auctionInfo.getId());
                int decPrice = (int) Math.ceil(price * Global.Guild_auction_earnings_tariff / 100);
                int lastPaice = price - decPrice;
                int maxPiece = (int) Math.ceil(lastPaice * Global.Guild_auction_earnings_cap / 100);
                int everyMaxPiece = (int) Math.ceil(lastPaice / roleList.size());
                everyMaxPiece = everyMaxPiece > maxPiece ? maxPiece : everyMaxPiece;
                everyMaxPiece = everyMaxPiece <= 1 ? 1 : everyMaxPiece;
                Cfg_Daily_Bean daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey((int) auctionInfo.getOwnId());
                String dailyName = daily_bean == null ? "" : daily_bean.getName();
                dailyName = ServerStr.getChatTableName(dailyName);

                Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
                for (long roleid : roleList) {
                    List<Item> ownItems = Item.createItems(bean.getAuction_use_coin(), everyMaxPiece, false);
                    String mailCoinName =  Manager.backpackManager.manager().getName(bean.getAuction_use_coin());
                    String content = MessageString.GuildBattleMailContext
                            + "@_@" + dailyName + "@_@" + Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId())
                            + "@_@" + 1 + "@_@" + price + "@_@" + everyMaxPiece
                            + "@_@" + mailCoinName + "@_@" + mailCoinName;
                    Manager.mailManager.sendMailToPlayer(roleid, MailType.AuctionMail, MessageString.Auction,
                            MessageString.GuildBattleMailTitle, content, ownItems,ItemChangeReason.SaleFailureGet, auctionInfo.getId());
                }

                if (ServerParamUtil.auctionRoleIDMap.containsKey(auctionInfo.getId())) {
                    ServerParamUtil.auctionRoleIDMap.remove(auctionInfo.getId());
                    ServerParamUtil.saveAuctionRoleID();
                }
            }
        }
    }

    @Override
    public void auctionFastPur(Player player, EquipMessage.ReqEquipSyn mess) {
        Cfg_Equip_synthesis_Bean equipSyn = CfgManager.getCfg_Equip_synthesis_Container().getValueByKey(mess.getId());
        if (equipSyn == null) {
            return;
        }
        Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(mess.getId());
        if (equipBean == null) {
            return;
        }

        //直接合成
        List<Long> eqs = new ArrayList<>(mess.getEquipIdsList());
        if (!mess.getIsHaveAuction()) {
            Manager.equipManager.deal().OnReqEquipSynthetic(player, equipSyn, eqs, false, mess.getType());
            return;
        }

        //未配置装备材料概率即不需要装备材料，首饰直接合成
        if (equipSyn.getJoin_num_probability() == null || equipSyn.getJoin_num_probability().size() == 0) {
            Manager.equipManager.deal().OnReqEquipSynthetic(player, equipSyn, eqs, true, mess.getType());
            return;
        }

        //自动从拍卖行购买,计算概率需购买数量
        int needBuyNum = 0;
        long totalProbability = 0L;
        int gradeIndex, qualityIndex, diamondIndex;
        for (Long eId : eqs) {
            Equip equip = (Equip) Manager.backpackManager.manager().getItemById(player, eId);
            if (equip == null) {
                continue;
            }
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (equipBean.getGrade() < bean.getGrade()) {
                gradeIndex = 1;
            } else if (equipBean.getGrade() == bean.getGrade()) {
                gradeIndex = 0;
            } else if (equipBean.getGrade() - bean.getGrade() <= 2) {
                gradeIndex = equipBean.getGrade() - bean.getGrade() == 1 ? 2 : 3;
            } else {
                logger.error("装备阶数过低，不能合成");
                return;
            }
            if (equipSyn.getJoin_num_probability().size() <= gradeIndex) {
                logger.error("Cfg_Equip_synthesis_Bean装备合成配置表配置错误：" + equipSyn.getId() + ", grade" + bean.getGrade());
                return;
            }

            qualityIndex = getArrayIndex(equipSyn.getQuality(), bean.getQuality());
            diamondIndex = getArrayIndex(equipSyn.getDiamond(), bean.getDiamond_Number());
            if (qualityIndex == -1 || diamondIndex == -1) {
                logger.error(String.format("Cfg_Equip_synthesis_Bean装备合成配置表概率配置错误%s： 品质：%s，星数：%s",
                        equipSyn.getId(), bean.getQuality(), bean.getDiamond_Number()));
                return;
            }
            totalProbability += 1L * equipSyn.getJoin_num_probability().get(gradeIndex)
                    * equipSyn.getQuality_Number().get(qualityIndex)
                    * equipSyn.getDiamond_Number().get(diamondIndex);
        }
        long total = 10000 * 10000 * 10000L;
        if (totalProbability < total) {
            needBuyNum = (int) Math.ceil((total - totalProbability) / 10000_0000 / equipSyn.getJoin_num_probability().get(0));
        }
        if (needBuyNum == 0) {
            Manager.equipManager.deal().OnReqEquipSynthetic(player, equipSyn, eqs, true, mess.getType());
            return;
        }

        //背包格子不足
        if (Manager.backpackManager.manager().getEmptyGridNum(player) < needBuyNum) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.NoBagCell);
            return;
        }

        //拍卖行找装备
        Map<Integer, Integer> needAllCoin = new HashMap<>();
        List<AuctionInfo> list = new ArrayList<>();
        for (AuctionInfo auctionInfo : Manager.auctionManager.getAuctions().values()) {
            //密码交易物品不能直接购买
            if(StringUtils.isNotEmpty(auctionInfo.getPassword())){
                continue;
            }
            if (list.size() >= needBuyNum) {
                break;
            }
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
            if (itemBean.getType() != ItemTypeConst.EQUIP) {
                continue;
            }

            //只购买指定职业同阶金色1星装备
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
            if (!bean.getGender().contains(equipSyn.getProfessional()) ||
                    equipBean.getGrade() != bean.getGrade() ||
                    equipSyn.getQuality().get(0) != bean.getQuality() ||
                    equipSyn.getDiamond().get(0) != bean.getDiamond_Number()) {
                continue;
            }

            if (bean.getAuction_max_price() == -1) {
                continue;
            }

            if (auctionInfo.getGuildId() != 0 && player.getGuildId() != auctionInfo.getGuildId()) {
                continue;
            }

            int Auction_countdown = auctionInfo.getGuildId() > 0 ? Global.Guild_auction_countdown : Global.Auction_countdown;
            if (bean.getAuction_countdown() == 1 && (auctionInfo.getTime() + Auction_countdown * 1000 > TimeUtils.Time())) {
                continue;
            }

            int needCoin = bean.getAuction_max_price() * auctionInfo.getItem().getNum();
            int coinType = bean.getAuction_use_coin();
            Integer allCoin = needAllCoin.get(coinType);
            if(allCoin != null){
                needCoin += allCoin;
            }
            needAllCoin.put(coinType, needCoin);
            list.add(auctionInfo);
        }

        //拍卖行物品不足
        if (list.size() < needBuyNum) {
            sendResEquipSyn(player, 2, mess.getType());
            return;
        }

        //货币不足
        if (needAllCoin.size() > 0) {
            for(Map.Entry<Integer, Integer> entry : needAllCoin.entrySet()){
                if(!Manager.currencyManager.manager().canRemoveCurrency(player, entry.getKey(), entry.getValue())){
                    sendResEquipSyn(player, 3, mess.getType());
                    return;
                }
            }

            for(Map.Entry<Integer, Integer> entry : needAllCoin.entrySet()){
                if (!Manager.currencyManager.manager().onDecItemCoin(player, entry.getValue(), ItemChangeReason.AuctionFastPurDec, equipSyn.getId(), entry.getKey())) {
                    sendResEquipSyn(player, 3, mess.getType());
                    return;
                }
            }
        }


        //自动购买处理
        int num = 0;
        for (AuctionInfo auction : list) {
            //添加到合成列表
            eqs.add(auction.getItem().getId());

            auctionSuccess(auction, player);

            Manager.backpackManager.manager().addItems(player, getItems(auction.getItem(), true), ItemChangeReason.AuctionFastPurGet, auction.getId());
//            Manager.saveThreadManager.getOtherServerSave().deal(auction.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
//
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(auction.getItem().getItemModelId());
//            buildWorldRecord(auction.getItem().getItemModelId(), itemBean.getAuction_max_price() * auction.getItem().getNum(), auction.getItem().getNum());
//            buildPersonRecord(player, auction.getItem().getItemModelId(), itemBean.getAuction_max_price() * auction.getItem().getNum(), auction.getItem().getNum(), 0);
            buildAuctionLog(auction, player.getId(), 9);

            Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(itemBean.getId());
            if (eqBean != null) {
                long key = eqBean.getGrade() * 100000 + eqBean.getQuality() * 1000 + eqBean.getPart();
                Manager.countManager.addCount(player, BaseCountType.AuctionBuy, key, Count.RefreshType.CountType_Forever, 1);
                num++;
            }
        }
        if (num > 0) Manager.controlManager.operate(player, FunctionVariable.AuchtionBuy, 1);
        Manager.equipManager.deal().OnReqEquipSynthetic(player, equipSyn, eqs, true, mess.getType());
    }

    private int getArrayIndex(ReadIntegerArray array, int target) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == target) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void auctionTick() {
        Iterator iterator = Manager.auctionManager.getAuctions().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, AuctionInfo> entry = (Map.Entry<Long, AuctionInfo>) iterator.next();
            AuctionInfo auctionInfo = entry.getValue();
            int Auction_time = getOverTime(auctionInfo);
            if (auctionInfo.getTime() + Auction_time * 1000 > TimeUtils.Time()) {
                continue;
            }
            Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
            if (bean == null) {
                logger.error("配置不存在" + auctionInfo.getItem().getItemModelId());
                return;
            }
            List<Integer> list = new ArrayList<>();
            for (ReadArray<Integer> l : Global.Trade_First_Buy_Item.getValuees()) {
                list.add(l.get(1));
            }

            //系统自动回收此道具
            if (list.contains(auctionInfo.getItem().getItemModelId()) && auctionInfo.getOwnId() != 0 && auctionInfo.getOwnId() > 10000000) {
                Player ownPlayer = Manager.playerManager.getPlayer(auctionInfo.getOwnId());
                if (ownPlayer != null) {
                    sendSuccessMail(ownPlayer, auctionInfo, auctionInfo.getPrice());
                    buildPersonRecord(ownPlayer, auctionInfo.getItem().getItemModelId(), auctionInfo.getPrice(), auctionInfo.getItem().getNum(), 1);
                }
                Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
                buildAuctionLog(auctionInfo, 0, 7);
                iterator.remove();
                continue;
            }

            String mailCoinName = Manager.backpackManager.manager().getName(bean.getAuction_use_coin());
            //竞拍成功
            if (auctionInfo.getRoleId() != 0) {
                String successContent = MessageString.AuctionSuccessfulMail + "@_@" + auctionInfo.getPrice() + "@_@" + Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId())
                        + "@_@" + mailCoinName;
                Manager.mailManager.sendMailToPlayer(auctionInfo.getRoleId(), MailType.AuctionMail, MessageString.Auction,
                        MessageString.AuctionSuccessfulMailTitle, successContent, getItems(auctionInfo.getItem(), true),ItemChangeReason.AuctionSuccessfulGet, auctionInfo.getId());
                //todo 未考虑系统分红

                Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
                buildWorldRecord(auctionInfo.getItem().getItemModelId(), auctionInfo.getPrice(), auctionInfo.getItem().getNum());

                if (auctionInfo.getRoleId() != 0) {
                    Player player = Manager.playerManager.getPlayer(auctionInfo.getRoleId());
                    if (player != null) {
                        buildPersonRecord(player, auctionInfo.getItem().getItemModelId(), auctionInfo.getPrice(), auctionInfo.getItem().getNum(), 0);
                    }
                }

                if (auctionInfo.getOwnId() != 0 && auctionInfo.getOwnId() > 10000000) {
                    Player ownPlayer = Manager.playerManager.getPlayer(auctionInfo.getOwnId());
                    if (ownPlayer != null) {
                        sendSuccessMail(ownPlayer, auctionInfo, auctionInfo.getPrice());
                        buildPersonRecord(ownPlayer, auctionInfo.getItem().getItemModelId(), auctionInfo.getPrice(), auctionInfo.getItem().getNum(), 1);
                        if(ownPlayer.isOnline()){
                            AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
                            builder.setId(auctionInfo.getId());
                            builder.setOwnId(auctionInfo.getOwnId());
                            MessageUtils.send_to_player(ownPlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                        }
                    }
                } else if (auctionInfo.getGuildId() > 0) {
                    sendGuildAuction(auctionInfo, auctionInfo.getPrice());
                }
                buildAuctionLog(auctionInfo, 0, 7);

                Player player = Manager.playerManager.getPlayerCache(auctionInfo.getRoleId());
                if (player != null) {
                    if (bean != null) {
                        if(bean.getType() == ItemTypeConst.EQUIP){
                            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(bean.getId());
                            if (equipBean != null) {
                                long key = equipBean.getGrade() * 100000 + equipBean.getQuality() * 1000 + equipBean.getPart();
                                Manager.countManager.addCount(player, BaseCountType.AuctionBuy, key, Count.RefreshType.CountType_Forever, 1);
                                Manager.controlManager.operate(player, FunctionVariable.AuchtionBuy, 1);
                            }
                        }else if(bean.getType() == ItemTypeConst.DEVIL_CARD){
                            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.AuchtionDevilCardBuy, Count.RefreshType.CountType_Forever, 1);
                        }
                    }
                }

                iterator.remove();
                //拍卖行BI
                auctionBI(auctionInfo.getId(), auctionInfo.getRoleId(), auctionInfo.getItem(), auctionInfo.getPrice(), Action_Ok, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
                continue;
            }

            //世界流拍
            if (auctionInfo.getGuildId() <= 0) {
                if (auctionInfo.getOwnId() != 0 && auctionInfo.getOwnId() > 10000000) {
                    Player ownPlayer = Manager.playerManager.getPlayer(auctionInfo.getOwnId());
                    if (ownPlayer != null) {
                        String content = MessageString.SaleFailureMail + "@_@" + Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId());
                        Manager.mailManager.sendMailToPlayer(auctionInfo.getOwnId(), MailType.AuctionMail, MessageString.Auction,
                                MessageString.SaleFailureMailTitle, content, getItems(auctionInfo.getItem(), false),ItemChangeReason.SaleFailureGet, auctionInfo.getId());
                        if(ownPlayer.isOnline()){
                            AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
                            builder.setId(auctionInfo.getId());
                            builder.setOwnId(auctionInfo.getOwnId());
                            MessageUtils.send_to_player(ownPlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                        }
                    }
                }
                Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
                buildAuctionLog(auctionInfo, 0, 6);
                iterator.remove();
                //拍卖行BI
                auctionBI(auctionInfo.getId(), auctionInfo.getOwnId(), auctionInfo.getItem(), auctionInfo.getPrice(), Action_TimeOutDown, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
                continue;
            }

            //仙盟流拍
            if (auctionInfo.getGuildId() > 0) {
                if (auctionInfo.getOwnId() != 0) {
                    //仙盟个人流拍
                    if (auctionInfo.getOwnId() > 10000000) {
                        String content = MessageString.SaleFailureMail + "@_@" + Manager.backpackManager.manager().getName(auctionInfo.getItem().getItemModelId());
                        Manager.mailManager.sendMailToPlayer(auctionInfo.getOwnId(), MailType.AuctionMail, MessageString.Auction,
                                MessageString.SaleFailureMailTitle, content, getItems(auctionInfo.getItem(), false),ItemChangeReason.SaleFailureGet, auctionInfo.getId());
                        Player onlinePlayer = Manager.playerManager.getPlayerOnline(auctionInfo.getOwnId());
                        if(onlinePlayer != null){
                            AuctionMessage.ResAuctionDelete.Builder builder = AuctionMessage.ResAuctionDelete.newBuilder();
                            builder.setId(auctionInfo.getId());
                            builder.setOwnId(auctionInfo.getOwnId());
                            MessageUtils.send_to_player(onlinePlayer, AuctionMessage.ResAuctionDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                        }
                        Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_DELETE, SaveServer.DELETE);
                        buildAuctionLog(auctionInfo, 0, 6);
                        iterator.remove();
                        //拍卖行BI
                        auctionBI(auctionInfo.getId(), auctionInfo.getOwnId(), auctionInfo.getItem(), auctionInfo.getPrice(), Action_TimeOutDown, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
                        continue;
                    } else {
                        //仙盟活动流拍 改为 世界拍卖
                        auctionInfo.setGuildId(0);
                        auctionInfo.setTime(TimeUtils.Time());
                        Manager.saveThreadManager.getOtherServerSave().deal(auctionInfo.toAuctionBean(), DbSqlName.AUCTION_UPDATE, SaveServer.UPDATE);
                        buildAuctionLog(auctionInfo, 0, 5);
                        if (ServerParamUtil.auctionRoleIDMap.containsKey(auctionInfo.getId())) {
                            ServerParamUtil.auctionRoleIDMap.remove(auctionInfo.getId());
                            ServerParamUtil.saveAuctionRoleID();
                        }
                        //拍卖行BI
                        auctionBI(auctionInfo.getId(), auctionInfo.getOwnId(), auctionInfo.getItem(), auctionInfo.getPrice(), Action_GoWorldAuction, auctionInfo.getOwnId() < 10000000 ? 3 : auctionInfo.getGuildId() <= 0 ? 1 : 2, auctionInfo.getGuildId());
                    }
                }
            }
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.AuctionBaseScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    private AuctionMessage.AuctionInfo.Builder buildAuctionInfo(AuctionInfo auctionInfo) {
        AuctionMessage.AuctionInfo.Builder info = AuctionMessage.AuctionInfo.newBuilder();
        int Auction_time = getOverTime(auctionInfo);
        info.setTime((int) ((auctionInfo.getTime() + Auction_time * 1000 - TimeUtils.Time()) / 1000));
        info.setId(auctionInfo.getId());
        info.setPrice(auctionInfo.getPrice());
        info.setGuildId(auctionInfo.getGuildId());
        info.setRoleId(auctionInfo.getRoleId());
        info.setOwnId(auctionInfo.getOwnId());
        if (auctionInfo.getItem() != null) {
            info.setItem(Manager.backpackManager.manager().buildItemInfo(auctionInfo.getItem()));
        }
        info.addAllRoleIds(auctionInfo.getRoleIds());
        info.setIsPassword(StringUtils.isEmpty(auctionInfo.getPassword()) ? false : true);
        return info;
    }

    private AuctionMessage.AuctionRecord.Builder buildAuctionRecord(AuctionRecord auctionRecord) {
        AuctionMessage.AuctionRecord.Builder info = AuctionMessage.AuctionRecord.newBuilder();
        info.setItemId(auctionRecord.getItemId());
        info.setPrice(auctionRecord.getPrice());
        info.setType(auctionRecord.getType());
        info.setNum(auctionRecord.getNum());
        info.setTime(auctionRecord.getTime() / 1000);
        return info;
    }

    private void buildAuctionLog(AuctionInfo auctionInfo, long playerId, int operate) {
        AuctionLog auctionLog = new AuctionLog();
        auctionLog.setOperateId(playerId);
        auctionLog.setOperate(operate);
        auctionLog.setType(auctionInfo.getGuildId());
        auctionLog.setOwnId(auctionInfo.getOwnId());
        auctionLog.setPrice(auctionInfo.getPrice());
        auctionLog.setAuctionId(auctionInfo.getId());
        auctionLog.setNum(auctionInfo.getItem().getNum());
        auctionLog.setItemUid(auctionInfo.getItem().getId());
        auctionLog.setItemId(auctionInfo.getItem().getItemModelId());
        auctionLog.setPassword(auctionInfo.getPassword());
        LogService.getInstance().execute(auctionLog);
    }

    private void buildWorldRecord(int itemId, int price, int num) {
        List<AuctionRecord> list = ServerParamUtil.auctionRecords;
        AuctionRecord auctionRecord = new AuctionRecord();
        auctionRecord.setItemId(itemId);
        auctionRecord.setPrice(price);
        auctionRecord.setNum(num);
        auctionRecord.setTime(TimeUtils.Time());

        if (list.size() >= Global.Auctioneer_Records_Number) {
            list.remove(0);
        }
        list.add(auctionRecord);
        ServerParamUtil.saveAuctionRecord();
    }

    private void buildPersonRecord(Player player, int itemId, int price, int num, int type) {
        AuctionRecord auctionRecord = new AuctionRecord();
        auctionRecord.setItemId(itemId);
        auctionRecord.setPrice(price);
        auctionRecord.setType(type);
        auctionRecord.setNum(num);
        auctionRecord.setTime(TimeUtils.Time());

        if (player.getAuctionRecords().size() >= Global.Auctioneer_Records_Number) {
            player.getAuctionRecords().remove(0);
        }
        player.getAuctionRecords().add(auctionRecord);
    }

    private int getAuctionPutNum(Player player) {
        int num = 0;
        Iterator iterator = Manager.auctionManager.getAuctions().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, AuctionInfo> entry = (Map.Entry<Long, AuctionInfo>) iterator.next();
            AuctionInfo auctionInfo = entry.getValue();
            if (auctionInfo.getOwnId() == player.getId()) {
                num++;
            }
        }
        return num;
    }

    private List<Item> getItems(Item item, boolean isBind) {
        List<Item> items = new ArrayList<>();
        item.setBind(isBind);
        items.add(item);
        return items;
    }

    private void sendResEquipSyn(Player player, int state, int type) {
        EquipMessage.ResEquipSyn.Builder msg = EquipMessage.ResEquipSyn.newBuilder();
        msg.setState(state);
        msg.setType(type);
        MessageUtils.send_to_player(player, EquipMessage.ResEquipSyn.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void auctionBI(long targetId, Player player, Cfg_Item_Bean bean, Item item, int auctionPrice, int opType, int roleType, long guild_id) {
        int maxPrice = 0;
        if (bean.getAuction_max_price() > 0) {
            maxPrice = bean.getAuction_max_price() * item.getNum();
        }
        if (bean.getType() == ItemTypeConst.EQUIP) {
            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            if (equipBean != null) {
                Manager.biManager.getScript().biAuction(player.getId(), player.getLevel(), player.getName(), targetId, roleType, opType, item.getItemModelId(), bean.getType(), equipBean.getQuality(), equipBean.getGrade(), equipBean.getDiamond_Number(), item.getId(), bean.getName(), item.getNum(), auctionPrice, maxPrice, guild_id);
            }
        } else {
            Manager.biManager.getScript().biAuction(player.getId(), player.getLevel(), player.getName(), targetId, roleType, opType, item.getItemModelId(), bean.getType(), bean.getColor(), 0, 0, item.getId(), bean.getName(), item.getNum(), auctionPrice, maxPrice, guild_id);
        }
    }

    private void auctionBI(long targetId, long roleId, Item item, int auctionPrice, int opType, int roleType, long guild_id) {
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (bean == null) {
            return;
        }

        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(roleId);
        if (pwi == null) {
            return;
        }

        int maxPrice = 0;
        if (bean.getAuction_max_price() > 0) {
            maxPrice = bean.getAuction_max_price() * item.getNum();
        }
        if (bean.getType() == ItemTypeConst.EQUIP) {
            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            if (equipBean != null) {
                Manager.biManager.getScript().biAuction(pwi.getRoleid(), pwi.getLevel(), pwi.getRolename(),targetId, roleType, opType, item.getItemModelId(), bean.getType(), equipBean.getQuality(), equipBean.getGrade(), equipBean.getDiamond_Number(), item.getId(), equipBean.getName(), item.getNum(), auctionPrice, maxPrice, guild_id);
            }
        } else {
            Manager.biManager.getScript().biAuction(pwi.getRoleid(), pwi.getLevel(), pwi.getRolename(),targetId, roleType, opType, item.getItemModelId(), bean.getType(), bean.getColor(), 0, 0, item.getId(), bean.getName(), item.getNum(), auctionPrice, maxPrice, guild_id);
        }
    }

    private void activityAuctionBI(long targetId, long dailyId, Item item, int auctionPrice, int opType, int roleType, long guildId) {
        Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
        if (bean == null) {
            return;
        }

        long role_id = dailyId;
        String role_name = "活动上架";
        int role_level = 0;
        int maxPrice = 0;
        if (bean.getAuction_max_price() > 0) {
            maxPrice = bean.getAuction_max_price() * item.getNum();
        }
        if (bean.getType() == ItemTypeConst.EQUIP) {
            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            if (equipBean != null) {
                Manager.biManager.getScript().biAuction(role_id, role_level, role_name, targetId, roleType, opType, item.getItemModelId(), bean.getType(), equipBean.getQuality(), equipBean.getGrade(), equipBean.getDiamond_Number(), item.getId(), bean.getName(), item.getNum(), auctionPrice, maxPrice, guildId);
            }
        } else {
            Manager.biManager.getScript().biAuction(role_id, role_level, role_name, targetId, roleType, opType, item.getItemModelId(), bean.getType(), bean.getColor(), 0, 0, item.getId(), bean.getName(), item.getNum(), auctionPrice, maxPrice, guildId);
        }
    }

    private int getOverTime(AuctionInfo auctionInfo) {
        if (auctionInfo.getItem() == null) {
            return 0;
        }
        Cfg_Item_Bean item_bean = CfgManager.getCfg_Item_Container().getValueByKey(auctionInfo.getItem().getItemModelId());
        if (item_bean == null)
            return 0;
        return auctionInfo.getGuildId() > 0 ? item_bean.getAuction_guild_all_time() : item_bean.getAuction_all_time();
    }

    public boolean checkIsGuildAuction(Player player, long roleID) {
        long checkRoleID;
        if (roleID > 0) {
            checkRoleID = roleID;
        } else {
            checkRoleID = player.getId();
        }
        ConcurrentHashMap<Long, AuctionInfo> auctions = Manager.auctionManager.getAuctions();
        for (Map.Entry<Long, AuctionInfo> auctionInfoEntry : auctions.entrySet()) {
            AuctionInfo ac = auctionInfoEntry.getValue();
            if (ac.getGuildId() <= 0)
                continue;
            if (ac.getOwnId() == checkRoleID)
                return true;
            if (ac.getRoleId() == checkRoleID)
                return true;
        }
        return false;
    }
}
