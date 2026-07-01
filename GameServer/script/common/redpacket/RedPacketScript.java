package common.redpacket;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_NewRedPacket_Bean;
import com.data.bean.Cfg_RedPacket_Bean;
import com.data.container.Cfg_NewRedPacket_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.guild.structs.Guild;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import  com.data.MessageString;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerRedPacket;
import com.game.player.structs.PlayerWorldInfo;
import com.game.redpacket.log.RedPacketClickLog;
import com.game.redpacket.log.RedPacketCreateLog;
import com.game.redpacket.manager.RedPacketManager;
import com.game.redpacket.script.IRedPacketScript;
import com.game.redpacket.structs.RedPacket;
import com.game.redpacket.structs.RedPacketData;
import com.game.redpacket.structs.RedPacketEnum;
import com.game.redpacket.structs.RedPacketLog;
import com.game.script.structs.ScriptEnum;
import com.data.ItemChangeReason;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.RedPacketMessage;
import game.message.RedPacketMessage.ResClickRedpacket;
import game.message.RedPacketMessage.ResMineHaveRedpacket;
import game.message.RedPacketMessage.ResRedpacketList;
import game.message.RedPacketMessage.ResSendMineRechargeRedpacket;
import game.message.RedPacketMessage.ResSendRedPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 红包的实现处理， 请注意红包因为要访问公共数据。因此要加锁处理
 *
 * @author gouzhongliang
 */
public class RedPacketScript implements IRedPacketScript {

    private static final Logger LOG = LogManager.getLogger(RedPacketScript.class);

    @Override
    public void OnReqClickRedpacket(Player player, RedPacketMessage.ReqClickRedpacket mess) {
        long rpId = mess.getRpId();
        RedPacket rpd = Manager.redPacketManager.getRedPacketList().get(rpId);
        if (rpd == null) {
            sendclickMess(player, 1, rpId, 0, rpd);
            return;
        }

        //如果还不是公共红包， 则不处理
        if (rpd.getSendTime() == 0) {
            sendclickMess(player, 1, rpId, 0, rpd);
            return;
        }

        synchronized (rpd) {
            if (rpd.getValues().size() == 0) {
                sendclickMess(player, 2, rpId, 0, rpd);
                return;
            }

            //已经开过了， 不能再开了
            if (rpd.getRolelist().containsKey(player.getId())) {
                sendclickMess(player, 3, rpId, 0, rpd);
                return;
            }

            int index = RandomUtils.random(rpd.getValues().size());
            int value = rpd.getValues().get(index);
            if (Manager.currencyManager.manager().onAddItemCoin(player, rpd.getItemType(), value, ItemChangeReason.RedPacketClickGet, rpId)) {
                //去除目标值
                rpd.getValues().remove(index);
                //记录玩家当前获得的值
                rpd.getRolelist().put(player.getId(), value);
                //保存数据值
                Manager.redPacketManager.saveData(rpd, true);
                //写日志
                RedPacketClickLog rpc = new RedPacketClickLog();
                rpc.setPlayer(player);
                rpc.setRpId(rpId);
                rpc.setValue(value);
                rpc.setRpRoleId(rpd.getRoleId());
                LogService.getInstance().execute(rpc);

//                //如果是口令红包，向所有玩家发送口令
//                if (rpd.getGoldType() == 2) {
//                    String info = rpd.getNotice();
//                    if (rpd.getType() == 1) {
//                        manager.chatManager.sendSystemStrToGuild(player, info, true);
//                    } else {
//                        manager.chatManager.sendSystemStrToPlayer(player, info);
//                    }
//                }
                sendclickMess(player, 0, rpId, value, rpd);

                //抢了一个红包后， 更新红包列表
                sendPlayer(player);
            } else {
                sendclickMess(player, 3, rpId, 0, rpd);
            }
        }

    }

    private void sendclickMess(Player player, int state, long rpId, int value, RedPacket rpd) {
        ResClickRedpacket.Builder msg = ResClickRedpacket.newBuilder();
        msg.setRpId(rpId);
        msg.setState(state);
        msg.setRpvalue(value);
        if(rpd != null){
            Iterator<Entry<Long, Integer>> iter = rpd.getRolelist().entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Long, Integer> en = iter.next();
                RedPacketMessage.RedpacketgetroleInfo.Builder info = RedPacketMessage.RedpacketgetroleInfo.newBuilder();
                info.setRoleId(en.getKey());
                info.setRpvalue(en.getValue());
                info.setRoleName(Manager.registerManager.getRoleName(en.getKey()));
                PlayerWorldInfo pw = Manager.playerManager.getPlayerWorldInfo(en.getKey());
                info.setCareer(0);
                if(pw != null){
                    info.setCareer(pw.getCareer());
                    info.setHead(MapUtils.getHead(pw));
                }
                msg.addRoleinfo(info);
            }
        }
        MessageUtils.send_to_player(player, ResClickRedpacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqGetRedPacketInfo(Player player, RedPacketMessage.ReqGetRedPacketInfo mess) {
        long rpId = mess.getRpId();
        RedPacket rpd = Manager.redPacketManager.getRedPacketList().get(rpId);
        if (rpd == null) {
            LOG.error(player.nameIdString() + " 请求的红包数据ID =" + rpId + "不存在！");
            return;
        }
        RedPacketMessage.ResGetRedPacketInfo.Builder msg = RedPacketMessage.ResGetRedPacketInfo.newBuilder();
//        synchronized (rpd) {
        Iterator<Entry<Long, Integer>> iter = rpd.getRolelist().entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Long, Integer> en = iter.next();
            RedPacketMessage.RedpacketgetroleInfo.Builder info = RedPacketMessage.RedpacketgetroleInfo.newBuilder();
            info.setRoleId(en.getKey());
            info.setRpvalue(en.getValue());
            info.setRoleName(Manager.registerManager.getRoleName(en.getKey()));
            PlayerWorldInfo pw = Manager.playerManager.getPlayerWorldInfo(en.getKey());
            info.setCareer(0);
            if(pw != null){
                info.setCareer(pw.getCareer());
                info.setHead(MapUtils.getHead(pw));
            }
            msg.addRoleinfo(info);
        }
//        }

        msg.setRpId(rpId);
        MessageUtils.send_to_player(player, RedPacketMessage.ResGetRedPacketInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqRedpacketList(Player player, RedPacketMessage.ReqRedpacketList mess) {
        if (!player.isHaveGuild()) {
            LOG.error(player.nameIdString() + " 还没有公会， 不能查看红包列表！");
            return;
        }
        sendPlayer(player);
    }

    private void sendPlayer(Player player) {
        ResRedpacketList.Builder msg = ResRedpacketList.newBuilder();
        HashSet<Long> list = null;
        if (!player.isHaveGuild()) {
            MessageUtils.send_to_player(player, ResRedpacketList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        //发送自己的
        list = Manager.redPacketManager.getRoleRedpacket().get(player.getId());
        if (list != null) {
            makeMesslist(list, player, msg);
            list = null;
        }

        list = Manager.redPacketManager.getGuildRedpacket().get(player.getGuildId());
        if (list != null) {
            makeMesslist(list, player, msg);
        }
        List<RedPacketLog> logs = Manager.redPacketManager.getGuildRPlog().get(player.getGuildId());
        if (logs == null) {
            MessageUtils.send_to_player(player, ResRedpacketList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        //帮会的红包日志
        for (RedPacketLog rpl : logs) {
            RedPacketMessage.Rpsendlog.Builder info = RedPacketMessage.Rpsendlog.newBuilder();
            info.setReason(rpl.getReason());
            info.setRoleId(rpl.getRoleId());
            info.setRoleName(Manager.registerManager.getRoleName(rpl.getRoleId()));
            info.setSendtime(rpl.getSendtime());
            info.setValue(rpl.getValue());
            info.setItemType(rpl.getItemType());
            msg.addRploginfo(info);
        }

        MessageUtils.send_to_player(player, ResRedpacketList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void makeMesslist(HashSet<Long> list, Player player, ResRedpacketList.Builder msg) {
        Iterator<Long> it = list.iterator();
        long now = TimeUtils.Time();
        while(it.hasNext()){
            Long rpId = it.next();
            RedPacket rpd = Manager.redPacketManager.getRedPacketList().get(rpId);
            if (rpd == null) {
                it.remove();
                continue;
            }

            long expireTime = rpd.getCreateTime() + GlobalType.MILLIS_PER_DAY;
            if(now > expireTime){
                continue;
            }

            RedPacketMessage.RedpacketInfo.Builder info = RedPacketMessage.RedpacketInfo.newBuilder();
            info.setCurnum(rpd.getValues().size());
            info.setDemo(rpd.getNotice() == null ? "" : rpd.getNotice());
            info.setMark(rpd.getRolelist().containsKey(player.getId()));
            info.setMaxValue(rpd.getTotalMoney());
            info.setMaxnum(rpd.getTotalNum());
            info.setRoleId(rpd.getRoleId());
            info.setRoleName(Manager.registerManager.getRoleName(rpd.getRoleId()));
            info.setRpId(rpId);
            info.setItemType(rpd.getItemType());
            if(rpd.getSendTime() > 0){
                info.setSent(true);
            }
            info.setExpiretime(expireTime);
            info.setCareer(0);
            PlayerWorldInfo pw = Manager.playerManager.getPlayerWorldInfo(rpd.getRoleId());
            if(pw != null){
                info.setHead(MapUtils.getHead(pw));
                info.setCareer(pw.getCareer());
            }
            if(rpd.getSendTime() > 0){
                info.setSent(true);
            }else{
                info.setSent(false);
            }
            info.setConfigId(rpd.getConfigId());
            msg.addRpinfo(info);
        }
    }

    @Override
    public void OnReqSendRedpacket(Player player, RedPacketMessage.ReqSendRedpacket mess) {
        int maxnum = mess.getMaxNum();
        int bindmoney = mess.getMaxValue();
        long rpId= mess.getRpId();
        String notice = mess.getNotice();

        if (!player.isHaveGuild()) {
            sendPlayerSendState(player, 3, 0L);
            return;
        }

        if (mess.getMaxValue() < 1) {
            sendPlayerSendState(player, 2, 0L);
            return;
        }

        if (maxnum < 1) {
            sendPlayerSendState(player, 2, 0L);
            return;
        }

        //有人不能获得1个绑金，则本次提交是失败的
        if (bindmoney < maxnum) {
            sendPlayerSendState(player, 2, 0L);
            return;
        }

        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null) {
            sendPlayerSendState(player, 3, 0L);
            return;
        }

        if(rpId > 0){
            //发送已有的系统红包
            RedPacket redPacket = Manager.redPacketManager.getRedPacketList().get(rpId);
            if(redPacket == null){
                LOG.info("红包不存在redPacketId:" + rpId);
                return;
            }
            if(redPacket.getSendTime() > 0){
                LOG.info("红包已发送redPacketId:" + rpId);
                return;
            }
            Cfg_NewRedPacket_Bean bean = Cfg_NewRedPacket_Container.GetInstance().getValueByKey(redPacket.getConfigId());
            int totalNum = bean.getMinGetValue() <= 0 ? bean.getValue() : bean.getValue() / bean.getMinGetValue();
            if(maxnum > totalNum){
                LOG.info("红包数量超出范围redPacketId:" + rpId);
                return;
            }
            if(redPacket.getTotalMoney() >= maxnum && maxnum >= 1){
                sendToGuild(player, redPacket, maxnum, notice, bean.getMinGetValue());
            }
        }else{
            boolean valid = false;
            int getItemType = mess.getItemType();
            for(ReadArray<Integer> arr : Global.RedPacket.getValuees()){
                if(arr.get(1) == mess.getItemType() && arr.get(0) >= mess.getMaxNum()){
                    valid = true;
                    if(arr.size() > 2){
                        getItemType = arr.get(2);
                    }
                    break;
                }
            }
            if(!valid){
                return;
            }

            //发送个人红包
            if (!Manager.currencyManager.manager().canDecItemCoin(player, mess.getMaxValue(), mess.getItemType())) {
                Manager.backpackManager.manager().sendItemNotEnough(player, mess.getItemType());
                sendPlayerSendState(player, 1, 0L);
                return;
            }

            rpId = IDConfigUtil.getLogId();
            boolean isKou = Manager.currencyManager.manager().onDecItemCoin(player, mess.getMaxValue(), ItemChangeReason.RedPacketSubmitDelDec, rpId, mess.getItemType());
            if (isKou) {
                //创建红包
                RedPacket data = create(player, getItemType, mess.getMaxValue(), 0);
                data.setSendItemType(mess.getItemType());
                Manager.redPacketManager.addRedpacket(data);
                Manager.redPacketManager.addRoleRedpacket(player, data);
                Manager.redPacketManager.addRedpacketLog(player, data);
                sendToGuild(player, data, maxnum, notice, 1);

                int msg = 0;
                if(getItemType == ItemCoinType.BindGemCoin){
                    msg = MessageString.RP_SEND_CHAT;
                }else if(getItemType == ItemCoinType.GoldCoin){
                    msg = MessageString.RED_PACKET_YUANBAO;
                }
                if(msg != 0){
                    MessageUtils.notify_Chat_To_GuildPlayer(player, guild, false, msg, player.getName(), "" + mess.getMaxValue());
                    MessageUtils.notify_guildPlayer(guild, Notify.MARQUEE, msg, player.getName(), "" + mess.getMaxValue());
                }
            } else {
                sendPlayerSendState(player, 1, 0L);
            }
        }
        //更新红包列表
        sendPlayer(player);
    }

    /**
     * 红包发送到仙盟
     * @param player
     * @param redPacket
     */
    private void sendToGuild(Player player, RedPacket redPacket, int maxnum, String notice, int minGetValue) {
        //正式发送
        redPacket.setSendTime(TimeUtils.Time());
        redPacket.setNotice(notice);
        redPacket.setTotalNum(maxnum);
        redPacket.setGuildId(player.getGuildId());
        //设置红包金额
        if(maxnum == 1){
            redPacket.getValues().add(redPacket.getTotalMoney());
        }else{
            for(int i=0;i<maxnum;i++){
                redPacket.getValues().add(minGetValue <= 0 ? 1 : minGetValue);
            }
            if(maxnum < redPacket.getTotalMoney()){
                int v = redPacket.getTotalMoney() - maxnum * minGetValue;
                for(int i=0; i<v; i++){
                    int r = RandomUtils.random(maxnum);
                    redPacket.getValues().set(r, redPacket.getValues().get(r) + 1);
                }
            }
        }
        //删除玩家红包
        HashSet<Long> rs = Manager.redPacketManager.getRoleRedpacket().get(player.getId());
        if(rs != null){
            rs.remove(redPacket.getId());
        }
        //添加到工会
        Manager.redPacketManager.addGuildRedpacket(player.getGuildId(), redPacket);

        //保存到数据库
        Manager.redPacketManager.saveData(redPacket, true);

        //告知玩家发送状态
        sendPlayerSendState(player, 0, redPacket.getId());

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        if (guild == null) {
            return;
        }

        //给频道发通知
//        ResNewRedPacket.Builder msg = ResNewRedPacket.newBuilder();
//        msg.setRoleId(player.getId());
//        msg.setRpId(redPacket.getId());
//        msg.setType(redPacket.getType());
//        MessageUtils.send_TO_Guild(guild, ResNewRedPacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        ResMineHaveRedpacket.Builder msg = ResMineHaveRedpacket.newBuilder();
        msg.setMineSendNum(1);
        msg.addRpId(redPacket.getId());
        MessageUtils.send_to_player(player, ResMineHaveRedpacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        addCreateLog(player, redPacket);
    }

//    private void savedata(RedPacket rpd) {
//        Manager.redPacketManager.saveData(rpd, true);
//        HashSet<Long> list = null;
//        if (rpd.getType() == 2) {
//            list = Manager.redPacketManager.getGuildRedpacket().get(rpd.getGuildId());
//            if (list == null) {
//                list = new HashSet<>();
//                Manager.redPacketManager.getGuildRedpacket().put(rpd.getGuildId(), list);
//            }
//        } else {
//            list = Manager.redPacketManager.getRoleRedpacket().get(rpd.getRoleId());
//            if (list == null) {
//                list = new HashSet<>();
//                Manager.redPacketManager.getRoleRedpacket().put(rpd.getRoleId(), list);
//            }
//        }
//        list.add(rpd.getId());
//        //保存呀
//        Manager.redPacketManager.getRedPacketList().put(rpd.getId(), rpd);
//    }

    //给公会增加日志
    private synchronized void addRedPacketLog(long guildId, RedPacketLog rplog) {
        if (guildId == 0) {
            return;
        }

        List<RedPacketLog> rploglist = Manager.redPacketManager.getGuildRPlog().get(guildId);
        if (rploglist == null) {
            rploglist = new ArrayList<>();
            Manager.redPacketManager.getGuildRPlog().put(guildId, rploglist);
        }
        rploglist.add(0, rplog);
        if (rploglist.size() > 200) {
            rploglist.remove(200);
        }

        Guild guild = Manager.guildsManager.getGuildById(guildId);
        if (guild == null) {
            return;
        }
        RedPacketManager.getGuildDao().changeGuildRedpacket(guild.toGuildBean());
    }

    /**
     * 向玩家通知红包发送的结果
     *
     * @param player
     * @param state
     * @param rpId
     */
    private void sendPlayerSendState(Player player, int state, long rpId) {
        ResSendRedPacket.Builder msg = ResSendRedPacket.newBuilder();
        msg.setRpId(rpId);
        msg.setState(state);
        MessageUtils.send_to_player(player, ResSendRedPacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqSendMineRechargeRedpacket(Player player, RedPacketMessage.ReqSendMineRechargeRedpacket mess) {
//        long rpId = mess.getRpId();
//        RedPacketData rpd = Manager.redPacketManager.getRedPacketList().get(rpId);
//        if (rpd == null) {
//            LOG.error(player.nameIdString() + "红包已经不存在了！");
//            sendShareRedpacket(player, rpId, 2);
//            return;
//        }
//
//        if (mess.getMaxNum() < 1) {
//            LOG.error(player.nameIdString() + "分享的数量值小于1！num=" + mess.getMaxNum());
//            sendShareRedpacket(player, rpId, 4);
//            return;
//        }
//
//        if (mess.getMaxNum() > rpd.getMaxValue()) {
//            LOG.error(player.nameIdString() + "分享的数量大于总值！num=" + mess.getMaxNum() + " 总：" + rpd.getMaxValue());
//            sendShareRedpacket(player, rpId, 4);
//            return;
//        }
//
//        if (!player.isHaveGuild()) {
//            LOG.error(player.nameIdString() + "还没有帮会，不能分享红包！");
//            sendShareRedpacket(player, rpId, 3);
//            return;
//        }
//
//        if (rpd.getType() == 2) {
//            LOG.error(player.nameIdString() + "红包已经分享了！");
//            sendShareRedpacket(player, rpId, 5);
//            return;
//        }
//
//        if (rpd.getRoleId() != player.getId()) {
//            LOG.error(player.nameIdString() + "不是你的红包！");
//            sendShareRedpacket(player, rpId, 1);
//            return;
//        }
//        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
//        if (guild == null) {
//            sendShareRedpacket(player, rpId, 3);
//            return;
//        }
//        //删除老的
//        Manager.redPacketManager.getRoleRedpacket().get(player.getId()).remove(rpd.getRpId());
//
//        rpd.setCreateTime(TimeUtils.Time());
//        rpd.setType(2);
//        rpd.setMaxNum(mess.getMaxNum());
//        rpd.setGuildId(player.getGuildId());
//        compRedPacket(player, rpd);
//        LOG.error(player.nameIdString() + "分享了红包=" + rpId + "！");
//        sendShareRedpacket(player, rpId, 0);
////        String noticeStr = getStr(1, player.getName(), rpd.getMaxValue());
////        manager.chatManager.sendSystemStrToGuild(player, noticeStr, false);
////        MessageUtils.notify_guildPlayer(guild, Notify.MARQUEE, MessageString.WANGNENGTISHI, noticeStr);
//        MessageUtils.notify_Chat_To_GuildPlayer(player, guild, false, MessageString.RP_SEND_CHAT, player.getName(), "" + rpd.getMaxValue());
//        MessageUtils.notify_guildPlayer(guild, Notify.MARQUEE, MessageString.RP_SEND_CHAT, player.getName(), "" + rpd.getMaxValue());
//        //抢了一个红包后， 更新红包列表
//        sendPlayer(player);
    }

    private void sendShareRedpacket(Player player, long rpId, int state) {
        ResSendMineRechargeRedpacket.Builder msg = ResSendMineRechargeRedpacket.newBuilder();
        msg.setRpId(rpId);
        msg.setState(state);
        MessageUtils.send_to_player(player, ResSendMineRechargeRedpacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void compRedPacket(Player player, RedPacket rpd) {
//        String values = compute(rpd);//计算当前的红包值
//        savedata(rpd);
//        //写日志
//        RedPacketCreateLog rpc = new RedPacketCreateLog();
//        rpc.setGoldType(rpd.getGoldType());
//        rpc.setGroupId(rpd.getGroupId());
//        rpc.setGuildId(rpd.getGuildId());
//        rpc.setMaxNum(rpd.getMaxNum());
//        rpc.setMaxvalue(rpd.getMaxValue());
//        rpc.setKouvalue(rpd.getKouValue());
//        rpc.setNotice(rpd.getNotice());
//        rpc.setPlayer(player);
//        rpc.setRpId(rpd.getRpId());
//        rpc.setType(rpd.getType());
//        rpc.setRpvalues(values);
//        LogService.getInstance().execute(rpc);
//        sendPlayerSendState(player, 0, rpd.getRpId());
//
//        if (rpd.getGuildId() == 0) {
//            return;
//        }
//
//        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
//        if (guild == null) {
//            return;
//        }
//
////        String str = "%s发了一个价值%d的红包，大家快来抢红包了！";
////        //发公会公告
////        manager.chatManager.sendSystemStrToGuild(player, str, true);
//        if (rpd.getType() == 1) {
////            playerLogin(player);
//        } else {
//            //给频道发通知
//            ResNewRedPacket.Builder msg = ResNewRedPacket.newBuilder();
//            msg.setRoleId(player.getId());
//            msg.setRpId(rpd.getRpId());
//            msg.setType(rpd.getType());
//            MessageUtils.send_TO_Guild(guild, ResNewRedPacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//        }
        //生成帮会日志
//        RedPacketLog rpl = new RedPacketLog();
//        rpl.setGoldType(rpd.getType() == 1 ? rpd.getGoldType() : ((rpd.getGoldType() != 5) ? rpd.getGoldType() + 5 : 5));
//        rpl.setGoldvalue(rpd.getMaxValue());
//        rpl.setRoleId(rpd.getRoleId());
//        rpl.setSendtime((int) (TimeUtils.Time() / 1000));
//        rpl.setValue(rpd.getKouValue());
//        addRedPacketLog(rpd.getGuildId(), rpl);
    }

    @Override
    public void rechargeSendRedpacket(Player player, int rItemId, int gold) {
        PlayerRedPacket pdr = player.getRedPacket();
        long totalrecharge = (int) Manager.rechargeManager.deal().rechargeAll(player);
        int daytotalrecharge = (int) Manager.rechargeManager.deal().rechargeDay(player);
        boolean isHave = false;
        for (Cfg_RedPacket_Bean bean : CfgManager.getCfg_RedPacket_Container().getValuees()) {
            int redid = bean.getId();
            if (bean.getType() < 3) {//长期充值
                if (bean.getValue() > totalrecharge) {
                    continue;
                }
                //是否已经发过此红包了
                if (pdr.getRedpacket().containsKey(redid)) {
                    continue;
                }
            } else {//每天的充值
                if (bean.getValue() > daytotalrecharge) {
                    continue;
                }
                //是否已经发过此红包了
                if (pdr.getDayredpacket().containsKey(redid)) {
                    continue;
                }

            }

            //发红包了
            RedPacketData rpd = new RedPacketData();
            rpd.setCreateTime(TimeUtils.Time());
            rpd.setCurNum(0);
            rpd.setGoldType(bean.getType());
//            rpd.setGroupId(player.getGroup());
            rpd.setGuildId(player.getGuildId());
            rpd.setMaxNum(0);
            rpd.setMaxValue(bean.getNum());
            rpd.setKouValue(bean.getValue());
            rpd.setNotice(bean.getName());
            rpd.setRoleId(player.getId());
            rpd.setRpId(IDConfigUtil.getLogId());
            rpd.setType(1);
//            compRedPacket(player, rpd);

            if (bean.getType() < 3) {
                pdr.getRedpacket().put(redid, true);
            } else {
                pdr.getDayredpacket().put(redid, true);
            }

//            String noticeStr = getStr(2, rpd.getNotice(), rpd.getMaxValue());
            // manager.chatManager.sendSystemStrToPlayer(player, noticeStr, ChatManager.CHATCHANNEL_GUILD);
//            MessageUtils.notify_player(player, Notify.CHAT_SYS_MARQUEE, MessageString.WANGNENGTISHI, noticeStr);
//                    MessageUtils.notify_Chat_To_GuildPlayer(player, guild, false, MessageString.RP_SEND_CHAT, player.getName(), "" + rpd.getMaxValue());
            MessageUtils.notify_player(player, Notify.CHAT_SYS_MARQUEE, MessageString.RP_SEND_GUILD, "2&_" + rpd.getNotice(), "" + rpd.getMaxValue());
//            MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, noticeStr);
            isHave = true;
        }
        if (!isHave) {
            return;
        }
        //发送玩家红包有红点的呢
        playerLogin(player);
        //抢了一个红包后， 更新红包列表
        sendPlayer(player);
    }

//    private String getStr(int type, String name, int value) {
//        String valueStr = String.valueOf(value);
//        StringBuilder sb = new StringBuilder();
//        if (type == 1) {
//            sb.append(ServerStr.getStr(ServerStr.RP_SEND_CHAT));
//        } else {
//            sb.append(ServerStr.getStr(ServerStr.RP_SEND_GUILD));
//        }
//        String tmp = sb.toString();
//        tmp = tmp.replace("{0}", name);
//        tmp = tmp.replace("{1}", valueStr);
//        return tmp;
//    }
    @Override
    public void tick() {
//        if (Manager.redPacketManager.getRedPacketList().size() < 1) {
//            return;
//        }
//
//        List<RedPacket> redlist = new ArrayList<>(Manager.redPacketManager.getRedPacketList().values());
////        Iterator<Entry<Long, RedPacketData>> iter = manager.redPacketManager.getRedPacketList().entrySet().iterator();
//
//        long offsettime = Global.RedBagTime;//删除的值
//        offsettime *= 1000;
//        long now = TimeUtils.Time();
//        for (RedPacket rpd : redlist) {
////            Entry<Long, RedPacketData> en = iter.next();
////            RedPacketData rpd = en.getValue();
//            if (rpd == null) {
//                continue;
//            }
//
//            if (rpd.getCreateTime() + offsettime > now) {
//                continue;
//            }
//
//            LOG.error("删除帮会：" + rpd.getGuildId() + "的红包：" + rpd.getRpId() + " ,roleId=" + rpd.getRoleId() + "的红包!");
////            iter.remove();
//            Manager.redPacketManager.dataDelete(rpd);
//            Manager.redPacketManager.getRedPacketList().remove(rpd.getRpId());
//            //写日志
//            RedPacketDeleteLog pdl = new RedPacketDeleteLog();
//            pdl.setCurNum(rpd.getCurNum());
//            pdl.setGoldType(rpd.getGoldType());
//            pdl.setMaxNum(rpd.getMaxNum());
//            pdl.setMaxvalue(rpd.getMaxValue());
//            pdl.setRoleId(rpd.getRoleId());
//            pdl.setRoleName(Manager.registerManager.getRoleName(rpd.getRoleId()));
//            pdl.setRpId(rpd.getRpId());
//            pdl.setType(rpd.getType());
//            pdl.setCreatetime(rpd.getCreateTime());
//            LogService.getInstance().execute(pdl);
//        }
    }

    @Override
    public void playerLogin(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        long guildId = player.getGuildId();

        int ishave = 0;

        HashSet<Long> list = Manager.redPacketManager.getRoleRedpacket().get(player.getId());
        if (list != null) {
            ishave = list.size();
        }

        Set<Long> redlist = new HashSet<>();
        list = Manager.redPacketManager.getGuildRedpacket().get(guildId);
        if (list != null && list.size() > 0) {
            for (long rpId : list) {
                RedPacket rpd = Manager.redPacketManager.getRedPacketList().get(rpId);
                if (rpd == null) {
                    continue;
                }

                //已经领取过红包了
                if (rpd.getRolelist().containsKey(player.getId())) {
                    continue;
                }

                if (rpd.getValues().size() > 0) {
                    redlist.add(rpId);
                }
            }
        }

        if (ishave > 0 || redlist.size() > 0) {
            ResMineHaveRedpacket.Builder msg = ResMineHaveRedpacket.newBuilder();
            msg.setMineSendNum(ishave);
            msg.addAllRpId(redlist);
            MessageUtils.send_to_player(player, ResMineHaveRedpacket.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.RedPacketBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 计算红包的分配置值
     *
     * @param rpd
     */
    private String compute(RedPacketData rpd) {
        if (rpd.getMaxNum() == 0) {
            return "";
        }

        int aver = rpd.getMaxValue() / rpd.getMaxNum();
        int max = rpd.getMaxValue() * 30 / 100 + aver;
        int min = 1;
        int[] value = generate(rpd.getMaxValue(), rpd.getMaxNum(), aver, max, min);
        rpd.getValues().clear();
        StringBuilder sb = new StringBuilder();
        for (int val : value) {
            rpd.getValues().add(val);
            if (sb.length() < 1) {
                sb.append("[");
            } else {
                sb.append(",");
            }
            sb.append(val);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @param total 红包总额
     * @param count 红包个数
     * @param max 每个小红包的最大额
     * @param min 每个小红包的最小额
     * @return 存放生成的每个小红包的值的数组
     */
    private int[] generate(int total, int count, int average, int max, int min) {
        int[] result = new int[count];
        // 这样的随机数的概率实际改变了，产生大数的可能性要比产生小数的概率要小。
        // 这样就实现了大部分红包的值在平均数附近。大红包和小红包比较少。
        for (int i = 0; i < result.length; i++) {
            // 因为小红包的数量通常是要比大红包的数量要多的，因为这里的概率要调换过来。
            // 当随机数>平均值，则产生小红包
            // 当随机数<平均值，则产生大红包
            if (nextInteger(min, max) > average) {
                // 在平均线上减钱
                // long temp = min + sqrt(nextLong(range1));
                int temp = min + xRandom(min, average);
                result[i] = temp;
                total -= temp;
            } else {
                // 在平均线上加钱
                // long temp = max - sqrt(nextLong(range2));
                int temp = max - xRandom(average, max);
                result[i] = temp;
                total -= temp;
            }
        }
        // 如果还有余钱，则尝试加到小红包里，如果加不进去，则尝试下一个。
        while (total > 0) {
            for (int i = 0; i < result.length; i++) {
                if (total > 0 && result[i] < max) {
                    ++result[i];
                    total--;
                }
            }
        }
        // 如果钱是负数了，还得从已生成的小红包中抽取回来
        while (total < 0) {
            for (int i = 0; i < result.length; i++) {
                if (total < 0 && result[i] > min) {
                    --result[i];
                    total++;
                }
            }
        }
        return result;
    }

    /**
     * 生产min和max之间的随机数，但是概率不是平均的，从min到max方向概率逐渐加大。
     * 先平方，然后产生一个平方值范围内的随机数，再开方，这样就产生了一种“膨胀”再“收缩”的效果。
     *
     * @param min
     * @param max
     * @return
     */
    int xRandom(int min, int max) {
        return sqrt(nextInteger(sqr(max - min)));
    }

    int sqrt(int n) {
        return (int) Math.sqrt(n);
    }

    int sqr(int n) {
        if (n <= 0) {
            n = 1;
        }
        // 查表快，还是直接算快？
        return n * n;
    }

    int nextInteger(int n) {
        return RandomUtils.random(n);
    }

    int nextInteger(int min, int max) {
        return RandomUtils.random(max - min + 1) + min;
    }

    @Override
    public boolean sendRedpacket(Player player, Guild guild, int num, int bindGoldNum) {
        if (guild == null) {
            LOG.error("发红包时，传入的帮会为空了！");
            return false;
        }
        if (player == null) {
            LOG.error("发红包时， 传入的玩家为空了！");
            return false;
        }

        long rpId = IDConfigUtil.getLogId();
        RedPacketData rpd = new RedPacketData();
        rpd.setCreateTime(TimeUtils.Time());
        rpd.setCurNum(0);
        rpd.setGoldType(5);
//        rpd.setGroupId(player.getGroup());
        rpd.setGuildId(guild.getId());
        rpd.setMaxNum(num);
        rpd.setMaxValue(bindGoldNum);
        rpd.setKouValue(bindGoldNum);
        rpd.setNotice("");
        rpd.setRoleId(player.getId());
        rpd.setRpId(rpId);
        rpd.setType(2);
//        compRedPacket(player, rpd);
//        String noticeStr = getStr(1, player.getName(), rpd.getMaxValue());
//        manager.chatManager.sendSystemStrToGuild(player, noticeStr, false);
        MessageUtils.notify_Chat_To_GuildPlayer(player, guild, false, MessageString.RP_SEND_CHAT, player.getName(), "" + rpd.getMaxValue());
        MessageUtils.notify_guildPlayer(guild, Notify.MARQUEE, MessageString.RP_SEND_CHAT, player.getName(), "" + rpd.getMaxValue());
        return true;
    }

    @Override
    public void createRedpacket(Player player, RedPacketEnum redPacketEnum) {
        //注销----------------玩家没加入仙盟，也可以获得红包
//        if(!player.isHaveGuild()){
//            LOG.error(player.nameIdString() + "还没有帮会，不能分享红包！");
//            return;
//        }
        if(redPacketEnum == null){
            LOG.info("发红包时，类型为空");
            return;
        }

        Cfg_NewRedPacket_Bean[] beans = Cfg_NewRedPacket_Container.GetInstance().getValuees();
        Cfg_NewRedPacket_Bean bean = null;
        for(Cfg_NewRedPacket_Bean b : beans){
            if(b.getGetReasons() == redPacketEnum.getType()){
                bean = b;
                break;
            }
        }
        if(bean == null){
            LOG.info("发红包时，配置为空");
            return;
        }

        //创建红包
        RedPacket data = create(player, bean.getItemType(), bean.getValue(), redPacketEnum.getType());
        data.setConfigId(bean.getId());

        synchronized (player){
            Manager.redPacketManager.addRedpacket(data);
            Manager.redPacketManager.addRoleRedpacket(player, data);
            Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
            if(guild != null){
                Manager.redPacketManager.addRedpacketLog(player, data);
            }
        }

        //保存到数据库
        Manager.redPacketManager.saveData(data, true);

        sendPlayer(player);
    }

    /**
     * 创建红包
     * @param player
     * @param totalMoney
     * @param reason
     * @return
     */
    private RedPacket create(Player player, int itemType, int totalMoney, int reason) {
        RedPacket data = new RedPacket();
        data.setId(IDConfigUtil.getId());
        data.setCreateTime(TimeUtils.Time());
        data.setRoleId(player.getId());
        data.setReason(reason);
        data.setType(reason != 0 ? RedPacket.TYPE_SYSTEM : RedPacket.TYPE_PLAYER);
        data.setTotalMoney(totalMoney);
        data.setItemType(itemType);
        return data;
    }

    private void addCreateLog(Player player, RedPacket rpd){
        //写日志
        RedPacketCreateLog rpc = new RedPacketCreateLog();
        rpc.setGoldType(0);
        rpc.setGroupId(0);
        rpc.setGuildId(player.getGuildId());
        rpc.setMaxNum(rpd.getTotalNum());
        rpc.setMaxvalue(rpd.getTotalMoney());
        rpc.setKouvalue(0);
        rpc.setNotice(rpd.getNotice());
        rpc.setPlayer(player);
        rpc.setRpId(rpd.getId());
        rpc.setType(rpd.getType());
        rpc.setRpvalues(JsonUtils.toJSONString(rpd.getValues()));
        LogService.getInstance().execute(rpc);
    }

    @Override
    public void clearExpireRedpacket() {
        long now = TimeUtils.Time();
        ConcurrentHashMap<Long, RedPacket> list = Manager.redPacketManager.getRedPacketList();
        Iterator<Entry<Long, RedPacket>> it = list.entrySet().iterator();
        while (it.hasNext()){
            Entry<Long, RedPacket> entry = it.next();
            RedPacket redPacket = entry.getValue();
            long createTime = redPacket.getCreateTime();
            if(createTime + GlobalType.MILLIS_PER_DAY < now){
                //创建的红包已过期
                it.remove();
                Manager.redPacketManager.dataDelete(redPacket);
                if(redPacket.getType() == RedPacket.TYPE_PLAYER){//玩家手动发送
                    List<Integer> vs = redPacket.getValues();
                    if(vs.size() > 0){
                        int total = 0;
                        for(Integer v : vs){
                            total += v;
                        }
                        List<Item> items = Item.createItems(redPacket.getSendItemType(), total, false);
                        Manager.mailManager.sendMailToPlayer(redPacket.getRoleId(), MailType.SysCommonRewardMail, MessageString.System,
                                MessageString.REDPACKET_MAIL_TITLE, MessageString.REDPACKET_MAIL_CONTENT, items,
                                ItemChangeReason.RedPacketRebateGet, IDConfigUtil.getLogId());
                    }
                }
            }
        }
    }

    @Override
    public void joinGuild(Player player) {
        HashSet<Long> ls = Manager.redPacketManager.getRoleRedpacket().get(player.getId());
        for(Long id : ls){
            RedPacket redPacket = Manager.redPacketManager.getRedPacketList().get(id);
            if(redPacket != null){
                Manager.redPacketManager.addRedpacketLog(player, redPacket);
            }
        }
        sendPlayer(player);
    }
}
