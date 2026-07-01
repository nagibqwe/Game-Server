package com.backend.module.admin;

import com.backend.bean.GameInfo;
import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.manager.GameInfoManager;
import com.backend.utils.*;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 游戏信息管理
 */
@IocBean
@At("/gameInfo")
@Ok("json")
public class GameInfoModule {

    @Inject
    protected Dao dao;

    @At
    public int count() {
        return dao.count(Server.class);
    }

    @At("/")
    @Ok("jsp:jsp.server.gameInfo")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index() {}

    @At
    @POST
    public Object add(@Param("..") GameInfo gameInfo, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (gameInfo.getGameId() < 1) {
            return Toolkit.outResult(false, "游戏ID错误");
        }

        if (Strings.isBlank(gameInfo.getRechargeSecretkey())) {
            return Toolkit.outResult(false, "充值配置密钥错误");
        }

        int gameId = ServerKeyUtil.getGameID().equals("")?0:Integer.parseInt(ServerKeyUtil.getGameID());
        GameInfo updateGameInfo = dao.fetch(GameInfo.class, Cnd.where("gameId", "=", gameInfo.getGameId()));
        // 如果没有记录
        if (updateGameInfo == null) {
            gameInfo.setTime(TimeUtils.Time());
            GameInfo no = dao.insert(gameInfo);
            if (no != null) {
                BackendLogUtil.getInstance().log(request, "增加服务器信息，serverId:" + gameInfo.getGameId());
                GameInfoManager.getInstance().updateGameInfo(gameInfo);
                if(gameId == updateGameInfo.getGameId()){//同步游戏信息到APIServer
                    noticeUpdateGameInfo(gameInfo);
                }
                return Toolkit.outResult(true, "插入游戏信息成功" + no.getGameId());
            }
            return Toolkit.outResult(false, msg.get("db.insert.failure"));
        } else {
            if(gameId != updateGameInfo.getGameId()){
                updateGameInfo.setGameId(gameInfo.getGameId());
            }
            updateGameInfo.setRechargeSecretkey(gameInfo.getRechargeSecretkey().trim());
            updateGameInfo.setAutoFirstServerId(gameInfo.getAutoFirstServerId());
            updateGameInfo.setAutoUserCount(gameInfo.getAutoUserCount());
            updateGameInfo.setAutoServerId(gameInfo.getAutoServerId()) ;
            updateGameInfo.setTime(TimeUtils.Time());
            int no = dao.update(updateGameInfo);
            if (no > 0) {
                BackendLogUtil.getInstance().log(request, "修改游戏信息，gameId:" + gameInfo.getGameId());
                GameInfoManager.getInstance().updateGameInfo(updateGameInfo);

                if(gameId == updateGameInfo.getGameId()){//同步游戏信息到APIServer
                    noticeUpdateGameInfo(gameInfo);
                }

                return Toolkit.outResult(true, msg.get("db.update.success"));
            }
            return Toolkit.outResult(false, msg.get("db.update.failure"));
        }
    }

    private void noticeUpdateGameInfo(@Param("..") GameInfo gameInfo) {
        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("gameInfo", JsonUtils.toJSONString(gameInfo));
        String httpResult = HttpConnectionUtils.post(ServerKeyUtil.getKey("APIServerUrl") + "/gameInfo/updateGameInfo", paramMap);
    }

    @At
    public Object query(@Param("query_gameId") int query_gameId, @Param("..") Pager pager) {
        Cnd cnd = Cnd.NEW();
        cnd = query_gameId<1 ? cnd : cnd.where("gameId", "=", query_gameId);
        cnd.orderBy("gameId", "asc");

        QueryResult qr = new QueryResult();
        qr.setList(dao.query(GameInfo.class, cnd, pager));
        pager.setRecordCount(dao.count(GameInfo.class, cnd));
        qr.setPager(pager);
        return qr;
    }

    @At
    public Object queryByGameId(@Param("gameId") int gameId) {
        return dao.fetch(GameInfo.class, gameId);
    }

    @At
    @GET
    @Filters
    public Object addServer(@Param("..") GameInfo gameInfo, HttpServletRequest request) {
        if (Toolkit.checkSign(request)) {
            return add(gameInfo, request);
        }
        return Toolkit.outResult(false, "1");
    }

    @At
    @POST
    public Object delete(@Param("gameId") int gameId, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        int cfgGameId = ServerKeyUtil.getGameID().equals("")?0:Integer.parseInt(ServerKeyUtil.getGameID());
        if(cfgGameId == gameId){
            return Toolkit.outResult(false, "无法删除当前使用游戏数据");
        }
        GameInfo gameInfo = dao.fetch(GameInfo.class, gameId);
        int no = dao.delete(GameInfo.class, gameId);
        if (no > 0) {
            GameInfoManager.getInstance().updateGameInfo(gameInfo);
            BackendLogUtil.getInstance().log(request, "删除服务器信息，serverId:" + gameId);
            return Toolkit.outResult(true, "删除成功");
        }
        return Toolkit.outResult(false, msg.get("dblog.delete.tishi1") + gameId + msg.get("dblog.delete.tishi2"));
    }
}
