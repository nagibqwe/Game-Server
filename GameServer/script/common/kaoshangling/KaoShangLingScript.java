package common.kaoshangling;

import com.data.*;
import com.data.bean.Cfg_KaoShangLing_Horse_Bean;
import com.data.bean.Cfg_KaoShangLing_Horse_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.kaoshangling.manager.KaoShangLingManager;
import com.game.kaoshangling.script.IKaoShangLingScript;
import com.game.kaoshangling.struct.KaoShangLingBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.KaoShangLingMessage;
import game.message.PlatformEvaluateMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 犒赏令脚本
 */
public class KaoShangLingScript implements IScript,IKaoShangLingScript {
    @Override
    public int getId() {
        return ScriptEnum.KaoShangLingScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
    /**
     * 请求刷新犒赏令轮次
     * @param player
     */
    @Override
    public void reqKaoShangLingRefreshRankHandler(Player player,int type) {
        KaoShangLingBean kaoShangLingBean =  player.getKaoShangLingData().getKaoShangLingMap().get(type);
        if(kaoShangLingBean == null){
            return;
        }
        int kaoShangLingRank = kaoShangLingBean.getRank();//轮次
        int if_end = 0;
        for (Cfg_KaoShangLing_Horse_Bean bean :  CfgManager.getCfg_KaoShangLing_Horse_Container().getValuees()) {
            if(bean.getRank() == kaoShangLingRank){
                if_end = bean.getIf_end();
               break;
            }
        }
        //领取未领取的奖励
        this.reqKaoShangLingRewardHandler(player,type,1,0,1);
        //重置为零
        kaoShangLingBean.setIsBuySpecail(0);

        if(if_end != 1){
            kaoShangLingRank = kaoShangLingRank + 1;
            kaoShangLingBean.setRank(kaoShangLingRank);
        }
        //清除领取奖励记录
        kaoShangLingBean.getCommonRewardList().clear();
        kaoShangLingBean.getSpecailRewardist().clear();
        //设置犒赏令
        if(type == KaoShangLingManager.EnKaoShangLingHorse){
            Manager.countManager.setCount(player,BaseCountType.KaoShangLingScore_Horse_Total,DailyActiveDefine.CrossHosreBoss.getValue(), Count.RefreshType.CountType_Forever,0);
        }
        //刷新数据
        sendResOpenKaoShangLingPanel(player);
        //响应
        KaoShangLingMessage.ResKaoShangLingRefreshRank.Builder resKaoShangLingRefreshRank = KaoShangLingMessage.ResKaoShangLingRefreshRank.newBuilder();
        resKaoShangLingRefreshRank.setType(type);
        MessageUtils.send_to_player(player, KaoShangLingMessage.ResKaoShangLingRefreshRank.MsgID.eMsgID_VALUE, resKaoShangLingRefreshRank.build().toByteArray());
    }

    @Override
    public void playerOnline(Player player) {
        //测试用
        //Manager.countManager.addCount(player,BaseCountType.KaoShangLingScore_Horse_Total,0, Count.RefreshType.CountType_Forever,3750);
        sendResOpenKaoShangLingPanel(player);
    }
    @Override
    public void reqOpenKaoShangLingPanelHandler(Player player) {
        sendResOpenKaoShangLingPanel(player);
    }

    @Override
    public void reqBuySpecailKaoShangLing(Player player, int type) {
        KaoShangLingBean kaoShangLingBean = player.getKaoShangLingData().getKaoShangLingMap().get(type);
        if(kaoShangLingBean == null){
            return;
        }
        //已经购买
        if(kaoShangLingBean.getIsBuySpecail() == 1){
            return;
        }
        int modelId = Global.KaoShangLing_BuyItem.get(0);
        int num = Global.KaoShangLing_BuyItem.get(1);
        if (!Manager.backpackManager.manager().removeItemOrCurrency(player, modelId, num, IDConfigUtil.getLogId(), ItemChangeReason.KaoShangLingHorseBuySpecailDec)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                return;
        }
        kaoShangLingBean.setIsBuySpecail(1);
        //响应
        KaoShangLingMessage.ResBuySpecailKaoShangLing.Builder res = KaoShangLingMessage.ResBuySpecailKaoShangLing.newBuilder();
        res.setType(type);
        res.setIsBuySpecail(kaoShangLingBean.getIsBuySpecail());
        MessageUtils.send_to_player(player, KaoShangLingMessage.ResBuySpecailKaoShangLing.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 打开面板同步相关数据
     * @param player
     */
    private void sendResOpenKaoShangLingPanel(Player player){

        //犒赏令数据同步
        Map<Integer, KaoShangLingBean>  kaoShangLingMap = player.getKaoShangLingData().getKaoShangLingMap();
        KaoShangLingMessage.ResOpenKaoShangLingPanel.Builder res = KaoShangLingMessage.ResOpenKaoShangLingPanel.newBuilder();
        for(int type : kaoShangLingMap.keySet()){
            KaoShangLingBean kaoShangLingBean = kaoShangLingMap.get(type);

            KaoShangLingMessage.KaoShangLingInfo.Builder kaoShangLingInfo = KaoShangLingMessage.KaoShangLingInfo.newBuilder();
            kaoShangLingInfo.setType(type);
            kaoShangLingInfo.setScoreDay(this.getKaoShangLingScoreDay(player,type));
            kaoShangLingInfo.setScoreTotal(this.getKaoShangLingScoreTotal(player,type));
            kaoShangLingInfo.setRank(kaoShangLingBean.getRank());
            kaoShangLingInfo.setIsBuySpecail(kaoShangLingBean.getIsBuySpecail());
            kaoShangLingInfo.addAllCommonRewardList(kaoShangLingBean.getCommonRewardList());
            kaoShangLingInfo.addAllSpecailRewardist(kaoShangLingBean.getSpecailRewardist());

            res.addKaoShangLingInfoList(kaoShangLingInfo.build());

        }
        MessageUtils.send_to_player(player, KaoShangLingMessage.ResOpenKaoShangLingPanel.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 计算奖励
     * @param player
     * @param allRewardItems
     * @param kaoShangLingBean
     * @param kaoShangLingScoreTotal
     * @param kaoShangLing_Bean
     */
    public  void calcKaoShangLingReward(Player player,List<Item> allRewardItems,KaoShangLingBean kaoShangLingBean ,long kaoShangLingScoreTotal,Cfg_KaoShangLing_Horse_Bean kaoShangLing_Bean){
        //配置不存在
        if(kaoShangLing_Bean == null){
            return ;
        }
        //轮次不匹配
        if(kaoShangLing_Bean.getRank() != kaoShangLingBean.getRank()){
            return;
        }
        //进度值不够
        if(kaoShangLingScoreTotal < kaoShangLing_Bean.getScore()){
            return;
        }
        //高级
        if(kaoShangLingBean.getIsBuySpecail() == 1){
            //高级奖励
            if(!kaoShangLingBean.getSpecailRewardist().contains(kaoShangLing_Bean.getId())){
                allRewardItems.addAll(Item.createItems( kaoShangLing_Bean.getSpecail_reward(), 1));
                kaoShangLingBean.getSpecailRewardist().add(kaoShangLing_Bean.getId());
            }
        }
        //普通奖励
        if(!kaoShangLingBean.getCommonRewardList().contains(kaoShangLing_Bean.getId())){
            allRewardItems.addAll(Item.createItems( kaoShangLing_Bean.getCommon_reward(), 1));
            kaoShangLingBean.getCommonRewardList().add(kaoShangLing_Bean.getId());
        }
    }

    /**
     * 获取总积分
     * @param player
     * @param type
     * @return
     */
    public int getKaoShangLingScoreTotal(Player player,int type){

        if(type == KaoShangLingManager.EnKaoShangLingHorse){
            return (int)Manager.countManager.getCount(player, BaseCountType.KaoShangLingScore_Horse_Total,DailyActiveDefine.CrossHosreBoss.getValue());
        }
        return 0;
    }
    /**
     * 获取每天积分
     * @param player
     * @param type
     * @return
     */
    public int getKaoShangLingScoreDay(Player player,int type){

        if(type == KaoShangLingManager.EnKaoShangLingHorse){
            return (int)Manager.countManager.getCount(player, BaseCountType.KaoShangLingScore_Horse_Day, DailyActiveDefine.CrossHosreBoss.getValue());
        }
        return 0;
    }

    /**
     * 获取物品原因码
     * @param player
     * @param type
     * @return
     */
    private int getItemChangeReasonGet(Player player,int type){

        if(type == KaoShangLingManager.EnKaoShangLingHorse){
            return ItemChangeReason.KaoShangLingHorseGet;
        }
        return 0;
    }
    @Override
    public void reqKaoShangLingRewardHandler( Player player,int type,int isOneKey,int key,int isSendMail) {

        KaoShangLingBean kaoShangLingBean = player.getKaoShangLingData().getKaoShangLingMap().get(type);
        if(kaoShangLingBean == null){
            return;
        }
        List<Integer> commonRewardList = kaoShangLingBean.getCommonRewardList();
        List<Integer> specailRewardist = kaoShangLingBean.getSpecailRewardist();
        int kaoShangLingScoreTotal = getKaoShangLingScoreTotal(player,type);
        List<Item> allRewardItems = new ArrayList<>();
        //是否一键领取
        if(isOneKey == 1){
            //坐骑犒赏令
            if(type == KaoShangLingManager.EnKaoShangLingHorse){
                for (Cfg_KaoShangLing_Horse_Bean Cfg_kaoShangLingBean :  CfgManager.getCfg_KaoShangLing_Horse_Container().getValuees()) {
                    this.calcKaoShangLingReward(player,allRewardItems,kaoShangLingBean,kaoShangLingScoreTotal,Cfg_kaoShangLingBean);
                }
            }
        }else {
            if(type == KaoShangLingManager.EnKaoShangLingHorse){
                Cfg_KaoShangLing_Horse_Bean Cfg_KaoShangLing_Horse_Bean = CfgManager.getCfg_KaoShangLing_Horse_Container().getValueByKey(key);
                this.calcKaoShangLingReward(player,allRewardItems,kaoShangLingBean,kaoShangLingScoreTotal,Cfg_KaoShangLing_Horse_Bean);
            }
        }
        //奖励
        if(allRewardItems.size()>0){
            //直接发邮件 不进入背包
            if(isSendMail == 1){
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.KaoShangLingHorseMailTitle, MessageString.KaoShangLingHorseMailContent, allRewardItems, getItemChangeReasonGet(player,type));
            }else
            {
                if (!Manager.backpackManager.manager().addItems(player, allRewardItems, getItemChangeReasonGet(player,type), IDConfigUtil.getLogId())) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                            MessageString.System, MessageString.NoBagCell, allRewardItems, getItemChangeReasonGet(player,type));
                }
                //领取奖励
                KaoShangLingMessage.ResKaoShangLingReward.Builder resKaoShangLingReward =   KaoShangLingMessage.ResKaoShangLingReward.newBuilder();
                resKaoShangLingReward.setType(type);
                resKaoShangLingReward.addAllCommonRewardList(commonRewardList);
                resKaoShangLingReward.addAllSpecailRewardist(specailRewardist);
                MessageUtils.send_to_player(player, KaoShangLingMessage.ResKaoShangLingReward.MsgID.eMsgID_VALUE, resKaoShangLingReward.build().toByteArray());

            }


        }

    }
}


