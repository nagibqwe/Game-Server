package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.manager.ActivityManager;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.ActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc LimitTimeLoginActivityScript 限时登陆的运营活动 300003
 * @date Created on 2020/8/18 18:06
 **/
public class LimitTimeLoginActivityScript implements IActivityScript {
    private static final Logger logger = LogManager.getLogger(LimitTimeLoginActivityScript.class);

    //保存到活动管理器上的主Key
    final String CN_MAIN_KEY = "limitTimeLogin";
    //发送给客户端的数据
    final String CN_RES_CLIENT_KEY = "client";
    //客户端请求的数据中的请求方法
    final String CN_C_REQUEST_KEY = "request";
    //客户端发送请求的奖励的
    final String CN_C_REQ_AWARD_KEY = "reqAward";
    //客户端发送请求的奖励的参数1
    final String CN_C_REQ_AWARD_P1_KEY = "day";
    //客户端发送请求的奖励的参数2
    final String CN_C_REQ_AWARD_P2_KEY = "isBuy";
    //客户端发送请求的解锁
    final String CN_C_REQ_UNLOCK_KEY = "reqUnlock";
    //玩家记录的活动数据之登陆天数
    final String CN_P_LOGIN_DAYS_KEY = "loginDays";
    //玩家记录的活动数据之普通奖励获取天数
    final String CN_P_GET_DAYS_KEY = "getDays";
    //玩家记录的活动数据之解锁奖励获取天数
    final String CN_P_GET_BUY_DAYS_KEY = "getBuyDays";
    //玩家记录的活动数据之是否解锁
    final String CN_P_IS_BOUGHT_KEY = "isBought";
    //玩家记录的活动数据之玩家最后一次登陆的时间记录
    final String CN_P_LAST_LOGIN_KEY = "lastLoginTime";


    @Override
    public int getId() {
        return ScriptEnum.LimitTimeLoginActivityScript;
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
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.LimitTimeLogin);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object obj = activityConfig.getCustomCfgMap().get(CN_MAIN_KEY);
            if (obj != null) {
                String customStr = JsonUtils.toJSONString(obj);
                LimitTimeLoginConfig newData = JsonUtils.toJavaObject(customStr, LimitTimeLoginConfig.class);
                activityConfig.getCustomCfgMap().put(CN_MAIN_KEY, newData);
            }
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        //logger.error("onReqActivityDeal:"+dataStr);

        ConcurrentHashMap<String, Object> data = JsonUtils.parseObject(dataStr, new TypeReference<ConcurrentHashMap<String, Object>>() {
        });
        String reqMethod = Utils.getOrDefaultFromMap(data, CN_C_REQUEST_KEY, "");
        if (reqMethod.equals(CN_C_REQ_AWARD_KEY)) {
            int day = Utils.getOrDefaultFromMap(data, CN_C_REQ_AWARD_P1_KEY, 0);
            onReqAwardHandler(player, day, actCfg);
        } else if (reqMethod.equals(CN_C_REQ_UNLOCK_KEY)) {
            onUnlockHandler(player, -1, actCfg);
        } else {
            logger.error("接受未知请求:" + reqMethod);
        }

    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        LimitTimeLoginConfig data = JsonUtils.toJavaObject(customStr, LimitTimeLoginConfig.class);
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
        onUpdateDaysHandler(player,actCfg);

        //获取玩家在线活动信息发送
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId,actCfg.getType());
        HashMap<String, Object> groupData = new HashMap<>();
        groupData.put(CN_P_LOGIN_DAYS_KEY, roleActDataMap.getOrDefault(CN_P_LOGIN_DAYS_KEY,0));
        groupData.put(CN_P_GET_DAYS_KEY, roleActDataMap.getOrDefault(CN_P_GET_DAYS_KEY,0));
        groupData.put(CN_P_GET_BUY_DAYS_KEY, roleActDataMap.getOrDefault(CN_P_GET_BUY_DAYS_KEY,0));
        groupData.put(CN_P_IS_BOUGHT_KEY, roleActDataMap.getOrDefault(CN_P_IS_BOUGHT_KEY,0));
        return JsonUtils.toJSONString(groupData);
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {
        if (actCfg != null) {
            LimitTimeLoginConfig cfg = (LimitTimeLoginConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);
            if (cfg != null && cfg.buyMoneyType < 0) {
                onUnlockHandler(player, rechargeNum, actCfg);
            }
        }
    }

    /**
     * 登录加载数据
     *
     * @param player 玩家
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {
        onUpdateDaysHandler(player,actCfg);
    }

    /**
     * 0点刷新
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {
        onUpdateDaysHandler(player,actCfg);
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
        //活动结束后把奖励通过邮件发送给玩家
        onSendEmailToAllPlayers(actCfg);
    }
    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){

    }

    //region //内部实现方法

    /**
     * 更新玩家的登陆天数
     *
     * @param player
     */
    private void onUpdateDaysHandler(Player player,ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());
        long currLoginTime = TimeUtils.Time();

        //1.初始化所有的数据
        Utils.putNoExistInMap(roleActDataMap, CN_P_LAST_LOGIN_KEY, currLoginTime);
        Utils.putNoExistInMap(roleActDataMap, CN_P_LOGIN_DAYS_KEY, 1);
        Utils.putNoExistInMap(roleActDataMap, CN_P_GET_DAYS_KEY, 0);
        Utils.putNoExistInMap(roleActDataMap, CN_P_GET_BUY_DAYS_KEY, 0);
        Utils.putNoExistInMap(roleActDataMap, CN_P_IS_BOUGHT_KEY, 0);


        //2.处理最后一次登陆的时间
        long lastLoginTime = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_LAST_LOGIN_KEY, currLoginTime);
        //3.获取登陆天数
        int loginDays = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_LOGIN_DAYS_KEY, 1);
        //4.对比最后一次和当前时间是否在一天内,如果不在,就把天数加1
        if (!TimeUtils.isSameDay(currLoginTime, lastLoginTime)) {
            loginDays++;
            roleActDataMap.put(CN_P_LOGIN_DAYS_KEY, loginDays);
            roleActDataMap.put(CN_P_LAST_LOGIN_KEY,currLoginTime);
        }
    }

    /**
     * 处理玩家请求奖励的处理
     *
     * @param player
     * @param day
     */
    private void onReqAwardHandler(Player player, int day, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }
        //1.判断领取的天数是否合理,天数需要在1,31之间,这个登陆奖励最多31天.
        if (day < 1 && day < 31) {
            logger.error("onReqAwardHandler:提交的天数小于1天是一个非法值. day=" + day);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }
        //2.获取玩家的活动数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        //3.判断登陆天数是否足够
        int loginDays = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_LOGIN_DAYS_KEY, 0);
        if (loginDays < day) {
            logger.error("onReqAwardHandler: 登陆天数小于领奖天数,非法领取奖励! loginDays=" + loginDays + "  day=" + day);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_LIMITTIMELOGIN_LOGINDAY_NOT_ENAOUGH);
            return;
        }

        //4.是否购买
        int isBought = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_IS_BOUGHT_KEY, 0);

        //5.判断是否已经发送过奖励
        int getDays = Utils.getOrDefaultFromMap(roleActDataMap, (CN_P_GET_DAYS_KEY), 0);
        int getBuyDays = Utils.getOrDefaultFromMap(roleActDataMap, (CN_P_GET_BUY_DAYS_KEY), 0);
        boolean isGet = hasGet(getDays, day);
        boolean isBuyGet = hasGet(getBuyDays, day);
        //5.1只要解锁的领过了,就表示全部都领过了
        if (isBuyGet) {
            logger.error("onReqAwardHandler:您已经领取过奖励! getDays=" + getDays + "  day=" + day + "  getBuyDays=" + getBuyDays);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_AWARD_HAVE_GETED);
            return;
        }
        //5.1在没有解封的时候,普通的是否领过了
        if (isGet && isBought <= 0) {
            logger.error("onReqAwardHandler:您已经领取过奖励! isBought=" + isBought + "  day=" + day + "  getDays=" + getDays);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_AWARD_HAVE_GETED);
            return;
        }

        //6.判断一下配置中奖励是否有效
        LimitTimeLoginConfig cfg = (LimitTimeLoginConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);
        if (cfg == null || cfg.getBuyAwardList() == null || cfg.getNormalAwardList() == null) {
            logger.error("onReqAwardHandler:活动配置数据中奖励错误! cfg.getBuyAwardList() == null  || cfg.getNormalAwardList() == null");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }
        if (cfg.getBuyAwardList().size() < day || cfg.getNormalAwardList().size() < day) {
            logger.error("onReqAwardHandler:活动配置数据中奖励错误! cfg.getBuyAwardList().size()=" + cfg.getBuyAwardList().size() + " day=" + day + "  cfg.getNormalAwardList().size()=" + cfg.getNormalAwardList().size());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }
        if (cfg.getBuyAwardList().get(day - 1) == null || cfg.getNormalAwardList().get(day - 1) == null) {
            logger.error("onReqAwardHandler:活动配置数据中奖励错误! cfg.getBuyAwardList().get(day-1) == null || cfg.getNormalAwardList().get(day-1) == null  day=" + day);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }

        //7.判断背包空间是否足够
        List<Item> items = new ArrayList<>();
        if (!isGet) {
            for (RewardData rd : cfg.getNormalAwardList().get(day - 1)) {
                if (rd.getC() == player.getCareer() || rd.getC() == 9) {
                    Item item = Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0);
                    if(item != null){
                        items.add(item);
                    }

                }
            }
        }
        if (isBought > 0) {
            for (RewardData rd : cfg.getBuyAwardList().get(day - 1)) {
                if (rd.getC() == player.getCareer() || rd.getC() == 9) {
                    Item item = Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0);
                    if(item != null){
                        items.add(item);
                    }
                }
            }
        }
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        //8.设置发奖标记
        roleActDataMap.put(CN_P_GET_DAYS_KEY, setGet(getDays, day));
        int reason = ItemChangeReason.LimitTimeLoginActivityGet;
        if (isBought > 0) {
            roleActDataMap.put(CN_P_GET_BUY_DAYS_KEY, setGet(getBuyDays, day));
            reason = ItemChangeReason.LimitTimeLoginActivityHighGradeGet;
        }

        //9.开始发奖
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, reason, IDConfigUtil.getLogId());

        //10.同步给用户,活动数据改变
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        //11.记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.LimitTimeLoginActivityGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LimitTimeLogin, reason, day);
    }

    /**
     * 处理玩家的那解锁的处理
     *
     * @param player
     */
    private void onUnlockHandler(Player player, int rechargeID, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }
        //1.获取玩家的活动数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        //2.是否购买
        int isBought = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_IS_BOUGHT_KEY, 0);
        if (isBought >= 1) {
            logger.error("onReqAwardHandler:已经解锁,不需要重复解锁! isBought=" + isBought +" roleID:"+player.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_LIMITTIMELOGIN_HAVE_UNLOCKED);
            return;
        }
        //3.读取活动配置
        LimitTimeLoginConfig cfg = (LimitTimeLoginConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);

        //4.判断当前如果是通过人民币购买的话,对比订单问题
        if (cfg.getBuyMoneyType() < 0 && (rechargeID < 0 || rechargeID != cfg.getBuyMoneyNum())) {
            logger.error("onReqAwardHandler:需要人民币购买,但是订单不对!cfg.buyMoneyType=" + cfg.getBuyMoneyType() + "  cfg.buyMoneyNum=" + cfg.getBuyMoneyNum() + " rechargeID=" + rechargeID+" roleID:"+player.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_LIMITTIMELOGIN_UNLOCKED_ORDER_ERROR);
            return;
        }

        //5.如果是游戏货币就开始扣款
        if (cfg.getBuyMoneyType() > 0 && cfg.getBuyMoneyNum() > 0) {
            long actionId = IDConfigUtil.getLogId();
            if (!Manager.currencyManager.manager().onDecItemCoin(player, cfg.getBuyMoneyNum(), ItemChangeReason.OwnDeleteDec, actionId, cfg.getBuyMoneyType())) {
                logger.error("onReqAwardHandler:扣款失败!cfg.buyMoneyType=" + cfg.getBuyMoneyType() + "  cfg.buyMoneyNum=" + cfg.getBuyMoneyNum()+" roleID:"+player.getId());
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough,Manager.backpackManager.manager().getName(cfg.getBuyMoneyType()));
                return;
            }
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.SHOP_KOUCHU, cfg.getBuyMoneyNum() + "",
                    Manager.backpackManager.manager().getName(cfg.getBuyMoneyType()), cfg.getBuyMoneyNum() + "", Manager.backpackManager.manager().getName(cfg.getBuyMoneyType()));

        }

        //6.把解锁数据写入到角色身上
        roleActDataMap.put(CN_P_IS_BOUGHT_KEY, 1);

        //7.同步给用户,活动数据改变
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
    }

    /**
     * 发送邮件给所有玩家
     */
    private void onSendEmailToAllPlayers(ActivityConfig act){
        //1.读取配置
        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(act.getType());
        LimitTimeLoginConfig cfg = (LimitTimeLoginConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);

        //2.判断一下配置中奖励是否有效
        if (cfg == null || cfg.getBuyAwardList() == null || cfg.getNormalAwardList() == null) {
            logger.error("onReqAwardHandler:活动配置数据中奖励错误! cfg.getBuyAwardList() == null  || cfg.getNormalAwardList() == null");
            return;
        }

        //3.轮询角色ID进行发送邮件
        for(long roleID: Manager.activityManager.deal().getRoleIdList(act.getType())){
            ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleID, act.getType());
            onSendEmailToPlayer(Manager.playerManager.getPlayer(roleID),roleActDataMap,cfg);
        }
    }
    /**
     * 发送剩余奖励到邮件中
     * @param player
     * @param roleActDataMap
     * @param cfg
     */
    private void onSendEmailToPlayer(Player player,ConcurrentHashMap<String, Object> roleActDataMap ,LimitTimeLoginConfig cfg){

        //1.获取登陆天数
        int loginDays = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_LOGIN_DAYS_KEY, 0);
        //2.是否购买
        int isBought = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_IS_BOUGHT_KEY, 0);
        //3.领奖数据的标记
        int getDays = Utils.getOrDefaultFromMap(roleActDataMap, (CN_P_GET_DAYS_KEY), 0);
        int getBuyDays = Utils.getOrDefaultFromMap(roleActDataMap, (CN_P_GET_BUY_DAYS_KEY), 0);

        //5.搜集所有没有领取的奖励
        List<Item> items = new ArrayList<>();
        int nmAwardCount = cfg.getNormalAwardList().size();
        int buyAwardCount = cfg.getNormalAwardList().size();
        for(int i = 1; i <= loginDays && i <= nmAwardCount && i <= buyAwardCount; i++) {
            int day = i;
            boolean isGet = hasGet(getDays, day);
            boolean isBuyGet = hasGet(getBuyDays, day);

            //5.1 根据领取标记,搜集奖励
            if (!isGet) {
                for (RewardData rd : cfg.getNormalAwardList().get(day - 1)) {
                    if (rd != null && (rd.getC() == player.getCareer() || rd.getC() == 9)) {
                        Item item = Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0);
                        if(item != null){
                            items.add(item);
                        }
                    }
                }
            }

            if(!isBuyGet && isBought > 0) {
                for (RewardData rd : cfg.getBuyAwardList().get(day - 1)) {
                    if (rd != null && (rd.getC() == player.getCareer() || rd.getC() == 9)) {
                        Item item = Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0);
                        if(item != null){
                            items.add(item);
                        }
                    }
                }
            }
        }

        //6.如果由未领取的奖励,就发送邮件
        if(items.size() > 0 ) {
            boolean ret = Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SpecialActivityMail, MessageString.System, MessageString.Activity_UnReceive_Award_Mail_Title, MessageString.Activity_UnReceive_Award_Mail_Content, items, ItemChangeReason.LimitTimeLoginActivityGet);
            if (ret) {
                //7.设置发奖标记
                roleActDataMap.put(CN_P_GET_DAYS_KEY, Integer.MAX_VALUE);
                if (isBought > 0) {
                    roleActDataMap.put(CN_P_GET_BUY_DAYS_KEY, Integer.MAX_VALUE);
                }
            }
        }

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

    /**
     * 判断是否领取
     *
     * @param hasget
     * @param day
     * @return
     */
    private boolean hasGet(int hasget, int day) {
        return (hasget & (1 << (day - 1))) != 0;
    }

    /**
     * 设置是否领取
     *
     * @param hasget
     * @param day
     * @return
     */
    private int setGet(int hasget, int day) {
        return (hasget | (1 << (day - 1)));
    }


    //endregion


    //region  //脚本内部的子类定义

    /**
     * 限时登陆的配置数据,只定义在脚本内部
     */
    private static class LimitTimeLoginConfig {
        //普通的奖励列表
        private List<List<RewardData>> normalAwardList;
        //购买的奖励列表
        private List<List<RewardData>> buyAwardList;
        //购买货币类型
        private int buyMoneyType;
        //购买货币数量
        private int buyMoneyNum;
        //客户端展示数据
        private String client ;

        public List<List<RewardData>> getNormalAwardList() {
            return normalAwardList;
        }

        public void setNormalAwardList(List<List<RewardData>> normalAwardList) {
            this.normalAwardList = normalAwardList;
        }

        public List<List<RewardData>> getBuyAwardList() {
            return buyAwardList;
        }

        public void setBuyAwardList(List<List<RewardData>> buyAwardList) {
            this.buyAwardList = buyAwardList;
        }

        public int getBuyMoneyType() {
            return buyMoneyType;
        }

        public void setBuyMoneyType(int buyMoneyType) {
            this.buyMoneyType = buyMoneyType;
        }

        public int getBuyMoneyNum() {
            return buyMoneyNum;
        }

        public void setBuyMoneyNum(int buyMoneyNum) {
            this.buyMoneyNum = buyMoneyNum;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
    }
    //endregion

    //region //测试数据的制作
    //测试领奖
    public static void testAward(Player player, int day, int isbuy) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player, ActivityManager.getInstance().deal().toActType(ActivityType.LimitTimeLogin ,0), "{'request':'reqAward','day':" + day + ",'isBuy':" + isbuy + "}");
    }

    //测试是否锁定
    public static void testUnlock(Player player) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player, ActivityManager.getInstance().deal().toActType(ActivityType.LimitTimeLogin ,0), "{'request':'reqUnlock'}");
    }

    //测试登陆日期
    public static void testSetLoginDay(Player player, int day) {
        //2.获取玩家的活动数据
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), ActivityManager.getInstance().deal().toActType(ActivityType.LimitTimeLogin ,0));
        roleActDataMap.put("loginDays", day);
    }

    //endregion
}
