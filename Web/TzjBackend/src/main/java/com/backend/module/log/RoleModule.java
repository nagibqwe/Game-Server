package com.backend.module.log;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.manager.LoginServerManager;
import com.backend.struct.Account;
import com.backend.struct.RoleState;
import com.backend.utils.*;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@IocBean
@Ok("json")
@At("/role")
@Fail("http:500")
public class RoleModule {

    private static final Logger log = Logger.getLogger(RoleModule.class);

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.role.rolesearch")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index() {}

    @At
    public Object query(int serverId, int queryType, String queryString) {
        if (Strings.isBlank(queryString)) {
            return Toolkit.outResult(false, "query condition is blank");
        }
        queryString = queryString.trim();
        Dblog dblog = QueryUtil.getInstance().getFinalHeFuDB(serverId);
        if (dblog == null) {
            return Toolkit.outResult(false, "server not found");
        }

        switch (queryType) {
            case 1://角色名
                return queryRoleByRoleName(dblog, queryString);
            case 2://FunCell帐号
                return queryRoleByUserName(dblog, queryString);
            case 3://角色ID(10进制)
                return queryRoleByRoleId(dblog, queryString, 0);
            case 4://角色ID(36进制)
                return queryRoleByRoleId(dblog, queryString, 1);
            case 5://帐号ID
                return queryRoleByUserId(dblog, queryString);
            case 6://平台帐号
                return queryRoleByPlatFormAccount(dblog, queryString);
            default:
                return null;
        }
    }

    private Object queryRoleByRoleName(Dblog dblog, String roleName) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String roleSqlStr = "SELECT * FROM $table WHERE roleName like @roleName";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "roleName", "%" + roleName + "%");
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.outResult(false, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, msg.get("jsp.role.norole"));
        }
        String accountSqlStr = "SELECT userId,userName,platformAccount,platformName,createTime,time,lastLoginIp," +
                "machineCode,imei,mac FROM $table WHERE userid = @userid order by time desc limit 0,1";
        Dao daoLSLog = LoginServerManager.getInstance().getLoginLogDao();
        if (daoLSLog != null) {
            for (RoleState role : roles) {
                List<String> tableList = QueryUtil.getInstance().queryTables(daoLSLog, ServerKeyUtil.getKey("loginLogName"), "loguser");
                for (String table : tableList) {
                    List<Account> accounts = queryAccount(table, accountSqlStr, "userid", roles.get(0).getUserId());
                    if (!accounts.isEmpty()) {
                        role.setMachineCode(accounts.get(0).getMachineCode());
                        break;
                    }
                }
            }
        }
        return Toolkit.outResult(true).setv("roles", roles);
    }

    private Object queryRoleByUserName(Dblog dblog, String userName) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Dao loginLogDao = LoginServerManager.getInstance().getLoginLogDao();
        if (loginLogDao == null) {
            return Toolkit.outResult(false, msg.get("jsp.role.nolslog"));
        }
        List<String> tableList = QueryUtil.getInstance().queryTables(loginLogDao, ServerKeyUtil.getKey("loginLogName"), "loguser");
        Account account = null;
        String accountSqlStr = "SELECT userid,userName,platformAccount,platformName,createTime,time,lastLoginIp," +
                "machineCode,imei,mac FROM $table WHERE userName = @userName order by time desc limit 0,1";
        for (String table : tableList) {
            List<Account> accounts = queryAccount(table, accountSqlStr, "userName", userName);
            if (!accounts.isEmpty()) {
                account = accounts.get(0);
                break;
            }
        }
        if (account == null) {
            return Toolkit.outResult(false, msg.get("jsp.role.noaccount"));
        }
        String roleSqlStr = "SELECT * FROM $table WHERE userId = @userId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "userId", account.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.outResult(false, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            return Toolkit.outResult(true, msg.get("jsp.role.norole")).setv("accounts", account);
        }
        return Toolkit.outResult(true).setv("roles", roles).setv("accounts", Collections.singletonList(account));
    }

    private Object queryRoleByRoleId(Dblog dblog, String queryString, int type) {
        long roleId;
        try {
            roleId = type == 1 ? toBase10(queryString) : Long.valueOf(queryString);
        } catch (Exception e) {
            return Toolkit.outResult(false, "输入查询参数错误");
        }
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String roleSqlStr = "SELECT * FROM $table WHERE roleId = @roleId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "roleId", roleId);
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

    private Object queryRoleByUserId(Dblog dblog, String queryString) {
        long userId;
        try {
            userId = Long.parseLong(queryString);
        } catch (Exception e) {
            return Toolkit.outResult(false, "输入参数错误");
        }
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String roleSqlStr = "SELECT * FROM $table WHERE userId = @userId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "userId", userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.outResult(false, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, msg.get("jsp.role.nouserid"));
        }
        for (RoleState roleState : roles) {
            if (roleState.getMoonCardDay() > 0) {
                roleState.setMoonCardDay(roleState.getMoonCardDay() - DateUtil.getCurDay(0));
            }
        }

        String accountSqlStr = "SELECT userid,userName,platformAccount,platformName,createTime,time,lastLoginIp," +
                "machineCode,imei,mac FROM $table WHERE userid = @userid order by time desc limit 0,1";
        Dao daoLSLog = LoginServerManager.getInstance().getLoginLogDao();
        if (daoLSLog == null) {
            log.error("没有找到LSLog连接");
            return Toolkit.outResult(true).setv("roles", roles).setv("msg", msg.get("jsp.role.nolslog"));
        }
        List<String> tableList = QueryUtil.getInstance().queryTables(daoLSLog, ServerKeyUtil.getKey("loginLogName"), "loguser");
        for (String table : tableList) {
            List<Account> accounts = queryAccount(table, accountSqlStr, "userid", userId);
            if (!accounts.isEmpty()) {
                return Toolkit.outResult(true).setv("roles", roles).setv("accounts", accounts);
            }
        }
        return Toolkit.outResult(true, msg.get("jsp.role.noaccount")).setv("roles", roles);
    }

    private Object queryRoleByPlatFormAccount(Dblog dblog, String platformAccount) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String accountSqlStr = "SELECT userid,userName,platformAccount,platformName,createTime,time,lastLoginIp," +
                "machineCode,imei,mac FROM $table WHERE platformAccount = @platformAccount order by time desc limit 0,1";
        Dao daoLSLog = LoginServerManager.getInstance().getLoginLogDao();
        if (daoLSLog == null) {
            return Toolkit.outResult(false, msg.get("jsp.role.nolslog"));
        }
        List<String> tableList = QueryUtil.getInstance().queryTables(daoLSLog, ServerKeyUtil.getKey("loginLogName"), "loguser");
        Account account = null;
        for (String table : tableList) {
            List<Account> accounts = queryAccount(table, accountSqlStr, "platformAccount", platformAccount);
            if (!accounts.isEmpty()) {
                account = accounts.get(0);
                break;
            }
        }
        if (account == null) {
            return Toolkit.outResult(false, msg.get("jsp.role.noaccount"));
        }
        String roleSqlStr = "SELECT * FROM $table WHERE userId = @userId";
        List<RoleState> roles;
        try {
            roles = queryRoleState(dblog, roleSqlStr, "userId", account.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.outResult(false, "连接数据库失败");
        }
        if (roles.isEmpty()) {
            return Toolkit.outResult(true, msg.get("jsp.role.norole")).setv("accounts", account);
        }
        return Toolkit.outResult(true).setv("roles", roles).setv("accounts", Collections.singletonList(account));
    }


    private List<RoleState> queryRoleState(Dblog dblog, String sqlStr, String paramKey, Object paramValue) {
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

    /**
     * 从36进制的字符串转换为10进制的long型
     */
    private static long toBase10(String str) {
        return Long.parseLong(str, 36);
    }
}
