package com.backend.module.operation;

import com.backend.bean.Dblog;
import com.backend.bean.Recharge;
import com.backend.bean.Server;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.manager.ServerListManager;
import com.backend.struct.RoleState;
import com.backend.utils.*;
import com.mysql.jdbc.TimeUtil;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.dao.util.cri.Static;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 充值功能
 */
@IocBean
@Ok("json")
@At("/recharge")
@Fail("http:500")
public class RechargeModule {
    public static DateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger log = Logger.getLogger(RechargeModule.class);

    @Inject
    protected Dao dao;

    @At
    @Ok("forward:${obj}")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public String getPage(int type, HttpServletRequest request) {
        switch (type) {
            case 1:
                Map<String, List<Server>> serverMap = ServerListManager.getInstance().getNoHefuServerListMap();
                request.setAttribute("serverMap", Json.toJson(serverMap));
                return "/WEB-INF/jsp/recharge/recharge.jsp";
            case 3:
                return "/WEB-INF/jsp/recharge/rechargeShenHe.jsp";
            case 4:
                request.setAttribute("nowDate", sdfhms.format(new Date()));
                return "/WEB-INF/jsp/recharge/rechargeShow.jsp";
            default:
                return "/404.jsp";
        }
    }

    @At
    public Object rechargeOrder(int serverId, String roleId, int rechargeGold, int rechargeTotalGold, int rechargeVipExp, String reason, HttpServletRequest request) {

        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String roleSqlStr = "SELECT roleId FROM $table WHERE roleId = @roleId";
        Dblog dblog = DbLogListManager.getInstance().getDblog(serverId);
        if (dblog == null) {
            return Toolkit.outResult(false, "未找到服务器");
        }
        List<RoleState> roles = queryRoleState(dblog, roleSqlStr, roleId);
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, msg.get("recharge.roleiderror"));
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = (User) request.getSession().getAttribute("USER");
        String userName = user.getName();
        Recharge recharge = new Recharge();
        recharge.setServerId(serverId);
        recharge.setRoleId(Long.valueOf(roleId));
        recharge.setRechargeNumber(rechargeGold);
        recharge.setRechargeTotalGold(rechargeTotalGold);
        recharge.setRechargeVipExp(rechargeVipExp);
        recharge.setCreateUser(userName);
        recharge.setCreateTime(df.format(new Date()));
        recharge.setRechargeState(0);
        recharge.setReason(reason);
        dao.insert(recharge);
        return Toolkit.outResult(true, "保存成功");
    }

    @At
    public Object list(int pageSize, int pageIndex) {
        Pager pager = dao.createPager(pageIndex, pageSize);
        List<Recharge> list = dao.query(Recharge.class, Cnd.where("rechargeState", "=", "0"), pager);
        int count = dao.count(Recharge.class, Cnd.where("rechargeState", "=", "0"));
        return Toolkit.outResult(true).setv("list", list).setv("count", count);
    }

    @At
    public Object rechargeShenHeResult(HttpServletRequest request, int id, boolean send) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = (User) request.getSession().getAttribute("USER");
        String userName = user.getName();
        Recharge recharge = dao.fetch(Recharge.class, id);
        recharge.setApprovalUser(userName);
        recharge.setApprovalTime(df.format(new Date()));
        if (send) {
            NutMap result = (NutMap) recharge(recharge.getServerId(), recharge.getRoleId(), recharge.getRechargeNumber(), recharge.getRechargeTotalGold(), recharge.getRechargeVipExp(), request);
            if (result.getBoolean("ok")) {
                recharge.setRechargeState(1);
                dao.update(recharge);
            }
            return result;
        } else {
            recharge.setRechargeState(2);
            dao.update(recharge);
            return Toolkit.outResult(true, "处理完成");
        }
    }

    @At
    public Object rechargeShow(String serverIds, String startDate, String endDate) {
        String startTime = startDate + " 00:00:00";
        String endTime = endDate + " 23:59:59";
        List<Recharge> list = new ArrayList<>();
        String[] serverList = serverIds.split(",");
        for (String serverId : serverList) {
            SqlExpressionGroup e1 = Cnd.exps("serverId", "=", serverId)
                    .and(new Static("createTime >= \"" + startTime + "\""))
                    .and(new Static("createTime <= \"" + endTime + "\""));
            List<Recharge> tempList = dao.query(Recharge.class, Cnd.where(e1));
            list.addAll(tempList);
        }
        return Toolkit.outResult(true, list);
    }

    @At
    public Object recharge(int serverId, long roleId, int rechargeGold, int rechargeTotalGold, int rechargeVipExp, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (rechargeGold <= 0) {
            return Toolkit.outResult(false, msg.get("recharge.rechargegolderror"));
        }

        Dblog dbLog = DbLogListManager.getInstance().getDblog(serverId);
        if (dbLog == null) {
            return Toolkit.outResult(false, msg.get("recharge.serverdbnotfound"));
        }
        String roleSqlStr = "SELECT roleId FROM $table WHERE roleId = @roleId";
        List<RoleState> roles = queryRoleState(dbLog, roleSqlStr, roleId);
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, msg.get("recharge.roleiderror"));
        }

        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, msg.get("recharge.servernotfound"));
        }

        NutMap backMess = GameServerRequestUtil.gmRecharge(server, Long.valueOf(roleId), rechargeGold, rechargeTotalGold, rechargeVipExp);
        if (backMess.getBoolean("ok")) {
            User user = (User) request.getSession().getAttribute("USER");
            BackendLogUtil.getInstance().log(request, roleId + "充值完成，数量：" + rechargeGold + "，操作者：" + user.getName());
        }
        return backMess;
    }

    private List<RoleState> queryRoleState(Dblog dblog, String sqlStr, Object paramValue) {
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create(sqlStr);
        sql.vars().set("table", "rolestate");
        sql.params().set("roleId", paramValue);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getList(RoleState.class);
    }

}
