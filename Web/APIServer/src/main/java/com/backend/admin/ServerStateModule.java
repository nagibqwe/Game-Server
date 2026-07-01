package com.backend.admin;

import com.backend.bean.Server;
import com.backend.bean.ServerNum;
import com.backend.bean.ServerState;
import com.backend.manager.RechargeItemManager;
import com.backend.struct.ServerType;
import com.backend.struct.StaticField;
import com.backend.utils.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@IocBean
@At("/serverstate")
@Filters
@Ok("json")
public class ServerStateModule {

    @Inject
    protected Dao dao;

    private final SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat ymdhmssdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @At
    public Object checkstate(HttpServletRequest request) {

        long now = System.currentTimeMillis();

        String cmd = ParamUtil.getString(request, "c");
        int serverId = ParamUtil.getInt(request, "sid", 0);
        int state = ParamUtil.getInt(request, "state", 0);
        String tip = ParamUtil.getString(request, "tip");
        int online = ParamUtil.getInt(request, "online", 0);

        if (cmd.length() < 1) {
            Toolkit.outResult(false, "没有任务的指令");
        }

        if (serverId < 1) {
            return Toolkit.outResult(false, "没有指定相关的服务器ID");
        }

        String serverIp = request.getHeader(StaticField.USER_REAL_IP);
        if(serverIp==null){
            serverIp="";
        }
        String nowStr = ymdhmssdf.format(new Date(now));
        ServerState serverState = new ServerState();
        serverState.setServerId(serverId);
        serverState.setState(state);
        serverState.setIp(serverIp);
        serverState.setCurrentNum(online);
        serverState.setUpdateTime(nowStr);
        serverState.setIsConnectWord(0);

        switch (cmd) {
            case "heart":
                insertServerNum(now, serverId, online);

                //更新服务器时间
                updateServerInfo(request, serverId, tip, serverIp);
                break;
            case "connectpublic":
                if (tip.equalsIgnoreCase("start")) {
                    serverState.setIsConnectWord(1);
                } else {
                    serverState.setIsConnectWord(0);
                }
                break;
            case "rechargeItem"://请求同步充值配置
                RechargeItemManager.getInstance().sendRechargeInfos(serverId);
                break;
            case "start"://游戏服启动时同步服务器配置
                String serverInfoStr = ParamUtil.getString(request, "serverInfos", "");
                if(StringUtils.isEmpty(serverInfoStr)){
                    break;
                }

                initServerInfo(serverId, serverInfoStr);
                break;
            case "serverIdList"://请求获取合服id
                List<Server> servers = dao.query(Server.class, Cnd.where("hefuServerID", "=", serverId));
                StringBuilder sb = new StringBuilder();
                sb.append(serverId);
                if(servers != null && servers.size() > 0){
                    for(Server server : servers){
                        sb.append(",").append(server.getServerId());
                    }
                }
                return Toolkit.outResult(true, sb.toString());
            default:
                break;
        }
        Cnd cnd = Cnd.where("serverId", "=", serverState.getServerId());
        ServerState over = dao.fetch(ServerState.class, cnd);

        if (over == null) {
            dao.insert(serverState);
        } else {
            serverState.setId(over.getId());
            if (!cmd.equalsIgnoreCase("connectpublic")) {
                serverState.setIsConnectWord(over.getIsConnectWord());
            }
            dao.update(serverState);
        }

        return Toolkit.outResult(true, "处理结果完成");
    }

    private void insertServerNum(long now, int serverId, int online) {
        ServerNum serverNum = new ServerNum();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(now);

        String createDate = ymd.format(new Date(now));
        serverNum.setDay(createDate);
        serverNum.setHour(instance.get(Calendar.HOUR_OF_DAY));
        serverNum.setMin(instance.get(Calendar.MINUTE));
        serverNum.setNum(online);
        serverNum.setServerId(serverId);
        serverNum.setTime((int) (System.currentTimeMillis() / 1000));
        dao.insert(serverNum);
    }

    private void updateServerInfo(HttpServletRequest request, int serverId, String serverName, String serverIp) {
        String groupName = "game";
        int serverType = ParamUtil.getInt(request, "serverType", 0);
        int serverPort = ParamUtil.getInt(request, "port", 0);
        String serverOpenTime = ParamUtil.getString(request, "serverOpenTime");
        int registerNum = ParamUtil.getInt(request, "registerNum", 0);

        Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", serverId));
        if(server==null){
            server = new Server();
            server.setServerId(serverId);
            server.setServerName(serverName);
            server.setGroupName(groupName);
            server.setServerType((byte)serverType);
            server.setServerIP(serverIp);
            server.setServerPort(serverPort);
            server.setDblogIp("");
            server.setDblogPort(0);
            server.setDblogName("");
            server.setDblogUser("");
            server.setDblogPwd("");
            server.setServerIdList("");
            server.setServerOpenTime(serverOpenTime);
            server.setHeartTime(TimeUtils.NowToString());
            server.setRegisterNum(registerNum);
            dao.insert(server);
        }else{
            //更新服务器初始信息
            if(StringUtils.isEmpty(server.getServerName())){
                server.setServerName(serverName);
            }
            if(StringUtils.isEmpty(server.getGroupName())){
                server.setGroupName(groupName);
            }
            if(server.getServerType()< ServerType.Test){
                server.setServerType((byte)serverType);
            }
            if(StringUtils.isEmpty(server.getServerIP())){
                server.setServerIP(serverIp);
            }
            if(server.getServerPort()<=0){
                server.setServerPort(serverPort);
            }
            if(StringUtils.isEmpty(server.getServerOpenTime())){
                server.setServerOpenTime(serverOpenTime);
            }
            server.setHeartTime(TimeUtils.NowToString());
            server.setRegisterNum(registerNum);
            dao.update(server);
        }
    }

    private void initServerInfo(int serverId, String serverInfoStr) {
        HashMap<String, String> serverInfoMap = JsonUtils.parseObject(serverInfoStr, new TypeReference<HashMap<String, String>>() {});
        if(serverInfoMap.isEmpty()){
            return;
        }
        String serverName = serverInfoMap.get("serverName");
        String groupName = "game";
        String serverType = serverInfoMap.get("serverType");
        String serverIp = serverInfoMap.get("serverIp") == null?"":serverInfoMap.get("serverIp");
        String serverPort = serverInfoMap.get("serverPort");
        String dblogIpPort = serverInfoMap.get("dblogIpPort");
        String dblogName = serverInfoMap.get("dblogName");
        String dblogUser = serverInfoMap.get("dblogUser");
        String dblogPassword = serverInfoMap.get("dblogPassword");
        String serverOpenTime = serverInfoMap.get("serverOpenTime");

        Server server = dao.fetch(Server.class, Cnd.where("serverId", "=", serverId));
        if(server == null){
            //设置服务器初始信息
            server = new Server();
            server.setServerId(serverId);
            server.setServerName(serverName);
            server.setGroupName(groupName);
            server.setServerType(Byte.valueOf(serverType));
            server.setServerIP(serverIp);
            server.setServerPort(Integer.parseInt(serverPort));
            server.setDblogIp("");
            server.setDblogPort(0);
            server.setDblogName("");
            server.setDblogUser("");
            server.setDblogPwd("");
            server.setServerIdList("");
            server.setServerOpenTime(serverOpenTime);
            server.setHeartTime(TimeUtils.NowToString());
            dao.insert(server);
        }else{
            //更新服务器初始信息
            if(StringUtils.isEmpty(server.getServerName())){
                server.setServerName(serverName);
            }
            if(StringUtils.isEmpty(server.getGroupName())){
                server.setGroupName(groupName);
            }
            if(server.getServerType()< ServerType.Test){
                server.setServerType(Byte.valueOf(serverType));
            }
            if(StringUtils.isEmpty(server.getServerIP())){
                server.setServerIP(serverIp);
            }
            if(server.getServerPort()<=0){
                server.setServerPort(Integer.parseInt(serverPort));
            }
//            if(StringUtils.isEmpty(server.getServerOpenTime())){
                server.setServerOpenTime(serverOpenTime);
//            }
            server.setHeartTime(TimeUtils.NowToString());
            dao.update(server);
        }
    }
}
