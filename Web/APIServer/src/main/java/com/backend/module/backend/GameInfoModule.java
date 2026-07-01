package com.backend.module.backend;

import com.backend.bean.GameInfo;
import com.backend.manager.GameInfoManager;
import com.backend.utils.JsonUtils;
import com.backend.utils.ServerKeyUtil;
import com.backend.utils.Toolkit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import javax.servlet.http.HttpServletRequest;

/**
 * 游戏信息管理
 */
@IocBean
@At("/gameInfo")
@Ok("json")
@Filters
public class GameInfoModule {

    private static final Log logger = Logs.get();

    @Inject
    protected Dao dao;

    @At
    @POST
    public Object updateGameInfo(HttpServletRequest request) {
        Cnd cnd = Cnd.where("gameId", "=", ServerKeyUtil.getGameID());
        GameInfo gameInfo = dao.fetch(GameInfo.class, cnd);
        if(gameInfo!=null){
            GameInfoManager.getInstance().setGameInfo(gameInfo);
            logger.error("更新游戏信息成功");
        }

        return Toolkit.outResult(true, "更新游戏信息成功");
    }

    @At
    public Object test(HttpServletRequest request) {
        return Toolkit.outResult(true, GameInfoManager.getInstance().getGameInfo());
    }
}
