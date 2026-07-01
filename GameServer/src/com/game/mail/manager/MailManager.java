package com.game.mail.manager;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Item_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.backpack.structs.ItemTypeConst;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.db.bean.Mail;
import com.game.db.dao.MailDao;
import com.game.mail.log.MailLog;
import com.game.mail.structs.MailData;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.structs.GlobalType;
import com.game.structs.ItemChangeAction;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import game.message.mailMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MailManager {

    private static final Logger log = LogManager.getLogger(MailManager.class);

    //邮件变化动作，1：邮件收取，2：邮件读取，3：邮件附件领取，4：玩家一键删除邮件，5，邮件到期系统删除
    private static final int MAIL_RECEIVE = 1;
    private static final int MAIL_READ = 2;
    private static final int MAIL_ATTACH_GET = 3;
    private static final int MAIL_DELETE_BY_PLAYER = 4;
    private static final int MAIL_DELETE_BY_SYSTEM = 5;

    //每天0点清理一次数据库中的到期邮件
    public static final int CLEAR_HOUR = 0;

    //保存记录清理的时间(天数)
    private static int clearDay;

    private final MailDao mailDao = new MailDao();

    //玩家超过50封邮件后的剩余邮件列表缓存(<playerId, surplusMailList>)
    private static final ConcurrentHashMap<Long, List<MailData>> surplusMailMap = new ConcurrentHashMap<>();

    public static String linkContext(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object str : args) {
            sb.append(str).append("@_@");
        }
        sb.delete(sb.lastIndexOf("@_@"), sb.length());
        return sb.toString();
    }

    /**
     * 请求读取邮件内容
     *
     * @param player 玩家
     * @param mailId 邮件id
     */
    public void reqReadMail(Player player, long mailId) {
        MailData mailData = player.getMailCacheMap().get(mailId);
        if (mailData == null) {
            log.error("不存在邮件mailId=" + mailId);
            return;
        }

        //设置已读标志
        mailData.setIsRead((byte) 1);
        mailData.setSaveType(SaveServer.UPDATE);
        if (!saveMailToDB(mailData)) {
            log.error("读取邮件" + mailData.getMailId() + "更新失败！");
            return;
        }

        //记录日志
        writeMailLog(player, MAIL_READ, mailData, 0);

        //返回消息
        mailMessage.ResReadMail.Builder resMsg = mailMessage.ResReadMail.newBuilder();
        resMsg.setMailDetailInfo(createMailDetailInfo(mailData));
        MessageUtils.send_to_player(player, mailMessage.ResReadMail.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    /**
     * 请求领取单封邮件的附件物品
     *
     * @param player 玩家
     * @param mailId 邮件id
     */
    public void reqReceiveSingleMailAttach(Player player, long mailId) {
        MailData mailData = player.getMailCacheMap().get(mailId);
        if (mailData == null) {
            log.error("不存在邮件mailId=" + mailId);
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        mailMessage.ResReceiveSingleMailAttach.Builder resMsg = mailMessage.ResReceiveSingleMailAttach.newBuilder();
        resMsg.setMailId(mailId);
        resMsg.setIsAttachReceived(false);
        if (receiveAttachItem(player, mailData, actionId) == 1) {
            mailData.setIsRead((byte) 1);
            mailData.setIsAttachReceived((byte) 1);
            mailData.setSaveType(SaveServer.UPDATE);
            if (!saveMailToDB(mailData)) {
                log.error("领取单封邮件" + mailData.getMailId() + "附件后邮件更新失败！");
            }
            resMsg.setIsAttachReceived(true);
            writeMailLog(player, MAIL_ATTACH_GET, mailData, actionId);
        }

        MessageUtils.send_to_player(player, mailMessage.ResReceiveSingleMailAttach.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    /**
     * 请求一键领取邮件的附件物品
     *
     * @param player     玩家
     * @param mailIdList 邮件id列表
     */
    public void reqOneClickReceiveMailAttach(Player player, List<Long> mailIdList) {
        Manager.countManager.addCount(player, BaseCountType.OneKeyMailRecv, 0, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.MailRecive, 1);
        for (long mailId : mailIdList) {
            MailData mailData = player.getMailCacheMap().get(mailId);
            if (mailData == null) {
                log.error("不存在邮件mailId=" + mailId);
                continue;
            }
            long actionId = IDConfigUtil.getLogId();
            int result = receiveAttachItem(player, mailData, actionId);
            if (result == 1) {
                mailData.setIsRead((byte) 1);
                mailData.setIsAttachReceived((byte) 1);
                mailData.setSaveType(SaveServer.UPDATE);
                if (!saveMailToDB(mailData)) {
                    log.error("一键领取邮件" + mailData.getMailId() + "附件后邮件更新失败！");
                }
                writeMailLog(player, MAIL_ATTACH_GET, mailData, actionId);
            }
            //背包空间不足直接break
            if (result == 2) {
                break;
            }
        }
        //领取处理完后同步邮件列表信息到客户端
        syncMailInfoListToClient(player);
    }

    /**
     * 领取邮件附件物品包括货币在内
     *
     * @param player   玩家
     * @param mailData 邮件
     * @param actionId actionId
     * @return 0：无附件可领或已领取，1：领取成功，2：背包空间不足，-1：领取失败
     */
    private int receiveAttachItem(Player player, MailData mailData, long actionId) {
        if (mailData.getIsAttachReceived() == 1) {
            log.error("邮件Id=" + mailData.getMailId() + " 附件已领取！");
            return 0;
        }

        if (mailData.getItemList().isEmpty() || mailData.getHasAttachment() == (byte) 0) {
            log.error("Error，附件没有物品可领取！");
            return 0;
        }

        int msId = Manager.backpackManager.manager().onHasAddSpaces(player, mailData.getItemList());
        if (msId != 0) {
            log.error("背包空间不足！");
            MessageUtils.notify_player(player, Notify.ERROR, msId);
            return 2;
        }

        //防止邮件与背包物品共享同一实例导致邮件物品堆叠
        List<Item> itemList = new ArrayList<>();
        for (Item item : mailData.getItemList()) {
            itemList.add(Item.createItem(item.getItemModelId(), item.getNum(), item.isBind()));
        }

        if (Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.MailAttachReceiveGet, actionId)) {
            return 1;
        }

        return -1;
    }

    /**
     * 请求一键删除邮件
     *
     * @param player     玩家
     * @param mailIdList 邮件id列表
     */
    public void reqOneClickDeleteMail(Player player, List<Long> mailIdList) {
        for (Long mailId : mailIdList) {
            MailData mailData = player.getMailCacheMap().get(mailId);

            if (mailData == null) {
                log.error("不存在要删除的邮件Id=" + mailId);
                continue;
            }
            if (!mailData.getItemList().isEmpty() && mailData.getHasAttachment() == (byte) 1 && mailData.getIsAttachReceived() == 0) {
                log.error("邮件Id=" + mailData.getMailId() + " 附件未领取！");
                continue;
            }

            //因为还要清理mailCacheMap中的mail，所以直接调Dao接口而不通过MailThread去处理
            if (mailDao.delete(DbSqlName.MAIL_DELETE_BY_MAILID.getName(), mailId) > 0) {
                writeMailLog(player, MAIL_DELETE_BY_PLAYER, mailData, -2);
                player.getMailCacheMap().remove(mailId);
            }
        }
        //删除处理完后直接同步邮件列表信息到客户端
        syncMailInfoListToClient(player);
    }

    //请求获取玩家剩余邮件列表(超过50封未发给客户端的，登录最多发50封) [此请求不需要了，删除邮件后同步更新会将剩余邮件补上]
    public void reqGetRemainMailList(Player player) {
        syncMailInfoListToClient(player); //发送邮件列表信息到客户端
    }

    /**
     * World请求同步邮件信息
     */
    public void reqSyncToGameMailInfo(String mailInfo) {
        log.info("world请求同步mailInfo");
        MailData mailData = JsonUtils.parseObject(mailInfo, MailData.class);
        if (mailData == null) {
            log.error("来自world的mailInfo解析出错！");
            return;
        }
        Player player = Manager.playerManager.getPlayerOnline(mailData.getReceiverId());
        if (player == null) {
            log.error("player == null");
            return;
        }
        sendNewMailToClient(player, mailData);
    }

    /**
     * 登录加载邮件列表
     */
    public void loginLoadMail(Player player) {
        if (player == null) {
            return;
        }
        player.getMailCacheMap().clear();

        int mailNum = 0;
        List<MailData> surplusMailList = new ArrayList<>();
        List<Mail> mailList = mailDao.selectByReceiverId(player.getId());
        for (Mail mail : mailList) {
            MailData mailData = JsonUtils.parseObject(mail.getMailData(), MailData.class);
            if (mailData == null) {
                log.error("mailData解析错误！");
                continue;
            }

            //过期邮件进行清理
            if (mail.getReceiveTime() < getMailDeleteTime()) {
                mail.setWhere(mail.getMailId());
                Manager.saveThreadManager.getOtherServerSave().deal(mail, DbSqlName.MAIL_DELETE_BY_MAILID, SaveServer.DELETE);
                writeMailLog(player, MAIL_DELETE_BY_SYSTEM, mailData, -1);
                continue;
            }

            mailData.setSaveType(SaveServer.UPDATE);
            if (mailNum >= getMailMax()) {
                surplusMailList.add(mailData);
            } else {
                player.getMailCacheMap().put(mailData.getMailId(), mailData);
            }
            mailNum++;
        }

        if (!surplusMailList.isEmpty()) {
            surplusMailMap.put(player.getId(), surplusMailList);
        }
        //发送邮件列表信息到客户端
        syncMailInfoListToClient(player);
    }

    //登录、一键领取和删除后、客户端主动请求剩余邮件时 同步发送邮件列表信息到客户端
    private void syncMailInfoListToClient(Player player) {

        int sendMailNum = player.getMailCacheMap().size();
        int surplusMailNum = 0;
        //如果缓存中邮件不足50封，并且还存在未发送邮件，则将未发送邮件移入缓存中一起发给客户端
        if (sendMailNum < getMailMax() && surplusMailMap.containsKey(player.getId())) {
            List<MailData> surplusMailList = surplusMailMap.get(player.getId());
            Iterator<MailData> iterator = surplusMailList.iterator();
            while (iterator.hasNext()) {
                MailData mailData = iterator.next();
                player.getMailCacheMap().put(mailData.getMailId(), mailData);
                iterator.remove();
                //达到发送邮件上限
                if (++sendMailNum >= getMailMax()) {
                    break;
                }
            }
            surplusMailNum = surplusMailMap.get(player.getId()).size();
            //如果未发送邮件数为0，则移除
            if (surplusMailNum == 0) {
                surplusMailMap.remove(player.getId());
            }
        }
        if (surplusMailMap.containsKey(player.getId())) {
            surplusMailNum = surplusMailMap.get(player.getId()).size();
        }

        //返回消息
        mailMessage.ResMailInfoList.Builder resMsg = mailMessage.ResMailInfoList.newBuilder();
        for (MailData mailData : player.getMailCacheMap().values()) {
            resMsg.addMailList(createMailSummaryInfo(mailData));
        }
        resMsg.setRemainMailNum(surplusMailNum);
        MessageUtils.send_to_player(player, mailMessage.ResMailInfoList.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    /**
     * 每天0点定期清理一次数据库中的到期邮件
     */
    public void clearOverTimeMail() {
        int curDay = TimeUtils.getCurDay(CLEAR_HOUR);
        if (clearDay == curDay) {
            return;
        }
        //需要同步的玩家
        Set<Long> players = new HashSet<>();

        List<Mail> allMailList = Manager.mailManager.mailDao.selectAll();
        //工会跨天邮件
        List<Mail> guildMailList = allMailList.stream()
                .filter(n -> (n.getType() == MailType.GuildMail) &&
                        (TimeUtils.isSameDay(n.getReceiveTime(), TimeUtils.Time())))
                .collect(Collectors.toList());
        //过期邮件
        List<Mail> mailList = Manager.mailManager.mailDao.selectByReceiveTime(getMailDeleteTime());
        mailList.addAll(guildMailList);
        for (Mail mail : mailList) {
            log.info("清理邮件Id:" + mail.getMailId());
            MailData mailData = JsonUtils.parseObject(mail.getMailData(), MailData.class);
            if (mailData == null) {
                log.error("mailData解析错误！");
                continue;
            }
            mail.setWhere(mail.getMailId());
            Manager.saveThreadManager.getOtherServerSave().deal(mail, DbSqlName.MAIL_DELETE_BY_MAILID, SaveServer.DELETE);

            Player player = Manager.playerManager.getPlayerCache(mail.getReceiverId());
            if (player == null) {
                continue;
            }
            if (player.getMailCacheMap().containsKey(mail.getMailId())) {
                player.getMailCacheMap().remove(mail.getMailId());
                players.add(player.getId());
            } else {
                List<MailData> surplusMailList = surplusMailMap.get(player.getId());
                if (surplusMailList == null) {
                    log.error("清理邮件时，玩家不存在该邮件，mailId:" + mail.getMailId());
                    continue;
                }
                Iterator<MailData> iterator = surplusMailList.iterator();
                while (iterator.hasNext()) {
                    MailData data = iterator.next();
                    if (mail.getMailId() == data.getMailId()) {
                        iterator.remove();
                        break;
                    }
                }
            }

            writeMailLog(player, MAIL_DELETE_BY_SYSTEM, mailData, -1);
        }
        for (Long roleId : players) {
            Player player = Manager.playerManager.getPlayerCache(roleId);
            if (player == null) {
                continue;
            }
            syncMailInfoListToClient(player);
        }
        clearDay = curDay;
    }

    /**
     * 给玩家发送邮件接口
     *
     * @param receiverId     收件人角色Id
     * @param mailType       邮件类型，1：系统，2：后台
     * @param sender         邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)
     * @param mailTitle      邮件标题
     * @param mailContent    邮件内容
     * @param attachItemList 邮件附件物品列表(包括货币在内)
     * @param source         附件来源{@link com.data.ItemChangeReason}
     * @return true：成功，false：失败
     */
    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, Object mailTitle, Object mailContent, List<Item> attachItemList, int source, long actionId) {
        return sendMailTo(receiverId, mailType, String.valueOf(sender)
                , mailTitle != null ? mailTitle.toString() : "1"
                , mailContent == null ? "1" : mailContent.toString(), attachItemList, 1, source, actionId);
    }

    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, Object mailTitle, Object mailContent, List<Item> attachItemList, int source) {
        return sendMailToPlayer(receiverId, mailType, sender, mailTitle, mailContent, attachItemList, source, 0);
    }

    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, Object mailTitle, Object mailContent) {
        return sendMailToPlayer(receiverId, mailType, sender, mailTitle, mailContent, null, 0, 0);
    }

//    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, int mailTitle, String mailContent, List<Item> attachItemList, int source) {
//        return sendMailTo(receiverId, mailType, String.valueOf(sender), String.valueOf(mailTitle), mailContent, attachItemList, 1, source);
//    }

//    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, String mailTitle, String mailContent, List<Item> attachItemList) {
//        return sendMailTo(receiverId, mailType, String.valueOf(sender), mailTitle, mailContent, attachItemList, 1,0);
//    }
//
//    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, int mailTitle, int mailContent, List<Item> attachItemList) {
//        return sendMailTo(receiverId, mailType, String.valueOf(sender), String.valueOf(mailTitle), String.valueOf(mailContent), attachItemList, 1,0);
//    }
//
//    public boolean sendMailToPlayer(long receiverId, int mailType, int sender, int mailTitle, String mailContent, List<Item> attachItemList) {
//        return sendMailTo(receiverId, mailType, String.valueOf(sender), String.valueOf(mailTitle), mailContent, attachItemList, 1,0);
//    }
//
//    public boolean sendMailTo(long receiverId, int mailType, String sender, String mailTitle, String mailContent, List<Item> attachItemList, int sendSrcType) {
//        return sendMailTo(receiverId, mailType, String.valueOf(sender), String.valueOf(mailTitle), mailContent, attachItemList, 1,0);
//    }
    /**
     * 发送邮件，根据附件数量单封或多封邮件发送
     *
     * @param receiverId     接收者id
     * @param mailType       邮件类型
     * @param sender         发送者
     * @param mailTitle      邮件标题
     * @param mailContent    邮件内容
     * @param attachItemList 附件列表
     * @param sendSrcType    是否是语言包邮件 0 为非语言包， 1是需要读取语言包
     * @param source         附件来源{@link com.data.ItemChangeReason}
     * @return 发送结果
     */
    public boolean sendMailTo(long receiverId, int mailType, String sender, String mailTitle, String mailContent, List<Item> attachItemList, int sendSrcType, int source, long actionId) {
        //物品列表
        List<Item> itemList = new ArrayList<>();
        HashMap<Integer, Item> coinMap = new HashMap<>();
        //货币列表(只包括金币、元宝绑与非绑 3类货币，其他货币当做物品发放)
        if (attachItemList != null && !attachItemList.isEmpty()) {
            for (Item item : attachItemList) {
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
                if (itemBean == null) {
                    log.error("邮件道具错误:" + receiverId + " itemID:" + item.getItemModelId() + " content:" + mailContent);
                    continue;
                }
                if (item.getItemModelId() == ItemCoinType.BindMoney
                        || item.getItemModelId() == ItemCoinType.GemCoin
                        || item.getItemModelId() == ItemCoinType.BindGemCoin) {
                    int itemId = item.getItemModelId();
                    Item it = coinMap.get(itemId);
                    int num = item.getNum();
                    if (it == null) {
                        it = Item.createItem(itemId, num, false);
                        coinMap.put(itemId, it);
                    } else {
                        it.setNum(it.getNum() + item.getNum());
                    }
                } else if (item.getItemModelId() == ItemCoinType.EXP) {
                    Player player = Manager.playerManager.getPlayerCache(receiverId);
                    if (player != null && player.getLevel() >= Global.PlayerMaxLevel) continue;
                    PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(receiverId);
                    if (info.getLevel() >= Global.PlayerMaxLevel) continue;
                    itemList.add(item);
                } else if (itemBean.getType() == ItemTypeConst.XiSui) {
                    // 特殊处理邮件中的转职道具
                    List<Item> dropItems = Manager.playerManager.xiSuiScript().getDropItems(
                            Manager.playerManager.getPlayer(receiverId), item.getItemModelId(), item.getNum());
                    itemList.addAll(dropItems);
                } else
                    itemList.add(item);
            }
        }
        itemList.addAll(coinMap.values());

        //只需一封邮件发送
        if (IsAttachCorrect(itemList)) {
            return sendMail(receiverId, mailType, sender, mailTitle, mailContent, itemList, sendSrcType, source, actionId);
        }

        //每个附件最多5种物品和最多4种货币
        int itemNum = itemList.size();
        int itemMailNum = itemNum % 5 == 0 ? (itemNum / 5) : (itemNum / 5 + 1);
        for (int i = 0; i < itemMailNum; i++) {
            List<Item> attachList = new ArrayList<>();
            for (int j = i * 5; j < i * 5 + 5; j++) {
                if (j >= itemList.size()) {
                    break;
                }
                attachList.add(itemList.get(j));
            }
            sendMail(receiverId, mailType, sender, mailTitle, mailContent, attachList, sendSrcType, source, actionId);
        }
        return true;
    }

    //发送单封邮件，此处的itemList是已符合条件的[最多9种物品，包括最多4种货币和最多5种的非货币道具]
    private boolean sendMail(long receiverId, int mailType, String sender, String mailTitle, String mailContent, List<Item> itemList, int sendSrcType, int source, long actionId) {

        //检测mailTitle和mailContent中是否有半角单引号，有的话转为全角单引号
        mailTitle = mailTitle.replace("'", "＇");
        mailContent = mailContent.replace("'", "＇");

        MailData mailData = new MailData();
        mailData.setSaveType(SaveServer.INSERT);
        mailData.setType(mailType);
        mailData.setMailId(IDConfigUtil.getId());
        mailData.setReceiveTime(TimeUtils.Time());
        mailData.setSender(sender);
        mailData.setMailTitle(mailTitle);
        mailData.setMailContend(mailContent);
        mailData.setReceiverId(receiverId);
        mailData.setIsRead((byte) 0);
        if (itemList != null && !itemList.isEmpty()) {
            mailData.setHasAttachment((byte) 1);
            mailData.setItemList(itemList);
        } else {
            mailData.setHasAttachment((byte) 0);
        }
        mailData.setIsAttachReceived((byte) 0);
        mailData.setReadTable(sendSrcType > 0 ? 1 : 0);
        mailData.setSource(source);
        if (!saveMailToDB(mailData)) {
            log.error("邮件发送保存至DB失败！");
            return false;
        }

        Player player = Manager.playerManager.getPlayerOnline(receiverId);
        if (player != null) {
            sendNewMailToClient(player, mailData);
        }
        //邮件接收日志
        writeMailLog(player, MAIL_RECEIVE, mailData, 1);
        //道具变更和bi日志
        addItemLogAndBI(player, itemList, receiverId, source, actionId);
        return true;
    }

    private void addItemLogAndBI(Player player, List<Item> itemList, long receiverId, int source, long actionId) {
        if(itemList != null && !itemList.isEmpty()){
            try{
                if(actionId == 0){
                    actionId = IDConfigUtil.getLogId();
                }
                if(player == null){
                    player = Manager.playerManager.getPlayer(receiverId);
                }
                for(Item item : itemList){
                    Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
                    int itemModelId = item.getItemModelId();
                    if(model != null){
                        if(model.getType() == ItemTypeConst.COPPER){//货币处理
                            //货币产出日志
                            if(itemModelId == ItemCoinType.GemCoin){
                                int currentGold = player.getGold().getReaminGold();
                                int afterNum = currentGold + item.getNum();
                                //金币产出BI
                                Manager.biManager.getScript().biMoney(player, player.getLoginIP(),ItemCoinType.GemCoin, item.getNum()
                                        , currentGold,afterNum,source,actionId,1, player.getCurGps().getModelId());
                                //金币产出系统日志
                                Manager.currencyManager.manager().writeLog(player, currentGold, afterNum,item.getNum(),0, source, actionId, ItemCoinType.GemCoin);
                                //金币消耗BI
                                Manager.biManager.getScript().biMoney(player, player.getLoginIP(),ItemCoinType.GemCoin, item.getNum()
                                        , afterNum,currentGold,ItemChangeReason.MailSentUse,actionId,0, player.getCurGps().getModelId());
                                //金币消耗系统日志
                                Manager.currencyManager.manager().writeLog(player, afterNum, currentGold, item.getNum(),0, ItemChangeReason.MailSentUse, actionId, ItemCoinType.GemCoin);
                            }else if (ItemCoinType.EXP == itemModelId) {
                                long changeExp = item.realNum();
                                BigInteger bigChangeExp = BigInteger.valueOf(changeExp);
                                BigInteger beforeExp = player.getTotalExp();
                                BigInteger afterExp = beforeExp.add(bigChangeExp);
                                //经验产出BI
                                Manager.biManager.getScript().biResource(player, 1, ItemCoinType.EXP, bigChangeExp, beforeExp, afterExp, player.getLevel(), player.getLevel(), source, actionId);
                                //经验产出系统日志
                                Manager.currencyManager.manager().writeLog(player, beforeExp.longValue(), afterExp.longValue(), changeExp, 0, source, actionId, ItemCoinType.EXP);

                                //经验消耗BI
                                Manager.biManager.getScript().biResource(player, 0, ItemCoinType.EXP, bigChangeExp, afterExp, beforeExp, player.getLevel(), player.getLevel(), ItemChangeReason.MailSentUse, actionId);
                                //经验消耗系统日志
                                Manager.currencyManager.manager().writeLog(player, afterExp.longValue(), beforeExp.longValue(), changeExp, 0, ItemChangeReason.MailSentUse, actionId, ItemCoinType.EXP);
                            }else{
                                int currencyType = itemModelId;
                                int num = (int)item.realNum();
                                long beforeNum = Manager.currencyManager.manager().getCurrencyNum(player, currencyType);
                                long after = beforeNum + num;
                                //其他货币产出BI
                                Manager.biManager.getScript().biMoney(player, player.getLoginIP(),currencyType,(int) num,beforeNum, after,source,actionId,1, player.getCurGps().getModelId());
                                Manager.biManager.getScript().biResource(player, 1, currencyType, BigInteger.valueOf(num), BigInteger.valueOf(beforeNum), BigInteger.valueOf(after), player.getLevel(), player.getLevel(), source, actionId);
                                //其他货币产出系统日志
                                Manager.currencyManager.manager().writeLog(player, beforeNum, after, num, 0, source, actionId, currencyType);

                                //其他货币消耗BI
                                Manager.biManager.getScript().biMoney(player, player.getLoginIP(),currencyType,(int) num, after,beforeNum,ItemChangeReason.MailSentUse,actionId,0, player.getCurGps().getModelId());
                                Manager.biManager.getScript().biResource(player, 0, currencyType, BigInteger.valueOf(num), BigInteger.valueOf(after), BigInteger.valueOf(beforeNum), player.getLevel(), player.getLevel(), ItemChangeReason.MailSentUse, actionId);
                                //其他货币消耗系统日志
                                Manager.currencyManager.manager().writeLog(player, after, beforeNum, num, 0, ItemChangeReason.MailSentUse, actionId, currencyType);
                            }

                        }else{
                            //道具产出系统日志
                            Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), 0, item.getNum(),
                                    source, ItemChangeAction.ADD, actionId, 0, 0, item.getGridId());
                            //消耗系统日志
                            Manager.backpackManager.manager().writeItemLog(player, item.getId(), item.getItemModelId(), item.getNum(), 0,
                                    ItemChangeReason.MailSentUse, ItemChangeAction.CHANGE, actionId, 0, 0, item.getGridId());
                        }
                    }
                }
            }catch (Exception e){
                log.error("",e);
            }
        }
    }

    //检查附件物品种类是否超出 最多货币4种+道具5种,允许为空
    private boolean IsAttachCorrect(List<Item> itemList) {
        if (itemList == null) {
            return true;
        }
        return itemList.size() <= 5;
    }

    //收到新邮件保存到DB、邮件读取和附件领取后更新到DB
    private boolean saveMailToDB(MailData mailData) {
        Mail mail = new Mail();
        mail.setType(mailData.getType());
        mail.setMailId(mailData.getMailId());
        mail.setReceiveTime(mailData.getReceiveTime());
        mail.setSender(mailData.getSender());
        mail.setReceiverId(mailData.getReceiverId());
        mail.setIsRead(mailData.getIsRead());
        mail.setHasAttachment(mailData.getHasAttachment());
        mail.setIsAttachReceived(mailData.getIsAttachReceived());
        mail.setMailData(JsonUtils.toJSONString(mailData));
        mail.setReadTable(mailData.getReadTable());
        mail.setSource(mailData.getSource());
        if (mailData.getSaveType() == 1) {
            Manager.saveThreadManager.getOtherServerSave().deal(mail, DbSqlName.MAIL_INSERT, SaveServer.INSERT);
        } else if (mailData.getSaveType() == 2) {
            mail.setWhere(mail.getMailId());
            Manager.saveThreadManager.getOtherServerSave().deal(mail, DbSqlName.Mail_UPDATE, SaveServer.UPDATE);
        } else if (mailData.getSaveType() == 3) {
            mail.setWhere(mail.getMailId());
            Manager.saveThreadManager.getOtherServerSave().deal(mail, DbSqlName.MAIL_DELETE_BY_MAILID, SaveServer.DELETE);
        }
        return true;
    }

    //发送新邮件到客户端
    private void sendNewMailToClient(Player player, MailData mailData) {

        //mailCacheMap满则将最早一封移至surplus中，保证最新邮件玩家都能看到
        player.getMailCacheMap().put(mailData.getMailId(), mailData);
        synchronized (player.getMailCacheMap()) {
            if (player.getMailCacheMap().size() > getMailMax()) {
                List<MailData> mailList = new ArrayList<>(player.getMailCacheMap().values());
                Collections.sort(mailList);
                MailData moveMail = mailList.get(0);
                if (mailList.size() >= Global.Mail_Caches_Num) {
                    long lastMailId = mailList.get(mailList.size() - 1).getMailId();
                    player.getMailCacheMap().remove(lastMailId);
                }

                List<MailData> surplusMailList = surplusMailMap.get(player.getId());
                if (surplusMailList != null) {
                    surplusMailList.add(moveMail);
                } else {
                    surplusMailList = new ArrayList<>();
                    surplusMailList.add(moveMail);
                    surplusMailMap.put(player.getId(), surplusMailList);
                }
                player.getMailCacheMap().remove(moveMail.getMailId());
                //有新邮件不推送剩余邮件列表，客户端自己做显示处理
//                syncMailInfoListToClient(player);
            }
        }

        mailMessage.ResNewMail.Builder resMsg = mailMessage.ResNewMail.newBuilder();
        resMsg.setNewMail(createMailSummaryInfo(mailData));
        MessageUtils.send_to_player(player, mailMessage.ResNewMail.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    //获取邮件保留时间
    private long getMailKeepTime() {
        int keepDays = Global.MailDelete;
        return keepDays * GlobalType.MILLIS_PER_DAY;
    }

    //计算取得邮件删除时间点
    private long getMailDeleteTime() {
        return TimeUtils.Time() - getMailKeepTime();
    }

    //登录发给客户端最大邮件数量(客户端最大邮件保存数量)
    private int getMailMax() {
        return Global.MailMaxNum;
    }

    //邮件log
    private void writeMailLog(Player player, int mailAction, MailData mailData, long actionId) {
        MailLog mailLog = new MailLog();
        mailLog.setMailAction(mailAction);
        mailLog.setType(mailData.getType());
        mailLog.setMailId(mailData.getMailId());
        mailLog.setReceiveTime(mailData.getReceiveTime());
        mailLog.setSender(mailData.getSender());
        mailLog.setMailTitle(mailData.getMailTitle());
        mailLog.setMailContend(mailData.getMailContend());
        mailLog.setReceiverId(mailData.getReceiverId());
        mailLog.setMessageStringId(mailData.getReadTable() > 0 ? 1 : 0);

        if (player == null) {
            PlayerWorldInfo pwInfo = Manager.playerManager.getPlayerWorldInfo(mailData.getReceiverId());
            if (pwInfo != null) {
                mailLog.setReceiverName(pwInfo.getRolename());
                mailLog.setReceiverSid(pwInfo.getCsid());
            }
        } else {
            mailLog.setReceiverName(player.getName());
            mailLog.setReceiverSid(player.getCreateServerId());
        }

        mailLog.setIsRead(mailData.getIsRead());
        mailLog.setHasAttachment(mailData.getHasAttachment());
        mailLog.setAttachment(JsonUtils.toJSONString(mailData.getItemList()));
        mailLog.setIsAttachReceived(mailData.getIsAttachReceived());

        int notBindGoldNum = 0;
        //有附件且未领取
        if (mailData.getHasAttachment() == 1 && mailData.getIsAttachReceived() == 0) {
            for (Item item : mailData.getItemList()) {
                if (item.getItemModelId() == ItemCoinType.GemCoin) {
                    notBindGoldNum += item.getNum();
                }
            }
        }
        mailLog.setNotBindGoldNum(notBindGoldNum);
        mailLog.setActionId(actionId);
        mailLog.setSource(mailData.getSource());
        LogService.getInstance().execute(mailLog);
    }

    //生成邮件摘要信息
    private mailMessage.MailSummaryInfo.Builder createMailSummaryInfo(MailData mailData) {
        mailMessage.MailSummaryInfo.Builder info = mailMessage.MailSummaryInfo.newBuilder();

        info.setMailId(mailData.getMailId());
        info.setReceiveTime(mailData.getReceiveTime());
        info.setMailTitle(mailData.getMailTitle());
        info.setIsRead(mailData.getIsRead() == 1);
        info.setHasAttachment(mailData.getHasAttachment() == 1);
        info.setIsAttachReceived(mailData.getIsAttachReceived() == 1);
        boolean isReadTable = false;
        if (mailData.getReadTable() > 0) {
            isReadTable = true;
        }
        try {
            int titleId = Integer.parseInt(mailData.getMailTitle());
            if (titleId > 0) {
                isReadTable = true;
            }
        } catch (NumberFormatException e) {
            isReadTable = false;
        }
        info.setReadTable(isReadTable);
        return info;
    }

    //生成邮件详细信息
    private mailMessage.MailDetailInfo.Builder createMailDetailInfo(MailData mailData) {
        mailMessage.MailDetailInfo.Builder info = mailMessage.MailDetailInfo.newBuilder();

        info.setMailId(mailData.getMailId());
        info.setSender(mailData.getSender());
        info.setMailTitle(mailData.getMailTitle());
        boolean isReadTable = false;
        if (mailData.getMailContend().contains("@_@")) {
            isReadTable = true;
        }
        if (mailData.getReadTable() > 0) {
            isReadTable = true;
        }
        if (!isReadTable) {
            //检查是否是邮件需要转换
            try {
                isReadTable = StringUtils.isNumber(mailData.getMailTitle());
            } catch (NumberFormatException e) {
                log.error("检查邮件的标题出异常了！" + mailData.getMailId());
            }
        }

        if (!isReadTable) {
            info.setMailContent(mailData.getMailContend());
        } else {
            String[] tmp = mailData.getMailContend().split("@_@");
            info.setMailContent(tmp[0]);
            if (tmp.length > 1) {
                for (int i = 1; i < tmp.length; ++i) {
                    info.addParamlists(parseParam(tmp[i]));
                }
            }
        }
        info.setHasAttachment(mailData.getHasAttachment() == 1);
        for (Item item : mailData.getItemList()) {
            info.addItemList(Manager.backpackManager.manager().buildItemInfo(item));
        }
        info.setIsAttachReceived(mailData.getIsAttachReceived() == 1);
        info.setReadTable(isReadTable);
        return info;
    }

    private static mailMessage.paramStruct.Builder parseParam(String value) {
        mailMessage.paramStruct.Builder info = mailMessage.paramStruct.newBuilder();
        if (value.length() < 3) {
            info.setMark(0);
            info.setParamsValue(value);
            return info;
        }
        char sr = value.charAt(1);
        char srs = value.charAt(2);

        if (sr == '&' && srs == '_') {
            String[] tt = value.split("&_");
            info.setMark(Integer.parseInt(tt[0]));
            info.setParamsValue(value.substring(3));
        } else {
            info.setMark(0);
            info.setParamsValue(value);
        }
        return info;
    }

    private enum Singleton {

        INSTANCE;
        MailManager manager;

        Singleton() {
            this.manager = new MailManager();
        }

        MailManager getProcessor() {
            return manager;
        }
    }

    public static MailManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

}
