package common.activity;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 上线图片展示
 * @Auther: gouzhongliang
 * @Date: 2021/6/8 10:27
 */
public class PicTipsActivityScript implements IActivityScript {

    private static final Logger logger = LogManager.getLogger(PicTipsActivityScript.class);
    //发送给客户端的数据九 零一起玩 www.9 0175.com
    final String CN_RES_CLIENT_KEY = "client";

    @Override
    public void reload() {

    }

    @Override
    public int getId() {
        return ScriptEnum.PicTipsScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        logger.error("parseCustomConfig:"+customStr);
        PicTipsConfig data = JsonUtils.toJavaObject(customStr, PicTipsConfig.class);
        actCfg.getCustomCfgMap().put(CN_RES_CLIENT_KEY, data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return false;
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {
        return "{}";
    }

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }

    @Override
    public void everyHourDeal(ActivityConfig actCfg) {

    }

    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }


    /**
     * //客户端展示数据
     */
    private static class PicTipsConfig {
        //客户端展示数据
        private String client ;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
    }
}
