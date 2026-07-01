package com.backend.admin;

import com.backend.bean.Server;
import com.backend.manager.DbLogListManager;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.Toolkit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@IocBean
@At("/dblog")
@Ok("json")
@Fail("jsp:500")
public class DblogModule {

    @Inject
    private Dao dao;

    @At
    @GET
    @Filters
    public Object addLog(@Param("..") Server dblog, HttpServletRequest request) {
        if (Toolkit.checkSign(request)) {
            return add(dblog, request);
        }
        return Toolkit.outResult(false, "1");
    }

    @At
    @POST
    public Object add(@Param("..") Server dblog, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (dblog.getServerId() < 1) {
            return Toolkit.outResult(false, msg.get("server.id.error"));
        }

        if (Strings.isBlank(dblog.getServerName())) {
            return Toolkit.outResult(false, msg.get("server.name.null"));
        }

        if (Strings.isBlank(dblog.getGroupName())) {
            return Toolkit.outResult(false, msg.get("server.group.null"));
        }

        if (Strings.isBlank(dblog.getServerIP())) {
            return Toolkit.outResult(false, msg.get("dblog.serveripPort.null"));
        }

//        if (!dblog.getServerIP().contains(":")) {
//            return Toolkit.outResult(false, msg.get("jsp.dblog.dblogurlmore"));
//        }

        if (Strings.isBlank(dblog.getDblogName())) {
            return Toolkit.outResult(false, msg.get("server.dbname.null"));
        }

        if (Strings.isBlank(dblog.getDblogPwd())) {
            return Toolkit.outResult(false, msg.get("server.dbpass.null"));
        }

        if (Strings.isBlank(dblog.getDblogUser())) {
            return Toolkit.outResult(false, msg.get("server.dbuser.null"));
        }

        if (Strings.isBlank(dblog.getServerIdList())) {
            dblog.setServerIdList("," + dblog.getServerId());
        }
        //去掉首尾空格
        dblog.setServerIP(dblog.getServerIP().trim());
        dblog.setServerPort(dblog.getServerPort());
        dblog.setDblogName(dblog.getDblogName().trim());
        dblog.setDblogUser(dblog.getDblogUser().trim());
        dblog.setDblogPwd(dblog.getDblogPwd().trim());
        Server logs = dao.fetch(Server.class, Cnd.where("serverId", "=", dblog.getServerId()));
        if (logs == null) {
            logs = dao.insert(dblog);
            if (logs == null) {
                return Toolkit.outResult(false, msg.get("db.insert.failure"));
            }
            DbLogListManager.getInstance().updateDBlog(logs);
            BackendLogUtil.getInstance().log(request, "增加服务器数据库信息，serverId:" + dblog.getServerId());
            return Toolkit.outResult(true, msg.get("db.insert.success"));
        } else {
            dblog.setId(logs.getId());
            dblog.setDblogPwd(logs.getDblogPwd());
            int no = dao.updateIgnoreNull(dblog);
            if (no < 1) {
                return Toolkit.outResult(false, msg.get("db.update.failure"));
            }
            DbLogListManager.getInstance().updateDBlog(logs);
            BackendLogUtil.getInstance().log(request, "修改服务器数据库信息，serverId:" + dblog.getServerId());
            return Toolkit.outResult(true, msg.get("db.update.success"));
        }
    }

    @At
    @Ok("json:{locked:'dbpassword',ignoreNull:true}")
    public Object query(String groupName, String name, int type, Pager pager) {
        Cnd cnd = Cnd.where("isDeleted", "=", 0).and("type", "=", type);
        cnd = Strings.isBlank(groupName) ? cnd : cnd.and("groupName", "like", "%" + groupName + "%");
        cnd = Strings.isBlank(name) ? cnd : cnd.and("serverName", "like", "%" + name + "%");

        QueryResult qr = new QueryResult();
        List<Server> dbLogList = dao.query(Server.class, cnd, pager);
        Collections.sort(dbLogList);
        for (Server db : dbLogList) {
            db.setDblogPwd(db.getDblogPwd().replaceAll(".", "*"));
        }
        qr.setList(dbLogList);
        pager.setRecordCount(dao.count(Server.class, cnd));
        qr.setPager(pager);
        return qr;
    }

    @At
    public Object modify(@Param("id") int id) {
        return dao.fetch(Server.class, id);
    }

    @At
    @POST
    public Object delete(@Param("id") int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (id < 1) {
            return Toolkit.outResult(false, msg.get("server.id.error"));
        }
        Server dblog = dao.fetch(Server.class, id);
        int no = dao.delete(Server.class, id);
        if (no > 0) {
            BackendLogUtil.getInstance().log(Mvcs.getReq(), "删除服务器数据库信息，serverId:" + id);
            DbLogListManager.getInstance().updateDBlog(dblog);
            return Toolkit.outResult(true);
        }
        return Toolkit.outResult(false, msg.get("dblog.delete.tishi1") + id + msg.get("dblog.delete.tishi2"));
    }

    @At
    public Object group() {
        return DbLogListManager.getInstance().getPlatformDBNames();
    }

    @At
    public Object serverByGroup(String groupName) {
        return DbLogListManager.getInstance().getServerDBs(groupName);
    }

    @At
    public Object getHeFuDBs(String groupName) {
        return DbLogListManager.getInstance().getNoHeFuServerDBs(groupName);
    }

}
