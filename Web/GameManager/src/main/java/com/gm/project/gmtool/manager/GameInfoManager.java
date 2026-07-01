package com.gm.project.gmtool.manager;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.framework.config.GameManagerConfig;
import com.gm.project.gmtool.gameInfo.domain.GameInfo;
import com.gm.project.gmtool.gameInfo.service.IGameInfoService;
import com.gm.project.gmtool.utils.HttpConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service()
public class GameInfoManager {

    private final static Logger log = LoggerFactory.getLogger(GameInfoManager.class);

    @Autowired
    private IGameInfoService gameInfoService;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    private GameInfo gameInfo = new GameInfo();

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public static GameInfoManager getInstance() {
        return (GameInfoManager)SpringUtils.getBean("gameInfoManager");
    }

    @PostConstruct
    public void init() {
        loadData();
        log.info("初始化活动信息...");
    }

    public void loadData() {
        GameInfo gameInfo = gameInfoService.selectGameInfoById(Integer.parseInt(gameManagerConfig.getGameID()));
        if(gameInfo != null){
            this.gameInfo = gameInfo;
        }
    }

    public void updateGameInfo(GameInfo gameInfo) {
        this.gameInfo.setRechargeSecretkey(gameInfo.getRechargeSecretkey());
        this.gameInfo.setAutoFirstServerId(gameInfo.getAutoFirstServerId());
        this.gameInfo.setAutoUserCount(gameInfo.getAutoUserCount());
        this.gameInfo.setAutoServerId(gameInfo.getAutoServerId());
    }

    public void noticeUpdateGameInfo(GameInfo gameInfo) {
        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("gameInfo", JsonUtils.toJSONString(gameInfo));
        String httpResult = HttpConnectionUtils.post(gameManagerConfig.getAPIServerUrl()+"/gameInfo/updateGameInfo", paramMap);
    }
}
