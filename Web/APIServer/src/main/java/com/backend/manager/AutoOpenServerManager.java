package com.backend.manager;

import com.backend.bean.GameInfo;
import com.backend.bean.Server;
import com.backend.gm.GameServerRequestUtil;
import com.backend.utils.*;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动开服管理
 */
public class AutoOpenServerManager {

    private final static Logger logger = Logger.getLogger(AutoOpenServerManager.class);

    private enum Singleton {

        INSTANCE;
        AutoOpenServerManager manager;

        Singleton() {
            this.manager = new AutoOpenServerManager();
        }

        AutoOpenServerManager getProcessor() {
            return manager;
        }
    }

    public static AutoOpenServerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    public void init(Dao dao) {
        this.dao = dao;
    }

    public Object checkServerOpen() {
        logger.error("检查服务器开放状态");
        StringBuilder sb = new StringBuilder();
        //通知游戏服更新某一条的充值信息
        Cnd cnd = Cnd.where("isDeleted", "=", 0).and("isHeFu", "=", 0).
//                and("serverType", "=", "1").
        and("serverType", "in", "0,1").
                        and("openState", "=", "0");
        int startServer = GameInfoManager.getInstance().getGameInfo().getAutoServerId() > 0 ? GameInfoManager.getInstance().getGameInfo().getAutoServerId() : 0;
        if (startServer > 0) {
            cnd.and("serverId", ">", startServer);
        }
        cnd.orderBy("serverId", "asc");
        List<Server> servers = dao.query(Server.class, cnd);
        List<Integer> serverSuccessList = new ArrayList<>();
        List<Integer> serverFailedList = new ArrayList<>();
        int force = 0;//是否强制开服
        int openUserCount = GameInfoManager.getInstance().getGameInfo().getAutoUserCount();
        int firstServerId = GameInfoManager.getInstance().getGameInfo().getAutoFirstServerId();
        for (Server server : servers) {
            int serverId = server.getServerId();
            if (StringUtils.isEmpty(server.getServerOpenTime())) {
                continue;
            }

            //检查开服人数是否满足强制开服条件
            if (!(firstServerId > 0 && firstServerId == serverId) && openUserCount > 0) {
                force = 0;
                int preServerId = getPreServerId(serverId);
                int count = -1;
                try {
                    Server s = dao.fetch(Server.class, Cnd.where("serverId", "=", preServerId));
                    if (s != null) {
                        count = s.getRegisterNum();

//                        String sql = "SELECT COUNT(t2.userId) as uCount FROM (SELECT t1.userId FROM rolestate t1 GROUP BY t1.userId) t2";
//                        List<Map<String, Object>> list = QueryUtil.getInstance().query(s, sql);
//                        if (!(list.isEmpty() || list.get(0).isEmpty())) {
//                            count = Integer.parseInt(list.get(0).get("uCount").toString());
//                        }

                        if (count >= openUserCount) {
                            force = 1;
                            logger.error(serverId + "服满足强制开服条件,前一个服" + preServerId + "注册人数：" + count);
                        }
                    }else{
                        logger.error(serverId + "服强制开服检查失败，前一个服" + preServerId + "的信息未找到");
                    }
                } catch (Exception e) {
                    logger.error(preServerId + "服强制开服检查时查询数据错误，error：" + e.getMessage() + "\n");
//                    continue;
                }
            }

            try {
                //开服时间到了请求检查游戏服是否开服
                if (force!=1 && TimeUtils.Time() < TimeUtils.getDateByString1(server.getServerOpenTime()).getTime()) {
                    continue;
                }
            } catch (ParseException e) {
                logger.error("时间解析错误,serverId=" + server.getServerId() + ",serverOpenTime=" + server.getServerOpenTime());
                continue;
            }

            try {
                NutMap resultMap = GameServerRequestUtil.gmOpenServer(server, force, 30*1000);
                if (!resultMap.getBoolean("ok")) {
                    serverFailedList.add(serverId);
                    logger.error(serverId + "服,更新开服状态失败,错误结果:"+resultMap.get("data"));
                } else {
                    server.setOpenState(1);
                    if (!server.getServerOpenTime().equals(resultMap.getString("data"))) {
                        server.setServerOpenTime(resultMap.getString("data"));
                    }
                    int uCount = dao.update(server);
                    if (uCount > 0) {
                        serverSuccessList.add(serverId);
                        logger.error(serverId + "服,更新开服状态成功\n");
                        ServerListManager.getInstance().updateServer(server);

                        //更新处理的服务器进度
                        GameInfo gameInfo = GameInfoManager.getInstance().getGameInfo();
                        gameInfo.setAutoServerId(serverId);
                        gameInfo.setTime(TimeUtils.Time());
                        dao.update(gameInfo);

                        //TODO 开服成功后，通知GM后台发布开服活动
                        HashMap<String,String> paramMap = new HashMap<>();
                        paramMap.put("serverId", String.valueOf(serverId));
                        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("GMUrl") + "/gmtool/activity/sendOpenServerActivity", paramMap);
                        logger.error("通知GM后台发布开服活动结果：" + httpResult);

//                        if (openUserCount > 0) {
//                            break;
//                        }
                    }
                }
            } catch (Exception e) {
                logger.error(serverId + "服更新开服状态失败，error：" + e.getMessage() + "\n");
                serverFailedList.add(serverId);
            }
        }
        sb.append("游戏服同步成功列表：").append(serverSuccessList).append("\n");
        sb.append("游戏服同步失败列表：").append(serverFailedList).append("\n");
        return Toolkit.outResult(true, sb.toString());
    }

    private int getPreServerId(int serverId) {
        return serverId - 1;
    }

}
