package common.openserver;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_VipHelp_Bean;
import com.data.bean.Cfg_Vip_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.openserverac.scripts.IV4HelpScript;
import com.game.openserverac.structs.V4HelpBean;
import com.game.openserverac.structs.V4HelpRecordLog;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerRedPacket;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.OpenServerAcMessage;
import org.apache.ibatis.jdbc.Null;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class V4HelpScript implements IV4HelpScript {
    private static final Logger log = LogManager.getLogger(V4HelpScript.class);

    @Override
    public int getId() {
        return ScriptEnum.V4HelpScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
    final int ActivityStateClose = 1;



    @Override
    public void zeroClockDeal() {

        int openServerDay = TimeUtils.getOpenServerDay();

        if(openServerDay>14){
            return;
        }
        //活动结束
       // if(this.isClose()){
            for(long playerId : Manager.v4HelpManager.getV4HelpBeanMap().keySet()){
                V4HelpBean v4HelpBean = Manager.v4HelpManager.getV4HelpBeanMap().get(playerId);
                //投资时间
                long helpTime = v4HelpBean.getHelpTime();
                //投资时候对应开服时间
                int helpOpenServerDay = TimeUtils.getOpenServerDay(helpTime*1000);
                //当前开服时间
                int currOpenServerDay = TimeUtils.getOpenServerDay();
                int day = currOpenServerDay - helpOpenServerDay;
                for(int i = 1;i<=day;i++){
                    Cfg_VipHelp_Bean[] vfg_VipHelp_Bean_list = CfgManager.getCfg_VipHelp_Container().getValuees();
                    for(int m = 0;m<vfg_VipHelp_Bean_list.length;m++){
                        Cfg_VipHelp_Bean  Cfg_VipHelp = vfg_VipHelp_Bean_list[m];
                        //判断领取投资人的奖励
                        if(Cfg_VipHelp.getHelpType() == 0 && Cfg_VipHelp.getDay() == i){
                            //判断没有领取发送邮件
                            if(!v4HelpBean.getHelperAwardState().contains(Cfg_VipHelp.getId())){
                                //发送邮件
                                sendZeroClockRewardMail(v4HelpBean.getHelperPlayerId(),Cfg_VipHelp,MessageString.C_V4_HELP_NO_GET_TOUZI_MAIL+ "@_@" + i);

                                v4HelpBean.getHelperAwardState().add(Cfg_VipHelp.getId());
                            }
                        }
                        //判断被投资人 领取奖励
                        if(Cfg_VipHelp.getHelpType() == 1 && Cfg_VipHelp.getDay() == i){
                            //判断没有领取发送邮件
                            if(!v4HelpBean.getBeHelperAwardState().contains(Cfg_VipHelp.getId())){
                                //发送邮件
                                sendZeroClockRewardMail(v4HelpBean.getBeHelperPlayerId(),Cfg_VipHelp,MessageString.C_V4_HELP_NO_GET_SHENQING_MAIL+ "@_@" + i);
                                v4HelpBean.getBeHelperAwardState().add(Cfg_VipHelp.getId());
                            }
                        }
                    }
                }

            }
       // }
    }




    public void sendZeroClockRewardMail(long playerId,Cfg_VipHelp_Bean  cfg_VipHelp_Bean,  Object mailContent){

        Player player = Manager.playerManager.getPlayer(playerId);
        if (player == null) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> itemList = Item.createItems(player.getCareer(),Item.randomNumAward(cfg_VipHelp_Bean.getHelpReward()),1);
        if (itemList.size() > 0) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.C_V4_HELP_NO_GET_MAIL_TITTLE
                    , mailContent, itemList,  ItemChangeReason.V4GetAward1Get, actionId);
        }
        //同步数据
        if (player.isOnline()) {
            sendResV4HelpInfos(player);
        }
    }
    /**
     * 活动检测
     */
    @Override
    public void tick() {

        long state = Manager.countManager.getServerVariant(VariantType.V4HelpScript);
        if (state == ActivityStateClose) {
            return;
        }
        if (isClose()) {
            /**
             * 活动结束
             */



            Manager.countManager.setServerVariant(VariantType.V4HelpScript, ActivityStateClose);
            doActivityEnd();
        }
    }

    boolean isClose() {
        int openServerDay = TimeUtils.getOpenServerDay();
        return openServerDay > Global.V4_Help_Day_Count;
    }

    /**
     * 活动结束 处理接口
     */
    @Override
    public void doActivityEnd() {
        ConcurrentHashMap<Long, Long> helpApplyMap = Manager.v4HelpManager.getV4HelpApplyMap();
        if(helpApplyMap!=null){
            for(long playerId : helpApplyMap.keySet()){
                log.info("doActivityEnd  playerId {}",playerId);
               Player player =   Manager.playerManager.getPlayerOnline(playerId);
               if(player!=null){
                   activityEndAddVip(player,helpApplyMap);
               }
            }
        }

//        for (Player player : Manager.playerManager.getPlayersCache().values()) {
//             if(player.isOnline()){
//                 //直接升到vip4
//                 if(helpApplyMap.containsKey(player.getId())){
//
//                 }
//
//             }
//        }
    }

    /**
     * 活动结束 申请的人 获得vip4
     */
    private void activityEndAddVip(Player player,ConcurrentHashMap<Long, Long> helpApplyMap){
      // Cfg_Vip_Bean cfg_Vip_Bean = CfgManager.getCfg_Vip_Container().getValueByIndex(4);
      // long vipExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.VipExp);
      // if(cfg_Vip_Bean.getVipLevelUp() > vipExp){
      //     Manager.vipManager.deal().addVipExp(player, (cfg_Vip_Bean.getVipLevelUp()  - (int)vipExp), ItemChangeReason.GM, IDConfigUtil.getLogId());
      // }
      // long newVipExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.VipExp);
      // player.setVipLv(Manager.vipManager.deal().getVipLvByExp(newVipExp));
      // helpApplyMap.remove(player.getId());


      //Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.C_VIP_HELP_MailTittle
      //       , MessageString.C_VIP_HELP_MailContent  );
        autoHelpOther(player);
        helpApplyMap.remove(player.getId());
    }
    @Override
    public void playerOnline(Player player) {

        if(!Manager.v4HelpManager.getV4HelpBeanMap().containsKey(player.getV4HelpData().getV4HelpPlayerId())){
            player.getV4HelpData().setV4HelpPlayerId(0);
        }

        if(isClose()){
            ConcurrentHashMap<Long, Long> helpApplyMap = Manager.v4HelpManager.getV4HelpApplyMap();
            if(helpApplyMap!=null){
                if(helpApplyMap.containsKey(player.getId())){
                    activityEndAddVip(player,helpApplyMap);
                }
            }
        }

        this.sendResV4HelpInfos(player);
    }
    @Override
    public void ReqV4HelpBeApply(Player player, OpenServerAcMessage.ReqV4HelpBeApply msg) {
        if (TimeUtils.getOpenServerDay() > Global.V4_Help_Day_Count) {
            return;
        }
        //判断vip等级
        int minVipLevel = Global.V4_Help_ShenQing_NeedVipLevel.get(0);
        int maxVipLevel = Global.V4_Help_ShenQing_NeedVipLevel.get(1);
        if(player.getVipLv() < minVipLevel || player.getVipLv()> maxVipLevel){
            return;
        }
        //已经被投资了
        if(Manager.v4HelpManager.getV4HelpBeanMap().containsKey(player.getId())){
            return;
        }

        //判断是否已经 成为申请人了
        if(Manager.v4HelpManager.getV4HelpApplyMap().containsKey(player.getId())){
            return;
        }
//        V4HelpBean v4ApplyPlayer = new V4HelpBean();
//        v4ApplyPlayer.setBeHelperPlayerId(player.getId());
        Manager.v4HelpManager.getV4HelpApplyMap().put(player.getId(),player.getId());
        //保存数据
        ServerParamUtil.saveV4HelpApplyMap();

        MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.C_V4HELP_SHENQING_SUCC);

        //MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);



        this.sendResV4HelpInfos(player);
    }

    @Override
    public void ReqV4HelpInfo(Player player, OpenServerAcMessage.ReqV4HelpInfo msg) {
        this.sendResV4HelpInfos(player);
    }

    @Override
    public void ReqV4HelpOther(Player player, OpenServerAcMessage.ReqV4HelpOther msg) {
        if (TimeUtils.getOpenServerDay() > Global.V4_Help_Day_Count) {
            return;
        }
        //判断vip等级
        if(player.getVipLv() < Global.V4_Help_TouZi_NeedVipLevel){
            return;
        }
        //自己不能投资自己
        if(player.getId() == msg.getPlayerId()){
            return;
        }

        //没有申请列表
        if(Manager.v4HelpManager.getV4HelpApplyMap()== null && Manager.v4HelpManager.getV4HelpApplyMap().size()<=0){
            return;
        }
        //判断投资玩家id是否存在申请列表
        if(!Manager.v4HelpManager.getV4HelpApplyMap().containsKey(msg.getPlayerId())){
            return;
        }
        if(Manager.v4HelpManager.getV4HelpBeanMap().containsKey(msg.getPlayerId())){
            //目标玩家 已经被其他玩家投资了
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_V4HELP_TOUZI_FAILED_ALREADY);
            return;
        }
        /**
         * 判断是否已经 投资了 其他玩家
         */
        for(V4HelpBean v4ApplyPlayer : Manager.v4HelpManager.getV4HelpBeanMap().values()){
            if(v4ApplyPlayer.getHelperPlayerId() == player.getId()){
                return;
            }
        }

        if (!Manager.currencyManager.manager().canDecItemCoin(player, Global.V4_Help_Need_Item.get(1), Global.V4_Help_Need_Item.get(0))) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        Manager.currencyManager.manager().onDecItemCoin(player, Global.V4_Help_Need_Item.get(1), ItemChangeReason.V4HelpOtherCost, actionId, Global.V4_Help_Need_Item.get(0));


        //存储目标信息
        V4HelpBean v4otherPlayer = new V4HelpBean();
        v4otherPlayer.setBeHelperPlayerId(msg.getPlayerId());
        v4otherPlayer.setHelperPlayerId(player.getId());


        v4otherPlayer.setHelpTime((int)(TimeUtils.Time()/1000));
        Manager.v4HelpManager.getV4HelpBeanMap().put(msg.getPlayerId(),v4otherPlayer);
        Manager.v4HelpManager.getV4HelpApplyMap().remove(msg.getPlayerId());
        MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.C_V4HELP_TOUZI_SUCC);


        player.getV4HelpData().setV4HelpPlayerId(msg.getPlayerId());
        //记录日志
        List<V4HelpRecordLog> V4HelpRecordLogList = Manager.v4HelpManager.getV4HelpRecordLog();
        if(V4HelpRecordLogList.size()>50){
            V4HelpRecordLogList.remove(0);
        }
        V4HelpRecordLog log = new V4HelpRecordLog();
        log.setHelpPlayerId(player.getId());
        log.setBeHelpPlayerId(msg.getPlayerId());
        V4HelpRecordLogList.add(log);
        //保存数据
        ServerParamUtil.saveV4HelpApplyMap();
        ServerParamUtil.saveV4HelpBeanMap();
        ServerParamUtil.saveV4HelpRecordLog();

        //V4HelpBean v4ApplyPlayer = Manager.v4HelpManager.getV4HelpBeanMap().get(msg.getPlayerId());

        this.sendResV4HelpInfos(player);
        //通知其他玩家
        Player ohterPlayer = PlayerManager.getInstance().getPlayerOnline(msg.getPlayerId());
        if(ohterPlayer!=null){
            this.sendResV4HelpInfos(ohterPlayer);
        }
        //发送邮件

    }
    private void autoHelpOther(Player player){
        //存储目标信息
        V4HelpBean v4otherPlayer = new V4HelpBean();
        v4otherPlayer.setBeHelperPlayerId(player.getId());
        v4otherPlayer.setHelperPlayerId(0);

        v4otherPlayer.setHelpTime((int)(TimeUtils.Time()/1000));
        Manager.v4HelpManager.getV4HelpBeanMap().put(player.getId(),v4otherPlayer);
        Manager.v4HelpManager.getV4HelpApplyMap().remove(player.getId());


        player.getV4HelpData().setV4HelpPlayerId(0);
        log.info("autoHelpOther  {}",player.getId());
        //记录日志
        List<V4HelpRecordLog> V4HelpRecordLogList = Manager.v4HelpManager.getV4HelpRecordLog();
        if(V4HelpRecordLogList.size()>50){
            V4HelpRecordLogList.remove(0);
        }
        V4HelpRecordLog log = new V4HelpRecordLog();
        log.setHelpPlayerId(0);
        log.setBeHelpPlayerId(player.getId());
        V4HelpRecordLogList.add(log);
        //保存数据
        ServerParamUtil.saveV4HelpApplyMap();
        ServerParamUtil.saveV4HelpBeanMap();
        ServerParamUtil.saveV4HelpRecordLog();

        //V4HelpBean v4ApplyPlayer = Manager.v4HelpManager.getV4HelpBeanMap().get(msg.getPlayerId());

        //通知其他玩家
        this.sendResV4HelpInfos(player);
    }

    @Override
    public void ReqV4GetAward(Player player, OpenServerAcMessage.ReqV4GetAward msg) {
        Cfg_VipHelp_Bean vfg_VipHelp_Bean = CfgManager.getCfg_VipHelp_Container().getValueByKey(msg.getAwardId());
        if(vfg_VipHelp_Bean == null){
            return;
        }
        //领取投资的奖励
        if(vfg_VipHelp_Bean.getHelpType() == 0){
            //判断是否投资了
            if(!Manager.v4HelpManager.getV4HelpBeanMap().containsKey(player.getV4HelpData().getV4HelpPlayerId())){
                return;
            }
            V4HelpBean v4HelpBean = Manager.v4HelpManager.getV4HelpBeanMap().get(player.getV4HelpData().getV4HelpPlayerId());
            //已经领取奖励
            if( v4HelpBean.getHelperAwardState().contains(msg.getAwardId())){
                return;
            }
            List<Item> itemList = Item.createItems(player.getCareer(),Item.randomNumAward(vfg_VipHelp_Bean.getHelpReward() ),1);
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.V4GetAwardGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.V4GetAwardGet);
            }
            v4HelpBean.getHelperAwardState().add(msg.getAwardId());
        }

        //领取被投资的奖励
        if(vfg_VipHelp_Bean.getHelpType() == 1){
            //判断是否被投资了
            if(!Manager.v4HelpManager.getV4HelpBeanMap().containsKey(player.getId())){
                return;
            }
            V4HelpBean v4HelpBean = Manager.v4HelpManager.getV4HelpBeanMap().get(player.getId());
            //已经领取奖励
            if( v4HelpBean.getBeHelperAwardState().contains(msg.getAwardId())){
                return;
            }
            List<Item> itemList = Item.createItems(player.getCareer(),Item.randomNumAward(vfg_VipHelp_Bean.getHelpReward()),1);
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.V4GetAward1Get, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.V4GetAward1Get);
            }
            v4HelpBean.getBeHelperAwardState().add(msg.getAwardId());

        }

        ServerParamUtil.saveV4HelpBeanMap();
        this.sendResV4HelpInfos(player);
    }

    private void sendResV4HelpInfos(Player player){
        OpenServerAcMessage.ResV4HelpInfos.Builder resV4HelpInfosBuilder = OpenServerAcMessage.ResV4HelpInfos.newBuilder();

        //申请列表
        if(Manager.v4HelpManager.getV4HelpApplyMap()!=null && Manager.v4HelpManager.getV4HelpApplyMap().size()>0){
            for(long playerId : Manager.v4HelpManager.getV4HelpApplyMap().keySet()){
                PlayerWorldInfo playerWorldInfo = PlayerManager.getInstance().getPlayerWorldInfo(playerId);
                if(playerWorldInfo!=null){
                    resV4HelpInfosBuilder.addApplyList(this.getV4ApplyPlayer(playerWorldInfo));
                }
            }
        }

        //投资我的人
        if(Manager.v4HelpManager.getV4HelpBeanMap().containsKey(player.getId())){
            //自己的数据
            V4HelpBean v4ApplyPlayer = Manager.v4HelpManager.getV4HelpBeanMap().get(player.getId());
            PlayerWorldInfo helperPlayerWorldInfo = PlayerManager.getInstance().getPlayerWorldInfo(v4ApplyPlayer.getHelperPlayerId());
            OpenServerAcMessage.V4HelpPlayer.Builder v4HelpPlayerBuilder = getV4HelpPlayer(helperPlayerWorldInfo);
            v4HelpPlayerBuilder.setHelpTime(v4ApplyPlayer.getHelpTime());

            if(v4ApplyPlayer.getBeHelperAwardState() !=null && v4ApplyPlayer.getBeHelperAwardState().size()>0){
                //投资人的奖励
                v4HelpPlayerBuilder.addAllAwardState(v4ApplyPlayer.getBeHelperAwardState());
            }
            resV4HelpInfosBuilder.setMyHelper(v4HelpPlayerBuilder);
        }
        //我投资的人
        if(Manager.v4HelpManager.getV4HelpBeanMap().containsKey(player.getV4HelpData().getV4HelpPlayerId())){
            V4HelpBean beHelpV4ApplyPlayer = Manager.v4HelpManager.getV4HelpBeanMap().get(player.getV4HelpData().getV4HelpPlayerId());
            PlayerWorldInfo beHelperPlayerWorldInfo = PlayerManager.getInstance().getPlayerWorldInfo(beHelpV4ApplyPlayer.getBeHelperPlayerId());
            if(beHelperPlayerWorldInfo!=null){
                OpenServerAcMessage.V4HelpPlayer.Builder  beHelpPlayerBuilder = getV4HelpPlayer(beHelperPlayerWorldInfo);
                beHelpPlayerBuilder.setHelpTime(beHelpV4ApplyPlayer.getHelpTime());
                if(beHelpV4ApplyPlayer.getHelperAwardState() !=null && beHelpV4ApplyPlayer.getHelperAwardState().size()>0){
                    //投资人的奖励
                    beHelpPlayerBuilder.addAllAwardState(beHelpV4ApplyPlayer.getHelperAwardState());
                }
                resV4HelpInfosBuilder.setHelpOther(beHelpPlayerBuilder);
            }
        }

        //发送日志
        if(Manager.v4HelpManager.getV4HelpRecordLog() != null && Manager.v4HelpManager.getV4HelpRecordLog().size()>0){
            for(int i = 0;i<Manager.v4HelpManager.getV4HelpRecordLog().size();i++){
                V4HelpRecordLog V4HelpRecordLog = Manager.v4HelpManager.getV4HelpRecordLog().get(i);
                OpenServerAcMessage.V4HelpRecord.Builder  V4HelpRecordV4HelpRecord =  OpenServerAcMessage.V4HelpRecord.newBuilder();
                PlayerWorldInfo helpPlayerPlayerWorldInfo = PlayerManager.getInstance().getPlayerWorldInfo(V4HelpRecordLog.getHelpPlayerId());
                if(helpPlayerPlayerWorldInfo!=null){
                    V4HelpRecordV4HelpRecord.setHelper(helpPlayerPlayerWorldInfo.getRolename());
                }
                PlayerWorldInfo beHelpPlayerPlayerWorldInfo = PlayerManager.getInstance().getPlayerWorldInfo(V4HelpRecordLog.getBeHelpPlayerId());
                if(beHelpPlayerPlayerWorldInfo!=null){
                    V4HelpRecordV4HelpRecord.setBeHelper(beHelpPlayerPlayerWorldInfo.getRolename());
                }
                resV4HelpInfosBuilder.addRecords(V4HelpRecordV4HelpRecord);
            }
        }

        MessageUtils.send_to_player(player,  OpenServerAcMessage.ResV4HelpInfos.MsgID.eMsgID_VALUE, resV4HelpInfosBuilder.build().toByteArray());
        //OpenServerAcMessage.V4ApplyPlayer.Builder v4ApplyPlayerListBuilder =  OpenServerAcMessage.V4ApplyPlayer.newBuilder();
    }

    private OpenServerAcMessage.V4HelpPlayer.Builder getV4HelpPlayer(PlayerWorldInfo playerWorldInfo){
        OpenServerAcMessage.V4HelpPlayer.Builder v4HelpPlayer = OpenServerAcMessage.V4HelpPlayer.newBuilder();
        v4HelpPlayer.setPlayer(this.getV4ApplyPlayer(playerWorldInfo));


        return v4HelpPlayer;
    }


    private OpenServerAcMessage.V4ApplyPlayer.Builder getV4ApplyPlayer(PlayerWorldInfo playerWorldInfo){
        OpenServerAcMessage.V4ApplyPlayer.Builder v4ApplyPlayerBuilder = OpenServerAcMessage.V4ApplyPlayer.newBuilder();
        if (playerWorldInfo == null){
            CommonMessage.HeadAttribute.Builder msg = CommonMessage.HeadAttribute.newBuilder();
            msg.setFashionHead(0);
            msg.setFashionFrame(0);
            msg.setCustomHeadPath("");
            msg.setUseCustomHead(false);
            v4ApplyPlayerBuilder.setHead(msg);
            v4ApplyPlayerBuilder.setId(0);
            v4ApplyPlayerBuilder.setLevel(0);
            v4ApplyPlayerBuilder.setName("");
            v4ApplyPlayerBuilder.setOcc(0);
        }else {
            v4ApplyPlayerBuilder.setHead(MapUtils.getHead(playerWorldInfo));
            v4ApplyPlayerBuilder.setId(playerWorldInfo.getRoleid());
            v4ApplyPlayerBuilder.setLevel(playerWorldInfo.getLevel());
            v4ApplyPlayerBuilder.setName(playerWorldInfo.getRolename());
            v4ApplyPlayerBuilder.setOcc(playerWorldInfo.getCareer());
        }


        return v4ApplyPlayerBuilder;
    }



}
