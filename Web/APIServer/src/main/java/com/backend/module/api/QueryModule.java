package com.backend.module.api;

import com.backend.bean.Server;
import com.backend.manager.LoginServerManager;
import com.backend.struct.Account;
import com.backend.struct.RoleState;
import com.backend.utils.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import java.util.*;

/**
 * @Desc 第三方对外API接口
 * @Date 2020/11/30 14:50
 * @Auth ZUncle
 */
@IocBean
@At("/query")
@Ok("json")
@Filters
public class QueryModule {

    private static final Log logger = Logs.get();

    @At("/")
    public void index() {}

    @Inject
    private Dao dao;

    @At
    @POST
    @GET
    public Object queryRoleByRoleId(int serverId, String roleId) {
        if (Strings.isBlank(roleId)) {
            return Toolkit.outResult(false, "query condition is blank");
        }
        roleId = roleId.trim();
        Server dblog = QueryUtil.getInstance().getFinalHeFuDB(serverId);
        if (dblog == null) {
            return Toolkit.outResult(false, "server not found");
        }

        long playerId;
        try {
            playerId = roleId.matches("[0-9]+") ? Long.parseLong(roleId): Long.parseLong(roleId, 36);
        } catch (Exception e) {
            return Toolkit.outResult(false, "输入查询参数错误");
        }
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String roleSqlStr = "SELECT * FROM $table WHERE roleId = @roleId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "roleId", playerId);
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.outResult(false, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, msg.get("jsp.role.norole"));
        }

        String userRoleSqlStr = "SELECT rechargeGold FROM $table WHERE userId=@userId order by ts desc limit 1";
        List<RoleState> userRoles = queryRoleState(dblog, userRoleSqlStr, "userId", roles.get(0).getUserId());

        for (RoleState roleState : roles) {
            if (roleState.getMoonCardDay() > 0) {
                roleState.setMoonCardDay(roleState.getMoonCardDay() - DateUtil.getCurDay(0));
            }
            roleState.setRechargeGold(userRoles.get(0).getRechargeGold());
        }

        String accountSqlStr = "SELECT userId,userName,platformAccount,platformName,createTime,time,lastLoginIp," +
                "machineCode,imei,mac FROM $table WHERE userid = @userid order by time desc limit 0,1";
        Dao daoLSLog = LoginServerManager.getInstance().getLoginLogDao();
        if (daoLSLog == null) {
            return Toolkit.outResult(true, msg.get("jsp.role.nolslog")).setv("roles", roles);
        }

        List<String> tableList = QueryUtil.getInstance().queryTables(daoLSLog, ServerKeyUtil.getKey("loginLogName"), "loguser");
        for (String table : tableList) {
            List<Account> accounts = queryAccount(table, accountSqlStr, "userid", roles.get(0).getUserId());
            if (!accounts.isEmpty()) {
                return Toolkit.outResult(true).setv("roles", roles).setv("accounts", accounts);
            }
        }
        return Toolkit.outResult(true, msg.get("jsp.role.noaccount")).setv("roles", roles);
    }

    private List<Account> queryAccount(String table, String sqlStr, String paramKey, Object paramValue) {
        Dao loginLogDao = LoginServerManager.getInstance().getLoginLogDao();
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", table);
        sql.params().set(paramKey, paramValue);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(loginLogDao.getEntity(Account.class));
        loginLogDao.execute(sql);
        return sql.getList(Account.class);
    }

    private List<RoleState> queryRoleState(Server dblog, String sqlStr, String paramKey, Object paramValue) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", "rolestate");
        sql.params().set(paramKey, paramValue);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getList(RoleState.class);
    }

    @At
    @POST
    @GET
    public Object getServerState(int serverId) {
        if(serverId<=0){
//            Cnd cnd = Cnd.where("isDeleted", "=", 0).
//                    and("isHeFu","=", 0).
//                    and("serverType", "=", "1");
//            List<Server> servers = dao.query(Server.class, cnd);
            String sqlStr = "SELECT serverId,openState,registerNum FROM t_server WHERE isDeleted=0 AND isHeFu=0 AND serverType=1;";
            List<Map<String, Object>> result= QueryUtil.getInstance().query(dao, sqlStr);
            return Toolkit.outResult(true, result);
        }
        Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", serverId));
        if(server == null){
            logger.error("没有找到服务器信息,serverId="+serverId);
            return Toolkit.outResult(false, "没有找到服务器信息,serverId="+serverId);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("serverId", server.getServerId());
        map.put("openState", server.getOpenState());
        map.put("registerNum", server.getRegisterNum());
        return Toolkit.outResult(true, Arrays.asList(map));
    }
}
