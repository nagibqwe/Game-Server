package com.backend.module.operation;

import com.backend.bean.*;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.DbLogListManager;
import com.backend.manager.LoginServerManager;
import com.backend.manager.ServerListManager;
import com.backend.struct.RoleState;
import com.backend.struct.StaticField;
import com.backend.utils.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/forbidden")
@Fail("http:500")
public class ForbiddenModule {

    private static final Log log = Logs.get();

    private final static String ACCESS_KEY = "4399AccessKey";
    private final static String PARAM_ERROR = "parameters error";
    private final static String SIGN_ERROR = "sign error";
    private final static String TIME_ERROR = "time error";
    private final static String USER_ERROR = "user error";
    private final static String SUCCESS = "";

//    //申明一个长用线程池
//	private static final ExecutorService excutor = Executors.newCachedThreadPool();

    // 时间格式数据处理
    private SimpleDateFormat ymdhmssdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    private Dao dao;

    @Inject
    private Dao loginDao;

    @Inject
    private Dao loginLogDao;

    @At
    @Ok("jsp:jsp.forbidden.kickplayer")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void kickPlayerPage() {
    }

    @At
    @Ok("jsp:jsp.forbidden.forbidaccount")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void forbidAccount() {
    }

    @At
    @Ok("jsp:jsp.forbidden.releaseforbidden")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void releaseForbidden() {
    }

    @At
    @Ok("jsp:jsp.forbidden.silence")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void silence() {
    }

    @At
    @Ok("jsp:jsp.forbidden.releasesilence")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void releaseSilence() {
    }

    @At
    @Ok("jsp:jsp.forbidden.whitelist")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void whiteList() {
    }

    @At
    @Ok("jsp:jsp.forbidden.shieldkeyword")
    public void shieldKeyword() {
    }

    @At
    @Ok("jsp:jsp.forbidden.chatreplaceword")
    public void chatReplaceWord() {
    }

    @At
    @Ok("jsp:jsp.forbidden.chatblacklist")
    public void chatBlackList() {
    }

    private RoleState queryRole(Server server, String name) {
        if (server == null) {
            return null;
        }
        Dblog dblog = QueryUtil.getInstance().getFinalHeFuDB(server.getServerId());
        if (dblog == null) {
            return null;
        }
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create("SELECT * FROM $table WHERE roleName = @roleName");
        sql.vars().set("table", "rolestate");
        sql.params().set("roleName", name);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getObject(RoleState.class);
    }

    private RoleState queryRoleById(Server server, String roleId) {
        if (server == null) {
            return null;
        }
        Dblog dblog = QueryUtil.getInstance().getFinalHeFuDB(server.getServerId());
        if (dblog == null) {
            return null;
        }
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create("SELECT * FROM $table WHERE roleId = @roleId");
        sql.vars().set("table", "rolestate");
        sql.params().set("roleId", roleId);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getObject(RoleState.class);
    }

    private List<RoleState> queryRoleByUserId(Server server, String fieldName, String userId) {
        if (server == null) {
            return null;
        }
        Dblog dblog = QueryUtil.getInstance().getFinalHeFuDB(server.getServerId());
        if (dblog == null) {
            return null;
        }
        SimpleDataSource dsLog = DbConfigUtil.getInstance().getSDS(dblog);
        Dao daoLog = new NutDao(dsLog);
        Sql sql = Sqls.create("SELECT * FROM $table WHERE " + fieldName + " = @userId");
        sql.vars().set("table", "rolestate");
        sql.params().set("userId", userId);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(daoLog.getEntity(RoleState.class));
        daoLog.execute(sql);
        dsLog.close();
        return sql.getList(RoleState.class);
    }

    @At
    @GET
    @Filters
    public Object kickPlayer(HttpServletRequest request, String server, String nickname, String time, String ticket, String reason) {
        try {
            log.info(request.getRequestURL() + " | " + request.getQueryString() + " | " + Toolkit.getIp(request));
            if (!Strings.isNumber(server) || Strings.isBlank(nickname) || !Strings.isNumber(time) || Strings.isBlank(ticket)) {
                return Toolkit.result(request, 0, PARAM_ERROR);
            }
            Integer accessTime = Integer.valueOf(time);
            int now = (int) (System.currentTimeMillis() / 1000);
            if (now - accessTime > 120) {
                return Toolkit.result(request, 0, TIME_ERROR);
            }
            String md5Sign = Lang.md5(nickname + time + ServerKeyUtil.getKey(ACCESS_KEY));
            if (!md5Sign.equalsIgnoreCase(ticket)) {
                return Toolkit.result(request, 0, SIGN_ERROR);
            }
            Server serverObj = ServerListManager.getInstance().getServer(server);
            RoleState roleState = queryRole(serverObj, nickname);
            if (roleState == null) {
                return Toolkit.result(request, 0, USER_ERROR);
            }
            NutMap ret = GameServerRequestUtil.gmKickPlayer(serverObj, roleState.getRoleId());
            if (!ret.getBoolean("ok")) {
                return Toolkit.result(request, 0, USER_ERROR);
            }
            return Toolkit.result(request, 1, SUCCESS);
        } catch (Exception e) {
            log.error(e, e);
            return "";
        }
    }

    @At
    @GET
    @POST
    @Filters
    public Object kickUser(HttpServletRequest request, String server, String sendType, String data, int kickAll, String reason, String time, String flag) {
        log.info(request.getRequestURL() + " | " + request.getQueryString() + " | " + Toolkit.getIp(request));

        try {
            if (!Strings.isNumber(server) || Strings.isBlank(sendType) || Strings.isBlank(data)
                    || Strings.isBlank(reason) || Strings.isBlank(flag) || Strings.isBlank(time)) {
                return Toolkit.result(request, 200, "参数错误", "", "");
            }
            StringBuilder paramStr = new StringBuilder();
            TreeMap<String, String> paramMap = new TreeMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                paramMap.put(entry.getKey(), URLEncoder.encode(entry.getValue()[0], "UTF8"));
            }

            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("flag")) {
                    continue;
                }
                paramStr.append(entry.getKey()).append(entry.getValue());
            }
            paramStr.append(ServerKeyUtil.getKey(ACCESS_KEY));
            String md5Sign = Lang.md5(paramStr);
            if (!flag.equalsIgnoreCase(md5Sign)) {
                return Toolkit.result(request, 201, "签名验证失败", "", "");
            }

            Server serverObj = ServerListManager.getInstance().getServer(server);
            if (serverObj == null) {
                return Toolkit.result(request, 202, "服务器Id错误", "", "");
            }
            if (kickAll == 1) {
                NutMap ret = GameServerRequestUtil.gmKickPlayer(serverObj, "0");
                log.info("全服踢人下线,结果" + ret);
                if (ret.getBoolean("ok")) {
                    return Toolkit.result(request, 0, "全服踢人成功", "", "");
                }
                return Toolkit.result(request, 300, "全服踢人失败", "", "");
            }
            int type = Integer.valueOf(sendType);
            RoleState roleState = null;
            switch (type) {
                case 1:
                    roleState = queryRoleById(serverObj, data);
                    break;
                case 2:
                    roleState = queryRole(serverObj, data);
                    break;
                case 3:
                    List<RoleState> roleStates = queryRoleByUserId(serverObj, "cpUserId", data);
                    if (roleStates != null && roleStates.size() > 0) {
                        roleState = roleStates.get(0);
                    }
                    break;
            }
            if (roleState == null) {
                return Toolkit.result(request, 301, "不存在指定玩家", "", "");
            }
            NutMap ret = GameServerRequestUtil.gmKickPlayer(serverObj, roleState.getRoleId());
            log.info("踢人下线,结果" + ret);
            if (ret.getBoolean("ok")) {
                return Toolkit.result(request, 0, "踢人成功:" + roleState.getRoleName(), "", "");
            }
            return Toolkit.result(request, 300, "踢人失败:" + roleState.getRoleName(), "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.result(request, 303, "操作失败", "", "");
        }
    }

    @At
    @GET
    @POST
    @Filters
    public Object ban(HttpServletRequest request, String server, String banType, String data, int status, int banDate, String reason, String flag, String time) {
        log.info(request.getRequestURL() + " | " + request.getQueryString() + " | " + Toolkit.getIp(request));

        try {
            if (!Strings.isNumber(server) || Strings.isBlank(banType) || Strings.isBlank(data)
                    || Strings.isBlank(reason) || Strings.isBlank(flag) || Strings.isBlank(time)) {
                return Toolkit.result(request, 200, "参数错误", "", "");
            }
            StringBuilder paramStr = new StringBuilder();
            TreeMap<String, String> paramMap = new TreeMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                paramMap.put(entry.getKey(), URLEncoder.encode(entry.getValue()[0], "UTF8"));
            }

            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("flag")) {
                    continue;
                }
                paramStr.append(entry.getKey()).append(entry.getValue());
            }
            paramStr.append(ServerKeyUtil.getKey(ACCESS_KEY));
            String md5Sign = Lang.md5(paramStr);
            if (!flag.equalsIgnoreCase(md5Sign)) {
                return Toolkit.result(request, 201, "签名验证失败", "", "");
            }

            Server serverObj = ServerListManager.getInstance().getServer(server);
            if (serverObj == null) {
                return Toolkit.result(request, 202, "服务器Id错误", "", "");
            }
            if (banDate < System.currentTimeMillis() / 1000) {
                return Toolkit.result(request, 203, "封禁时间错误", "", "");
            }

            Server loginServer = LoginServerManager.getInstance().getLoginServer();
            if (loginServer == null) {
                return Toolkit.result(request, 300, "服务器配置错误", "", "");
            }
            String[] datalist = data.split(",");

            String response;
            RoleState roleState = null;
            List<String> successList = new ArrayList<>();
            List<String> failList = new ArrayList<>();
            String operateType = status == 1 ? "增加" : "解除";
            switch (banType.toLowerCase()) {
                case "banrole":
                    for (String tempData : datalist) {
                        roleState = queryRole(serverObj, tempData);
                        if (roleState == null) {
                            failList.add(tempData);
                            log.error("角色不存在：" + tempData);
                            continue;
                        }
                        if (status == 1) {
                            response = forbidUser(loginServer, roleState.getFuncellUUid(), banDate);
                        } else {
                            response = unForbidUser(loginServer, roleState.getFuncellUUid());
                        }
                        if (!"ok".equalsIgnoreCase(response)) {
                            failList.add(tempData);
                            log.info(operateType + "封号失败：" + roleState.getFuncellUUid() + "|" + banDate + "|" + response);
                        } else {
                            successList.add(tempData);
                            log.info(operateType + "封号成功：" + roleState.getFuncellUUid() + "|" + banDate);
                            NutMap ret = GameServerRequestUtil.gmKickPlayer(serverObj, roleState.getRoleId());
                            log.info("踢玩家下线：" + roleState.getRoleName()+ "_" + ret.getBoolean("ok"));
                        }
                    }
                    break;
                case "banchat":
                    for (String tempData : datalist) {
                        roleState = queryRole(serverObj, tempData);
                        if (roleState == null) {
                            failList.add(tempData);
                            log.error("角色不存在：" + tempData);
                            continue;
                        }
                        int banTime = (int) Math.max(banDate - (System.currentTimeMillis() / 1000), 1);
                        NutMap ret = (NutMap) playerCloseChat(request, new String[] {serverObj.getServerId() + ""}, roleState.getUserId(), reason, banTime, 2, 4);
                        if (!ret.getBoolean("ok")) {
                            failList.add(tempData);
                            log.info("禁言失败：" + roleState.getUserId() + "|" + banDate + "|" + banTime + "|" + ret.toString());
                        } else {
                            successList.add(tempData);
                            log.info("禁言成功：" + roleState.getUserId() + "|" + banDate + "|" + banTime + "|" + ret.toString());
                        }
                    }
                    break;
                case "banroleid":
                    for (String tempData : datalist) {
                        roleState = queryRoleById(serverObj, tempData);
                        if (roleState == null) {
                            failList.add(tempData);
                            log.error("角色不存在：" + tempData);
                            continue;
                        }
                        if (status == 1) {
                            response = forbidUser(loginServer, roleState.getFuncellUUid(), banDate);
                        } else {
                            response = unForbidUser(loginServer, roleState.getFuncellUUid());
                        }
                        if (!"ok".equalsIgnoreCase(response)) {
                            failList.add(tempData);
                            log.info(operateType + "封号失败：" + roleState.getFuncellUUid() + "|" + banDate);
                        } else {
                            successList.add(tempData);
                            log.info(operateType + "封号成功：" + roleState.getFuncellUUid() + "|" + banDate);
                            NutMap ret = GameServerRequestUtil.gmKickPlayer(serverObj, roleState.getRoleId());
                            log.info("踢玩家下线：" + roleState.getRoleName()+ "_" + ret.getBoolean("ok"));
                        }
                    }
                    break;
                case "banroleidchat":
                    for (String tempData : datalist) {
                        roleState = queryRoleById(serverObj, tempData);
                        if (roleState == null) {
                            failList.add(tempData);
                            log.error("角色不存在：" + tempData);
                            continue;
                        }
                        int banTime = (int) Math.max(banDate - (System.currentTimeMillis() / 1000), 1);
                        NutMap ret = (NutMap) playerCloseChat(request, new String[] {serverObj.getServerId() + ""}, roleState.getUserId(), reason, banTime, 2, 4);
                        if (!ret.getBoolean("ok")) {
                            failList.add(tempData);
                            log.info(operateType + "禁言失败：" + roleState.getUserId() + "|" + banDate + "|" + banTime + "|" + ret.toString());
                        } else {
                            successList.add(tempData);
                            log.info(operateType + "禁言成功：" + roleState.getUserId() + "|" + banDate + "|" + banTime + "|" + ret.toString());
                        }
                    }
                    break;
                case "banact":
                    for (String tempData : datalist) {
                        List<RoleState> roleStates = queryRoleByUserId(serverObj, "cpUserId", tempData);
                        if (roleStates != null && roleStates.size() > 0) {
                            roleState = roleStates.get(0);
                        }
                        if (roleState == null) {
                            failList.add(tempData);
                            log.error("角色不存在：" + tempData);
                            continue;
                        }
                        if (status == 1) {
                            response = forbidUser(loginServer, roleState.getFuncellUUid(), banDate);
                        } else {
                            response = unForbidUser(loginServer, roleState.getFuncellUUid());
                        }
                        if (!"ok".equalsIgnoreCase(response)) {
                            failList.add(tempData);
                            log.info(operateType + "封号失败：" + tempData + "|" + banDate);
                        } else {
                            successList.add(tempData);
                            for (RoleState role : roleStates) {
                                NutMap ret = GameServerRequestUtil.gmKickPlayer(serverObj, role.getRoleId());
                                log.info("踢玩家下线：" + role.getRoleName()+ "_" + ret.getBoolean("ok"));
                            }
                            log.info(operateType + "封号成功：" + tempData + "|" + banDate);
                        }
                    }
                    break;
                case "banip":
                case "banimei":
                    for (String tempData : datalist) {
                        if (status == 1) {
                            response = forbidUser(loginServer, tempData, banDate);
                        } else {
                            response = unForbidUser(loginServer, tempData);
                        }
                        if (!"ok".equalsIgnoreCase(response)) {
                            failList.add(tempData);
                            log.info(operateType + "封号失败：" + tempData + "|" + banDate);
                        } else {
                            successList.add(tempData);
                            log.info(operateType + "封号成功：" + tempData + "|" + banDate);
                        }
                    }
                    break;
                default:
                    return Toolkit.result(request, 204, "不存在的封禁类型", "", "");
            }
            return Toolkit.result(request, 0, "操作完成，成功列表：" + Json.toJson(successList) + "失败列表：" + Json.toJson(failList), "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return Toolkit.result(request, 300, "操作失败", "", "");
        }
    }

    @At
    @POST
    public Object kickPlayer(String serverId, String roleId, String reason, HttpServletRequest request) {
        Server ser = ServerListManager.getInstance().getServer(serverId);
        if (ser == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        NutMap ret = GameServerRequestUtil.gmKickPlayer(ser, roleId);

        String result;
        if (ret.getBoolean("ok")) {
            result = String.format("【%s】 %s", ser.getServerName(), ret.getString("msg"));
        } else {
            result = String.format("【%s】" + "踢人下线失败，原因：%s", ser.getServerName(), ret.getString("msg"));
        }
        BackendLogUtil.getInstance().log(request, "踢人下线\t理由：" + reason + "\t区服ID:" + ser.getServerId() + "\t结果：" + result);
        return Toolkit.outResult(true, result);
    }

    @At
    @GET
    @Filters
    public Object playerCloseChat(HttpServletRequest request, String server, String minutes, String nickname, String time, String ticket, String reason) {

        try {
            log.info(request.getRequestURL() + " | " + request.getQueryString() + " | " + Toolkit.getIp(request));
            if (!Strings.isNumber(server) || Strings.isBlank(nickname) || !Strings.isNumber(minutes)
                    || !Strings.isNumber(time) || Strings.isBlank(ticket)) {
                return Toolkit.result(request, 0, PARAM_ERROR);
            }
            int closeChatTime = Integer.parseInt(minutes);
            Integer accessTime = Integer.valueOf(time);
            int now = (int) (System.currentTimeMillis() / 1000);
            if (now - accessTime > 120) {
                return Toolkit.result(request, 0, TIME_ERROR);
            }
            String md5Sign = Lang.md5(nickname + minutes + time + ServerKeyUtil.getKey(ACCESS_KEY));
            if (!md5Sign.equalsIgnoreCase(ticket)) {
                return Toolkit.result(request, 0, SIGN_ERROR);
            }
            Server serverObj = ServerListManager.getInstance().getServer(server);
            RoleState roleState = queryRole(serverObj, nickname);
            if (roleState == null) {
                return Toolkit.result(request, 0, USER_ERROR);
            }
            String[] serverId = new String[]{serverObj.getServerId() + ""};
            User user = new User();
            user.setName("4399");
            request.getSession().setAttribute("USER", user);
            NutMap ret = (NutMap) playerCloseChat(request, serverId, roleState.getRoleId(), reason, closeChatTime * 60, 2, 4);
            if (!ret.getBoolean("ok")) {
                return Toolkit.result(request, 0, USER_ERROR);
            }
            return Toolkit.result(request, 1, SUCCESS);
        } catch (Exception e) {
            log.error(e, e);
            return "";
        }
    }

    /**
     * 禁言
     */
    @At
    public Object playerCloseChat(HttpServletRequest request, @Param("serverId") String[] serverId, @Param("playerId") String playerId,
                                  @Param("reason") String reason, @Param("times") int times, @Param("crimeType") int crimeType, @Param("forbidType") int forbidType) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (serverId == null || serverId.length == 0) {
            return Toolkit.outResult(false, "未选择服务器");
        }

        ForbidChatPlayer fcp = dao.fetch(ForbidChatPlayer.class, Cnd.where("userId", "=", playerId));
        boolean isUpdate = fcp != null;
        if (fcp == null) {
            fcp = new ForbidChatPlayer();
            fcp.setBackUserName(user == null ? "other" : user.getName());
        }
        fcp.setBackMUserName(user == null ? "other" : user.getName());
        fcp.setUserId(playerId);
        fcp.setReason(reason);
        fcp.setCrimeType(crimeType);
        fcp.setForbidType(forbidType);
        fcp.setCreateTime(ymdhmssdf.format(new Date()));
        long createTime = System.currentTimeMillis();
        long endTime = createTime + times * 1000L;
        fcp.setEndTime(times > 0 ? ymdhmssdf.format(new Date(endTime)) : "-1");
        if (times <= 0) {
            endTime = -1;
        }
        log.info("userId:" + playerId + ",禁言结束时间：" + endTime);

        Sql sql = Sqls.create("select count(*) from forbidspeeking where `userId`=" + playerId + ";");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = (int) sql.getResult();
        if (num > 0) {
            sql = Sqls.create("update forbidspeeking set `endTime`= " + endTime + ", `forbidType`=" + forbidType + " where userId = " + playerId + ";");
        } else {
            sql = Sqls.create("insert into forbidspeeking(userId, forbidType, endTime, createTime) values(" + playerId + ","+ forbidType +"," + endTime + ",'" + fcp.getCreateTime() + "')");
        }
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        log.info("刷新forbidspeeking禁言表的结果是：" + (exeNum > 0));

        StringBuilder serverIdStr = new StringBuilder();
        StringBuilder BSSB = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        for (String sid : serverId) {
            Cnd gp = Cnd.where("serverId", "=", sid);
            Server server = dao.fetch(Server.class, gp);
            if (server == null) {
                log.error("未找到id对应的服务器:" + sid);
                continue;
            }
            NutMap result = GameServerRequestUtil.gmForbidChat(server);
            BSSB.append(server.getServerName()).append(":");
            if (result.getBoolean("ok")) {
                serverIdStr.append(sid).append(",");
                sb1.append(server.getServerName()).append("禁言刷新成功 ！");
                BSSB.append("ok;\n");
            } else {
                sb1.append(server.getServerName()).append("禁言失败，原因：").append(result.get("msg"));
                BSSB.append("failed,").append(language.get("forbid.deal.reason")).append(result.get("msg")).append(";\n");
            }
            log.info("禁言处理结果：" + sb1.toString());
        }
        fcp.setServerIds(serverIdStr.toString());
        fcp.setState(0);
        // 更新记录
        if (isUpdate) {
            dao.update(fcp);
        } else {
            dao.insert(fcp);
        }

        BackendLogUtil.getInstance().log(request, "禁言操作\t理由：" + reason + "\t,区服ID:" + Arrays.toString(serverId) + "\t,刷表结果：" + BSSB.toString() + "\t,禁言结果：" + BSSB.toString());
        return Toolkit.outResult(true, BSSB.toString());
    }

    /**
     * 批量禁言
     */
    @At
    public Object batchForbidChat(HttpServletRequest request, String serverId, String roleIds, String reason, int times, int crimeType, int forbidType) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        if (Strings.isBlank(serverId) || Strings.isBlank(roleIds) || Strings.isBlank(reason)) {
            return Toolkit.outResult(false, "param error");
        }

        Cnd gp = Cnd.where("serverId", "=", serverId);
        Server server = dao.fetch(Server.class, gp);
        if (server == null) {
            log.error("未找到id对应的服务器:" + serverId);
            return null;
        }

        Dblog dblog = DbLogListManager.getInstance().getDblog(serverId);
        String roleStateSqlStr = "SELECT rs.userId FROM rolestate rs WHERE rs.roleId in (" + roleIds+ ") AND rs.createsid = " + serverId;
        List<Map<String, Object>> userMap = QueryUtil.getInstance().query(dblog, roleStateSqlStr);
        if (userMap.isEmpty()) {
            return null;
        }

        StringBuilder BSSB = new StringBuilder();
        for(Map<String, Object> map:userMap) {
            String userId = map.get("userId").toString();

            ForbidChatPlayer fcp = dao.fetch(ForbidChatPlayer.class, Cnd.where("userId", "=", userId));
            boolean isUpdate = fcp != null;
            if (fcp == null) {
                fcp = new ForbidChatPlayer();
                fcp.setBackUserName(user == null ? "other" : user.getName());
            }
            fcp.setBackMUserName(user == null ? "other" : user.getName());
            fcp.setUserId(userId);
            fcp.setReason(reason);
            fcp.setCrimeType(crimeType);
            fcp.setForbidType(forbidType);
            fcp.setCreateTime(ymdhmssdf.format(new Date()));
            long createTime = System.currentTimeMillis();
            long endTime = createTime + times * 1000L;
            fcp.setEndTime(times > 0 ? ymdhmssdf.format(new Date(endTime)) : "-1");
            if (times <= 0) {
                endTime = -1;
            }
            log.info("userId:" + userId + ",禁言结束时间：" + endTime);

            Sql sql = Sqls.create("select count(*) from forbidspeeking where `userId`=" + userId + ";");
            sql.setCallback(Sqls.callback.integer());
            loginDao.execute(sql);
            int num = (int) sql.getResult();
            if (num > 0) {
                sql = Sqls.create("update forbidspeeking set `endTime`= " + endTime + ", `forbidType`=" + forbidType + " where userId = " + userId + ";");
            } else {
                sql = Sqls.create("insert into forbidspeeking(userId, forbidType, endTime, createTime) values(" + userId +"," + forbidType + "," + endTime + ",\"" + fcp.getCreateTime() + "\")");
            }
            loginDao.execute(sql);
            int exeNum = sql.getUpdateCount();
            log.info("刷新forbidspeeking禁言表的结果是：" + (exeNum > 0));


            StringBuilder serverIdStr = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();

            NutMap result = GameServerRequestUtil.gmForbidChat(server);
            BSSB.append(server.getServerName()).append(":");
            if (result.getBoolean("ok")) {
                serverIdStr.append(serverId).append(",");
                sb1.append(server.getServerName()).append("禁言刷新成功 ！");
                BSSB.append("ok;\n");
            } else {
                sb1.append(server.getServerName()).append("禁言失败，原因：").append(result.get("msg"));
                BSSB.append("failed,").append(language.get("forbid.deal.reason")).append(result.get("msg")).append(";\n");
            }
            log.info("禁言处理结果：" + sb1.toString());

            fcp.setServerIds(serverIdStr.toString());
            fcp.setState(0);
            // 更新记录
            if (isUpdate) {
                dao.update(fcp);
            } else {
                dao.insert(fcp);
            }
        }

        BackendLogUtil.getInstance().log(request, "禁言操作\t理由：" + reason + "\t,区服ID:" + serverId + "\t,刷表结果：" + BSSB.toString() + "\t,禁言结果：" + BSSB.toString());
        return Toolkit.outResult(true, BSSB.toString());
    }

    /**
     * 禁言列表分页查询
     */
    @At
    @POST
    public Object forbidPlayerChatList(@Param("..") Pager pager) {
        long createTime = System.currentTimeMillis();
        String createDate = ymdhmssdf.format(new Date(createTime));

        Cnd cnd = Cnd.where("state", "=", 0)
                .and(new SqlExpressionGroup().and("endTime", "=", -1).or("endTime", ">", createDate));
        List<ForbidChatPlayer> list = dao.query(ForbidChatPlayer.class, cnd, pager);
        QueryResult qr = new QueryResult(list, pager);
        pager.setRecordCount(dao.count(ForbidChatPlayer.class, cnd));
        return Toolkit.outResult(true, qr);
    }

    /**
     * 解除玩家的禁言
     */
    @At
    @POST
    public Object unforbidPlayerChat(@Param("id") int id, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        ForbidChatPlayer fbc = dao.fetch(ForbidChatPlayer.class, id);
        if (fbc == null) {
            return Toolkit.outResult(false, language.get("announce.imedia.serverNull"));
        }

        // 保存数据到LS的表中
        Sql sql = Sqls.create("update forbidspeeking set `endTime`= 0 where userId=" + fbc.getUserId() + ";");
        loginDao.execute(sql);
        log.error("刷新forbidspeeking禁言表的结果是：" + (sql.getUpdateCount() > 0));

        StringBuilder sb = new StringBuilder();
        String serverList = fbc.getServerIds();
        if (serverList != null) {
            String[] serverIds = serverList.split(",");
            for (String sid : serverIds) {
                Server ser = ServerListManager.getInstance().getServer(sid);
                if (ser == null) {
                    continue;
                }
                NutMap ret = GameServerRequestUtil.gmForbidChat(ser);
                if (ret.getBoolean("ok")) {
                    sb.append(ser.getServerName()).append("解除禁言刷新成功 ！");
                    serverList = serverList.replace(sid + ",", "");
                } else {
                    sb.append(ser.getServerName()).append("解除禁言失败，原因：").append(ret.get("msg"));
                }
                log.error("解除禁言处理结果：" + sb.toString());
            }
        }

        fbc.setState(1);
        fbc.setServerIds(serverList);
        int num = dao.update(fbc);
        if (num < 1) {
            return Toolkit.outResult(false, language.get("forbid.deal.exceptionError"));
        }
        BackendLogUtil.getInstance().log(request, "解除禁言\t记录id：" + id);
        return Toolkit.outResult(true, language.get("forbid.deal.chatTonot"));
    }

    @At
    @GET
    @Filters
    public Object forbidUser(HttpServletRequest request, String nickname, String server, String banTime,
                             String username, String time, String ticket, String reason) {
        try {
            log.info(request.getRequestURL() + " | " + request.getQueryString() + " | " + Toolkit.getIp(request));
            if (!Strings.isNumber(server) || Strings.isBlank(username) || !Strings.isNumber(banTime)
                    || Strings.isBlank(nickname) || !Strings.isNumber(time) || Strings.isBlank(ticket)) {
                return Toolkit.result(request, 0, PARAM_ERROR);
            }
            int forbidTime = Integer.parseInt(banTime);
            Integer accessTime = Integer.valueOf(time);
            int now = (int) (System.currentTimeMillis() / 1000);
            if (now - accessTime > 120) {
                return Toolkit.result(request, 0, TIME_ERROR);
            }
            String md5Sign = Lang.md5(nickname + username + banTime + time + ServerKeyUtil.getKey(ACCESS_KEY));
            if (!md5Sign.equalsIgnoreCase(ticket)) {
                return Toolkit.result(request, 0, SIGN_ERROR);
            }
            Server serverObj = ServerListManager.getInstance().getServer(server);
            RoleState roleState = queryRole(serverObj, nickname);
            if (roleState == null) {
                return Toolkit.result(request, 0, USER_ERROR);
            }

            Server loginServer = LoginServerManager.getInstance().getLoginServer();
            if (loginServer == null) {
                return Toolkit.outResult(false, "未找到登录服服务器配置信息");
            }
            String response = forbidUser(loginServer, roleState.getFuncellUUid(), now + forbidTime * 60);
            if ("ok".equalsIgnoreCase(response)) {
                return Toolkit.result(request, 1, SUCCESS);
            }
            return Toolkit.result(request, 0, USER_ERROR);
        } catch (Exception e) {
            log.error(e, e);
            return "";
        }
    }

    /**
     * 封号
     */
    @At
    @POST
    public Object forbidUser(HttpServletRequest request, String groupName, int type, String condition, String endTime, String reason) {

        User user = (User) request.getSession().getAttribute("USER");
        if (Strings.isBlank(condition) || Strings.isBlank(endTime) || Strings.isBlank(reason)) {
            return Toolkit.outResult(false, "param error");
        }
        int forbidEndTime;
        try {
            Date eDate = sdf.parse(endTime);
            forbidEndTime = (int) (eDate.getTime() / 1000);
        } catch (ParseException e1) {
            log.error(e1);
            return Toolkit.outResult(false, "param error");
        }
        if (type == 1) {
            try {
                List<Server> servers = ServerListManager.getInstance().getServers(groupName);
                for (Server server : servers) {
                    List<RoleState> roleStates = queryRoleByUserId(server, "funcellUUid", condition);
                    if (roleStates == null || roleStates.isEmpty()) {
                        continue;
                    }
                    for (RoleState role : roleStates) {
                        NutMap ret = GameServerRequestUtil.gmKickPlayer(server, role.getRoleId());
                        log.info("内部封号并踢玩家下线：" + role.getRoleName() + "_" + ret.getBoolean("ok"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Forbiduser fcp = dao.fetch(Forbiduser.class, Cnd.where("userId", "=", condition));
        String createDate = ymdhmssdf.format(new Date(System.currentTimeMillis()));
        boolean isUpdate = (fcp != null);
        if (fcp == null) {
            fcp = new Forbiduser();
            fcp.setBackUserName(user.getName());
        }
        fcp.setCreateTime(createDate);
        fcp.setBackMUserName(user.getName());
        fcp.setUserId(condition);
        fcp.setReason(reason);
        fcp.setEndTime(endTime);
        fcp.setState(0);

        Server loginServer = LoginServerManager.getInstance().getLoginServer();
        if (loginServer == null) {
            return Toolkit.outResult(false, "未找到登录服服务器配置信息");
        }

        String response = forbidUser(loginServer, condition, forbidEndTime);
        BackendLogUtil.getInstance().log(request, "封号，条件" + condition + "\t理由：" + reason + "\t" + "\t结果：" + reason);
        if ("ok".equalsIgnoreCase(response)) {
            fcp.setBackStr(response);
            if (isUpdate) {
                dao.update(fcp);
            } else {
                dao.insert(fcp);
            }
            return Toolkit.outResult(true, response);
        }
        return Toolkit.outResult(false, response);
    }

    @At
    @POST
    public Object forbidUsers(HttpServletRequest request, String serverId, String roleIds, String endTime, String reason) {
        User user = (User) request.getSession().getAttribute("USER");
        if (Strings.isBlank(roleIds) || Strings.isBlank(endTime) || Strings.isBlank(reason)) {
            return Toolkit.outResult(false, "param error");
        }
        int forbidEndTime;
        try {
            Date eDate = sdf.parse(endTime);
            forbidEndTime = (int) (eDate.getTime() / 1000);
        } catch (ParseException e1) {
            log.error(e1);
            return Toolkit.outResult(false, "param error");
        }
        Dblog dblog = DbLogListManager.getInstance().getDblog(serverId);
        String roleStateSqlStr = "SELECT rs.roleId,rs.userId,rs.ip,rs.machineCode,rs.funcellUUid FROM rolestate rs WHERE rs.roleId in (" + roleIds+ ") AND rs.createsid = " + serverId;
        List<Map<String, Object>> userMap = QueryUtil.getInstance().query(dblog, roleStateSqlStr);
        if (userMap.isEmpty()) {
            return null;
        }

        Server loginServer = LoginServerManager.getInstance().getLoginServer();
        if (loginServer == null) {
            return Toolkit.outResult(false, "未找到登录服服务器配置信息");
        }

        StringBuilder sb = new StringBuilder();
        boolean isOk = false;
        for(Map<String, Object> map:userMap) {
            String condition=null;
            if(map.get("ip").toString() != null){
                condition = map.get("ip").toString();
            }else if(map.get("machineCode").toString() != null){
                condition = map.get("machineCode").toString();
            }else if(map.get("funcellUUid").toString() != null){
                condition = map.get("funcellUUid").toString();
            }
            sb.append(condition);
            Forbiduser fcp = dao.fetch(Forbiduser.class, Cnd.where("userId", "=", condition));
            String createDate = ymdhmssdf.format(new Date(System.currentTimeMillis()));
            boolean isUpdate = (fcp != null);
            if (fcp == null) {
                fcp = new Forbiduser();
                fcp.setBackUserName(user.getName());
            }
            fcp.setCreateTime(createDate);
            fcp.setBackMUserName(user.getName());
            fcp.setUserId(condition);
            fcp.setReason(reason);
            fcp.setEndTime(endTime);
            fcp.setState(0);

            //通知登录服封号
            String response = forbidUser(loginServer, condition, forbidEndTime);
            BackendLogUtil.getInstance().log(request, "封号，条件" + condition + "\t理由：" + reason + "\t" + "\t结果：" + reason);
            if ("ok".equalsIgnoreCase(response)) {
                fcp.setBackStr(response);
                if (isUpdate) {
                    dao.update(fcp);
                } else {
                    dao.insert(fcp);
                }
                sb.append(response);
                isOk = true;

                //通知游戏服踢下线
                Server server = ServerListManager.getInstance().getServer(serverId);
                NutMap ret = GameServerRequestUtil.gmKickPlayer(server, map.get("roleId").toString());
                log.info("内部封号并踢玩家下线：" + map.get("roleId").toString() + "_" + ret.getBoolean("ok"));
                continue;
            }
            sb.append(response);
        }
        if(isOk){
            return Toolkit.outResult(true, sb.toString());
        }else{
            return Toolkit.outResult(false, sb.toString());
        }
    }

    private String forbidUser(Server loginServer, String forbidStr, int time) {
        try {
            String url = "http://" + loginServer.getWorldIP() + ":" + loginServer.getWorldPort() + "/forbiddenuser?secret_key="
                    + ServerKeyUtil.GetLSRequestKey() + "&forbidStr=" + forbidStr + "&forbiddenTime=" + time;
            Response res = Http.get(url);
            log.info(url + "======>>>" + res.getContent());
            return res.getContent();
        } catch (Exception e) {
            log.error("封禁账号，请求发送失败！！");
            log.error(e, e);
            return "error";
        }
    }

    @At
    @POST
    public Object forbidUserList(@Param("queryString") String queryString, @Param("..") Pager pager) {
        Cnd cnd = Cnd.where("state", "=", 0).and("userId", "like", "%" + queryString.trim() + "%");
        QueryResult qr = new QueryResult();
        qr.setList(dao.query(Forbiduser.class, cnd, pager));
        pager.setRecordCount(dao.count(Forbiduser.class, cnd));
        qr.setPager(pager);
        return qr;
    }

    @At
    @GET
    @Filters
    public Object unForbidUser(HttpServletRequest request, String nickname, String server, String minutes,
                             String time, String ticket, String status) {
        try {
            log.info(request.getRequestURL() + " | " + request.getQueryString() + " | " + Toolkit.getIp(request));
            if (!Strings.isNumber(server) || !Strings.isNumber(minutes) || Strings.isBlank(nickname)
                    || !Strings.isNumber(time) || Strings.isBlank(ticket)) {
                return Toolkit.result(request, 0, PARAM_ERROR);
            }
            Integer accessTime = Integer.valueOf(time);
            int now = (int) (System.currentTimeMillis() / 1000);
            if (now - accessTime > 120) {
                return Toolkit.result(request, 0, TIME_ERROR);
            }
            String md5Sign = Lang.md5(nickname + minutes + time + ServerKeyUtil.getKey(ACCESS_KEY));
            if (!md5Sign.equalsIgnoreCase(ticket)) {
                return Toolkit.result(request, 0, SIGN_ERROR);
            }
            Server serverObj = ServerListManager.getInstance().getServer(server);
            RoleState roleState = queryRole(serverObj, nickname);
            if (roleState == null) {
                return Toolkit.result(request, 0, USER_ERROR);
            }

            Server loginServer = LoginServerManager.getInstance().getLoginServer();
            if (loginServer == null) {
                return Toolkit.outResult(false, "未找到登录服服务器配置信息");
            }
            String response = unForbidUser(loginServer, roleState.getFuncellUUid());
            if ("ok".equalsIgnoreCase(response)) {
                return Toolkit.result(request, 1, SUCCESS);
            }
            return Toolkit.result(request, 0, USER_ERROR);
        } catch (Exception e) {
            log.error(e, e);
            return "";
        }
    }

    /**
     * 解封账号
     */
    @At
    @POST
    public Object unForbidUser(int id, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        Forbiduser fcp = dao.fetch(Forbiduser.class, id);
        if (fcp == null) {
            return Toolkit.outResult(false, language.get("forbid.deal.userNoRecord"));
        }
        Server loginServer = LoginServerManager.getInstance().getLoginServer();
        if (loginServer == null) {
            log.error("未找到登录服服务器配置信息");
            return false;
        }

        // 查询登录服务器
        String response = unForbidUser(loginServer, fcp.getUserId());
        fcp.setBackStr(response);
        fcp.setState(1);
        int num = dao.update(fcp);
        if (num < 1) {
            return Toolkit.outResult(false, language.get("forbid.deal.userUpdaterError"));
        }

        BackendLogUtil.getInstance().log(request, "解除封号 \t条件：" + fcp.getUserId() + "\t结果：" + response);
        return Toolkit.outResult(true, language.get("forbid.deal.userToSuccess"));
    }

    private String unForbidUser(Server loginServer, String forbidStr) {
        try {
            String url = "http://" + loginServer.getWorldIP() + ":" + loginServer.getWorldPort() + "/cancelforbiddenuser?secret_key="
                    + ServerKeyUtil.GetLSRequestKey() + "&forbidStr=" + forbidStr;
            Response res = Http.get(url);
            log.info(url + "======>>>" + res.getContent());
            return res.getContent();
        } catch (Exception e) {
            log.error("解封账号，请求发送失败！！");
            log.error(e, e);
            return "error";
        }
    }

    /**
     * 设置白名单
     */
    @At
    @POST
    public Object writeWhiteList(String condition, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");

        Server loginServer = LoginServerManager.getInstance().getLoginServer();
        if (loginServer == null) {
            return Toolkit.outResult(false, "未找到登录服服务器配置信息");
        }

        // 查询登录服务器
        StringBuilder sb = new StringBuilder();
        try {
            String url = "http://" + loginServer.getWorldIP() + ":" + loginServer.getWorldPort() + "/whiteadd?secret_key="
                    + ServerKeyUtil.GetLSRequestKey() + "&whiteStr=" + condition;
            Response res = Http.get(url);
            sb.append("LS").append(loginServer.getServerName()).append("(").append(loginServer.getServerId()).append("):").append(res.getContent()).append("      ");
        } catch (Exception e) {
            sb.append(loginServer.getWorldIP()).append(":").append(loginServer.getWorldPort()).append(" 连接异常，发送失败!");
            log.error(e, e);
        }

        WhiteList list = dao.fetch(WhiteList.class, Cnd.where("whiteCon", "=", condition));
        WhiteList wl = null;
        if (list == null) {
            list = new WhiteList();
            list.setWhiteCon(condition);
            list.setCtype(0);
            list.setBackStr(sb.toString());
            list.setCreatetime(ymdhmssdf.format(new Date()));
            list.setUserIP(Toolkit.getIp(request));
            list.setUserName(user.getName());
            list.setLsId(loginServer.getServerId());
            wl = dao.insert(list);
        } else {
            list.setBackStr(sb.toString());
            list.setCtype(0);
            int num = dao.update(list);
            if (num > 0) {
                wl = list;
            }
        }
        BackendLogUtil.getInstance().log(request, "添加白名单  publicId:" + loginServer.getServerId() + " \t条件：" + condition + "\t结果：" + sb.toString());
        log.error(user.getName() + "(" + user.getIp() + ") 添加白名单 \t条件：" + condition + "\t结果：" + sb.toString() + " 插入数据库的结果：" + (wl != null ? wl.getId() : " 插入失败！"));
        return Toolkit.outResult(true, condition + " + " + language.get("whitelist.deal.success"));//"");
    }

    //删除白名单
    @At
    @POST
    public Object deleteWhiteList(String condition, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");

        Server loginServer = LoginServerManager.getInstance().getLoginServer();
        if (loginServer == null) {
            return Toolkit.outResult(false, "未找到登录服服务器配置信息");
        }

        // 查询登录服务器
        StringBuilder sb = new StringBuilder();
        try {
            String url = "http://" + loginServer.getWorldIP() + ":" + loginServer.getWorldPort() + "/whitecancel?secret_key="
                    + ServerKeyUtil.GetLSRequestKey() + "&whiteStr=" + condition;
            Response res = Http.get(url);
            sb.append("LS").append(loginServer.getServerName()).append("(").append(loginServer.getServerId()).append("):").append(res.getContent()).append("      ");

            Sql sql = Sqls.create("select count(*) from white where `str`= \"" + condition + "\";");
            sql.setCallback(Sqls.callback.integer());
            loginDao.execute(sql);
            int num = sql.getInt();
            if (num > 0) {
                sql = Sqls.create("delete from white where str = \"" + condition + "\"");
                loginDao.execute(sql);
                int exeNum = sql.getUpdateCount();
                sb.append("删除数据状态：").append(exeNum > 0);
            }
        } catch (Exception e) {
            sb.append(loginServer.getWorldIP()).append(":").append(loginServer.getWorldPort()).append(" 连接异常，发送失败!");
            log.error(e, e);
        }

        WhiteList list = dao.fetch(WhiteList.class, Cnd.where("whiteCon", "=", condition));
        WhiteList wl = null;
        if (list == null) {
            list = new WhiteList();
            list.setBackStr(sb.toString());
            list.setWhiteCon(condition);
            list.setCtype(1);
            list.setCreatetime(ymdhmssdf.format(new Date()));
            list.setUserIP(request.getHeader(StaticField.USER_REAL_IP));
            list.setUserName(user.getName());
            wl = dao.insert(list);
        } else {
            list.setBackStr(sb.toString());
            list.setCtype(1);
            int num = dao.update(list);
            if (num > 0) {
                wl = list;
            }
        }
        BackendLogUtil.getInstance().log(request, "删除白名单 \t条件：" + condition + "\t结果：" + sb.toString());
        log.info(user.getName() + "(" + user.getIp() + ") 删除白名单 \t条件：" + condition + "\t结果：" + sb.toString() + " 插入数据库的结果：" + (wl != null ? wl.getId() : " 插入失败！"));
        return Toolkit.outResult(true, condition + " - " + language.get("whitelist.deal.success"));
    }

    @At
    @POST
    public Object queryWhiteList(Pager pager) {
        Cnd cnd = Cnd.NEW();
        cnd.orderBy("id", "desc");
        List<WhiteList> list = dao.query(WhiteList.class, cnd, pager);
        QueryResult qr = new QueryResult(list, pager);
        pager.setRecordCount(dao.count(WhiteList.class, cnd));
        return Toolkit.outResult(true, qr);
    }

    @At
    @POST
    public Object updateWhileList(int id, HttpServletRequest request) {
        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        User user = (User) request.getSession().getAttribute("USER");
        WhiteList wl = dao.fetch(WhiteList.class, id);

        if (wl == null) {
            return Toolkit.outResult(false, language.get("forbid.deal.userNoRecord"));
        }

        String condition = wl.getWhiteCon();
        int type = 1;
        if (wl.getCtype() == 1) {
            type = 0;
        }
        wl.setCtype(type);
        // 查询登录服务器

        Server loginServer = LoginServerManager.getInstance().getLoginServer();
        if (loginServer == null) {
            return Toolkit.outResult(false, "未找到登录服服务器配置信息");
        }

        StringBuilder sb = new StringBuilder();
        try {
            String url = "http://" + loginServer.getWorldIP() + ":" + loginServer.getWorldPort() + (type == 0 ? "/whiteadd?secret_key=" : "/whitecancel?secret_key=")
                    + ServerKeyUtil.GetLSRequestKey() + "&whiteStr=" + condition;
            Response res = Http.get(url);
            sb.append("LS").append(loginServer.getServerName()).append("(").append(loginServer.getServerId()).append("):").append(res.getContent()).append("      ");
        } catch (Exception e) {
            sb.append(loginServer.getWorldIP()).append(":").append(loginServer.getWorldPort()).append(" 连接异常，发送失败!");
            log.error(e, e);
        }
        wl.setBackStr(sb.toString());
        int num = dao.update(wl);

        BackendLogUtil.getInstance().log(request, "删除白名单 \t条件：" + condition + "\t结果：" + sb.toString());
        log.error(user.getName() + "(" + user.getIp() + ") 删除白名单 \t条件：" + condition + "\t结果：" + sb.toString() + " 更新数据库的结果：" + num);
        return Toolkit.outResult(true, language.get("whitelist.deal.success"));
    }


    @At
    public Object getRoleLoinInfo(String roleId, String serverId) {

        Dblog dblog = DbLogListManager.getInstance().getDblog(serverId);
        String roleStateSqlStr = "SELECT rs.userId FROM rolestate rs WHERE rs.roleId = " + roleId + " AND rs.createsid = " + serverId;
        List<Map<String, Object>> userMap = QueryUtil.getInstance().query(dblog, roleStateSqlStr);
        if (userMap.isEmpty()) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String table;
        if (month < 10) {
            table = "loguser" + year + "0" + month;
        } else {
            table = "loguser" + year + month;
        }
        String roleloginSqlStr = "";
        roleloginSqlStr += "SELECT lu.id,lu.userid,lu.lastLoginIp,lu.userName,lu.mac,lu.imei,lu.machineCode ";
        roleloginSqlStr += "FROM " + table + " lu where lu.userid="+userMap.get(0).get("userId");
        List<Map<String, Object>> roleLoginMap = QueryUtil.getInstance().query(loginLogDao, roleloginSqlStr);
        if (roleLoginMap.size() == 0) {
            return null;
        }
        return roleLoginMap.get(0);
    }

    @At
    public Object getShieldKeywordList(String serverId) {
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap resultMap = GameServerRequestUtil.gmOrderSendMess(server, "gmGetKeyWords", "");
        if (!resultMap.getBoolean("ok")) {
            log.error("服务器编号" + serverId + "反馈信息:被屏蔽的关键字列表获取失败！");
            return Toolkit.outResult(false, "列表获取失败");
        }
        return Toolkit.outResult(true, resultMap.get("msg"));
    }

    @At
    public Object addShieldKeyWord(String serverId, String keyword, String type) {
        Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", serverId).and("isDeleted", "=", 0));
        if (server == null) {
            log.error("服务器获取失败！sid=" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap resultMap = GameServerRequestUtil.gmAddKeyWord(server, type, keyword);
        if (!resultMap.getBoolean("ok")) {
            return Toolkit.outResult(false, "关键字添加失败！");
        }
        return Toolkit.outResult(false, "关键字添加成功！");
    }

    @At
    public Object deleteShieldKeyWord(String serverId, int id) {
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "服务器连接信息获取失败");
        }
        return GameServerRequestUtil.gmDeleteKeyWord(server, id);
    }

    @At
    public Object getChatReplaceWordList(String serverId) {
        String table = "chatword";
        String sqlStr = "SELECT id,serverId,word,`replace`,`type` FROM " + table + " where serverId="+serverId;
        List<Map<String, Object>> resultMap = QueryUtil.getInstance().query(loginDao, sqlStr);
        if (resultMap.size() == 0) {
            return Toolkit.outResult(false, "列表获取失败");
        }
        return Toolkit.outResult(true, resultMap);
    }

    @At
    public Object addChatReplaceWord(String serverId, String keyword, String replace, String type) {
//        Map<String, String> language = Mvcs.getMessages(Mvcs.getReq());
        if (serverId == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }

        Sql sql = Sqls.create("select count(*) from chatword where `serverId`=" + serverId + " and word='" + keyword +"';");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = (int) sql.getResult();
        if (num > 0) {
            sql = Sqls.create("update chatword set `replace`= " + replace + " where serverId = " + serverId + " and word=" + keyword +";");
        } else {
            sql = Sqls.create("insert into chatword(serverId, type, `word`, `replace`) values(" + serverId + "," + type + ",'" + keyword + "','" + replace + "')");
        }
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        log.info("添加chatWord替换关键字表的结果是：" + (exeNum > 0));

        Cnd gp = Cnd.where("serverId", "=", serverId);
        Server server = dao.fetch(Server.class, gp);
        if (server == null) {
            log.error("未找到id对应的服务器:" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap result = GameServerRequestUtil.gmLoadChatWord(server);
        if (!result.getBoolean("ok")) {
            BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
            return Toolkit.outResult(false, "替换关键字表刷新失败");
        }
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
        return Toolkit.outResult(true, result.get("msg"));
    }

    @At
    public Object deleteChatReplaceWord(String serverId, int id) {
        if (serverId == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }

        Sql sql = Sqls.create("DELETE from chatword where id=" + id + ";");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = sql.getUpdateCount();
        if (num <= 0) {
            return Toolkit.outResult(false, "删除替换关键词失败");
        }
        log.info("删除chatWord替换关键字表的结果是：" + (num > 0));

        Cnd gp = Cnd.where("serverId", "=", serverId);
        Server server = dao.fetch(Server.class, gp);
        if (server == null) {
            log.error("未找到id对应的服务器:" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap result = GameServerRequestUtil.gmLoadChatWord(server);
        if (!result.getBoolean("ok")) {
            BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
            return Toolkit.outResult(false, "替换关键字表刷新失败");
        }
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
        return Toolkit.outResult(true, result.get("msg"));
    }

    @At
    public Object getChatBlackList(String serverId) {
        String table = "chatblacklist";
        String sqlStr = "SELECT userId,serverId FROM " + table + " where serverId="+serverId;
        List<Map<String, Object>> resultMap = QueryUtil.getInstance().query(loginDao, sqlStr);
        if (resultMap.size() == 0) {
            return Toolkit.outResult(false, "列表获取失败");
        }
        return Toolkit.outResult(true, resultMap);
    }

    @At
    public Object addChatBlackList(String serverId, String userId) {
        if (serverId == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        Sql sql = Sqls.create("select count(*) from chatblacklist where `serverId`=" + serverId + " and userId='" + userId +"';");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = (int) sql.getResult();
        if (num > 0) {
            return Toolkit.outResult(false, "黑名单已存在");
        } else {
            sql = Sqls.create("insert into chatblacklist(userId,serverId) values(" + userId + "," + serverId + ")");
        }
        loginDao.execute(sql);
        int exeNum = sql.getUpdateCount();
        log.info("添加chatblacklist替换关键字表的结果是：" + (exeNum > 0));

        Cnd gp = Cnd.where("serverId", "=", serverId);
        Server server = dao.fetch(Server.class, gp);
        if (server == null) {
            log.error("未找到id对应的服务器:" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap result = GameServerRequestUtil.gmLoadChatBlackList(server);
        if (!result.getBoolean("ok")) {
            BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
            return Toolkit.outResult(false, "禁言黑名单表刷新失败");
        }
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
        return Toolkit.outResult(true, result.get("msg"));
    }

    @At
    public Object deleteChatBlackList(String serverId, String userId) {
        if (serverId == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }

        Sql sql = Sqls.create("DELETE from chatblacklist where userId="+userId+" and serverId=" + serverId + ";");
        sql.setCallback(Sqls.callback.integer());
        loginDao.execute(sql);
        int num = sql.getUpdateCount();
        if (num <= 0) {
            return Toolkit.outResult(false, "删除禁言黑名单失败");
        }
        log.info("删除chatblacklist禁言黑名单表的结果是：" + (num > 0));

        Cnd gp = Cnd.where("serverId", "=", serverId);
        Server server = dao.fetch(Server.class, gp);
        if (server == null) {
            log.error("未找到id对应的服务器:" + serverId);
            return Toolkit.outResult(false, "获取服务失败");
        }
        NutMap result = GameServerRequestUtil.gmLoadChatBlackList(server);
        if (!result.getBoolean("ok")) {
            BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
            return Toolkit.outResult(false, "禁言黑名单表刷新失败");
        }
        BackendLogUtil.getInstance().log(Mvcs.getReq(), "区服ID:" + serverId + "\t,刷表结果：" +result.getBoolean("ok"));
        return Toolkit.outResult(true, result.get("msg"));
    }
}



