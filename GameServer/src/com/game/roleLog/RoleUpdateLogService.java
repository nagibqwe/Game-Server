package com.game.roleLog;

import com.alibaba.druid.pool.DruidDataSource;
import com.game.bi.bi4399.tbllog_guild;
import com.game.bi.bi4399.tbllog_player;
import com.game.gold.structs.Gold;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.roleLog.script.IRoleUpdateScript;
import com.game.script.structs.ScriptEnum;
import game.core.dblog.ColumnInfo;
import game.core.dblog.base.MetaData;
import game.core.dblog.db.util.DBUtils;
import game.core.dblog.db.util.TableCompar;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class RoleUpdateLogService {
    private static final Logger logger = LogManager.getLogger(RoleUpdateLogService.class);

    private enum Singleton {
        INSTANCE;
        RoleUpdateLogService manager;

        Singleton() {
            this.manager = new RoleUpdateLogService();
        }

        RoleUpdateLogService getProcessor() {
            return manager;
        }
    }

    public static RoleUpdateLogService getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private static ThreadGroup threadGroup = new ThreadGroup("ROLESTATESAVETHREAD");
    private static int threadnum = 2;
    private List<UpdateThread> threadarray = new ArrayList<>();
    private DruidDataSource ds;
    private boolean start;

    private RoleUpdateLogService() {
        logger.info("初始化update日志数据库服务");
        try {
//            ds = new ComboPooledDataSource();
//            ds.setDriverClass(DbServerConfig.getLogDrivers());
//            ds.setJdbcUrl(DbServerConfig.getLogUrl());
//            ds.setPassword(DbServerConfig.getLogPassword());
//            ds.setUser(DbServerConfig.getLogUser());
//            ds.setInitialPoolSize(10);
//            ds.setAcquireIncrement(10);
//            ds.setMinPoolSize(10);
//            ds.setMaxPoolSize(30);
//            ds.setMaxIdleTime(60000);
//            ds.setCheckoutTimeout(2000);
//            ds.setIdleConnectionTestPeriod(60 * 10);
//            ds.setPreferredTestQuery(DbServerConfig.getLogValidationquery());
            ds = new DruidDataSource();
            ds.setDriverClassName(ServerConfig.getLogDrivers());
            ds.setUrl(ServerConfig.getLogUrl());
            ds.setUsername(ServerConfig.getLogUser());
            ds.setPassword(ServerConfig.getLogPassword());
            ds.setPoolPreparedStatements(true);//打开游标缓存
            ds.setMaxWait(60000);
            ds.setValidationQuery(ServerConfig.getLogValidationquery());
            ds.setMinIdle(10);
            ds.setMaxActive(30);
            ds.setMaxOpenPreparedStatements(20);
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);
            ds.setRemoveAbandoned(true);
            ds.setRemoveAbandonedTimeout(1800);//30分中的连接不用则关闭
            ds.setLogAbandoned(true);//记录删除日志
            logger.info("初始化update日志数据库服务");
            logger.info("启动update日志线程池完毕");
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
        logger.info("初始update化日志数据库服务结束");
        checkTable();

        for (int i = 0; i < threadnum; i++) {
            UpdateThread thread = new UpdateThread(threadGroup, "" + i, ds);
            threadarray.add(thread);
            thread.start();
        }
        start = true;
    }

    private void checkRoleState(Connection connection, boolean isExist) throws SQLException {
        if (!isExist) {
            logger.info("rolestate表不存在创建");
            try {
                String buildDDL = UpdateThread.buildDDL();
                Statement createStatement = connection.createStatement();
                createStatement.execute(buildDDL);
            } catch (Exception e) {
                logger.info("rolestate表创建失败");
            }
            logger.info("rolestate表创建完毕");
        } else {
            logger.info("rolestate表存在检查差异");
            List<ColumnInfo> columnDefine = DBUtils.getColumnDefine(connection, "rolestate");
            List<ColumnInfo> buildFields = UpdateThread.buildFields();
            try {
                List<String> compartor = TableCompar.getInstance().compartor("rolestate", buildFields, columnDefine);
                if (compartor.size() > 0) {
                    Statement createStatement = connection.createStatement();
                    for (String string : compartor) {
                        createStatement.execute(string);
                    }
                }
            } catch (Exception e) {
                logger.error("表结构自动适配失败", e);
            }
        }
    }

    private void checkRoleItems(Connection connection, boolean isExist) {
        if (!isExist) {
            logger.info("roleitems表不存在创建");
            try {
                String buildDDL = "CREATE TABLE `roleitems` (\n"
                        + "  `roleId` bigint(20) NOT NULL COMMENT '角色ID',\n"
                        + "  `bags` longtext COMMENT '背包物品数据',\n"
                        + "  `stores` longtext COMMENT '仓库物品数据',\n"
                        + "  `equips` longtext COMMENT '穿上的装备数据',\n"
                        + "  PRIMARY KEY (`roleId`)\n"
                        + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='当前玩家背包和仓库数据';";
                Statement createStatement = connection.createStatement();
                createStatement.execute(buildDDL);
            } catch (Exception e) {
                logger.info("roleitems表创建失败");
            }
            logger.info("roleitems表创建完毕");
        }
    }

    private void checkTblLogPlayer(Connection connection, boolean isExist) throws Exception {
        tbllog_player player = new tbllog_player();
        if (!isExist) {
            String buildCreateTableSql = player.buildCreateTableSql(TimeUtils.Time());
            Statement createStatement = connection.createStatement();
            createStatement.execute(buildCreateTableSql);
        } else {
            List<ColumnInfo> columnDefine = DBUtils.getColumnDefine(connection, "tbllog_player");
            //存在表  对比结构
            List<ColumnInfo> codeDefine = new ArrayList<>();
            HashSet<MetaData> metaDataSet = player.getMetadata();
            for (MetaData md : metaDataSet) {
                codeDefine.add(md.toColumnInfo());
            }
            List<String> compartor = TableCompar.getInstance().compartor("tbllog_player", codeDefine, columnDefine);
            if (compartor.size() > 0) {
                Statement createStatement = connection.createStatement();
                for (String string : compartor) {
                    logger.info("检查到变更" + string);
                    createStatement.addBatch(string);
                }
                createStatement.executeBatch();
            }
        }
    }

    private void checkTblLogGuild(Connection connection, boolean isExist) throws Exception {
        tbllog_guild guild = new tbllog_guild();
        if (!isExist) {
            String buildCreateTableSql = guild.buildCreateTableSql(TimeUtils.Time());
            Statement createStatement = connection.createStatement();
            createStatement.execute(buildCreateTableSql);
        } else {
            List<ColumnInfo> columnDefine = DBUtils.getColumnDefine(connection, "tbllog_guild");
            //存在表  对比结构
            List<ColumnInfo> codeDefine = new ArrayList<>();
            HashSet<MetaData> metaDataSet = guild.getMetadata();
            for (MetaData md : metaDataSet) {
                codeDefine.add(md.toColumnInfo());
            }
            List<String> compartor = TableCompar.getInstance().compartor("tbllog_guild", codeDefine, columnDefine);
            if (compartor.size() > 0) {
                Statement createStatement = connection.createStatement();
                for (String string : compartor) {
                    logger.info("检查到变更" + string);
                    createStatement.addBatch(string);
                }
                createStatement.executeBatch();
            }
        }
    }

    private void checkTable() {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            List<String> tableNames = DBUtils.getTableName(connection);
            checkRoleState(connection, tableNames.contains("rolestate"));
            checkRoleItems(connection, tableNames.contains("roleitems"));
            checkTblLogPlayer(connection, tableNames.contains("tbllog_player"));
            checkTblLogGuild(connection, tableNames.contains("tbllog_guild"));
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e, e);
                }
            }
        }
    }

    public void updateRoleDate(long roleId) {
        try {
            if (start) {
                int tag = (int) (Math.abs(roleId) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                if (updateThread != null && updateThread.getQueue().size() < 1000) {
                    updateThread.updateState(roleId);
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void updateTblLogPlayer(tbllog_player player) {
        try {
            if (start) {
                int tag = (int) (Math.abs(player.getRole_id()) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                if (updateThread != null && updateThread.getPlayerLinkedList().size() < 1000) {
                    updateThread.tbllogPlayer(player);
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public boolean createTblLogPlayer(tbllog_player player) {
        Connection connection = null;
        try {
            if (start) {
                int tag = (int) (Math.abs(player.getRole_id()) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                connection = updateThread.getDs().getConnection();
                String sql = player.buildSql();
                Statement createStatement = connection.createStatement();
                return createStatement.execute(sql);
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e, e);
                }
            }
        }
        return false;
    }

    public boolean createTblLogGuild(tbllog_guild guild) {
        Connection connection = null;
        try {
            if (start) {
                int tag = (int) (Math.abs(guild.getGuild_id()) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                connection = updateThread.getDs().getConnection();
                String sql = guild.buildSql();
                Statement createStatement = connection.createStatement();
                return createStatement.execute(sql);
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e, e);
                }
            }
        }
        return false;
    }

    public void updateTblLogGuild(tbllog_guild guild) {
        try {
            if (start) {
                int tag = (int) (Math.abs(guild.getGuild_id()) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                if (updateThread != null && updateThread.getGuildLinkedList().size() < 1000) {
                    updateThread.tbllogGuild(guild);
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void updateRoleItemData(long roleId) {
        try {
            if (start) {
                int tag = (int) (Math.abs(roleId) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                if (updateThread != null && updateThread.getRoleItemsQueue().size() < 1000) {
                    updateThread.updateRoleItemState(roleId);
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void deleteRoleState(long roleId) {
        try {
            if (start) {
                int tag = (int) (Math.abs(roleId) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                if (updateThread != null && updateThread.getRoleItemsQueue().size() < 1000) {
                    updateThread.deleteRoleState(roleId);
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void goldChange(Gold gold) {
        try {
            if (start) {
                int tag = (int) (Math.abs(gold.getUserId()) % threadnum);
                UpdateThread updateThread;
                if (threadarray.size() < tag + 1) {
                    updateThread = threadarray.get(0);
                } else {
                    updateThread = threadarray.get(tag);
                }
                if (updateThread != null && updateThread.getRoleItemsQueue().size() < 1000) {
                    updateThread.goldChange(gold);
                }
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    public void createRoleSave(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerEnterGameFinishBaseScript);
        if (is instanceof IRoleUpdateScript) {
            Connection cs;
            try {
                cs = ds.getConnection();
            } catch (SQLException ex) {
                logger.error(ex, ex);
                return;
            }
            try {
                ((IRoleUpdateScript) is).OndealCreateSave("创建角色的时保证数据", cs, player, UpdateThread.getFieldNum());
            } catch (Exception ex) {
                logger.error(ex, ex);
            }
            if (cs == null) {
                return;
            }
            try {
                cs.close();//回收线程
            } catch (SQLException ex) {
                logger.error(ex, ex);
            }

        } else {
            logger.error("没有找到处理玩家数据保存的脚本！");
        }
    }

    public DruidDataSource getDs() {
        return ds;
    }

    public void setDs(DruidDataSource ds) {
        this.ds = ds;
    }

    //更新的关闭系统
    public void shutdown() {
        logger.info("角色更新的数据库日志关闭！");
        ds.close();
    }

}
