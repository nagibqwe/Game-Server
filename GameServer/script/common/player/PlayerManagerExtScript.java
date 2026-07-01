package common.player;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Item_gift_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.equip.struct.EquipPart;
import com.game.friend.struct.PlayerRelation;
import com.game.guild.structs.Guild;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.marriage.struct.MarryChild;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.script.IPlayerManagerExtScript;
import com.game.friend.struct.GiftRecord;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.statestifle.structs.SoulSpiritInfo;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import com.google.protobuf.ByteString;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.StringUtils;
import game.core.util.VersionUpdateUtil;
import game.message.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @explain: desc
 * @time Created on 2019/11/27 11:35.
 * @author: tc
 */
public class PlayerManagerExtScript implements IPlayerManagerExtScript {
    private final Logger log = LogManager.getLogger("PlayerManager");

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.PlayerManagerExtScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 同步PlayerInfo到客户端
     *
     * @param player
     */
    @Override
    public void synSelfPlayerInfo(Player player) {

        MarryChild child = Utils.findOne(player.getChilds().values(), c -> c.getShow() > 0);

        long exp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
        PlayerMessage.ResPlayerBaseInfo.Builder playerInfo = PlayerMessage.ResPlayerBaseInfo.newBuilder();
        playerInfo.setRoleID(player.getId());
        playerInfo.setAccountId(player.getUserId());
        playerInfo.setName(player.getName());
        playerInfo.setStateVip(player.getStateVip().getLv());
        playerInfo.setPkMode(player.getPkState());
        playerInfo.setCurTitle(player.getTitleData().getWearId());
        playerInfo.setLevel(player.getLevel());
        playerInfo.setExp(exp);
        playerInfo.setOccupation(player.getCareer());
        playerInfo.setGenderClass(player.getXsGrade());
        if (player.getHorse().getRideState() == HorseRideStateEnum.Ride) {
            playerInfo.setMountId(player.getHorse().getHorseModelId());
        } else {
            playerInfo.setMountId(0);
        }

        if (child != null && child.getName() != null) {
            playerInfo.setChildName(child.getName());
        }

        if (Manager.guildsManager.getGuildById(player.getGuildId()) != null) {
            Guild g = Manager.guildsManager.getGuildById(player.getGuildId());
            if (g.getMembers().get(player.getId()) == null) {
                log.error("player:" + player + "公会异常");
                player.setGuildId(0);
                player.setGuildName("");
            }
        }
        playerInfo.setGuild(player.getGuildId());
        playerInfo.setGuildName(player.gainGuildName());
        playerInfo.setPos(player.gainGuildPost());
        playerInfo.setMapID(player.gainMapModelId());
        if (player.playerCrossData.toFightId > 0 && player.playerCrossData.toFightPos != null) {
            playerInfo.setPosX(player.playerCrossData.toFightPos.getX());
            playerInfo.setPosY(player.playerCrossData.toFightPos.getY());
        } else {
            playerInfo.setPosX(player.gainX());
            playerInfo.setPosY(player.gainY());
        }
        playerInfo.setXsLvl(player.getXsLevel());
        playerInfo.setCurWakan(player.getCurWakan());
        playerInfo.setCurHP((int) player.getCurHp());
        playerInfo.setFabaoId(player.getStifleData().getNature().getCurrentModelId());
        playerInfo.setFabaoUid(player.getStifleData().getNature().getId());
        playerInfo.setFightpower(player.getFightPoint());
        long vipExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.VipExp);
        playerInfo.setVipExp(vipExp);
        player.setCurServerId(ServerConfig.getServerId());
        playerInfo.setVipLevel(player.getVipLv());
        if (player.getCurHuaxinEntity() != null) {
            playerInfo.setFeijianId(player.getCurHuaxinEntity().getExcelId());
            playerInfo.setFeijianUid(player.getCurHuaxinEntity().getId());
            playerInfo.setFeijianMaxID(player.getFlyswordAllInfo().getCurFlySwordSkillId());
        }
        playerInfo.setChildId(player.getChildId());
        playerInfo.setCamp(player.getCamp());
        //2020/3/9临时需求普通武器切换时也要更新外观
        playerInfo.setSoulSpirte1(0);
        playerInfo.setSoulSpirte2(0);
        playerInfo.setSoulSpirte3(0);
        Map<Integer, SoulSpiritInfo> spiritMap = player.getStifleData().getSpiritMap();
        if (spiritMap.containsKey(1)) {
            playerInfo.setSoulSpirte1(100 + spiritMap.get(1).getEvolveLv());
        }
        if (spiritMap.containsKey(2)) {
            playerInfo.setSoulSpirte2(200 + spiritMap.get(2).getEvolveLv());
        }
        if (spiritMap.containsKey(3)) {
            playerInfo.setSoulSpirte3(300 + spiritMap.get(3).getEvolveLv());
        }
        playerInfo.setServerId(MapUtils.getServerId(player));
        playerInfo.setShiHaiCfgId(player.getShiHaiData().getCfgId());

        playerInfo.setFacade(MapUtils.getFacade(player));


        playerInfo.setHead(MapUtils.getHead(player));


        MessageUtils.send(player.getIosession(), new SMessage(PlayerMessage.ResPlayerBaseInfo.MsgID.eMsgID_VALUE, playerInfo.build().toByteArray()));
    }

    /**
     * 查看他人信息
     *
     * @param player
     * @param otherId
     */
    @Override
    public void lookOtherPlayer(Player player, long otherId) {
        if (player.getId() == otherId) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LOOKOTHERGUILDNOTSELF);
            return;
        }
        PlayerMessage.ResLookOtherPlayerResult.Builder msg = PlayerMessage.ResLookOtherPlayerResult.newBuilder();
        Player otherPlayer = Manager.playerManager.getPlayerOnline(otherId);
        if (otherPlayer == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHAT_TISHI_8);
            return;
        } else {
            msg.setFightPoint(otherPlayer.getFightPoint());
        }

        msg.setRoleId(otherPlayer.getId());
        msg.setRoleName(otherPlayer.getName());
        msg.setRoleLv(otherPlayer.getLevel());
        msg.setCareer(otherPlayer.getCareer());
        msg.setVip(otherPlayer.getStateVip().getLv());
        msg.setXsLvl(otherPlayer.getXsLevel());
        msg.setGuildName(Manager.guildsManager.getGuildById(otherPlayer.getGuildId()) == null ? "" : Manager.guildsManager.getGuildById(otherPlayer.getGuildId()).getName());

        msg.setFacade(MapUtils.getFacade(otherPlayer));

        //角色属性
        game.message.PlayerMessage.Attribute.Builder a = game.message.PlayerMessage.Attribute.newBuilder();
        for (int i = 1; i <= AttributeType.ATTR_MAX; i++) {
            a.setType(i);
            if (i == AttributeType.ATTR_MaxHp) {
                a.setValue(otherPlayer.getAttribute().MaxHP());
            } else {
                a.setValue(otherPlayer.getAttribute().getAdditionValue(i));
            }
            msg.addAttList(a);
        }
        //角色副属性
        for (int i = 1; i <= AttributeType.SystemAttr_Max; i++) {
            int value = otherPlayer.getSysAttriBute().getAttribute(i);
            if (value == 0) {
                continue;
            }
            a.setType(1000 + i);
            a.setValue(value);
            msg.addAttList(a);
        }
        a.setType(AttributeType.AttackSpeedFinal);
        a.setValue(otherPlayer.getAttribute().gainFinalAttackSpeed());
        msg.addAttList(a);
        a.setType(AttributeType.MoveSpeedFinal);
        a.setValue(otherPlayer.getAttribute().gainFinalMoveSpeed());
        msg.addAttList(a);
        msg.setStifleFabaoId(otherPlayer.getStifleData().getNature().getCurrentModelId());
        //身上的装备
        for (EquipPart equipPart : otherPlayer.getEquipParts()) {
            if (equipPart == null || null == equipPart.getEquip()) {
                continue;
            }
            backpackMessage.ItemInfo.Builder equipInfo = Manager.backpackManager.manager().buildItemInfo(equipPart.getEquip());
            equipInfo.setStrengLv(equipPart.getLevel());
            msg.addEquipList(equipInfo);
        }
        msg.setShiHaiCfgId(otherPlayer.getShiHaiData().getCfgId());

        MessageUtils.send_to_player(player, PlayerMessage.ResLookOtherPlayerResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    /**
     * 同步玩家的账号
     *
     * @param fid
     * @param player
     * @param fightId
     * @param modelId
     * @param ratt
     * @param mapModelId
     * @param zoneLevel
     * @param cloneAtt
     * @param onlyJoin
     * @return
     */
    @Override
    public boolean OnSynPlayerInfoToFight(int fid, Player player, long fightId, int modelId, CrossFightMessage.roleAtt ratt, int mapModelId, int zoneLevel, List<CommonMessage.CrossAttribute> cloneAtt, boolean onlyJoin) {
        CrossFightMessage.G2FSynPlayerInfo.Builder msg = CrossFightMessage.G2FSynPlayerInfo.newBuilder();
        msg.setRoleId(player.getId());
        msg.setRoleName(player.getName());
        player.setGuildName(player.gainGuildName());
        msg.setPlayerData(VersionUpdateUtil.dataSave(JsonUtils.toJSONString(player)));
        log.info("同步玩家的数据:" + player.nameIdString() + "---   len=" + msg.getPlayerData().length() + " ,玩家的移动速度：" + player.getAttribute().gainFinalMoveSpeed());

        msg.setGuildId(player.getGuildId());
        msg.setGuildName(player.gainGuildName());
        msg.setGuildRank(player.gainGuildPost());

        msg.setFightId(fightId);
        msg.setZoneModelId(modelId);
        msg.setZoneLevel(Math.max(1, zoneLevel));
        msg.setCross(ratt);
        msg.setOnlyJoin(onlyJoin);
        msg.setMapModelId(mapModelId);
        String att = JsonUtils.toJSONString(player.PlayerCalculators());
        msg.setAttlist(att);

        //进入跨服之前自动退出队伍
//        manager.teamManager.OnQuitTeam(player);

        List<CommonMessage.CrossAttribute> crossAttlist = new ArrayList<>(cloneAtt);
        if (player.getCurHuaxinEntity() != null) {
            CommonMessage.CrossAttribute.Builder huaxingdata = CommonMessage.CrossAttribute.newBuilder();
            huaxingdata.setType(player.getCurHuaxinEntity().getExcelId());
            huaxingdata.setValue(player.getCurHuaxinEntity().getId());
            crossAttlist.add(huaxingdata.build());
        }
        msg.addAllMapSetList(crossAttlist);
        return ConnectFightManager.GetInstance().send_to_fight(fid, -1, CrossFightMessage.G2FSynPlayerInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 通知所有的战斗服玩家应该下线了
     *
     * @param player 需要从跨服退出的玩家
     */
    @Override
    public void onCrossPlayerOut(Player player) {
        if (player.playerCrossData.toFightId > 0 && player.playerCrossData.toFightSid > 0) {
            CopyMapMessage.ReqCopyMapOut.Builder msg = CopyMapMessage.ReqCopyMapOut.newBuilder();
            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), CopyMapMessage.ReqCopyMapOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else {
            for (int fightId : ConnectFightManager.GetInstance().getFightConnects().keySet()) {
                CopyMapMessage.ReqCopyMapOut.Builder msg = CopyMapMessage.ReqCopyMapOut.newBuilder();
                ConnectFightManager.GetInstance().send_to_fight(fightId, player.getId(), CopyMapMessage.ReqCopyMapOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public void sendGiftToFriend(Player player, PlayerMessage.ReqSendGift mess) {
        if (null == player || null == mess.getGiftsList()) {
            return;
        }
        //如果不是强制赠送，且两个玩家不是互为好友关系，那么返回
        if (!mess.getForce() && !Manager.friendManager.checkFriendRelation(player.getId(), mess.getRoleId())) {
            sendGiftResultToClient(player, 1);
            return;
        }
        //检查赠送物品是否拥有，数量是否足够
        for (PlayerMessage.Gift gift : mess.getGiftsList()) {
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, gift.getGiftId(), gift.getGiftNumber())) {
                return;
            }
        }

        long actionId = IDConfigUtil.getLogId();

        List<Item> items = new ArrayList<>();
        List<Item> bagItmes = new ArrayList<>();
        int totalIntimacy = 0;
        for (PlayerMessage.Gift gift : mess.getGiftsList()) {
            Cfg_Item_gift_Bean bean = CfgManager.getCfg_Item_gift_Container().getValueByKey(gift.getGiftId());
            if (bean == null) {
                continue;
            }
            if (bean.getType() == mess.getType()) {
                //增加亲密度
                Manager.friendManager.deal().addIntimacy(player, mess.getRoleId(), bean.getQinmi() * gift.getGiftNumber());
                totalIntimacy += bean.getQinmi() * gift.getGiftNumber();
                Manager.backpackManager.manager().onRemoveItem(player, gift.getGiftId(), gift.getGiftNumber(), ItemChangeReason.ItemUseAddIntimacyDec, actionId);

                Item item = Item.createItem(gift.getGiftId(), gift.getGiftNumber(), false);
                items.add(item);

                if (bean.getIf_enter_bag() == 1) {
                    bagItmes.add(item);
                }
            }
        }
        if (!bagItmes.isEmpty()) {
            String content = MessageString.FRIENDSENDITEM_NOTICETOFRIEND + "@_@" + player.getName();
            Manager.mailManager.sendMailToPlayer(mess.getRoleId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.FREIND_GIFT, content, bagItmes, ItemChangeReason.FreindGiftGet, actionId);
        }

        PlayerWorldInfo other = Manager.playerManager.getPlayerWorldInfo(mess.getRoleId());
        if (Manager.friendManager.checkFriendRelation(player.getId(), other.getRoleid())) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.FRIENDSENDITEM_NOTICETOSELF, other.getRolename(), totalIntimacy + "");
        } else {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.FRIENDSENDITEM_NOTICETOSELF1, other.getRolename());
        }
        addGiftRecord(player.getId(), mess.getRoleId(), items);

        Manager.controlManager.operate(player, FunctionVariable.Give_Gift, 1);
    }

    private void addGiftRecord(long playerId, long otherId, List<Item> itemList) {
        Player receiver = Manager.playerManager.getPlayerOnline(otherId);
        Player sender = Manager.playerManager.getPlayerCache(playerId);
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(playerId);
        PlayerRelation otherRelation = Manager.friendManager.getPlayerRelation(otherId);

        PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(playerId);
        PlayerWorldInfo other = Manager.playerManager.getPlayerWorldInfo(otherId);
        List<GiftRecord> sendRecordList = Collections.synchronizedList(playerRelation.getSendList());
        List<GiftRecord> receiveRecordList = Collections.synchronizedList(otherRelation.getReceiveList());

        PlayerMessage.ResSendGift.Builder sendBuilder = PlayerMessage.ResSendGift.newBuilder();
        sendBuilder.setResult(0);
        PlayerMessage.ResNewGiftLog.Builder receiveBuilder = PlayerMessage.ResNewGiftLog.newBuilder();
        for (Item item : itemList) {
            if (sendRecordList.size() >= 15) {
                sendRecordList.remove(0);
            }
            if (receiveRecordList.size() >= 15) {
                receiveRecordList.remove(0);
            }
            GiftRecord sendRecord = new GiftRecord(player.getRolename(), other.getRolename(), item, 0);
            GiftRecord receiveRecord = new GiftRecord(player.getRolename(), other.getRolename(), item, 1);
            sendRecordList.add(sendRecord);
            receiveRecordList.add(receiveRecord);
            sendBuilder.addLog(buildGiftLog(sendRecord));
            receiveBuilder.addLog(buildGiftLog(receiveRecord));
        }
        MessageUtils.send_to_player(sender, PlayerMessage.ResSendGift.MsgID.eMsgID_VALUE, sendBuilder.build().toByteArray());
        if (receiver != null) {
            MessageUtils.send_to_player(receiver, PlayerMessage.ResNewGiftLog.MsgID.eMsgID_VALUE, receiveBuilder.build().toByteArray());
        }
    }

    private PlayerMessage.GiftLog.Builder buildGiftLog(GiftRecord record) {
        PlayerMessage.GiftLog.Builder giftLog = PlayerMessage.GiftLog.newBuilder();
        giftLog.setId(record.getId());
        giftLog.setSender(record.getSender());
        giftLog.setReceiver(record.getReceiver());
        giftLog.setType(record.getType());
        giftLog.setItemId(record.getItemModelId());
        giftLog.setNum(record.getNum());
        giftLog.setTime(record.getTime());
        giftLog.setReadStatus(record.getReadStatus());
        return giftLog;
    }

    @Override
    public void onReqGetGiftLog(Player player, int type) {
        if (type < 0 || type > 1) {
            return;
        }
        PlayerMessage.ResGetGiftLog.Builder builder = PlayerMessage.ResGetGiftLog.newBuilder();
        builder.setType(type);
        PlayerRelation relation = Manager.friendManager.getPlayerRelation(player.getId());
        List<GiftRecord> recordList;
        if (type == 0) {
            recordList = relation.getSendList();
        } else {
            recordList = relation.getReceiveList();
        }
        for (GiftRecord record : recordList) {
            PlayerMessage.GiftLog.Builder giftLog = PlayerMessage.GiftLog.newBuilder();
            giftLog.setId(record.getId());
            giftLog.setSender(record.getSender());
            giftLog.setReceiver(record.getReceiver());
            giftLog.setType(record.getType());
            giftLog.setItemId(record.getItemModelId());
            giftLog.setNum(record.getNum());
            giftLog.setTime(record.getTime());
            giftLog.setReadStatus(record.getReadStatus());
            builder.addRecordList(giftLog);
        }
        MessageUtils.send_to_player(player, PlayerMessage.ResGetGiftLog.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqReadGiftLog(Player player, List<Long> ids) {
        List<Long> readList = new ArrayList<>();
        PlayerRelation relation = Manager.friendManager.getPlayerRelation(player.getId());
        List<GiftRecord> receiveList = relation.getReceiveList();
        for (GiftRecord record : receiveList) {
            if (ids.contains(record.getId())) {
                record.setReadStatus(1);
                readList.add(record.getId());
            }
        }
        PlayerMessage.ResReadGiftLog.Builder builder = PlayerMessage.ResReadGiftLog.newBuilder();
        builder.addAllIds(readList);
        MessageUtils.send_to_player(player, PlayerMessage.ResReadGiftLog.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 续上玩家的连接
     *
     * @param session
     * @param platsid
     */
    @Override
    public void addPlayerSession(ChannelHandlerContext session, String platsid) {
        List<Player> ll = new ArrayList<>(Manager.playerManager.getPlayersCache().values());
        for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            // 更新现有玩家的连接
            if (entry.getValue().playerCrossData.platSid.equalsIgnoreCase(platsid)) {
                entry.getValue().setIosession(session);
            }
        }
    }

    /**
     * 清理跨服玩家的连接信息
     *
     * @param key
     */
    @Override
    public void removePlayerSession(String key) {
        for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            Player player = entry.getValue();
            if (player.getIosession() == null)
                continue;

            if (!player.playerCrossData.platSid.equalsIgnoreCase(key))
                continue;
            Manager.mapManager.manager().onCrossOutMap(player);
            player.setIosession(null);
            Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, player.playerCrossData.toFightId, player.playerCrossData.toZoneModelId);
            // 设置下线
            player.dealOffLine();
            Manager.rechargeManager.discountScript().offline(player);
        }
    }

    /**
     * 通知战斗服更新某个功能的最新信息(广播战斗服周围玩家)
     *
     * @param player
     * @param type
     * @param value
     * @param msgId
     * @param messData
     */
    @Override
    public void noticeSynRoleInfoToFight(Player player, int type, Map<Integer, Object> value, int msgId, ByteString messData) {
        CrossFightMessage.G2FNoticeSynRoleInfo.Builder msg = CrossFightMessage.G2FNoticeSynRoleInfo.newBuilder();
        msg.setRoleId(player.getId());
        msg.setType(type);
        msg.setMsgId(msgId);
        msg.setMessData(messData);
        if (value != null) {
            msg.setValue(VersionUpdateUtil.dataSave(JsonUtils.toJSONString(value)));
        }
        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, -1, CrossFightMessage.G2FNoticeSynRoleInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void SendMaxHpChange(Player player) {
        PlayerMessage.ResMaxHpChange.Builder msg = PlayerMessage.ResMaxHpChange.newBuilder();
        msg.setRoleId(player.getId());
        msg.setMaxHp(player.getAttribute().MaxHP());
        MessageUtils.send_to_roundPlayer(player, PlayerMessage.ResMaxHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqPlayerSummaryInfo(Player player, long roleId) {
        PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
        if (info == null) {
            //发送到社交服查看跨服基本信息
            PlayerMessage.G2SReqPlayerSummaryInfo.Builder g2SReqPlayerSummaryInfoBuilder = PlayerMessage.G2SReqPlayerSummaryInfo.newBuilder();
            g2SReqPlayerSummaryInfoBuilder.setRoleId(player.getId());
            g2SReqPlayerSummaryInfoBuilder.setTargetRoleId(roleId);
            MessageUtils.send_to_social(PlayerMessage.G2SReqPlayerSummaryInfo.MsgID.eMsgID_VALUE, g2SReqPlayerSummaryInfoBuilder.build().toByteArray());
            return;
        }
        PlayerMessage.ResPlayerSummaryInfo.Builder builder = PlayerMessage.ResPlayerSummaryInfo.newBuilder();
        builder.setRoleId(info.getRoleid());
        builder.setRoleName(info.getRolename());
        builder.setCareer(info.getCareer());
        builder.setRoleLv(info.getLevel());
        builder.setFightpower(info.getFightPower());

        //@todo 头像修改
//        builder.setFashionHead(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID());
//        builder.setFashionFrame( player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID()) ;
//

        //builder.setHead(MapUtils.getHead(player));

        long guildId = info.getGuildId();
        Player other = Manager.playerManager.getPlayerCache(roleId);
        if (other != null) {
            guildId = other.getGuildId();
        }
        Guild guild = Manager.guildsManager.getGuildById(guildId);
        if (guild != null) {
            builder.setGuildName(guild.getName());
        }

        //@todo 头像修改
//        builder.setFashionHead(info.getFashionHeadId());
//        builder.setFashionFrame( info.getFashionHeadFrameId()) ;


        builder.setHead(MapUtils.getHead(info));
        CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(player);
        builder.setFacade(fa);


        MessageUtils.send_to_player(player, PlayerMessage.ResPlayerSummaryInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void sendGiftResultToClient(Player player, int result) {
        PlayerMessage.ResSendGift.Builder builder = PlayerMessage.ResSendGift.newBuilder();
        builder.setResult(result);
        MessageUtils.send_to_player(player, PlayerMessage.ResSendGift.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

}
