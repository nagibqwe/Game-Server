package com.game.friend.manager;

import com.game.db.bean.FriendBean;
import com.game.db.dao.FriendDao;
import com.game.friend.script.ICrossFriendScript;
import com.game.friend.script.IFriendScript;
import com.game.friend.struct.*;
import com.game.manager.Manager;
import com.game.friend.struct.GiftRecord;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;

import game.core.json.TypeReference;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 好友管理器
 */
public class FriendManager {

    private static final Logger log = LogManager.getLogger(FriendManager.class);

    private final FriendDao friendDao = new FriendDao();

    /**
     * 全服好友数据
     */
    private final ConcurrentHashMap<Long, PlayerRelation> allFriendsHashMap = new ConcurrentHashMap<>();


    public static FriendManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private enum Singleton {

        INSTANCE;
        FriendManager manager;

        Singleton() {
            this.manager = new FriendManager();
        }

        FriendManager getProcessor() {
            return manager;
        }
    }

    public IFriendScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FriendBaseScript);
        if (is instanceof IFriendScript) {
            return (IFriendScript) is;
        } else {
            log.error("没有实现好友脚本");
            return null;
        }
    }

    public ICrossFriendScript cross(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossFriendScript);
        if (is instanceof ICrossFriendScript) {
            return (ICrossFriendScript) is;
        } else {
            log.error("没有实现跨服好友脚本");
            return null;
        }
    }
    /**
     * 检查是否屏蔽
     */
    public boolean isShield(long roleId, long recRoleId) {
        PlayerRelation playerRelation = getPlayerRelation(recRoleId);
        if (playerRelation == null) {
            return false;
        }
        return playerRelation.getShields().containsKey(roleId);
    }

    /**
     * 检查其它玩家与自己的关系
     */
    public Relation getFriendRelation(Player player, long otherId) {
        if (player.getId() == otherId) {
            return Relation.RelationType_SELF;
        }
        PlayerRelation playerRelation = getPlayerRelation(player.getId());
        try {
            return deal().getFriendRelation(playerRelation, otherId);
        } catch (Exception e) {
            return Relation.RelationType_Normal;
        }
    }

    /**
     * 从数据库读取数据后获得所有关系数据
     */
    private void initRelationData(PlayerRelation playerRelation) {
        initLatelyPlayerData(playerRelation);
        initFriendsData(playerRelation);
        initEnemiesData(playerRelation);
        initShieldsData(playerRelation);
    }

    /**
     * 刷新最近数据
     */
    private void initLatelyPlayerData(PlayerRelation playerRelation) {
        Iterator<Map.Entry<Long, LatelyPlayer>> iterator = playerRelation.getLatelyPlayers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, LatelyPlayer> entry = iterator.next();
            long roleId = entry.getKey();
            LatelyPlayer latelyPlayer = entry.getValue();
            PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
            if (info == null) {
                iterator.remove();
                continue;
            }
            latelyPlayer.setInfo(info);
        }
    }

    /**
     * 刷新好友数据
     */
    private void initFriendsData(PlayerRelation playerRelation) {
        Iterator<Map.Entry<Long, Friend>> iterator = playerRelation.getFriends().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Friend> entry = iterator.next();
            long roleId = entry.getKey();
            Friend friend = entry.getValue();
            PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
            if (info == null) {
                iterator.remove();
                continue;
            }
            friend.setInfo(info);
        }
    }

    /**
     * 刷新仇人数据
     */
    private void initEnemiesData(PlayerRelation playerRelation) {
        Iterator<Map.Entry<Long, Enemy>> iterator = playerRelation.getEnemies().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Enemy> entry = iterator.next();
            long roleId = entry.getKey();
            Enemy enemy = entry.getValue();
            PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
            if (info == null) {
                iterator.remove();
                continue;
            }
            enemy.setInfo(info);
        }
    }

    /**
     * 刷新屏蔽数据
     */
    private void initShieldsData(PlayerRelation playerRelation) {
        Iterator<Map.Entry<Long, Shield>> iterator = playerRelation.getShields().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Shield> entry = iterator.next();
            long roleId = entry.getKey();
            Shield shield = entry.getValue();
            PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
            if (info == null) {
                iterator.remove();
                continue;
            }
            shield.setInfo(info);
        }

    }

    /**
     * 保存玩家好友数据
     */
    public void savePlayerRelation(PlayerRelation playerRelation) {
        if (!playerRelation.isDataChanged()) {
            return;
        }

        FriendBean bean = playerRelation.toFriendBean();
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.FRIEND_INSERT, SaveServer.MERGE);
        playerRelation.setDataChanged(false);
    }

    /**
     * 好友db数据转为PlayerRelation内存数据
     */
    private PlayerRelation friendBeanToPlayerRelation(FriendBean friendBean) {
        PlayerRelation playerRelation = new PlayerRelation();
        playerRelation.setRoleId(friendBean.getRoleId());
        try {
            ConcurrentHashMap<Long, LatelyPlayer> latelyPlayers = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getLatelyPlayers()), new TypeReference<ConcurrentHashMap<Long, LatelyPlayer>>() {
            });
            playerRelation.setLatelyPlayers(latelyPlayers);

            ConcurrentHashMap<Long, Friend> friends = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getFriends()), new TypeReference<ConcurrentHashMap<Long, Friend>>() {
            });
            playerRelation.setFriends(friends);
            ConcurrentHashMap<Long, Enemy> enemys = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getEnemies()), new TypeReference<ConcurrentHashMap<Long, Enemy>>() {
            });
            playerRelation.setEnemies(enemys);
            ConcurrentHashMap<Long, Shield> shields = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getShields()), new TypeReference<ConcurrentHashMap<Long, Shield>>() {
            });
            playerRelation.setShields(shields);
            List<GiftRecord> sendGiftLogs = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getSendLogs()), new TypeReference<ArrayList<GiftRecord>>(){});
            playerRelation.setSendList(sendGiftLogs);
            List<GiftRecord> receiveGiftLogs = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getReceiveLogs()), new TypeReference<ArrayList<GiftRecord>>(){});
            playerRelation.setReceiveList(receiveGiftLogs);


            ConcurrentHashMap<Long, Integer> approvalList = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getApprovalList()), new TypeReference<ConcurrentHashMap<Long, Integer>>() {
            });
            playerRelation.setApprovalList(approvalList);

            ConcurrentHashMap<Long, Long> shieldAddFriend = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(friendBean.getShieldAddFriend()), new TypeReference<ConcurrentHashMap<Long, Long>>() {
            });
            playerRelation.setShieldAddFriend(shieldAddFriend);


        } catch (Exception ex) {
            log.error("玩家好友数据解析出错 roleIdId:" + friendBean.getRoleId(), ex);
        }
        return playerRelation;
    }

    /**
     * 获得玩家关系数据，没有则新建一个
     */
    public PlayerRelation getPlayerRelation(long roleId) {
        if (allFriendsHashMap.containsKey(roleId)) {
            return allFriendsHashMap.get(roleId);
        }
        FriendBean friendBean = friendDao.selectFriend(roleId);
        PlayerRelation playerRelation;
        if (friendBean == null) {
            playerRelation = new PlayerRelation();
            playerRelation.setRoleId(roleId);
        } else {
            playerRelation = friendBeanToPlayerRelation(friendBean);
            initRelationData(playerRelation);
        }
        allFriendsHashMap.put(roleId, playerRelation);
        return playerRelation;
    }

    /**
     * 查看好友与我的亲密度值
     */
    public int getFriendIntimacy(Player player, long otherId) {
        if (player.getId() == otherId) {
            return 0;
        }
        PlayerRelation relation = getPlayerRelation(player.getId());

        try {
            return deal().getFriendIntimacy(relation, otherId);
        } catch (Exception e) {
            log.error(e, e);
        }
        return 0;
    }

    public boolean checkFriendRelation(long playerId, long otherId) {
        PlayerRelation playerRelation = getPlayerRelation(playerId);
        PlayerRelation otherRelation = getPlayerRelation(otherId);
        if (!playerRelation.getFriends().containsKey(otherId)) {
            return false;
        }
        if (!otherRelation.getFriends().containsKey(playerId)) {
            return false;
        }

        Friend friend = playerRelation.getFriends().get(otherId);
        Friend otherFriend = otherRelation.getFriends().get(playerId);
        if (friend == null || otherFriend == null) {
            return false;
        }

        return true;
    }

    /**
     * 获取玩家挚友列表
     */
    public List<Long> getMyIntimateFriend(Player player) {
        List<Long> intimateFriendList = new ArrayList<>();
        PlayerRelation playerRelation = getPlayerRelation(player.getId());
        for (Friend friend : playerRelation.getFriends().values()) {
            if (getPlayerRelation(friend.getRoleId()).getFriends().containsKey(player.getId())) {
                intimateFriendList.add(friend.getRoleId());
            }
        }
        return intimateFriendList;
    }
}
