package com.game.friend.struct;

import com.data.Global;
import com.game.db.bean.FriendBean;
import game.core.json.TypeReference;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.VersionUpdateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 好友关系
 */
public class PlayerRelation {

    /**
     * 玩家id
     */
    private long roleId;

    /**
     * 数据是否发生改变，存入数据库标志
     */
    private transient boolean dataChanged = false;

    /**
     * 最近聊天列表
     */
    private ConcurrentHashMap<Long, LatelyPlayer> latelyPlayers = new ConcurrentHashMap<>();

    /**
     * 好友列表
     */
    private ConcurrentHashMap<Long, Friend> friends = new ConcurrentHashMap<>();



    /**
     * 仇人列表
     */
    private ConcurrentHashMap<Long, Enemy> enemies = new ConcurrentHashMap<>();

    /**
     * 屏蔽列表
     */
    private ConcurrentHashMap<Long, Shield> shields = new ConcurrentHashMap<>();



    /**
     * 礼物赠送记录
     */
    private List<GiftRecord> sendList = new ArrayList<>();
    private List<GiftRecord> receiveList = new ArrayList<>();


    /**
     * 审批列表
     */
    private ConcurrentHashMap<Long,Integer> approvalList = new ConcurrentHashMap<>();

    /**
     * 屏蔽好友申请列表
     */
    private ConcurrentHashMap<Long, Long> shieldAddFriend = new ConcurrentHashMap<>();
    /**
     * 添加到最近列表
     * @param player
     */
    public void addLatelyPlayer(LatelyPlayer player) {
        if (latelyPlayers.containsKey(player.getRoleId())) {
            latelyPlayers.get(player.getRoleId()).setTime((int) (TimeUtils.Time() / 1000));
        } else {
            latelyPlayers.put(player.getRoleId(), player);
        }
        if (latelyPlayers.size() > Global.NearlyFriendMax) {
            LatelyPlayer latelyPlayer = latelyPlayers.values().stream()
                    .reduce(new LatelyPlayer(), (a, b) -> a.time < b.time ? a : b);
            latelyPlayers.remove(latelyPlayer.getRoleId());
        }
    }

    /**
     * 添加好友
     * @param friend
     */
    public void addFriend(Friend friend) {
        friends.put(friend.getRoleId(), friend);
        dataChanged = true;
    }



    /**
     * 删除好友
     * @param roleId
     */
    public void deleteFriend(long roleId) {
        friends.remove(roleId);
        dataChanged = true;
    }

    /**
     * 添加仇人
     * @param enemy
     */
    public void addEnemy(Enemy enemy) {
        enemies.put(enemy.getRoleId(), enemy);
        dataChanged = true;
    }

    /**
     * 删除仇人
     * @param roleId
     */
    public void deleteEnemy(long roleId) {
        enemies.remove(roleId);
        dataChanged = true;
    }

    /**
     * 添加屏蔽关系
     * @param shield
     */
    public void addShield(Shield shield) {
        shields.put(shield.getRoleId(), shield);
        dataChanged = true;
    }

    /**
     * 取消屏蔽关系
     * @param roleId
     */
    public void deleteShield(long roleId) {
        shields.remove(roleId);
        dataChanged = true;
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public ConcurrentHashMap<Long, LatelyPlayer> getLatelyPlayers() {
        return latelyPlayers;
    }

    public void setLatelyPlayers(ConcurrentHashMap<Long, LatelyPlayer> latelyPlayers) {
        this.latelyPlayers = latelyPlayers;
    }

    public ConcurrentHashMap<Long, Friend> getFriends() {
        return friends;
    }

    public void setFriends(ConcurrentHashMap<Long, Friend> friends) {
        this.friends = friends;
    }

    public ConcurrentHashMap<Long, Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ConcurrentHashMap<Long, Enemy> enemies) {
        this.enemies = enemies;
    }

    public ConcurrentHashMap<Long, Shield> getShields() {
        return shields;
    }

    public void setShields(ConcurrentHashMap<Long, Shield> shields) {
        this.shields = shields;
    }

    public List<GiftRecord> getSendList() {
        return sendList;
    }

    public void setSendList(List<GiftRecord> sendList) {
        this.sendList = sendList;
    }

    public List<GiftRecord> getReceiveList() {
        return receiveList;
    }

    public void setReceiveList(List<GiftRecord> receiveList) {
        this.receiveList = receiveList;
    }

    public FriendBean toFriendBean() {
        FriendBean friendbean = new FriendBean();
        friendbean.setRoleId(roleId);
        String latelyPlayersStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(latelyPlayers, new TypeReference<ConcurrentHashMap<Long, LatelyPlayer>>(){}), 512);
        friendbean.setLatelyPlayers(latelyPlayersStr);
        String friendsStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(friends,new TypeReference<ConcurrentHashMap<Long, Friend>>(){}), 512);
        friendbean.setFriends(friendsStr);
        String enemiesStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(enemies,new TypeReference<ConcurrentHashMap<Long, Enemy>>(){}), 512);
        friendbean.setEnemies(enemiesStr);
        String shieldsStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(shields,new TypeReference<ConcurrentHashMap<Long, Shield>>(){}), 512);
        friendbean.setShields(shieldsStr);
        String sendGiftLogStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(sendList), 512);
        friendbean.setSendLogs(sendGiftLogStr);
        String receiveGiftLogStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(receiveList), 512);
        friendbean.setReceiveLogs(receiveGiftLogStr);

        String approvalListStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(approvalList), 512);
        friendbean.setApprovalList(approvalListStr);

        String shieldAddFriendStr = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(shieldAddFriend), 512);
        friendbean.setShieldAddFriend(shieldAddFriendStr);
        return friendbean;
    }

    public ConcurrentHashMap<Long, Integer> getApprovalList() {
        return approvalList;
    }

    public void setApprovalList(ConcurrentHashMap<Long, Integer> approvalList) {
        this.approvalList = approvalList;
    }

    public ConcurrentHashMap<Long, Long> getShieldAddFriend() {
        return shieldAddFriend;
    }

    public void setShieldAddFriend(ConcurrentHashMap<Long, Long> shieldAddFriend) {
        this.shieldAddFriend = shieldAddFriend;
    }
}
