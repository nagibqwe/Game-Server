package com.backend.module.operation;

import com.backend.bean.Dblog;
import com.backend.bean.RoleTransfer;
import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
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
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色转移功能
 */
@IocBean
@Ok("json")
@At("/transfer")
@Fail("http:500")
public class TransferModule {

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.operation.transfer")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index() {}

    @At
    public Object list() {
        List<RoleTransfer> list = dao.query(RoleTransfer.class, Cnd.wrap("order by time desc limit 10"));
        return Toolkit.outResult(true, "", list);
    }

    /**
     * 角色转移
     */
    @At
    public Object transfer(int serverId, String roleId, String userId, String reason, HttpServletRequest request) {
        if (serverId <= 0) {
            return Toolkit.outResult(false, "区服错误");
        }

        if (Strings.isBlank(roleId)) {
            return Toolkit.outResult(false, "角色ID为空");
        }

        if (Strings.isBlank(userId)) {
            return Toolkit.outResult(false, "转移帐号ID为空");
        }

        if (Strings.isBlank(reason)) {
            return Toolkit.outResult(false, "转移原因为空");
        }

        Dblog dbLog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", serverId));
        if (dbLog == null) {
            return Toolkit.outResult(false, "服务器DB连接信息获取失败");
        }

        //检查角色ID
        String roleSqlStr = "SELECT roleId,userId FROM $table WHERE roleId = @roleId";
        List<RoleState> roles = queryRoleState(dbLog, roleSqlStr, "roleId", roleId);
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, "角色ID未找到");
        }

        //发送消息到GameServer
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "服务器连接信息获取失败");
        }

        NutMap result = GameServerRequestUtil.gmTranRole(server, roleId, userId);
        if (result.getBoolean("ok")) {
            RoleTransfer roleTransfer = new RoleTransfer();
            roleTransfer.setRoleId(roleId);
            roleTransfer.setSrcUserId(roles.get(0).getUserId());
            roleTransfer.setTargetUserId(userId);
            roleTransfer.setServerId(serverId);
            roleTransfer.setReason(reason);
            roleTransfer.setIsDeleted(0);
            roleTransfer.setTime((int) (System.currentTimeMillis() / 1000));
            dao.insert(roleTransfer);
            BackendLogUtil.getInstance().log(request, "角色转移成功,serverId:" + serverId + "roleId:" + roleId + ">>>userId:" + userId + ",原因:" + reason);
        }
        return result;
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
}
