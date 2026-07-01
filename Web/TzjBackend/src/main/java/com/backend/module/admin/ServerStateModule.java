package com.backend.module.admin;

import com.backend.bean.ServerNum;
import com.backend.bean.ServerState;
import com.backend.struct.StaticField;
import com.backend.utils.ParamUtil;
import com.backend.utils.Toolkit;
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

        ServerState serverState = new ServerState();
        serverState.setServerId(serverId);
        serverState.setState(state);
        serverState.setIp(request.getHeader(StaticField.USER_REAL_IP));
        serverState.setCurrentNum(online);
        serverState.setUpdateTime(ymdhmssdf.format(new Date(now)));
        serverState.setIsConnectWord(0);

        switch (cmd) {
            case "heart":
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
                break;
            case "connectpublic":
                if (tip.equalsIgnoreCase("start")) {
                    serverState.setIsConnectWord(1);
                } else {
                    serverState.setIsConnectWord(0);
                }
                break;
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
}
