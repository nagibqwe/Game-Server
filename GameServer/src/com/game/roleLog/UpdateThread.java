package com.game.roleLog;

import com.game.backpack.structs.ItemCoinType;
import com.game.bi.bi4399.tbllog_guild;
import com.game.bi.bi4399.tbllog_player;
import com.game.db.DBErrorToFile;
import com.game.gold.structs.Gold;
import com.game.manager.Manager;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.roleLog.script.IRoleUpdateScript;
import com.game.script.structs.ScriptEnum;
import com.game.utils.RandomUtils;
import game.core.dblog.ColumnInfo;
import game.core.script.IScript;
import game.core.util.DateFormatUtils;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class UpdateThread extends Thread {

    /**
     * Logger for this class
     */
    private static final Logger logger = LogManager.getLogger(UpdateThread.class);
//    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //命令执行队列
    private LinkedList<Long> queue = new LinkedList<>();

    private LinkedList<Long> roleItemsQueue = new LinkedList<>();

    private final LinkedList<Long> deleteRoleQueue = new LinkedList<>();

    private final LinkedList<Gold> goldQueue = new LinkedList<>();

    private final LinkedList<tbllog_player> playerLinkedList = new LinkedList<>();

    private final LinkedList<tbllog_guild> guildLinkedList = new LinkedList<>();


    private final Object lock = new Object();
    private final Object lockItem = new Object();
    private final Object lockDelete = new Object();
    private final Object lockGold = new Object();
    private final Object lockGp = new Object();
    private final Object lockGd = new Object();

    private static boolean stop = false;
    private static int stepnum = 100;//每次更新数量
    private static int stepsleep = 10 * 1000;
    private DataSource ds;

    public UpdateThread(ThreadGroup group, String threadName, DataSource ds) {
        super(group, threadName);
        this.ds = ds;
    }

    public UpdateThread(DataSource ds) {
        super("ROLESTATEUPDATE");
        this.ds = ds;
    }

    public DataSource getDs() {
        return ds;
    }

    @Override
    public void run() {
        while (!stop) {
            Connection connection = null;
            try {
                boolean isDBExecture = false;
                ArrayList<Long> array = new ArrayList<>();
                synchronized (lock) {
                    try {
                        while (array.size() < stepnum && queue.size() > 0) {
                            array.add(queue.removeLast());
                        }
                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }
                ArrayList<Player> update = new ArrayList<>();

                for (Long roleId : array) {
                    Player player = PlayerManager.getInstance().getPlayerCache(roleId);
                    if (player == null) {
                        continue;
                    }
                    isDBExecture = true;
                    update.add(player);
                }

                ArrayList<Long> itemList = new ArrayList<>();
                synchronized (lockItem) {
                    try {
                        while (roleItemsQueue.size() > 0) {
                            itemList.add(roleItemsQueue.removeLast());
                        }

                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }
                ArrayList<Player> itemSave = new ArrayList<>();
                for (Long roleId : itemList) {
                    Player player = PlayerManager.getInstance().getPlayerCache(roleId);
                    if (player == null) {
                        continue;
                    }
                    isDBExecture = true;
                    itemSave.add(player);
                }

                ArrayList<Long> delList = new ArrayList<>();
                synchronized (lockDelete) {
                    try {
                        while (deleteRoleQueue.size() > 0) {
                            isDBExecture = true;
                            delList.add(deleteRoleQueue.removeLast());
                        }

                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }

                ArrayList<Gold> goldList = new ArrayList<>();
                synchronized (lockGold) {
                    try {
                        while (goldQueue.size() > 0) {
                            isDBExecture = true;
                            goldList.add(goldQueue.removeLast());
                        }

                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }

                ArrayList<tbllog_player> tbllogPlayers = new ArrayList<>();
                synchronized (lockGp) {
                    try {
                        while (playerLinkedList.size() > 0) {
                            isDBExecture = true;
                            tbllogPlayers.add(playerLinkedList.removeLast());
                        }
                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }

                ArrayList<tbllog_guild> tbllogGuild = new ArrayList<>();
                synchronized (lockGd) {
                    try {
                        while (guildLinkedList.size() > 0) {
                            isDBExecture = true;
                            tbllogGuild.add(guildLinkedList.removeLast());
                        }
                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }

                if (!isDBExecture) {
                    continue;
                }

                connection = ds.getConnection();

                if (itemSave.size() > 0) {
                    saveItems(itemSave, connection);
                }
                IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerEnterGameFinishBaseScript);
                if (is instanceof IRoleUpdateScript) {
                    ((IRoleUpdateScript) is).OndealSave(getName(), connection, update, fieldNum);
                } else {
                    logger.error("没有找到处理玩家数据保存的脚本！");
                }

                if (delList.size() > 0) {
                    deletePlayer(delList, connection);
                }

                if (goldList.size() > 0) {
                    savePlayerGold(goldList, connection);
                }

                for (tbllog_player tbllogPlayer : tbllogPlayers) {
                    tbllogPlayer.update(connection);
                }

                for (tbllog_guild tbllog_guild : tbllogGuild) {
                    tbllog_guild.update(connection);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        DBErrorToFile.error(e, e);
                    }
                }
                try {
//                    Thread.sleep(stepsleep + RandomUtils.random(0, 50 * 1000));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    DBErrorToFile.error(e, e);
                }
            }
        }
    }

    private void saveItems(ArrayList<Player> itemSave, Connection connection) {
        try {
            String buildUpdateSqlExpress = "update roleitems set roleId=?,bags=?,stores=?,equips=? where roleId=?";
            PreparedStatement updateStmt = connection.prepareStatement(buildUpdateSqlExpress);
            for (Player player : itemSave) {
                buildItemParam(player, updateStmt);
                updateStmt.setLong(5, player.getId());
                updateStmt.addBatch();
            }
            //批量执行sql
            int[] updateBatch = new int[itemSave.size()];
            try {
                updateBatch = updateStmt.executeBatch();
            } catch (BatchUpdateException e) {
                updateBatch = e.getUpdateCounts();
            }
            //关闭语句
            updateStmt.close();
            boolean isInsert = false;
            String buildInsertSqlExpress = "insert into roleitems set roleId=?,bags=?,stores=?,equips=?";
            PreparedStatement insertStmt = connection.prepareStatement(buildInsertSqlExpress);
            for (int index = 0; index < updateBatch.length; index++) {
                Player player = itemSave.get(index);
                if (updateBatch[index] <= 0) { //对执行更新失败的角色进行插入操作
                    buildItemParam(player, insertStmt);
                    insertStmt.addBatch();
                    isInsert = true;
                }
            }
            if (!isInsert) {
                return;
            }
            //批量执行
            try {
                insertStmt.executeBatch();
            } catch (BatchUpdateException e) {
                e.getUpdateCounts();
            }
        } catch (Exception e) {
            DBErrorToFile.error(e, e);
        }
    }

    private void savePlayerGold(ArrayList<Gold> goldList, Connection connection) {
        String buildUpdateSqlExpress = "update rolestate set gold=?,rechargeGold=? where roleId=? and createsid=?";
        try (PreparedStatement updateStmt = connection.prepareStatement(buildUpdateSqlExpress)) {
            for (Gold gold : goldList) {
                updateStmt.setInt(1, gold.getReaminGold());
                updateStmt.setInt(2, gold.getRechargeGold());
                updateStmt.setLong(3, gold.getUserId());
                updateStmt.setInt(4, gold.getServerId());
                updateStmt.addBatch();
            }
            //批量执行sql
            updateStmt.executeBatch();
        } catch (SQLException ex) {
            DBErrorToFile.error(ex, ex);
        }
    }

    private void deletePlayer(ArrayList<Long> delete, Connection connection) {
        String buildUpdateSqlExpress = "update rolestate set isDelete=? where roleId=?";
        int now = (int) (TimeUtils.Time() / 1000);
        try (PreparedStatement updateStmt = connection.prepareStatement(buildUpdateSqlExpress)) {
            for (Long id : delete) {
                updateStmt.setInt(1, now);
                updateStmt.setLong(2, id);
                updateStmt.addBatch();
            }
            //批量执行sql
            updateStmt.executeBatch();
        } catch (SQLException ex) {
            DBErrorToFile.error(ex, ex);
        }
    }

    public void updateState(long roleId) {
        synchronized (lock) {
            try {
                if (queue.size() <= 10000 && !queue.contains(roleId)) {
                    queue.add(roleId);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            }
        }
    }

    public void updateRoleItemState(long roleId) {
        synchronized (lockItem) {
            try {
                if (roleItemsQueue.size() <= 10000 && !roleItemsQueue.contains(roleId)) {
                    roleItemsQueue.add(roleId);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            }
        }
    }

    public void deleteRoleState(long roleId) {
        synchronized (lockDelete) {
            try {
                if (deleteRoleQueue.size() <= 10000 && !deleteRoleQueue.contains(roleId)) {
                    deleteRoleQueue.add(roleId);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            }
        }
    }

    public void goldChange(Gold gold) {
        synchronized (lockGold) {
            try {
                if (goldQueue.size() <= 10000 && !goldQueue.contains(gold)) {
                    goldQueue.add(gold);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            }
        }
    }

    public void tbllogPlayer(tbllog_player player) {
        synchronized (lockGp) {
            try {
                if (playerLinkedList.size() <= 10000 && !playerLinkedList.contains(player)) {
                    playerLinkedList.add(player);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            }
        }
    }

    public void tbllogGuild(tbllog_guild guild) {
        synchronized (lockGd) {
            try {
                if (guildLinkedList.size() <= 10000 && !playerLinkedList.contains(guild)) {
                    guildLinkedList.add(guild);
                }
            } catch (Exception e) {
                DBErrorToFile.error(e, e);
            }
        }
    }



    public LinkedList<tbllog_player> getPlayerLinkedList() {
        return playerLinkedList;
    }

    public LinkedList<tbllog_guild> getGuildLinkedList() {
        return guildLinkedList;
    }

    private static int fieldNum = 0;

    public static List<ColumnInfo> buildFields() {
        List<ColumnInfo> parm = new ArrayList<>();
        parm.add(ColumnInfo.createColumnInfo("roleId", "bigint", 20, true, "", "角色ID值"));
        parm.add(ColumnInfo.createColumnInfo("userId", "bigint", 20, true, "", "账号ID值"));
        parm.add(ColumnInfo.createColumnInfo("roleName", "varchar", 50, true, "", "角色名"));
        parm.add(ColumnInfo.createColumnInfo("machineCode", "varchar", 70, true, "", "机器码"));
        parm.add(ColumnInfo.createColumnInfo("platUserId", "varchar", 128, true, "", "渠道ID"));
        parm.add(ColumnInfo.createColumnInfo("clientOS", "varchar", 30, true, "", "客户端系统"));
        parm.add(ColumnInfo.createColumnInfo("level", "int", 11, true, "", "等级"));
        parm.add(ColumnInfo.createColumnInfo("sex", "int", 4, true, "", "性别"));
        parm.add(ColumnInfo.createColumnInfo("career", "int", 4, true, "", "角色职业"));
        parm.add(ColumnInfo.createColumnInfo("createTime", "varchar", 50, true, "", "创建时间"));
        parm.add(ColumnInfo.createColumnInfo("onlineTime", "int", 11, true, "", "在线时长"));
        parm.add(ColumnInfo.createColumnInfo("createsid", "int", 11, true, "", "创建服ID"));
        parm.add(ColumnInfo.createColumnInfo("attack", "bigint", 20, true, "", "攻击力"));
        parm.add(ColumnInfo.createColumnInfo("lastupdatetime", "varchar", 50, true, "", "更新的最新时间值"));
        parm.add(ColumnInfo.createColumnInfo("ts", "int", 11, false, "", "更新的最新秒值"));
        parm.add(ColumnInfo.createColumnInfo("bagcellsnum", "int", 11, true, "", "背包格子数量"));
        parm.add(ColumnInfo.createColumnInfo("ip", "varchar", 50, true, "", "当前登录IP"));
        parm.add(ColumnInfo.createColumnInfo("money", "bigint", 20, true, "", "铜币"));
        parm.add(ColumnInfo.createColumnInfo("gold", "bigint", 20, true, "", "元宝"));
        parm.add(ColumnInfo.createColumnInfo("iron", "bigint", 20, true, "", "体力"));
        parm.add(ColumnInfo.createColumnInfo("isDelete", "int", 11, true, "", "是否删除"));
        parm.add(ColumnInfo.createColumnInfo("rechargeGold", "bigint", 20, true, "", "总充值获得元宝数"));
        parm.add(ColumnInfo.createColumnInfo("funcellUUid", "varchar", 64, true, "", "funcell生成的UUid"));
        parm.add(ColumnInfo.createColumnInfo("platformName", "varchar", 50, true, "", "渠道"));
        parm.add(ColumnInfo.createColumnInfo("horseLayer", "int", 11, true, "", "坐骑等级"));
        parm.add(ColumnInfo.createColumnInfo("horseIllusionLevel", "int", 11, true, "", "坐骑等级"));
        parm.add(ColumnInfo.createColumnInfo("horseIds", "varchar", 300,true,"","所有坐骑Id集"));
        parm.add(ColumnInfo.createColumnInfo("wingIds", "varchar", 300,true,"","所有翅膀Id集"));
        parm.add(ColumnInfo.createColumnInfo("petIds", "varchar", 300,true,"","所有宠物Id集"));
        parm.add(ColumnInfo.createColumnInfo("clotheStar", "int", 11, true, "", "衣服星级"));
        parm.add(ColumnInfo.createColumnInfo("weaponStar", "int", 11, true, "", "武器星级"));
        parm.add(ColumnInfo.createColumnInfo("equipAllStar", "int", 11, true, "", "穿戴装备总星级"));
        parm.add(ColumnInfo.createColumnInfo("moonCardDay", "int", 11, true, "", "玩家的月卡结束天数"));

        parm.add(ColumnInfo.createColumnInfo("isRecharge", "int", 11, true, "", "角色是否有充过值"));
        parm.add(ColumnInfo.createColumnInfo("lastLoginTime", "int", 11, true, "", "最后一次登录时间"));
        parm.add(ColumnInfo.createColumnInfo("fashionBodyId", "int", 11, true, "", "时装身体ID"));
        parm.add(ColumnInfo.createColumnInfo("fashionWeaponId", "int", 11, true, "", "时装武器ID"));
        parm.add(ColumnInfo.createColumnInfo("lifeCard", "int", 11, true, "", "是否激活终身卡"));
        parm.add(ColumnInfo.createColumnInfo("clearlevel", "int", 11, true, "", "清理等级"));
        parm.add(ColumnInfo.createColumnInfo("cpUserId", "varchar", 64, true, "", "平台账号ID"));
        fieldNum = parm.size();
        return parm;
    }

    public static void buildParam(Player player, PreparedStatement ppstat) {
        try {
            int index = 1;
            ppstat.setLong(index++, player.getId());
            ppstat.setLong(index++, player.getUserId());
            ppstat.setString(index++, player.getName());
            ppstat.setString(index++, player.getMaCode());
            ppstat.setString(index++, player.getPlatUserId());
            ppstat.setString(index++, player.getOs());
            ppstat.setInt(index++, player.getLevel());
            ppstat.setInt(index++, player.getSex());
            ppstat.setInt(index++, player.getCareer());
            ppstat.setString(index++, DateFormatUtils.format(new Date((long) player.getCreateTime() * 1000),"yyyy-MM-dd HH:mm:ss"));
            ppstat.setInt(index++, player.getAccunonlinetime());
            ppstat.setInt(index++, player.getCreateServerId());
            ppstat.setLong(index++, player.getFightPoint());
            ppstat.setString(index++, DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            ppstat.setInt(index++, (int) (TimeUtils.Time() / 1000));
            ppstat.setInt(index++, player.getBagCellsNum());
            ppstat.setString(index++, player.getLoginIP());
            ppstat.setLong(index++, Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.BindMoney));
            ppstat.setInt(index++, player.getGold() != null ? player.getGold().getReaminGold() : 0);
            ppstat.setInt(index++, (int) Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.PhysicalStrength));//体力
            ppstat.setInt(index++, player.getDeleteTime());//如果删除时间大于0表示删除
            ppstat.setInt(index++, (int) Manager.rechargeManager.deal().rechargeAll(player));
            ppstat.setString(index++, player.getUuid());
            ppstat.setString(index++, player.getPlatformName());
            ppstat.setInt(index++, player.getHorse().getHorseId() == 0 ? 0 : player.getHorse().getHorseSteps());
            ppstat.setInt(index++, player.getHorse().getHorseId() == 0 ? 0 : player.getHorse().getHorseStar());
            ppstat.setString(index++,player.getHorse().getHorseId() == 0 ? "" : JsonUtils.toJSONString(player.getHorse().getNature().getHuaxins().keySet()));
            ppstat.setString(index++,player.getWing() == null ? "" : JsonUtils.toJSONString(player.getWing().getHuaxins().keySet()));
            ppstat.setString(index++,player.getActivePet().getPets() == null ? "" : JsonUtils.toJSONString(player.getActivePet().getPets().keySet()));

            ppstat.setInt(index++, Manager.equipManager.getClothesStar(player));
            ppstat.setInt(index++, Manager.equipManager.getWeaponStar(player));
            ppstat.setInt(index++, Manager.equipManager.getAllStar(player));
            ppstat.setInt(index++, player.getMoonCardDays());

            //角色是否充过值
            int so = 0;
            if (player.getGold() != null) {
                so = 1;
            }
            ppstat.setInt(index++, so);
            ppstat.setInt(index++, (int) (player.getLastLoginTime() / 1000));
            ppstat.setInt(index++, player.getNewFashionData().getBodyID());
            ppstat.setInt(index++, player.getNewFashionData().getWeaponID());
            ppstat.setInt(index++, player.isLifeCard() ? 1 : 0);
            ppstat.setInt(index++, player.getClearLevel());
            ppstat.setString(index++, player.getBi().getCpUserId());
        } catch (SQLException e) {
            DBErrorToFile.error(e);
        }
    }

    public static void buildItemParam(Player player, PreparedStatement ppstat) {
        try {
            int index = 1;
            String bags = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(player.getBackpackItems()));
            String stores = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(player.getStoreItems()));
            /**
             * 装备系统重构——在EquipPart中增加私有变量Equip，表示此部位穿的装备，为空表示没穿
             * huangzhaomin 2019/04/30
             * 原始代码：
             * String equips = VersionUpdateUtil.dataSave(JSON.toJSONString(player.getEquips(), SerializerFeature.WriteClassName));
             * */
            String equips = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(player.getEquipParts()));
            ppstat.setLong(index++, player.getId());
            ppstat.setString(index++, bags);
            ppstat.setString(index++, stores);
            ppstat.setString(index++, equips);
        } catch (SQLException e) {
            DBErrorToFile.error(e);
        }
    }

    public static String buildDDL() {
        List<ColumnInfo> buildFields = buildFields();
        StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE TABLE IF NOT EXISTS `rolestate` (");
        for (ColumnInfo info : buildFields) {
            ddl.append("\r\n").append(info.toDDL()).append(",");
        }
        ddl.substring(0, ddl.length() - 1);

        ddl.append("\r\n" + "PRIMARY KEY (`roleId`),");
        ddl.append("\r\n" + "  KEY `rolename` (`rolename`),\r\n"
                + "  KEY `index_2` (`userId`),\r\n" + "  KEY `role_sid` (`rolename`, `createsid`),\r\n" + "  KEY `userId_sid` (`userId`,`createsid`)");
        ddl.append("\r\n" + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;");
        return ddl.toString();
    }

    public LinkedList<Long> getQueue() {
        return queue;
    }

    public void setQueue(LinkedList<Long> queue) {
        this.queue = queue;
    }

    public LinkedList<Long> getRoleItemsQueue() {
        return roleItemsQueue;
    }

    public void setRoleItemsQueue(LinkedList<Long> roleItemsQueue) {
        this.roleItemsQueue = roleItemsQueue;
    }

    public static boolean isStop() {
        return stop;
    }

    public static void setStop(boolean stop) {
        UpdateThread.stop = stop;
    }

    public static int getFieldNum() {
        return fieldNum;
    }

}
