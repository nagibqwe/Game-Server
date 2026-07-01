package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.manager.ActivityManager;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.db.bean.ActivityConfigBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc FB分享活动脚本 300020
 * @date Created on 2020/8/18 18:06
 **/
public class FBShareActivityScript implements IActivityScript {
    private static final Logger logger = LogManager.getLogger(FBShareActivityScript.class);

    //保存到活动管理器上的主Key
    final String CN_MAIN_KEY = "fbShare";
    //发送给客户端的数据
    final String CN_RES_CLIENT_KEY = "client";
    //客户端请求的数据中的请求方法
    final String CN_C_REQUEST_KEY = "request";
    //客户端发送请求的奖励的
    final String CN_C_REQ_AWARD_KEY = "reqAward";
    //客户端发送请求分享的Key
    final String CN_C_REQ_SHARE_KEY = "reqShare";
    //玩家记录的活动数据之是否领奖
    final String CN_P_AWARD_IS_GET_KEY = "shareState";

    //还没有分享
    final int CN_STATE_NONE = 0;
    //已经分享过了
    final int CN_STATE_SHARED = 1;
    //已经领奖过了
    final int CN_STATE_AWARDED = 2;

    @Override
    public int getId() {
        return ScriptEnum.FBShareActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.FBShare);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object obj = activityConfig.getCustomCfgMap().get(CN_MAIN_KEY);
            if (obj != null) {
                String customStr = JsonUtils.toJSONString(obj);
                FBShareChristmasConfig newData = JsonUtils.toJavaObject(customStr, FBShareChristmasConfig.class);
                activityConfig.getCustomCfgMap().put(CN_MAIN_KEY, newData);
            }
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        logger.error("onReqActivityDeal:"+dataStr);
        ConcurrentHashMap<String, Object> data = JsonUtils.parseObject(dataStr, new TypeReference<ConcurrentHashMap<String, Object>>() {
        });
        String reqMethod = Utils.getOrDefaultFromMap(data, CN_C_REQUEST_KEY, "");
        if (reqMethod.equals(CN_C_REQ_AWARD_KEY)) {
            onReqAwardHandler(player,actCfg);
        } else if (reqMethod.equals(CN_C_REQ_SHARE_KEY)) {
            onReqShareHandler(player,actCfg);
        } else{
            logger.error("接受未知请求:" + reqMethod);
        }
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        logger.error("parseCustomConfig:"+customStr);
        FBShareChristmasConfig data = JsonUtils.toJavaObject(customStr, FBShareChristmasConfig.class);
        actCfg.getCustomCfgMap().put(CN_MAIN_KEY, data);
        actCfg.getCustomCfgMap().put(CN_RES_CLIENT_KEY, data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        //判断玩家是否在线
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            logger.error("getActivityDataStr:玩家已经离线!roleID:" + roleId);
            return "";
        }
        //更新玩家信息
        onUpdateActiveDataHandler(player,actCfg);

        //获取玩家在线活动信息发送
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        HashMap<String, Object> groupData = new HashMap<>();
        groupData.put(CN_P_AWARD_IS_GET_KEY, roleActDataMap.getOrDefault(CN_P_AWARD_IS_GET_KEY,0));
        return JsonUtils.toJSONString(groupData);
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * 登录加载数据
     *
     * @param player 玩家
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {
        onUpdateActiveDataHandler(player,actCfg);
    }

    /**
     * 0点刷新
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {
        onUpdateActiveDataHandler(player,actCfg);
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

    /**
     * 活动掉落
     *
     * @param player
     * @param bossId
     * @return
     */
    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 活动掉落
     *
     * @param player
     * @param boxId
     * @return
     */
    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 副本掉落
     *
     * @param player
     * @param cloneId
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }
    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){

    }

    //region //内部实现方法

    /**
     * 更新玩家的登陆天数
     *
     * @param player
     */
    private void onUpdateActiveDataHandler(Player player, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());
        //1.初始化所有的数据
        Utils.putNoExistInMap(roleActDataMap, CN_P_AWARD_IS_GET_KEY, CN_STATE_NONE);
    }
    /**
     * 处理玩家分享的请求
     *
     * @param player
     */
    private void onReqShareHandler(Player player, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }
        //1.获取玩家的活动数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        //2.判断登陆天数是否足够
        int shareState = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_AWARD_IS_GET_KEY, CN_STATE_NONE);
        if(shareState != CN_STATE_NONE){
            logger.error("onReqShareHandler: 圣诞FB分享活动 玩家的分享错误!! roleID:"+player.getId()+";; shareState:"+shareState);
            return;
        }
        //3.设置分享标记
        roleActDataMap.put(CN_P_AWARD_IS_GET_KEY, CN_STATE_SHARED);

        //4.同步给用户,活动数据改变
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
    }

    /**
     * 处理玩家请求奖励的处理
     *
     * @param player
     */
    private void onReqAwardHandler(Player player, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }
        //1.获取玩家的活动数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        //2.判断登陆天数是否足够
        int shareState = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_AWARD_IS_GET_KEY, CN_STATE_NONE);
        if(shareState != CN_STATE_SHARED) {
            logger.error("onReqShareHandler: 圣诞FB分享活动 玩家的领奖错误!! roleID:"+player.getId()+";; shareState:"+shareState);
            return;
        }

        //3.判断一下配置中奖励是否有效
        FBShareChristmasConfig cfg = (FBShareChristmasConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);
        if (cfg == null || cfg.getAwardList() == null || cfg.getAwardList().size() == 0) {
            logger.error("onReqAwardHandler:圣诞FB分享活动配置数据中奖励错误! cfg.getAwardList() == null  || cfg.getAwardList().size() == 0 roleID:"+player.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }

        //4.整理所发放的奖励
        List<Item> items = new ArrayList<>();
        for (RewardData rd : cfg.getAwardList()) {
            if (rd.getC() == player.getCareer() || rd.getC() == 9) {
                items.add(Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0));
            }
        }

        //4.1判断奖励是否配置正确
        if (items.size() == 0) {
            logger.error("onReqAwardHandler:圣诞FB分享活动配置数据中奖励错误! 当前性别的的奖励为空 Career:" +player.getCareer());
            //MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }

        //4.2判断背包空间是否足够
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        //5.设置发奖标记
        roleActDataMap.put(CN_P_AWARD_IS_GET_KEY, CN_STATE_AWARDED);

        //6.开始发奖
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FBShareChristmasActivityGet, IDConfigUtil.getLogId());

        //7.同步给用户,活动数据改变
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.FBShareChristmasActivityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FBShare, ItemChangeReason.FBShareChristmasActivityGet);
    }


    /**
     * 判断活动是否还有效
     *
     * @return
     */
    private boolean checkActivityValid(ActivityConfig actCfg) {
        if (actCfg == null) {
            return false;
        }
        if (!actCfg.isActiviting()) {
            logger.error("ActivityConfig is stop: " + actCfg.getType());
            return false;
        }
        return true;
    }

    //endregion


    //region  //脚本内部的子类定义

    /**
     * 限时登陆的配置数据,只定义在脚本内部
     */
    private static class FBShareChristmasConfig {
        //普通的奖励列表
        private List<RewardData> awardList;
        //客户端展示数据
        private String client ;

        public List<RewardData> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<RewardData> awardList) {
            this.awardList = awardList;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
    }


    /*
    //活动的玩家数据
    private static class FBShareChristmasActData{

        //大于0表示已经领取
        private int shareState;

        public int getShareState() {
            return shareState;
        }

        public void setShareState(int shareState) {
            this.isGet = shareState;
        }
    }*/
    //endregion

    //region //测试数据的制作
    //测试领奖
    public static void testAward(Player player) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player,  ActivityManager.getInstance().deal().toActType(ActivityType.FBShare ,0), "{'request':'reqAward'}");
    }

    public static void testShare(Player player) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player, ActivityManager.getInstance().deal().toActType(ActivityType.FBShare ,0), "{'request':'reqShare'}");
    }

    //测试注册一个限时活动
    public static void testRegisterActivity() {
        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(9);
        acb.setType(ActivityManager.getInstance().deal().toActType(ActivityType.FBShare ,0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("圣诞FB分享");
        acb.setBeginTime(TimeUtils.Time() - 24*60*60*1000);
        acb.setEndTime(TimeUtils.Time() + 24*60*60*1000);
        acb.setIsDelete((byte) 0);

        List<RewardData> list = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            RewardData item = new RewardData();
            item.setI(12004);
            item.setN(i * 10);
            item.setB(1);
            item.setC(9);
            list.add(item);
        }

        HashMap<String, Object> hm = new HashMap();
        hm.put("id", 111);
        hm.put("awardList", list);
        hm.put("shareState", 0);

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("awardList", list);

        hm.put("client", JsonUtils.toJSONString(clientMap));

        acb.setCustom(JsonUtils.toJSONString(hm));
        Manager.activityManager.deal().registerActivityBean(acb);
    }

    //endregion
}
