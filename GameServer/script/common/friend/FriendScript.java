package common.friend;

import com.data.*;
import com.data.bean.Cfg_Npc_friend_Bean;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.LeaveMsg;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.friend.log.ReportLog;
import com.game.friend.manager.RelationInfoSort;
import com.game.friend.script.IFriendScript;
import com.game.friend.struct.*;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.marriage.struct.MarryTask;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.StringUtils;
import game.message.ChatMessage;
import game.message.PlayerMessage;
import game.message.friendMessage;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * 好友处理接口处理
 *
 * @author gongshengjun
 */
public class FriendScript implements IFriendScript {

    private static final Logger logger = LogManager.getLogger("FriendScript");

    @Override
    public int getId() {
        return ScriptEnum.FriendBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 添加亲密度
     * 九 零一起玩  www.90 175.com
     * @param player
     * @param otherId
     * @param num
     * @return
     */
    @Override
    public boolean addIntimacy(Player player, long otherId, int num) {
        long playerId = player.getId();
        if (!Manager.friendManager.checkFriendRelation(playerId, otherId)) {
            return false;
        }

        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(playerId);
        PlayerRelation otherRelation = Manager.friendManager.getPlayerRelation(otherId);
        Friend friend = playerRelation.getFriends().get(otherId);
        Friend otherFriend = otherRelation.getFriends().get(playerId);
        if (friend.getIntimacy() != otherFriend.getIntimacy()) {
            logger.error("亲密度不一致，按最低数值处理");
        }
        int intimacy = Math.min(friend.getIntimacy(), otherFriend.getIntimacy()) + num;
        friend.setIntimacy(intimacy);
        otherFriend.setIntimacy(intimacy);
        sendIntimacyChangeMsg(playerId, otherId, friend.getIntimacy());
        sendIntimacyChangeMsg(otherId, playerId, friend.getIntimacy());
        playerRelation.setDataChanged(true);
        otherRelation.setDataChanged(true);
        Manager.friendManager.savePlayerRelation(playerRelation);
        Manager.friendManager.savePlayerRelation(otherRelation);

        Player target = Manager.playerManager.getPlayer(otherId);
        if (target.getSex() != player.getSex() && intimacy >= 520) {
            Manager.marriageManager.manager().actionTask(player, MarryTask.Reach520, intimacy);
            Player bb = Manager.playerManager.getPlayer(otherId);
            if (bb != null) {
                Manager.marriageManager.manager().actionTask(bb, MarryTask.Reach520, intimacy);
            }
        }

        Friend max = playerRelation.getFriends().values().stream().max((a, b) -> a.getIntimacy() > b.getIntimacy() ? 1 : -1).get();
        Manager.rankListManager.deal().setIntimacy(player, max.getIntimacy());

        Friend max2 = otherRelation.getFriends().values().stream().max((a, b) -> a.getIntimacy() > b.getIntimacy() ? 1 : -1).get();
        Manager.rankListManager.deal().setIntimacy(target, max2.getIntimacy());
        //亲密度
        Manager.controlManager.operate(player, FunctionVariable.Marry_intimacy, intimacy);
        if (target.isOnline()) {
            Manager.controlManager.operate(target, FunctionVariable.Marry_intimacy, intimacy);
        }
        return true;
    }

    void sendIntimacyChangeMsg(long playerId, long otherId, int intimacy) {
        Player player = Manager.playerManager.getPlayerOnline(playerId);
        friendMessage.ResIntimacyChange.Builder builder = friendMessage.ResIntimacyChange.newBuilder();
        builder.setRoleId(otherId);
        builder.setIntimacy(intimacy);
        MessageUtils.send_to_player(player, friendMessage.ResIntimacyChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void addRelation(Player player, friendMessage.ReqAddRelation messInfo) {
        boolean isOpen = Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Friend);
        //好友功能还没有开启
        if (!isOpen) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FRIENDNOTOPEN);
            return;
        }

//        long otherId = messInfo.getTargetPlayerId();
//        PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(otherId);
//        if (info == null) {
//            return;
//        }
        int type = messInfo.getType();
        if (type == Relation.RelationType_Friend.getValue()) {
            addFriend(player, messInfo.getTargetPlayerId(), messInfo);
        } else if (type == Relation.RelationType_Enemy.getValue()) {
            addEnemy(player, messInfo.getTargetPlayerId());
        } else if (type == Relation.RelationType_Shield.getValue()) {
            addShield(player, messInfo.getTargetPlayerId());
        } else if (type == Relation.RelationType_LaterPlayerList.getValue()) {
            addLatelyPlayer(player.getId(), messInfo.getTargetPlayerId());
        }
    }

    @Override
    public void addFriend(Player player, long otherId) {
        this.addFriend(player, otherId, null);
    }

    /**
     * 添加好友
     */
    @Override
    public void addFriend(Player player, long otherId, friendMessage.ReqAddRelation messInfo) {
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        //好友数量已达上线
        if (playerRelation.getFriends().size() >= Global.FriendMax) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FriendMaxSize);
            return;
        }
        //不能添加自己为好友
        if (otherId == player.getId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FRIENDADDNOTCATSELF);
            return;
        }
        //好友已存在
        if (playerRelation.getFriends().containsKey(otherId)) {

            Friend friend = playerRelation.getFriends().get(otherId);
            if (friend != null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MSG_FRIEND_ADDFRIEND_ERROR, friend.getInfo().getRolename());
            }

            return;
        }
        if (messInfo != null) {
            PlayerWorldInfo targetPlayerWorldInfo = Manager.playerManager.getPlayerWorldInfo(messInfo.getTargetPlayerId());
            //判断好友是本服还是 跨服 发到对方服务器 判断
            if (targetPlayerWorldInfo == null) {
                //去社交服务器 加跨服好友
                friendMessage.G2SReqAddRelation.Builder g2SReqAddRelation = friendMessage.G2SReqAddRelation.newBuilder();
                g2SReqAddRelation.setTargetPlayerId(messInfo.getTargetPlayerId());
                g2SReqAddRelation.setType(messInfo.getType());
                //包装自己消息
                g2SReqAddRelation.setSourceApprovalPlayerInfo(builderApprovalPlayerInfo(player));
                g2SReqAddRelation.setTargetServerId(messInfo.getTargetServerId());
                MessageUtils.send_to_social(player.getId(), friendMessage.G2SReqAddRelation.MsgID.eMsgID_VALUE, g2SReqAddRelation.build().toByteArray());
                return;
            }
        }

        PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(otherId);
        if (info == null) {
            logger.error("PlayerId:" + otherId + " 在PlayerWorldInfo中不存在");
            return;
        }
        //本服处理
        PlayerRelation targetPlayerRelation = Manager.friendManager.getPlayerRelation(otherId);
        //对方好友数量已达上线
        if (targetPlayerRelation.getFriends().size() >= Global.FriendMax) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_GetFriendfull);
            return;
        }
        //对方屏蔽你的好友申请
        if (targetPlayerRelation.getShieldAddFriend().contains(player.getId())) {
            return;
        }

        if (targetPlayerRelation.getApprovalList().containsKey(player.getId())) {
            return;
        }

        //申请列表
        targetPlayerRelation.getApprovalList().put(player.getId(), player.getCurServerId());
        targetPlayerRelation.setDataChanged(true);
        //保存
        Manager.friendManager.savePlayerRelation(targetPlayerRelation);

        Player targetPlayer = Manager.playerManager.getPlayerOnline(otherId);
        if (targetPlayer != null) {
            //改为需要对方同意
            friendMessage.ResAddFriendApprovalToTarget.Builder resAddFriendApprovalToTarget = friendMessage.ResAddFriendApprovalToTarget.newBuilder();
            resAddFriendApprovalToTarget.setSourcePlayer(builderApprovalPlayerInfo(player));
            MessageUtils.send_to_player(otherId, friendMessage.ResAddFriendApprovalToTarget.MsgID.eMsgID_VALUE, resAddFriendApprovalToTarget.build().toByteArray());
        }
    }

    /**
     * 添加仇人
     */
    private void addEnemy(Player player, long otherId) {
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        if (playerRelation.getEnemies().size() >= Global.EnemyMax) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EnemyMaxSize);
            return;
        }
        //不能添加自己为仇人
        if (player.getId() == otherId) {
            return;
        }
        PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(otherId);
        if (info == null) {
            logger.error("addPlayerId:" + otherId + " 在PlayerWorldInfo中不存在");
            return;
        }

        //仇人已经存在
        if (playerRelation.getEnemies().containsKey(otherId)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ERROR_PLAYERRELATION_MESS_ENEMY, info.getRolename());
            return;
        }
        //添加仇人
        Enemy enemy = new Enemy();
        enemy.setRoleId(otherId);
        enemy.setInfo(info);
        playerRelation.addEnemy(enemy);

        //如果是好友，清除各自好友的亲密度
        if (playerRelation.getFriends().containsKey(otherId)) {
            playerRelation.getFriends().get(otherId).setIntimacy(0);
            Manager.friendManager.savePlayerRelation(playerRelation);

            PlayerRelation otherRelation = Manager.friendManager.getPlayerRelation(otherId);
            Friend friend = otherRelation.getFriends().get(player.getId());
            if (friend != null) {
                friend.setIntimacy(0);
                Manager.friendManager.savePlayerRelation(otherRelation);
            }
        }

        //返回消息
        friendMessage.ResAddFriendSuccess.Builder msg = friendMessage.ResAddFriendSuccess.newBuilder();
        msg.setType(Relation.RelationType_Enemy.getValue());
        msg.addResultList(toCommonInfoBuilder(player, enemy));
        MessageUtils.send_to_player(player, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //添加屏蔽
    private void addShield(Player player, long otherId) {
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        if (playerRelation.getShields().size() > Global.BanMax) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ShieldMaxSize);
            return;
        }
        //不能屏蔽自己
        if (playerRelation.getRoleId() == otherId) {
            return;
        }

        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(otherId);
        if (playerWorldInfo == null) {
            logger.error("addPlayerId:" + otherId + " 在PlayerWorldInfo中不存在");
            return;
        }
        //屏蔽玩家已存在
        if (playerRelation.getShields().containsKey(otherId)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ERROR_PLAYERRELATION_MESS_SHIELD, playerWorldInfo.getRolename());
            return;
        }

        //添加屏蔽，需移除最近和好友列表
        playerRelation.getLatelyPlayers().remove(otherId);
        if (playerRelation.getFriends().containsKey(otherId)) {
            playerRelation.getFriends().remove(otherId);
            Manager.friendManager.savePlayerRelation(playerRelation);

            PlayerRelation otherRelation = Manager.friendManager.getPlayerRelation(otherId);
            Friend friend = otherRelation.getFriends().get(player.getId());
            if (friend != null) {
                friend.setIntimacy(0);
                Manager.friendManager.savePlayerRelation(otherRelation);
            }
        }

        //添加屏蔽
        Shield shield = new Shield();
        shield.setRoleId(otherId);
        shield.setInfo(playerWorldInfo);
        playerRelation.addShield(shield);
        Manager.friendManager.savePlayerRelation(playerRelation);

        //返回消息
        friendMessage.ResAddFriendSuccess.Builder msg = friendMessage.ResAddFriendSuccess.newBuilder();
        msg.setType(Relation.RelationType_Shield.getValue());
        msg.addResultList(toCommonInfoBuilder(player, shield));
        MessageUtils.send_to_player(player, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //添加最近列表
    @Override
    public void addLatelyPlayer(long playerId, long otherId) {
        if (!Manager.playerManager.getAllPlayerWorldInfo().containsKey(playerId)) {
            return;
        }
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(playerId);
        if (playerRelation.getShields().containsKey(otherId)) {
            logger.info("玩家已被屏蔽，不能添加到最近列表");
            return;
        }
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(otherId);
        if (playerWorldInfo == null) {
            logger.error("addPlayerId:" + otherId + " 在PlayerWorldInfo中不存在");
            return;
        }

        LatelyPlayer latelyPlayer = new LatelyPlayer();
        latelyPlayer.setRoleId(otherId);
        latelyPlayer.setInfo(playerWorldInfo);
        playerRelation.addLatelyPlayer(latelyPlayer);
        Manager.friendManager.savePlayerRelation(playerRelation);

        //返回消息
        // Player player = Manager.playerManager.getPlayerCache(playerId);
        //从缓存取不到 到数据库取
        Player player = Manager.playerManager.getPlayer(playerId);
        if (player == null) {
            return;
        }
        friendMessage.ResAddFriendSuccess.Builder msg = friendMessage.ResAddFriendSuccess.newBuilder();
        msg.setType(Relation.RelationType_LaterPlayerList.getValue());

        if (playerRelation.getFriends().containsKey(latelyPlayer.getRoleId())) {
            Friend friend = playerRelation.getFriends().get(latelyPlayer.getRoleId());
            msg.addResultList(toCommonInfoBuilder(player, friend));
        } else {
            msg.addResultList(toCommonInfoBuilder(player, latelyPlayer));
        }

        MessageUtils.send_to_player(player, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    @Override
    public void deleteRelation(Player player, friendMessage.ReqDeleteRelation messInfo) {
        long otherId = messInfo.getTargetPlayerId();
        int type = messInfo.getType();
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        if (type == Relation.RelationType_Friend.getValue()) {
            deleteFriendRelation(player, playerRelation, otherId);
        } else if (type == Relation.RelationType_Enemy.getValue()) {
            deleteEnemyRelation(player, playerRelation, otherId);
        } else if (type == Relation.RelationType_Shield.getValue()) {
            deleteShieldRelation(player, playerRelation, otherId);
        }
        Manager.friendManager.savePlayerRelation(playerRelation);
    }

    /**
     * 删除好友
     */
    private void deleteFriendRelation(Player player, PlayerRelation playerRelation, long targetId) {
        if (!playerRelation.getFriends().containsKey(targetId)) {
            return;
        }
        PlayerWorldInfo info = Manager.marriageManager.getMarryTarget(player);
        if (info != null && info.getRoleid() == targetId) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Spouse_Delete_Friend_Each_Other);
            return;
        }
        playerRelation.deleteFriend(targetId);
        //判断目标玩家 是否为跨服
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(targetId);
        if (playerWorldInfo == null) {
            //发送到跨服 去删除
            friendMessage.G2SReqDeleteRelation.Builder g2SReqDeleteRelation = friendMessage.G2SReqDeleteRelation.newBuilder();
            g2SReqDeleteRelation.setTargetPlayerId(targetId);
            g2SReqDeleteRelation.setType(Relation.RelationType_Friend.getValue());
            g2SReqDeleteRelation.setSourcePlayerId(player.getId());
            MessageUtils.send_to_social(targetId, friendMessage.G2SReqDeleteRelation.MsgID.eMsgID_VALUE, g2SReqDeleteRelation.build().toByteArray());
        } else {
            //清除对方与自己的亲密度
            PlayerRelation targetRelation = Manager.friendManager.getPlayerRelation(targetId);
//        Friend friend = targetRelation.getFriends().get(player.getId());
//        if (friend != null) {
//            friend.setIntimacy(0);
//        }
            //删除对方
            targetRelation.deleteFriend(player.getId());
            Player targetPlayer = Manager.playerManager.getPlayerOnline(targetId);
            if (targetPlayer != null) {
                friendMessage.ResDeleteRelationSuccess.Builder msg = friendMessage.ResDeleteRelationSuccess.newBuilder();
                msg.setType(Relation.RelationType_Friend.getValue());
                msg.setTargetPlayerId(player.getId());
                MessageUtils.send_to_player(targetPlayer, friendMessage.ResDeleteRelationSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
            Manager.friendManager.savePlayerRelation(targetRelation);
        }
        Manager.friendManager.savePlayerRelation(playerRelation);
        friendMessage.ResDeleteRelationSuccess.Builder msg = friendMessage.ResDeleteRelationSuccess.newBuilder();
        msg.setType(Relation.RelationType_Friend.getValue());
        msg.setTargetPlayerId(targetId);
        MessageUtils.send_to_player(player, friendMessage.ResDeleteRelationSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 删除仇人
     */
    private void deleteEnemyRelation(Player player, PlayerRelation playerRelation, long targetId) {
        if (!playerRelation.getEnemies().containsKey(targetId)) {
            return;
        }
        playerRelation.deleteEnemy(targetId);
        Manager.friendManager.savePlayerRelation(playerRelation);
        friendMessage.ResDeleteRelationSuccess.Builder msg = friendMessage.ResDeleteRelationSuccess.newBuilder();
        msg.setType(Relation.RelationType_Enemy.getValue());
        MessageUtils.send_to_player(player, friendMessage.ResDeleteRelationSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 删除屏蔽
     */
    private void deleteShieldRelation(Player player, PlayerRelation playerRelation, long targetId) {
        if (!playerRelation.getShields().containsKey(targetId)) {
            return;
        }
        playerRelation.deleteShield(targetId);
        Manager.friendManager.savePlayerRelation(playerRelation);

        friendMessage.ResDeleteRelationSuccess.Builder msg = friendMessage.ResDeleteRelationSuccess.newBuilder();
        msg.setTargetPlayerId(targetId);
        msg.setType(Relation.RelationType_Shield.getValue());
        MessageUtils.send_to_player(player, friendMessage.ResDeleteRelationSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void getRelationList(Player player, int type) {
        long roleId = player.getId();
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(roleId);
        if (type == Relation.RelationType_Friend.getValue()) {
            sendFriendList(player, playerRelation);
        } else if (type == Relation.RelationType_Enemy.getValue()) {
            sendEnemyList(player, playerRelation);
        } else if (type == Relation.RelationType_Shield.getValue()) {
            sendShieldList(player, playerRelation);
        } else if (type == Relation.RelationType_RecommendFriend.getValue()) {
            sendRecommendInfoList(player, playerRelation);
        } else if (type == Relation.RelationType_LaterPlayerList.getValue()) {
            sendLaterPlayerList(player, playerRelation);
        }
    }

    //发送好友列表
    private void sendFriendList(Player own, PlayerRelation playerRelation) {
        friendMessage.ResFriendList.Builder msg = friendMessage.ResFriendList.newBuilder();
        msg.setType(Relation.RelationType_Friend.getValue());
        List<Friend> friends = new ArrayList<>(playerRelation.getFriends().values());
        //排序
        friends.sort(new RelationInfoSort());
        for (Friend friend : friends) {
            friendMessage.CommonInfo.Builder info = toCommonInfoBuilder(own, friend);
            if (info == null) {
                continue;
            }
            info.setIsFriend(true);
            info.setIntimacy(friend.getIntimacy());
            msg.addResultList(info);
        }
        PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(own);
        if (marryTarget != null) {
            msg.setMarryTargetId(marryTarget.getRoleid());
        }
        MessageUtils.send_to_player(own, friendMessage.ResFriendList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //发送仇人列表
    private void sendEnemyList(Player own, PlayerRelation playerRelation) {
        friendMessage.ResFriendList.Builder msg = friendMessage.ResFriendList.newBuilder();
        msg.setType(Relation.RelationType_Enemy.getValue());
        List<Enemy> enemies = new ArrayList<>(playerRelation.getEnemies().values());
        //排序
        enemies.sort(new RelationInfoSort());
        for (Enemy enemy : playerRelation.getEnemies().values()) {
            friendMessage.CommonInfo.Builder info = toCommonInfoBuilder(own, enemy);
            if (info == null) {
                continue;
            }
            msg.addResultList(info);
        }
        MessageUtils.send_to_player(own, friendMessage.ResFriendList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //发送屏蔽列表
    private void sendShieldList(Player own, PlayerRelation playerRelation) {
        friendMessage.ResFriendList.Builder msg = friendMessage.ResFriendList.newBuilder();
        msg.setType(Relation.RelationType_Shield.getValue());

        //按时间排序
        List<Shield> shields = playerRelation.getShields().values().stream()
                .sorted(Comparator.comparingInt(Shield::getTime))
                .collect(Collectors.toList());
        for (Shield shield : shields) {
            friendMessage.CommonInfo.Builder info = toCommonInfoBuilder(own, shield);
            if (info == null) {
                continue;
            }
            msg.addResultList(info);
        }
        MessageUtils.send_to_player(own, friendMessage.ResFriendList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //发送推荐列表
    private void sendRecommendInfoList(Player own, PlayerRelation playerRelation) {

        int recommendSize = Global.RecommendFriendMax;

        //优先在线玩家
        List<PlayerWorldInfo> result = new ArrayList<>();
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            if (result.size() > recommendSize * 10) {
                break;
            }
            if (!player.isOnline() || player.getId() == own.getId()) {
                continue;
            }
            if (playerRelation.getFriends().containsKey(player.getId()) ||
                    playerRelation.getShields().containsKey(player.getId()) ||
                    playerRelation.getEnemies().containsKey(player.getId())) {
                continue;
            }
            PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(player.getId());
            if (info != null) {
                result.add(info);
            }
        }
        //不够数量从离线玩家中补
        if (result.size() < recommendSize) {
            List<PlayerWorldInfo> offlinePlayer = new ArrayList<>();
            for (PlayerWorldInfo info : Manager.playerManager.getAllPlayerWorldInfo().values()) {
                if (info.isOnLine() || info.getRoleid() == own.getId()) {
                    continue;
                }
                if (playerRelation.getFriends().containsKey(info.getRoleid()) ||
                        playerRelation.getShields().containsKey(info.getRoleid()) ||
                        playerRelation.getEnemies().containsKey(info.getRoleid())) {
                    continue;
                }
                offlinePlayer.add(info);
            }
            while (result.size() < recommendSize) {
                if (offlinePlayer.size() <= 0) {
                    break;
                }
                result.add(offlinePlayer.remove(RandomUtils.nextInt(offlinePlayer.size())));
            }
        }

        friendMessage.ResFriendList.Builder msg = friendMessage.ResFriendList.newBuilder();
        msg.setType(Relation.RelationType_RecommendFriend.getValue());
        for (int i = 0; i < recommendSize; i++) {
            if (result.size() <= 0) {
                break;
            }
            PlayerWorldInfo info = result.remove(RandomUtils.nextInt(result.size()));
            msg.addResultList(toCommonInfoBuilder(info));
        }
        MessageUtils.send_to_player(own, friendMessage.ResFriendList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //发送最近聊天列表
    private void sendLaterPlayerList(Player own, PlayerRelation playerRelation) {
        friendMessage.ResFriendList.Builder msg = friendMessage.ResFriendList.newBuilder();
        msg.setType(Relation.RelationType_LaterPlayerList.getValue());
        //按时间排序
        List<LatelyPlayer> latelyPlayers = playerRelation.getLatelyPlayers().values().stream()
                .sorted(Comparator.comparingInt(LatelyPlayer::getTime).reversed())
                .collect(Collectors.toList());
        for (LatelyPlayer player : latelyPlayers) {
            friendMessage.CommonInfo.Builder builder = null;
            if (playerRelation.getFriends().containsKey(player.getRoleId())) {
                Friend friend = playerRelation.getFriends().get(player.getRoleId());
                builder = toCommonInfoBuilder(own, friend);

            } else {
                builder = toCommonInfoBuilder(own, player);

            }
            if (builder == null) {
                continue;
            }
            if (playerRelation.getFriends().containsKey(player.getRoleId())) {
                builder.setIntimacy(playerRelation.getFriends().get(player.getRoleId()).getIntimacy());
            }
            msg.addResultList(builder);
        }
        PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(own);
        if (marryTarget != null) {
            msg.setMarryTargetId(marryTarget.getRoleid());
        }
        MessageUtils.send_to_player(own, friendMessage.ResFriendList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void dimSelect(Player player, friendMessage.ReqDimSelect messInfo) {
        String name = messInfo.getName();
        if (StringUtils.isEmpty(name)) {
            return;
        }
        //TODO 2020年11月19日 搜索不限制屏蔽字  MMOB-8354
//        if (Utils.isForbiddenStr(name)) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ForbiddenString);
//            return;
//        }
        //检查
        long num = 0L;
        String Role_10 = "[0-9]+";  //10进制玩家ID
        String Role_36 = "^[A-Z0-9]{13}$";  //36进制玩家ID
        if (name.matches(Role_10)) {
            num = Long.parseLong(name);
        } else if (name.matches(Role_36)) {
            num = Long.parseLong(name, 36);
        }
        final long playerId = num;
        List<PlayerWorldInfo> infoList = Manager.playerManager.getAllPlayerWorldInfo().values().stream()
                .filter(n -> n.getRolename().contains(name)
                        || (n.getRoleid() == playerId && n.getRoleid() != 0))
                .limit(Global.FuzzyQueryMaxNum)
                .collect(Collectors.toList());
        if (infoList.isEmpty()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.FRIEND_ROLE_NOT_FOUNT);
            return;
        }
        friendMessage.ResDimSelectList.Builder msg = friendMessage.ResDimSelectList.newBuilder();
        for (PlayerWorldInfo info : infoList) {
            msg.addList(toCommonInfoBuilder(info));
        }
        MessageUtils.send_to_player(player, friendMessage.ResDimSelectList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    @Override
    public Relation getFriendRelation(PlayerRelation relation, long otherId) {
        if (relation.getFriends().containsKey(otherId)) {
            return Relation.RelationType_Friend;
        }
        if (relation.getEnemies().containsKey(otherId)) {
            return Relation.RelationType_Enemy;
        }
        if (relation.getShields().containsKey(otherId)) {
            return Relation.RelationType_Shield;
        }
        return Relation.RelationType_Normal;
    }

    /**
     * 是否双向好友
     *
     * @param player
     * @param targetId
     * @return
     */
    @Override
    public boolean isRealFriend(Player player, long targetId) {
        if (player.getId() == targetId) {
            return false;
        }
        Relation friendRelation = Manager.friendManager.getFriendRelation(player, targetId);
        if (friendRelation != Relation.RelationType_Friend) {
            return false;
        }
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(targetId);
        Relation friendRelation1 = getFriendRelation(playerRelation, player.getId());

        return friendRelation1 == Relation.RelationType_Friend;
    }

    @Override
    public int getFriendIntimacy(PlayerRelation relation, long otherId) {
        if (relation == null) {
            return 0;
        }
        if (!relation.getFriends().containsKey(otherId)) {
            return 0;
        }
        return relation.getFriends().get(otherId).getIntimacy();
    }

    @Override
    public void dealPlayerKillPlayer(Player diePlayer, Player attackerPlayer) {

    }

    @Override
    public void onReport(Player player, friendMessage.ReqReport messInfo) {
        long todayReportCount = Manager.countManager.getVariant(player, VariantType.TodayReportCount);
        if (todayReportCount >= Global.MaxReportNum) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.DAILY_REPORTED_MAX);
            return;
        }
        PlayerWorldInfo playerWorldInfo = PlayerManager.getInstance().getAllPlayerWorldInfo().get(messInfo.getRoleId());
        if (playerWorldInfo == null) {
            logger.error("举报的玩家不存在，id:" + messInfo.getRoleId());
            return;
        }
        //屏蔽字检查
        if (Utils.isForbiddenStr(messInfo.getContent())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ForbiddenString);
            return;
        }
        //记录日志
        ReportLog reportLog = new ReportLog();
        reportLog.setPlayer(player);
        reportLog.setType(messInfo.getType());
        reportLog.setOtherId(messInfo.getRoleId());
        reportLog.setContent(messInfo.getContent());
        LogService.getInstance().execute(reportLog);
        Manager.countManager.addVariant(player, VariantType.TodayReportCount, 1);
        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.REPORT_SUCC);
    }

    /**
     * 角色上下线更新好友状态并提示,lineState=0表示离线,1上线
     */
    private void updateOnOffLineState(long roleId, String name, byte lineState) {
//        int messageId = 0;
//        List<Player> lineOn = new ArrayList<>(manager.playerManager.getPlayersCache().values());
//        for (Player player : lineOn) {
//            long onlinePlayerId = player.getId();
//            if (player.getOwnMarriage().getMarriageId() > 0 && onlinePlayerId != roleId) {
//                if (player.getOwnMarriage().getInfo() != null && player.getOwnMarriage().getInfo().getMateId(onlinePlayerId) == roleId) {
//                    if (lineState == (byte) 1) {
//                        messageId = MessageString.MateOnline; //你的伴侣【{0}】上线了
//                    } else {
//                        messageId = MessageString.MateOffline; //你的伴侣【{0}】下线了
//                    }
//                    sendResOnOffLineNotice(player, 2, messageId, name);
//                    continue;
//                }
//            }
//
//            PlayerRelation playerRelation = manager.friendManager.getPlayerRelation(onlinePlayerId);
//            if (playerRelation.getShields().containsKey(roleId)) {
//                continue;
//            }
//            if (playerRelation.getFriends().containsKey(roleId)) {
//                if (lineState == (byte) 1) {
//                    messageId = MessageString.FriendOnline;
//                } else {
//                    messageId = MessageString.FriendOffline;
//                }
//                sendResOnOffLineNotice(player, 0, messageId, name);
//                continue;
//            }
//            if (playerRelation.getEnemies().containsKey(roleId)) {
//                if (lineState == (byte) 1) {
//                    messageId = MessageString.EnemyOnline;
//                } else {
//                    messageId = MessageString.EnemyOffline;
//                }
//                sendResOnOffLineNotice(player, 1, messageId, name);
//            }
//        }
    }

    public friendMessage.CommonInfo.Builder toCommonInfoBuilder(Player player, RelationInfo info) {
        PlayerWorldInfo playerInfo = Manager.playerManager.getPlayerWorldInfo(info.getRoleId());
        friendMessage.CommonInfo.Builder builder = null;
        //跨服直接处理
        if (playerInfo != null) {
            builder = toCommonInfoBuilder(playerInfo);
        } else {
            playerInfo = info.getInfo();
            builder = toCrossCommonInfoBuilder(playerInfo);
        }
        if (playerInfo == null) {
            return null;
        }

        if (info instanceof Friend) {
            Friend friend = (Friend) info;
            long npcFriendGiveShipPoint = Manager.countManager.getCount(player, BaseCountType.NpcFriendGiveShipPoint, friend.getRoleId());
            long npcFriendReceiveShipPoint = Manager.countManager.getCount(player, BaseCountType.NpcFriendReceiveShipPoint, friend.getRoleId());
            long npcFriendReceiveShipPointReward = Manager.countManager.getCount(player, BaseCountType.NpcFriendReceiveShipPointReward, friend.getRoleId());

            builder.setIsGiveFriendshipPoint(npcFriendGiveShipPoint >= 1);
            builder.setIsReceiveFriendshipPoint(npcFriendReceiveShipPoint >= 1);
            builder.setIsFriendshipPointAward(npcFriendReceiveShipPointReward >= 1);
            builder.setIsFriend(true);
        }
        return builder;
    }

    private friendMessage.CommonInfo.Builder toCrossCommonInfoBuilder(PlayerWorldInfo info) {
        friendMessage.CommonInfo.Builder msg = friendMessage.CommonInfo.newBuilder();
        msg.setLv(info.getLevel());
        if (!StringUtils.isEmpty(info.getRolename())) {
            msg.setName(info.getRolename());
        }
        msg.setPlayerId(info.getRoleid());
        msg.setCareer(info.getCareer());
        msg.setIsOnline(info.getLastOffTime() == 0);
        msg.setLastofftime(info.getLastOffTime());
//        msg.setHeadId(info.getFashionHeadId());
//        msg.setHeadFrameId(info.getFashionHeadFrameId());

        msg.setHead(MapUtils.getHead(info));

        msg.setServerId(info.getServerId());

        return msg;
    }

    private friendMessage.CommonInfo.Builder toCommonInfoBuilder(PlayerWorldInfo info) {
        friendMessage.CommonInfo.Builder msg = friendMessage.CommonInfo.newBuilder();
        msg.setLv(info.getLevel());
        msg.setName(info.getRolename());
        msg.setPlayerId(info.getRoleid());
        msg.setCareer(info.getCareer());
        msg.setIsOnline(info.getLastOffTime() == 0);
        msg.setLastofftime(info.getLastOffTime());
//        msg.setHeadId(info.getFashionHeadId());
//        msg.setHeadFrameId(info.getFashionHeadFrameId());

        msg.setHead(MapUtils.getHead(info));
        msg.setServerId(info.getServerId());
        Player player = Manager.playerManager.getPlayerCache(info.getRoleid());
        if (player != null) {
            //更新名字
            info.setRolename(player.getName());
            PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(player);
            if (marryTarget != null) {
                msg.setHasMarry(true);
            }
        }
        return msg;
    }

    public void addFriendEvent(Player player, int ohterPlayerSex) {
        //添加好友成功 触发事件
        Manager.countManager.addVariant(player, VariantType.AddFriendNum, 1);
        Manager.controlManager.operate(player, FunctionVariable.AddFriends, 1);
        if (ohterPlayerSex != player.getSex()) {
            Manager.marriageManager.manager().actionTask(player, MarryTask.AsFriend, 0);
        }
    }

    /**
     * 添加好友 审批结果请求
     *
     * @param player
     * @param messInfo
     */
    public void reqAddFriendApproval(Player player, friendMessage.ReqAddFriendApproval messInfo) {
        int type = 0;
        //返回结果
        friendMessage.ResAddFriendSuccess.Builder approvalAddFriendSuccessMsg = friendMessage.ResAddFriendSuccess.newBuilder();
        approvalAddFriendSuccessMsg.setType(Relation.RelationType_Friend.getValue());
        PlayerRelation approvalPlayerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        PlayerWorldInfo approvalPlayerWorldInfo = Manager.playerManager.getPlayerWorldInfo(player.getId());
        //同意列表
        List<friendMessage.ApprovalPlayerInfo> agreeListList = messInfo.getAgreeListList();
        List<friendMessage.ApprovalPlayerInfo> corssAgreeListList = new ArrayList<>();

        //String tipMsg = "我们是好友快来聊天吧";
        if (agreeListList != null && agreeListList.size() > 0) {
            type = 1;
            for (int i = 0; i < agreeListList.size(); i++) {
                //移除列表
                approvalPlayerRelation.getApprovalList().remove(agreeListList.get(i).getPlayerId());
                approvalPlayerRelation.setDataChanged(true);

                if (agreeListList.get(i).getIsShieldAddFriend()) {
                    approvalPlayerRelation.getShieldAddFriend().put(agreeListList.get(i).getPlayerId(), agreeListList.get(i).getPlayerId());
                    approvalPlayerRelation.setDataChanged(true);
                }
                //好友数量已达上线
                if (approvalPlayerRelation.getFriends().size() >= Global.FriendMax) {
                    break;
                }
                //判断本服 还是 跨服
                if (agreeListList.get(i).getServerId() != 0 && agreeListList.get(i).getServerId() != ServerConfig.getServerId()) {
                    corssAgreeListList.add(agreeListList.get(i));
                } else {
                    PlayerWorldInfo sourcePlayerWorldInfo = Manager.playerManager.getPlayerWorldInfo(agreeListList.get(i).getPlayerId());
                    if (sourcePlayerWorldInfo == null) {
                        continue;
                    }

                    PlayerRelation sourcePlayerRelation = Manager.friendManager.getPlayerRelation(agreeListList.get(i).getPlayerId());
                    //审批好友 对方好友已经满了
                    if (sourcePlayerRelation.getFriends().size() >= Global.FriendMax) {
                        break;
                    }
                    //添加好友
                    Friend sourceFriend = new Friend();
                    sourceFriend.setRoleId(sourcePlayerWorldInfo.getRoleid());
                    sourceFriend.setInfo(sourcePlayerWorldInfo);
                    approvalPlayerRelation.addFriend(sourceFriend);
                    //添加到最近联系人
                    this.addLatelyPlayer(player.getId(), sourceFriend.getRoleId());

                    approvalAddFriendSuccessMsg.addResultList(toCommonInfoBuilder(player, sourceFriend));
                    //添加好友成功 默认说化
                    Player sourcePlayer = PlayerManager.getInstance().getPlayer(sourcePlayerWorldInfo.getRoleid());
                    if (sourcePlayer != null) {
                        Manager.chatManager.deal().sendChatMessage(sourcePlayer, approvalPlayerWorldInfo, ChatChannel.CHATCHANNEL_ROLE, 0, "", MessageString.C_FriendFirstTalk);
                        Manager.chatManager.deal().leaveMsg(approvalPlayerWorldInfo.getRoleid(), LeaveMsg.makeLeaveMsg(sourcePlayer, MessageString.C_FriendFirstTalk, "", approvalPlayerWorldInfo.getRoleid(), ChatChannel.CHATCHANNEL_ROLE));
                    }
                    //从屏蔽列表移除
                    approvalPlayerRelation.getShields().remove(agreeListList.get(i).getPlayerId());

                    this.addFriendEvent(player, sourcePlayerWorldInfo.getSex());
                    //从屏蔽列表移除
                    sourcePlayerRelation.getShields().remove(approvalPlayerWorldInfo.getRoleid());
                    //对方添加好友
                    Friend approvalFriend = new Friend();
                    approvalFriend.setRoleId(approvalPlayerWorldInfo.getRoleid());
                    approvalFriend.setInfo(approvalPlayerWorldInfo);
                    sourcePlayerRelation.addFriend(approvalFriend);
                    this.addLatelyPlayer(sourcePlayerRelation.getRoleId(), approvalFriend.getRoleId());

                    friendMessage.ResAddFriendSuccess.Builder sourceAddFriendSuccessMsg = friendMessage.ResAddFriendSuccess.newBuilder();
                    sourceAddFriendSuccessMsg.setType(Relation.RelationType_Friend.getValue());
                    sourceAddFriendSuccessMsg.addResultList(toCommonInfoBuilder(sourcePlayer, approvalFriend));
                    Manager.chatManager.deal().leaveMsg(sourcePlayerWorldInfo.getRoleid(), LeaveMsg.makeLeaveMsg(player, MessageString.C_FriendFirstTalk, "", sourcePlayerWorldInfo.getRoleid(), ChatChannel.CHATCHANNEL_ROLE));
                    Player sourceOnlinePlayer = Manager.playerManager.getPlayerOnline(agreeListList.get(i).getPlayerId());
                    if (sourceOnlinePlayer == null) {
                        //离线存储
                        approvalFriend.setIsOffLineAdd(1);
                    } else {
                        Manager.chatManager.deal().sendChatMessage(player, sourcePlayerWorldInfo, ChatChannel.CHATCHANNEL_ROLE, 0, "", MessageString.C_FriendFirstTalk);
                        //检测添加好友触发条件检测
                        this.addFriendEvent(sourceOnlinePlayer, approvalPlayerWorldInfo.getSex());
                        MessageUtils.send_to_player(sourcePlayer, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, sourceAddFriendSuccessMsg.build().toByteArray());
                    }

                    Manager.friendManager.savePlayerRelation(approvalPlayerRelation);
                    Manager.friendManager.savePlayerRelation(sourcePlayerRelation);
                }
            }
        }
        //跨服审批
        if (corssAgreeListList != null && corssAgreeListList.size() > 0) {
            for (int i = 0; i < corssAgreeListList.size(); i++) {
                //好友数量已达上线
                if (approvalPlayerRelation.getFriends().size() >= Global.FriendMax) {
                    break;
                }
                friendMessage.G2SReqAddFriendApproval.Builder g2SReqAddFriendApproval = friendMessage.G2SReqAddFriendApproval.newBuilder();

                friendMessage.AddFriendApproval.Builder addFriendApproval = friendMessage.AddFriendApproval.newBuilder();

                addFriendApproval.setTargetPlayerId(corssAgreeListList.get(i).getPlayerId());
                addFriendApproval.setTargetServerId(corssAgreeListList.get(i).getServerId());
                addFriendApproval.setTargetPlat("");
                addFriendApproval.setApprovalPlayerId(player.getId());
                addFriendApproval.setApprovalServerId(ServerConfig.getServerId());
                addFriendApproval.setApprovalPlat(ServerConfig.getServerPlatform());
                PlayerMessage.GlobalPlayerWorldInfo.Builder globalPlayerWorldInfoBuilder = approvalPlayerWorldInfo.toGlobalPlayerWorldInfo();
                globalPlayerWorldInfoBuilder.setServerId(ServerConfig.getServerId());
                addFriendApproval.setGlobalPlayerWorldInfo(globalPlayerWorldInfoBuilder);
                addFriendApproval.setLeaveMessage(LeaveMsg.makeLeaveMsg(player, MessageString.C_FriendFirstTalk, "", corssAgreeListList.get(i).getPlayerId(), ChatChannel.CHATCHANNEL_ROLE).toBuildInfo());
                ChatMessage.ChatResInfo.Builder chat = Manager.chatManager.deal().MakeChatResInfoBuilder(player, approvalPlayerWorldInfo, ChatChannel.CHATCHANNEL_ROLE, 0, "", MessageString.C_FriendFirstTalk);
                addFriendApproval.setChatResSC(chat);

                g2SReqAddFriendApproval.setAddFriendApproval(addFriendApproval);
                MessageUtils.send_to_social(player.getId(), friendMessage.G2SReqAddFriendApproval.MsgID.eMsgID_VALUE, g2SReqAddFriendApproval.build().toByteArray());
            }
        }

        //拒绝列表
        List<friendMessage.ApprovalPlayerInfo> declineListList = messInfo.getDeclineListList();
        if (declineListList != null && declineListList.size() > 0) {
            type = 2;
            for (int i = 0; i < declineListList.size(); i++) {
                //移除列表
                approvalPlayerRelation.getApprovalList().remove(declineListList.get(i).getPlayerId());
                approvalPlayerRelation.setDataChanged(true);
                //屏蔽他人好友申请
                if (declineListList.get(i).getIsShieldAddFriend()) {
                    approvalPlayerRelation.getShieldAddFriend().put(declineListList.get(i).getPlayerId(), declineListList.get(i).getPlayerId());
                    approvalPlayerRelation.setDataChanged(true);
                }
            }
            Manager.friendManager.savePlayerRelation(approvalPlayerRelation);
        }
        MessageUtils.send_to_player(player, friendMessage.ResAddFriendSuccess.MsgID.eMsgID_VALUE, approvalAddFriendSuccessMsg.build().toByteArray());
        this.sendResAddFriendApproval(player, type);
    }

    /**
     * 玩家审批基础信息
     *
     * @param player
     * @return
     */
    private friendMessage.ApprovalPlayerInfo.Builder builderApprovalPlayerInfo(Player player) {
        friendMessage.ApprovalPlayerInfo.Builder approvalPlayerInfo = friendMessage.ApprovalPlayerInfo.newBuilder();
        approvalPlayerInfo.setPlayerId(player.getId());
        approvalPlayerInfo.setName(player.getName());
        approvalPlayerInfo.setLv(player.getLevel());
        approvalPlayerInfo.setCareer(player.getCareer());
        approvalPlayerInfo.setServerId(player.getCurServerId());
//        approvalPlayerInfo.setHeadId(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID());
//        approvalPlayerInfo.setHeadFrameId(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID());
        approvalPlayerInfo.setHead(MapUtils.getHead(player));
        return approvalPlayerInfo;
    }

    /**
     * 玩家审批基础信息
     *
     * @param playerWorldInfo
     * @return
     */
    private friendMessage.ApprovalPlayerInfo.Builder builderApprovalPlayerWorldInfo(PlayerWorldInfo playerWorldInfo) {
        friendMessage.ApprovalPlayerInfo.Builder approvalPlayerInfo = friendMessage.ApprovalPlayerInfo.newBuilder();
        approvalPlayerInfo.setPlayerId(playerWorldInfo.getRoleid());
        approvalPlayerInfo.setName(playerWorldInfo.getRolename());
        approvalPlayerInfo.setLv(playerWorldInfo.getLevel());
        approvalPlayerInfo.setCareer(playerWorldInfo.getCareer());
        approvalPlayerInfo.setIsShieldAddFriend(false);
        // approvalPlayerInfo.setServerId(playerWorldInfo.get);
//        approvalPlayerInfo.setHeadId(playerWorldInfo.getFashionHeadId());
//        approvalPlayerInfo.setHeadFrameId(playerWorldInfo.getFashionHeadFrameId());

        approvalPlayerInfo.setHead(MapUtils.getHead(playerWorldInfo));
        return approvalPlayerInfo;
    }


    /**
     * 刷新审批列表
     *
     * @param player
     */
    private void sendResAddFriendApprovalList(Player player) {

        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        //添加好友
        friendMessage.ResAddFriendApprovalList.Builder resAddFriendApprovalList = friendMessage.ResAddFriendApprovalList.newBuilder();
        ConcurrentHashMap<Long, Integer> approvalList = playerRelation.getApprovalList();
        if (approvalList != null && approvalList.size() > 0) {
            for (long approvaId : approvalList.keySet()) {
                PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(approvaId);
                if (playerWorldInfo != null) {
                    friendMessage.ApprovalPlayerInfo.Builder approvalPlayerInfo = builderApprovalPlayerWorldInfo(playerWorldInfo);
                    approvalPlayerInfo.setServerId(approvalList.get(approvaId));
                    resAddFriendApprovalList.addApprovalList(approvalPlayerInfo);
                }
            }
        }


        MessageUtils.send_to_player(player, friendMessage.ResAddFriendApprovalList.MsgID.eMsgID_VALUE, resAddFriendApprovalList.build().toByteArray());
    }


    /**
     * 添加好友 审批结果响应
     *
     * @param player
     */
    private void sendResAddFriendApproval(Player player, int type) {

        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        //添加好友
        friendMessage.ResAddFriendApproval.Builder resAddFriendApproval = friendMessage.ResAddFriendApproval.newBuilder();
        resAddFriendApproval.setType(type);
        ConcurrentHashMap<Long, Integer> approvalList = playerRelation.getApprovalList();
        if (approvalList != null && approvalList.size() > 0) {
            for (long approvaId : approvalList.keySet()) {
                PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(approvaId);
                if (playerWorldInfo != null) {
                    friendMessage.ApprovalPlayerInfo.Builder approvalPlayerInfo = builderApprovalPlayerWorldInfo(playerWorldInfo);
                    approvalPlayerInfo.setServerId(approvalList.get(approvaId));
                    resAddFriendApproval.addApprovalList(approvalPlayerInfo);
                }
            }
        }
        MessageUtils.send_to_player(player, friendMessage.ResAddFriendApproval.MsgID.eMsgID_VALUE, resAddFriendApproval.build().toByteArray());
    }


    /**
     * 情义点赠送
     *
     * @param givePlayer
     * @param messInfo
     */
    public void reqGiveFriendShipPoint(Player givePlayer, friendMessage.ReqGiveFriendShipPoint messInfo) {
        long giveRewardCount = Manager.countManager.getCount(givePlayer, BaseCountType.FriendShipPointGiveRewardCount, 0);
        //接受奖励次数
        long receiveRewardCount = Manager.countManager.getCount(givePlayer, BaseCountType.FriendShipPointReceiveRewardCount, 0);
        if (messInfo.getFriendType() == 1) {
            PlayerRelation givePlayerRelation = Manager.friendManager.getPlayerRelation(givePlayer.getId());
            //判断是否为好友
            if (!givePlayerRelation.getFriends().containsKey(messInfo.getFriendPlayerId())) {
                return;
            }
            //得到好友
            Friend givePlayer_friend = givePlayerRelation.getFriends().get(messInfo.getFriendPlayerId());
            boolean isSendFriendshipPointToFriend = false;
            long npcFriendGiveShipPoint = Manager.countManager.getCount(givePlayer, BaseCountType.NpcFriendGiveShipPoint, givePlayer_friend.getRoleId());
            //判断是否已经赠送了
            if (npcFriendGiveShipPoint == 0) {
                if (giveRewardCount < Global.Qingyi_send_goods_max.get(1)) {
                    Manager.countManager.addCount(givePlayer, BaseCountType.FriendShipPointGiveRewardCount, 0, Count.RefreshType.CountType_Day, 1);
                    //当天情义点增加
                    Manager.countManager.addCount(givePlayer, BaseCountType.DayFriendShipPoint, 0, Count.RefreshType.CountType_Day, Global.Qingyi_send_goods_max.get(0));
                    //增加货币
                    Manager.currencyManager.manager().onAddItemCoin(givePlayer, ItemCoinType.GoldCoin, Global.Qingyi_send_goods_max.get(0), ItemChangeReason.QingyiSendGoodsGet, IDConfigUtil.getLogId());
                    //赠送情谊
                    Manager.dailyActiveManager.deal().addDailyProgress(givePlayer, DailyActiveDefine.QingyiZengSong, 1);

                }
                Manager.countManager.setCount(givePlayer, BaseCountType.NpcFriendGiveShipPoint, givePlayer_friend.getRoleId(), Count.RefreshType.CountType_Day, 1);
                //是否发送给好友
                isSendFriendshipPointToFriend = true;
            }
            long npcFriendReceiveShipPoint = Manager.countManager.getCount(givePlayer, BaseCountType.NpcFriendReceiveShipPoint, givePlayer_friend.getRoleId());
            long npcFriendReceiveShipPointReward = Manager.countManager.getCount(givePlayer, BaseCountType.NpcFriendReceiveShipPointReward, givePlayer_friend.getRoleId());

            //判断 是否接受赠送
            if (npcFriendReceiveShipPoint >= 1) {
                if (npcFriendReceiveShipPointReward == 0) {
                    if (receiveRewardCount < Global.Qingyi_recive_goods_max.get(1)) {
                        Manager.countManager.addCount(givePlayer, BaseCountType.FriendShipPointReceiveRewardCount, 0, Count.RefreshType.CountType_Day, 1);
                        //当天情义点增加
                        Manager.countManager.addCount(givePlayer, BaseCountType.DayFriendShipPoint, 0, Count.RefreshType.CountType_Day, Global.Qingyi_recive_goods_max.get(0));
                        //增加货币
                        Manager.currencyManager.manager().onAddItemCoin(givePlayer, ItemCoinType.GoldCoin, Global.Qingyi_recive_goods_max.get(0), ItemChangeReason.QingyiReciveGoodsGet, IDConfigUtil.getLogId());

                    }
                    //表示已经领取了奖励
                    // givePlayer_friend.getFriendshipPointAwardCount().setCount(1);
                    Manager.countManager.setCount(givePlayer, BaseCountType.NpcFriendReceiveShipPointReward, givePlayer_friend.getRoleId(), Count.RefreshType.CountType_Day, 1);
                }
                //回赠
                isSendFriendshipPointToFriend = true;
            }
            Manager.friendManager.savePlayerRelation(givePlayerRelation);
            //发给好友回赠 或者 直接赠送
            if (isSendFriendshipPointToFriend) {
                PlayerWorldInfo receivePlayerWorldInfo = Manager.playerManager.getPlayerWorldInfo(givePlayer_friend.getRoleId());
                //本服赠送
                if (receivePlayerWorldInfo != null) {
                    PlayerRelation receivePlayerRelation = Manager.friendManager.getPlayerRelation(givePlayer_friend.getRoleId());
                    Friend receivePlayerFriend = receivePlayerRelation.getFriends().get(givePlayer.getId());
                    //接受玩家是否在线
                    Player receivePlayer = Manager.playerManager.getPlayer(givePlayer_friend.getRoleId());
                    if (receivePlayer != null) {
                        Manager.countManager.setCount(receivePlayer, BaseCountType.NpcFriendReceiveShipPoint, receivePlayerFriend.getRoleId(), Count.RefreshType.CountType_Day, 1);
                        this.sendResFriendShipPointCommonInfo(receivePlayer, receivePlayerFriend);
                    }
                    Manager.friendManager.savePlayerRelation(receivePlayerRelation);
                } else {
                    // 跨服
                    //发送到跨服好友赠送
                    friendMessage.G2SReqGiveFriendShipPoint.Builder g2SReqGiveFriendShipPoint = friendMessage.G2SReqGiveFriendShipPoint.newBuilder();
                    g2SReqGiveFriendShipPoint.setFriendPlayerId(givePlayer_friend.getRoleId());
                    g2SReqGiveFriendShipPoint.setGivePlayerId(givePlayer.getId());
                    MessageUtils.send_to_social(givePlayer_friend.getRoleId(), friendMessage.G2SReqGiveFriendShipPoint.MsgID.eMsgID_VALUE, g2SReqGiveFriendShipPoint.build().toByteArray());
                }
            }
            //给自己发消息
            this.sendResFriendShipPointCommonInfo(givePlayer, givePlayer_friend);
        } else if (messInfo.getFriendType() == 2) {
            //npc赠送
            long npcFriendGiveShipPoint = Manager.countManager.getCount(givePlayer, BaseCountType.NpcFriendGiveShipPoint, messInfo.getFriendPlayerId());
            //判断是否已经赠送了
            if (npcFriendGiveShipPoint == 0) {
                if (giveRewardCount < Global.Qingyi_send_goods_max.get(1)) {
                    Manager.countManager.addCount(givePlayer, BaseCountType.FriendShipPointGiveRewardCount, 0, Count.RefreshType.CountType_Day, 1);
                    //当天情义点增加
                    Manager.countManager.addCount(givePlayer, BaseCountType.DayFriendShipPoint, 0, Count.RefreshType.CountType_Day, Global.Qingyi_send_goods_max.get(0));
                    //增加货币
                    Manager.currencyManager.manager().onAddItemCoin(givePlayer, ItemCoinType.GoldCoin, Global.Qingyi_send_goods_max.get(0), ItemChangeReason.QingyiSendGoodsGet, IDConfigUtil.getLogId());
                    //赠送情谊
                    Manager.dailyActiveManager.deal().addDailyProgress(givePlayer, DailyActiveDefine.QingyiZengSong, 1);
                }
                Manager.countManager.setCount(givePlayer, BaseCountType.NpcFriendGiveShipPoint, messInfo.getFriendPlayerId(), Count.RefreshType.CountType_Day, 1);
            }
            long npcFriendReceiveShipPoint = Manager.countManager.getCount(givePlayer, BaseCountType.NpcFriendReceiveShipPoint, messInfo.getFriendPlayerId());
            if (npcFriendReceiveShipPoint != 0) {
                long npcFriendReceiveShipPointReward = Manager.countManager.getCount(givePlayer, BaseCountType.NpcFriendReceiveShipPointReward, messInfo.getFriendPlayerId());
                if (npcFriendReceiveShipPointReward != 1) {
                    if (receiveRewardCount < Global.Qingyi_recive_goods_max.get(1)) {
                        Manager.countManager.addCount(givePlayer, BaseCountType.FriendShipPointReceiveRewardCount, 0, Count.RefreshType.CountType_Day, 1);
                        //当天情义点增加
                        Manager.countManager.addCount(givePlayer, BaseCountType.DayFriendShipPoint, 0, Count.RefreshType.CountType_Day, Global.Qingyi_recive_goods_max.get(0));
                        //增加货币
                        Manager.currencyManager.manager().onAddItemCoin(givePlayer, ItemCoinType.GoldCoin, Global.Qingyi_recive_goods_max.get(0), ItemChangeReason.QingyiReciveGoodsGet, IDConfigUtil.getLogId());
                    }
                    Manager.countManager.setCount(givePlayer, BaseCountType.NpcFriendReceiveShipPointReward, messInfo.getFriendPlayerId(), Count.RefreshType.CountType_Day, 1);
                }
            }
            giveRewardCount = Manager.countManager.getCount(givePlayer, BaseCountType.FriendShipPointGiveRewardCount, 0);
            receiveRewardCount = Manager.countManager.getCount(givePlayer, BaseCountType.FriendShipPointReceiveRewardCount, 0);
            long dayFriendShipPoint = Manager.countManager.getCount(givePlayer, BaseCountType.DayFriendShipPoint, 0);
            //刷新信息给客户端
            friendMessage.ResFriendShipPointCommonInfo.Builder builder = friendMessage.ResFriendShipPointCommonInfo.newBuilder();
            builder.setFriendType(messInfo.getFriendType());
            builder.setNpcFriendInfo(this.npcFriendInfoBuilder(givePlayer, (int) messInfo.getFriendPlayerId()));
            builder.setResidueGiveRewardCount(Global.Qingyi_send_goods_max.get(1) - (int) giveRewardCount);
            builder.setDayFriendShipPoint((int) dayFriendShipPoint);
            builder.setResidueReceiveRewardCount(Global.Qingyi_recive_goods_max.get(1) - (int) receiveRewardCount);
            MessageUtils.send_to_player(givePlayer, friendMessage.ResFriendShipPointCommonInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    /**
     * 刷新信息
     *
     * @param player
     * @param friend
     */
    public void sendResFriendShipPointCommonInfo(Player player, Friend friend) {
        long giveRewardCount = Manager.countManager.getCount(player, BaseCountType.FriendShipPointGiveRewardCount, 0);
        long receiveRewardCount = Manager.countManager.getCount(player, BaseCountType.FriendShipPointReceiveRewardCount, 0);
        long dayFriendShipPoint = Manager.countManager.getCount(player, BaseCountType.DayFriendShipPoint, 0);
        friendMessage.ResFriendShipPointCommonInfo.Builder builder = friendMessage.ResFriendShipPointCommonInfo.newBuilder();
        builder.setFriendInfo(toCommonInfoBuilder(player, friend));
        builder.setResidueGiveRewardCount(Global.Qingyi_send_goods_max.get(1) - (int) giveRewardCount);
        builder.setDayFriendShipPoint((int) dayFriendShipPoint);
        builder.setResidueReceiveRewardCount(Global.Qingyi_recive_goods_max.get(1) - (int) receiveRewardCount);
        MessageUtils.send_to_player(player, friendMessage.ResFriendShipPointCommonInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    public void sendResFriendShipPointCommonInfo(Player player, int npcId) {

        //刷新信息给客户端
        friendMessage.ResFriendShipPointCommonInfo.Builder resFrriendShipPointCommonInfo = friendMessage.ResFriendShipPointCommonInfo.newBuilder();
        resFrriendShipPointCommonInfo.setFriendType(2);
        resFrriendShipPointCommonInfo.setNpcFriendInfo(this.npcFriendInfoBuilder(player, npcId));
        long giveRewardCount = Manager.countManager.getCount(player, BaseCountType.FriendShipPointGiveRewardCount, 0);
        long receiveRewardCount = Manager.countManager.getCount(player, BaseCountType.FriendShipPointReceiveRewardCount, 0);
        long dayFriendShipPoint = Manager.countManager.getCount(player, BaseCountType.DayFriendShipPoint, 0);
        resFrriendShipPointCommonInfo.setResidueGiveRewardCount(Global.Qingyi_send_goods_max.get(1) - (int) giveRewardCount);
        resFrriendShipPointCommonInfo.setDayFriendShipPoint((int) dayFriendShipPoint);
        resFrriendShipPointCommonInfo.setResidueReceiveRewardCount(Global.Qingyi_recive_goods_max.get(1) - (int) receiveRewardCount);
        MessageUtils.send_to_player(player, friendMessage.ResFriendShipPointCommonInfo.MsgID.eMsgID_VALUE, resFrriendShipPointCommonInfo.build().toByteArray());
    }

    /**
     * 零点刷新好友变量
     */
    public void zeroClockDeal(Player player) {
        //修改情义点 零点刷新
        Cfg_Npc_friend_Bean[] cfg_Npc_friend_BeanList = CfgManager.getCfg_Npc_friend_Container().getValuees();
        if (cfg_Npc_friend_BeanList != null && cfg_Npc_friend_BeanList.length > 0) {
            for (int i = 0; i < cfg_Npc_friend_BeanList.length; i++) {
                if (Manager.controlManager.deal().checkFuncProgress(player, cfg_Npc_friend_BeanList[i].getStart_variables())) {
                    int npcId = cfg_Npc_friend_BeanList[i].getId();
                    this.sendResFriendShipPointCommonInfo(player, npcId);
                }
            }
        }
    }


    /**
     * 在线 检测
     *
     * @param player
     */
    @Override
    public void online(Player player) {
        //在线检测
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
        ConcurrentHashMap<Long, Friend> friends = playerRelation.getFriends();
        if (friends != null && friends.size() > 0) {
            for (Friend friend : friends.values()) {
                if (friend.getIsOffLineAdd() == 1) {
                    //检测添加好友触发条件检测
                    Manager.countManager.addVariant(player, VariantType.AddFriendNum, 1);
                    Manager.controlManager.operate(player, FunctionVariable.AddFriends, 1);
                    if (friend.getInfo().getSex() != player.getSex()) {
                        Manager.marriageManager.manager().actionTask(player, MarryTask.AsFriend, 0);
                    }
                    //检测完毕
                    friend.setIsOffLineAdd(0);
                }
            }
        }
        this.sendResAddFriendApprovalList(player);
        //更新情义点
        friendMessage.ResFriendShipInfo.Builder resFriendShipInfoInfo = friendMessage.ResFriendShipInfo.newBuilder();
        long dayFriendShipPoint = Manager.countManager.getCount(player, BaseCountType.DayFriendShipPoint, 0);
        resFriendShipInfoInfo.setDayFriendShipPoint((int) dayFriendShipPoint);

        //获取npc好友
        List<game.message.friendMessage.NpcFriendInfo.Builder> list = this.npcFriendList(player);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                resFriendShipInfoInfo.addNpcFriendInfoList(list.get(i));
            }
        }

        MessageUtils.send_to_player(player, friendMessage.ResFriendShipInfo.MsgID.eMsgID_VALUE, resFriendShipInfoInfo.build().toByteArray());
        //刷新npc列表
        onRefreshUpProgress(player);

    }

    public friendMessage.NpcFriendInfo.Builder npcFriendInfoBuilder(Player player, int npcIc) {
        friendMessage.NpcFriendInfo.Builder npcFriendInfoBuilder = friendMessage.NpcFriendInfo.newBuilder();
        npcFriendInfoBuilder.setNpcId(npcIc);
        long npcFriendGiveShipPoint = Manager.countManager.getCount(player, BaseCountType.NpcFriendGiveShipPoint, npcIc);
        npcFriendInfoBuilder.setIsGiveFriendshipPoint(npcFriendGiveShipPoint == 1);
        long npcFriendReceiveShipPoint = Manager.countManager.getCount(player, BaseCountType.NpcFriendReceiveShipPoint, npcIc);
        npcFriendInfoBuilder.setIsReceiveFriendshipPoint(npcFriendReceiveShipPoint == 1);
        long npcFriendReceiveShipPointReward = Manager.countManager.getCount(player, BaseCountType.NpcFriendReceiveShipPointReward, npcIc);
        npcFriendInfoBuilder.setIsFriendshipPointAward(npcFriendReceiveShipPointReward == 1);
        return npcFriendInfoBuilder;
    }

    public List<game.message.friendMessage.NpcFriendInfo.Builder> npcFriendList(Player player) {
        List<game.message.friendMessage.NpcFriendInfo.Builder> list = new ArrayList<>();
        Cfg_Npc_friend_Bean[] cfg_Npc_friend_BeanList = CfgManager.getCfg_Npc_friend_Container().getValuees();
        if (cfg_Npc_friend_BeanList != null && cfg_Npc_friend_BeanList.length > 0) {
            for (int i = 0; i < cfg_Npc_friend_BeanList.length; i++) {
                if (Manager.controlManager.deal().checkFuncProgress(player, cfg_Npc_friend_BeanList[i].getStart_variables())) {
                    list.add(npcFriendInfoBuilder(player, cfg_Npc_friend_BeanList[i].getId()));
                }
            }
        }
        return list;
    }


    public void onRefreshUpProgress(Player player) {
        friendMessage.ResFriendNpcList.Builder resFriendNpcList = friendMessage.ResFriendNpcList.newBuilder();
        List<game.message.friendMessage.NpcFriendInfo.Builder> list = this.npcFriendList(player);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                resFriendNpcList.addNpcFriendInfoList(list.get(i));
            }
        }

        MessageUtils.send_to_player(player, friendMessage.ResFriendNpcList.MsgID.eMsgID_VALUE, resFriendNpcList.build().toByteArray());
    }

    /**
     * npc好友赠送请求
     *
     * @param player
     */
    public void ReqNpcFriendGiveShipPoint(Player player, friendMessage.ReqNpcFriendGiveShipPoint messInfo) {
        long npcFriendReceiveShipPoint = Manager.countManager.getCount(player, BaseCountType.NpcFriendReceiveShipPoint, messInfo.getNpcId());
        if (npcFriendReceiveShipPoint == 1) {
            return;
        }
        Manager.countManager.addCount(player, BaseCountType.NpcFriendReceiveShipPoint, messInfo.getNpcId(), Count.RefreshType.CountType_Day, 1);
        this.sendResFriendShipPointCommonInfo(player, messInfo.getNpcId());
    }
}
