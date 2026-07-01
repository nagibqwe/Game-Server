package com.backend.admin;

import com.backend.bean.Server;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.CrossManager;
import com.backend.manager.ServerListManager;
import com.backend.struct.ServerType;
import com.backend.utils.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务器管理接口类， 九零 一起玩 www.90 175.com用于管理服务器的添加， 合服， 修改
 */
@IocBean
@At("/server")
@Ok("json")
public class ServerModule {

    @Inject
    protected Dao dao;

    @At
    public int count() {
        return dao.count(Server.class);
    }

    @At
    @POST
    public Object add(@Param("..") Server server, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (server.getServerId() < 1) {
            return Toolkit.outResult(false, msg.get("server.id.error"));
        }

        if (Strings.isBlank(server.getServerName())) {
            return Toolkit.outResult(false, msg.get("server.name.null"));
        }

        if (Strings.isBlank(server.getServerIP())) {
            return Toolkit.outResult(false, msg.get("server.worldIp.null"));
        }

        if (Strings.isBlank(server.getGroupName())) {
            return Toolkit.outResult(false, msg.get("server.group.null"));
        }

        if (server.getServerPort() < 1) {
            return Toolkit.outResult(false, msg.get("server.worldport.null"));
        }

        if (server.check()) {
            return Toolkit.outResult(false, msg.get("server.other.error"));
        }

        server.setServerIP(server.getServerIP().trim());
        Server updateServer = dao.fetch(Server.class, Cnd.where("serverId", "=", server.getServerId()));
        // 如果没有记录
        if (updateServer == null) {
            Server no = dao.insert(server);
            if (no != null) {
                BackendLogUtil.getInstance().log(request, "增加服务器信息，serverId:" + server.getServerId());
                ServerListManager.getInstance().updateServer(server);
                return Toolkit.outResult(true, msg.get("server.insert.success") + no.getId());
            }
            return Toolkit.outResult(false, msg.get("db.insert.failure"));
        } else {
            updateServer.setServerName(server.getServerName());
            updateServer.setGroupName(server.getGroupName());
            updateServer.setServerIP(server.getServerIP());
            updateServer.setServerPort(server.getServerPort());
            updateServer.setServerType(server.getServerType());
            updateServer.setIsDeleted(server.getIsDeleted());

            int no = dao.update(updateServer);
            if (no > 0) {
                BackendLogUtil.getInstance().log(request, "修改服务器信息，serverId:" + server.getServerId());
                ServerListManager.getInstance().updateServer(updateServer);
                return Toolkit.outResult(true, msg.get("db.update.success"));
            }
            return Toolkit.outResult(false, msg.get("db.update.failure"));
        }
    }

    @At
    public Object query(@Param("groupName") String groupName, @Param("name") String name, @Param("..") Pager pager) {
        Cnd cnd = Cnd.where("isDeleted", "=", 0);
        cnd = Strings.isBlank(groupName) ? cnd : cnd.and("groupName", "like", "%" + groupName + "%");
        cnd = Strings.isBlank(name) ? cnd : cnd.and("serverName", "like", "%" + name + "%");
        cnd.orderBy("serverId", "asc");

        QueryResult qr = new QueryResult();
        qr.setList(dao.query(Server.class, cnd, pager));
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
    public Object combine(@Param("..") Server server) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (server.getServerId() < 1) {
            return Toolkit.outResult(false, msg.get("server.id.error"));
        }

        if (server.getIsDeleted() != 0) {
            return Toolkit.outResult(false, "服务器未启用");
        }

        if (server.getHefuServerID() < 1) {
            return Toolkit.outResult(false, msg.get("server.combine.destIdError"));
        }

        if (server.getHefuTime() == null) {
            server.setHefuTime(new Date());
        }

        Server srcDBlog = dao.fetch(Server.class, Cnd.where("serverId", "=", server.getServerId()));

        Server destDBlog = dao.fetch(Server.class, Cnd.where("serverId", "=", server.getHefuServerID()));
        if (destDBlog == null) {
            return Toolkit.outResult(false, msg.get("server.combine.noDblog"));
        }

        Server destServer = dao.fetch(Server.class, Cnd.where("serverId", "=", server.getHefuServerID()));
        if (destServer == null) {
            return Toolkit.outResult(false, msg.get("server.combine.destIdnoExist"));
        }
        if (destServer.getIsHeFu() > 0) {
            return Toolkit.outResult(false, msg.get("server.combine.destIdIsHefu"));
        }

        Server srcServer = dao.fetch(Server.class, Cnd.where("serverId", "=", server.getServerId()));
        if (srcServer == null) {
            return Toolkit.outResult(false, msg.get("server.combine.noExist"));
        }

        srcServer.setHefuServerID(server.getHefuServerID());
        srcServer.setIsHeFu(1);
        srcServer.setHefuTime(server.getHefuTime());
        int no = dao.update(srcServer);
        if (no > 0) {
            srcDBlog.setIsHeFu(1);
            srcDBlog.setHefuServerID(server.getHefuServerID());
            srcDBlog.setHefuTime(server.getHefuTime());
            // 更新原日志库信息
            dao.update(srcDBlog);
            destDBlog.setServerIdList(destDBlog.getServerIdList() + srcDBlog.getServerIdList());
            // 更新目标日志库信息
            dao.update(destDBlog);
            ServerListManager.getInstance().updateServer(server);
            return Toolkit.outResult(true);
        }
        return Toolkit.outResult(false, msg.get("db.update.failure"));
    }

    @At
    @POST
    public Object cleanCombine(@Param("id") int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (id < 1) {
            return Toolkit.outResult(false, msg.get("server.id.error"));
        }

        Server srcSer = dao.fetch(Server.class, id);
        if (srcSer == null) {
            return Toolkit.outResult(false, msg.get("server.combine.noExist"));
        }
        if (srcSer.getIsHeFu() < 1) {
            return Toolkit.outResult(false, msg.get("server.combine.not"));
        }

        Server srcDBlog = dao.fetch(Server.class, Cnd.where("serverId", "=", srcSer.getServerId()));
        if (srcDBlog == null) {
            return Toolkit.outResult(false, msg.get("server.combine.noDblog"));
        }

        String srcSerStr = srcDBlog.getServerIdList();
        int destSId = srcSer.getHefuServerID();

        // 检查日志库的修改记录， 如果有， 则可以清理， 如果没有是不可以清理的
        Server destDBlog = dao.fetch(Server.class, Cnd.where("serverId", "=", destSId));
        if (destDBlog == null) {
            return Toolkit.outResult(false, msg.get("server.combine.noDblog"));
        }

        String destSerStr = destDBlog.getServerIdList();

        if (destSerStr.contains(srcSerStr)) {
            destDBlog.setServerIdList(destSerStr.replace(srcSerStr, ""));
        }

        int no = dao.update(destDBlog);
        if (no < 1) {
            return Toolkit.outResult(false, msg.get("server.combine.clearUpLogerror"));
        }

        srcDBlog.setIsHeFu(0);
        srcDBlog.setHefuServerID(0);
        srcDBlog.setHefuTime(new Date(0));
        dao.update(srcDBlog);

        srcSer.setHefuServerID(0);
        srcSer.setHefuTime(new Date(0));
        srcSer.setIsHeFu(0);
        no = dao.update(srcSer);
        if (no < 1) {
            return Toolkit.outResult(false, msg.get("db.update.failure"));
        }
        ServerListManager.getInstance().updateServer(srcSer);
        return Toolkit.outResult(true, msg.get("db.update.success"));
    }

    @At
    @GET
    @Filters
    public Object addServer(@Param("..") Server server, HttpServletRequest request) {
        if (Toolkit.checkSign(request)) {
            return add(server, request);
        }
        return Toolkit.outResult(false, "1");
    }

    @At
    @GET
    @Filters
    public Object addCombine(@Param("..") Server server, HttpServletRequest request) {
        if (Toolkit.checkSign(request)) {
            return combine(server);
        }
        return Toolkit.outResult(false, "1");
    }

    @At
    @POST
    public Object delete(@Param("id") int id, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Server server = dao.fetch(Server.class, id);
        int no = dao.delete(Server.class, id);
        if (no > 0) {
            ServerListManager.getInstance().updateServer(server);
            BackendLogUtil.getInstance().log(request, "删除服务器信息，serverId:" + id);
            return new NutMap().setv("ok", true);
        }
        return Toolkit.outResult(false, msg.get("dblog.delete.tishi1") + id + msg.get("dblog.delete.tishi2"));
    }

    @At
    @POST
    public Object test(@Param("id") int id) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Server server = dao.fetch(Server.class, id);
        if (server == null) {
            return Toolkit.outResult(false, msg.get("server.combine.noExist"));
        }
        switch (server.getServerType()) {
            case 0:
            case 1:
            case 4:
                return GameServerRequestUtil.gmOrderSendMess(server,"gmTest","");
            case 2:
            case 3:
                try {
                    String url = "http://" + server.getServerIP() + ":" + server.getServerPort() + "/test?secret_key="
                            + ServerKeyUtil.GetLSRequestKey();
                    Response res = Http.get(url);
                    return Toolkit.outResult(true, res.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                    return Toolkit.outResult(false, "连接失败");
                }
        }
        return Toolkit.outResult(false, "");
    }

    @At
    @Ok("jsp:jsp.server.customSql")
    public void forwardToCustomSqlPage(@Param("serverId") int serverId, HttpServletRequest request) {
        request.setAttribute("serverId", serverId);
    }

    @At
    @POST
    public Object customSql(@Param("sql") String sqlStr, @Param("serverId") int serverId) {

        //首先拿到数据库的连接信息
        Server databaseInfo = QueryUtil.getInstance().getFinalHeFuDB(serverId);
        if (databaseInfo == null) {
            return Toolkit.outResult(false, "server not found");
        }

        //拿到SimpleDataSource
        SimpleDataSource dataSource = DbConfigUtil.getInstance().getSDS(databaseInfo);

        Dao dao = new NutDao(dataSource);

        //获取数据并拼装
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback((Connection con, ResultSet rs, Sql sql1) -> {
            //构建返回的信息
            List<List<String>> list = new ArrayList<>();
            List<String> rowTitle = new ArrayList<>();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                rowTitle.add(rs.getMetaData().getColumnLabel(i + 1));
            }
            list.add(rowTitle);

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i + 1));
                }
                list.add(row);
            }
            return list;
        });

        Object result = dao.execute(sql).getResult();

        dataSource.close();
        return result;
    }

    @At
    public Object group() {
        return ServerListManager.getInstance().getPlatformNames();
    }

    @At
    @GET
    @Filters
    public Object serverByGroup(@Param("groupName") String groupName) {
        String sqlStr = "SELECT id, serverId, serverName, groupName, WorldIP AS serverIP, worldPort AS serverPort, "
                + "isHeFu, hefuServerID, serverType, isDeleted, isShow, serverOpenTime, openState, heartTime, registerNum "
                + "FROM t_server WHERE isDeleted = 0 AND serverType < 2";
        if (!Strings.isBlank(groupName)) {
            sqlStr += " AND groupName = @groupName";
        }
        sqlStr += " ORDER BY serverId ASC";

        Sql sql = Sqls.create(sqlStr);
        if (!Strings.isBlank(groupName)) {
            sql.params().set("groupName", groupName);
        }
        sql.setCallback((Connection con, ResultSet rs, Sql sql1) -> {
            List<NutMap> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new NutMap()
                        .setv("id", rs.getInt("id"))
                        .setv("serverId", rs.getInt("serverId"))
                        .setv("serverName", rs.getString("serverName"))
                        .setv("groupName", rs.getString("groupName"))
                        .setv("serverIP", rs.getString("serverIP"))
                        .setv("serverPort", rs.getInt("serverPort"))
                        .setv("isHeFu", rs.getInt("isHeFu"))
                        .setv("hefuServerID", rs.getInt("hefuServerID"))
                        .setv("serverType", rs.getInt("serverType"))
                        .setv("isDeleted", rs.getInt("isDeleted"))
                        .setv("isShow", rs.getInt("isShow"))
                        .setv("serverOpenTime", rs.getString("serverOpenTime"))
                        .setv("openState", rs.getInt("openState"))
                        .setv("heartTime", rs.getString("heartTime"))
                        .setv("registerNum", rs.getInt("registerNum")));
            }
            return list;
        });
        dao.execute(sql);
        return sql.getResult();
    }

    @At("/serverlist")
@GET
@Ok("json")
@Filters
public Object serverlist(@Param("groupName") String groupName) {
    String sqlStr = "SELECT serverId, serverName, groupName, WorldIP, worldPort, " +
            "serverOpenTime, openState, registerNum " +
            "FROM t_server " +
            "WHERE isDeleted = 0 " +
            "AND isHeFu = 0 " +
            "AND isShow = 0 " +
            "AND serverType = 1 ";

    if (!Strings.isBlank(groupName)) {
        sqlStr += " AND groupName = @groupName ";
    }

    sqlStr += " ORDER BY serverId ASC";

    Sql sql = Sqls.create(sqlStr);

    if (!Strings.isBlank(groupName)) {
        sql.params().set("groupName", groupName);
    }

    sql.setCallback((Connection con, ResultSet rs, Sql sql1) -> {
        List<NutMap> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new NutMap()
                    .setv("serverId", rs.getInt("serverId"))
                    .setv("serverName", rs.getString("serverName"))
                    .setv("groupName", rs.getString("groupName"))
                    .setv("serverIP", rs.getString("serverIP"))
                    .setv("serverPort", rs.getInt("serverPort"))
                    .setv("serverOpenTime", rs.getString("serverOpenTime"))
                    .setv("openState", rs.getInt("openState"))
                    .setv("registerNum", rs.getInt("registerNum")));
        }
        return list;
    });

    dao.execute(sql);
    return sql.getResult();
}

    @At
    public Object getNoHeFuServer(String groupName, boolean hasFight) {
        List<Server> serverList = new ArrayList<>(ServerListManager.getInstance().getNoHeFuServerListByGroup(groupName));
        if (hasFight) {
            List<Server> fightServerList = CrossManager.getInstance().getServers(groupName, ServerType.Fight);
            if (fightServerList != null && !fightServerList.isEmpty()) {
                serverList.addAll(fightServerList);
            }
        }
        return serverList;
    }

    /**
     * 测试服列表
     */
    @At
    public Object getTestServerMap() {
        return ServerListManager.getInstance().getTestServerListMap();
    }

    /**
     * 正式服列表
     */
    @At
    public Object getOfficalServerMap() {
        return ServerListManager.getInstance().getOfficialServerMap();
    }

}
