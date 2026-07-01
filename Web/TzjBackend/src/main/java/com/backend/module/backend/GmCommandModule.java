package com.backend.module.backend;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.ServerListManager;
import com.backend.utils.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.backend.bean.GmCommand;
import com.backend.bean.Server;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;

@IocBean
@At("/gm")
@Ok("json")
public class GmCommandModule {

    @Inject
    protected Dao dao;

    @At
    @Ok("jsp:jsp.server.gsCommand")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void gsCommand() {
    }

    @At
    @Ok("jsp:jsp.server.psCommand")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void psCommand() {
    }

    /**
     * 请求某个玩家的操作刷新记录
     */
    @At
    public Object query(@Param("action") String action, @Param("params") String params, @Param("gmType") int gmType,
                        @Param("..") Pager pager, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("USER");
        Cnd cnd = Cnd.where("user", "=", user.getName());
        if (!Strings.isBlank(action)) {
            cnd.and("action", "like", "%" + action + "%");
        }
        if (!Strings.isBlank(params)) {
            cnd.and("params", "like", "%" + params + "%");
        }
        if (gmType > -1) {
            cnd.and("gmType", "=", gmType);
        }
        cnd.orderBy("operDate", "desc");

        QueryResult qr = new QueryResult();
        qr.setList(dao.query(GmCommand.class, cnd, pager));
        pager.setRecordCount(dao.count(GmCommand.class, cnd));
        qr.setPager(pager);
        return qr;
    }

    @At
    @Filters
    public Object sendCommand(HttpServletRequest request, String[] serverId, String action, String params) {
        if (Strings.isBlank(action)) {
            return Toolkit.outResult(false, "命令为空！");
        }
        if (serverId == null || serverId.length == 0) {
            return Toolkit.outResult(false, "没有选择服务器Id");
        }

        boolean isOk = true;
        StringBuilder sb = new StringBuilder();
        for (String id : serverId) {
            Server ser = ServerListManager.getInstance().getServer(id);
            if (ser == null) {
                return Toolkit.outResult(false, "选择服务器Id不存在");
            }
            if (ser.getIsHeFu() == 1) {
                continue;
            }

            NutMap map = GameServerRequestUtil.gmOrderSendMess(ser, action, params);
            if (!map.getBoolean("ok")) {
                isOk = false;
            }

            String result = "执行GM命令,区服:" + ser.getServerName() + "(" + ser.getServerId() + ")"
                    + "命令：" + action + ", 参数:" + params
                    + "结果：" + map.toString() + "\n";
            sb.append(result);

            writeGmRunLog(request, ser, 0, action, params, isOk, result);
            BackendLogUtil.getInstance().log(request, result);
        }
        return Toolkit.outResult(isOk, sb.toString());
    }

    @At
    @POST
    public Object sendPS(HttpServletRequest request, String[] serverId, String action, String params) throws UnsupportedEncodingException {
        if (Strings.isBlank(action)) {
            return Toolkit.outResult(false, "命令错误");
        }
        if (serverId == null || serverId.length == 0) {
            return Toolkit.outResult(false, "没有选择服务器Id");
        }

        HashMap<String,String> paramMap = new HashMap<>();
        if(!"".equals(params)){
            String[] param = params.split("&");
            for (String pStr:param) {
                if(!"".equals(pStr)){
                    String[] ps = pStr.split("=");
                    paramMap.put(ps[0], ps[1]);
                }
            }
        }
        paramMap.put("secret_key",ServerKeyUtil.GetPSRuquestKey());

        params += "secret_key=" + ServerKeyUtil.GetPSRuquestKey();

        String url;
        StringBuilder result = new StringBuilder();
        boolean isOk = true;
        for (String id : serverId) {
            Server ser = ServerListManager.getInstance().getServer(id);
            if (ser == null) {
                return Toolkit.outResult(false, "选择服务器Id不存在");
            }
            if (ser.getIsHeFu() == 1) {
                continue;
            }

            url = "http://" + ser.getWorldIP() + ":" + ser.getWorldPort() + "/" + action;
            String re = HttpConnectionUtils.get(url, null, paramMap);
            result.append(re);

            if (Strings.isBlank(result)) {
                isOk = false;
                result.append("请求异常!");
            }

            writeGmRunLog(request, ser, 1, action, params, isOk, re);
            BackendLogUtil.getInstance().log(request, "执行GM命令,结果:" + result);
        }
        return Toolkit.outResult(isOk, result.toString());
    }

    private void writeGmRunLog(HttpServletRequest request, Server server, int type, String action, String param, boolean isOk, String result) {
        User user = (User) request.getSession().getAttribute("USER");
        GmCommand dbLog = new GmCommand();
        dbLog.setAction(action);
        dbLog.setOk(isOk);
        dbLog.setOperDate(new Date());
        dbLog.setParams(param);
        dbLog.setResult(result);
        dbLog.setServerId(server.getServerId());
        dbLog.setServerName(server.getServerName());
        dbLog.setUser(user.getName());
        dbLog.setIp(Toolkit.getIp(request));
        dbLog.setGmType(type);
        dao.insert(dbLog);
    }

    @At
    @Ok("jsp:jsp.server.opstime")
    public void opstime() {}

    @At
    @POST
    public Object queryOpsTime(String serverId) {
        if (Strings.isBlank(serverId)) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmQueryOpsTime(server);
    }

    @At
    @POST
    public Object setOpsTime(String serverId, String time) {
        if (serverId == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmSetOpsTime(server, time);
    }

    @At
    @POST
    public Object queryRegisterLimitNum(String serverId) {

        if (Strings.isBlank(serverId)) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmQueryRegisterLimitNum(server);
    }

    @At
    @POST
    public Object setRegisterLimitNum(String serverId, int num) {
        if (serverId == null) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        Server server = ServerListManager.getInstance().getServer(serverId);
        if (server == null) {
            return Toolkit.outResult(false, "选择服务器Id不存在");
        }
        return GameServerRequestUtil.gmSetRegisterLimitNum(server, num);
    }
}
