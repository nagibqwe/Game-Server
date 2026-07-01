package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.struct.RoleState;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.DbConfigUtil;
import com.backend.utils.QueryUtil;
import com.backend.utils.Toolkit;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@IocBean
@Ok("json")
@At("/roleattr")
@Fail("http:500")
public class RoleAttrModule {

    private static final Logger log = Logger.getLogger(RoleAttrModule.class);

    //等级
    private static final int attrType_0 = 0;

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.operation.setattr")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void setAttr() {}

    @At
    public Object setRoleAttrByRoleId(RoleAttr roleAttr, String serverId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        int sid = QueryUtil.getInstance().getHeFuId(roleAttr.getServerId());

        Dblog dbLog = dao.fetch(Dblog.class, Cnd.where("serverId", "=", sid));
        if (dbLog == null) {
            return Toolkit.outResult(false, "服务器DB连接信息获取失败");
        }

        //检查角色ID
        String sqlStr = "SELECT roleId,userId FROM $table WHERE roleId = @roleId";
        List<RoleState> roles = queryRoleState(dbLog, sqlStr, roleAttr.getRoleId());
        if (roles.isEmpty()) {
            return Toolkit.outResult(false, "没有找到此角色ID");
        }
        if (roles.get(0).getIsDelete() != 0) {
            return Toolkit.outResult(false, "此角色已删除");
        }
        try {
            roleAttr.setActionTime(new Date());
            roleAttr.setActionUser(user.getName());
            roleAttr.setIsDelete(0);
            //发送消息到GameServer
            Server server = ServerListManager.getInstance().getServer(serverId);
            if (server == null) {
                return Toolkit.outResult(false, "服务器连接信息获取失败");
            }
            NutMap resultMap = GameServerRequestUtil.gmSetRoleAttrOpt(server, roleAttr);
            //得到实际数量
            int realValue = Integer.parseInt(resultMap.get("data").toString());
            roleAttr.setRealValue(realValue);
            String prompt;
            if (resultMap.getBoolean("ok")) {
                prompt = "操作成功！";
                dao.insert(roleAttr);
            } else {
                prompt = "操作失败！";
            }
            log.error("属性设置：sid=" + serverId + ",roleId=" + roleAttr.getRoleId() + ",操作结果:" + resultMap.getString("msg"));
            BackendLogUtil.getInstance().log(request, "设置角色(roleId=" + roleAttr.getRoleId() + ")的类型"+roleAttr.getAttrType()+"属性为："+ roleAttr.getRealValue());
            return Toolkit.outResult(resultMap.getBoolean("ok"), prompt);
        } catch (Exception e) {
            log.error(e);
            return Toolkit.outResult(false, "操作失败");
        }
    }

    @At
    public Object getRoleAttrList(int serverId) {
        Condition cnd = Cnd.where("serverId", "=", serverId).and("isDelete", "=", 0).desc("id");
        List<Evaluate> roleAttrList = dao.query(Evaluate.class, cnd);
        return Toolkit.outResult(true, roleAttrList);
    }

    @At
    public Object delRoleAttr(int id) {
        Evaluate roleAttr = dao.fetch(Evaluate.class, id);
        if (roleAttr != null) {
            roleAttr.setIsDelete(1);
            int num = dao.update(roleAttr);
            return Toolkit.outResult(num == 1);
        }
        return Toolkit.outResult(false);
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




