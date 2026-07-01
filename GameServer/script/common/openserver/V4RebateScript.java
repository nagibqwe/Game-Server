package common.openserver;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_VipRebate_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.manager.Manager;
import com.game.openserverac.scripts.IV4Rebate;
import com.game.openserverac.structs.VIP4GrouUpData;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * V4返利
 * cxl
 */
public class V4RebateScript implements IV4Rebate{


    private static final Logger log = LogManager.getLogger(V4RebateScript.class);
    @Override
    public int getId() {
        return ScriptEnum.V4RebateScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    /**
     * 上线初始化
     * @param player
     */
    public void onLineInit(Player player){
        OpenServerAcMessage.ResV4RebateInfo.Builder msg =  OpenServerAcMessage.ResV4RebateInfo.newBuilder();
        msg.setCurState(player.getVip4Rebate().getCurState());
        msg.addAllRewardState(player.getVip4Rebate().getRewardStates());
        OpenServerAcMessage.V4Rebate.Builder builder;
        for (VIP4GrouUpData grouUpData :player.getVip4Rebate().getVip4GrouUpDatas().values()){
            builder = OpenServerAcMessage.V4Rebate.newBuilder();
            builder.setId(grouUpData.getId());
            builder.setProgress(grouUpData.getProgress());
            builder.setIsComplete(grouUpData.isComplete());
            msg.addV4Rebates(builder.build());
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResV4RebateInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    @Override
    public void onReqV4RebateCompleteTask(Player player, int id) {
        if (player.getVipLv() < Global.VipRebate_NeedVipLevel){
            return;
        }
        if (!player.getVip4Rebate().getVip4GrouUpDatas().containsKey(id)){
            log.error("not find  vipid  === {} ",id);
            return;
        }

        VIP4GrouUpData vip4GrouUpData =  player.getVip4Rebate().getVip4GrouUpDatas().get(id);
        if (vip4GrouUpData.isComplete()){
            log.error("已经完成  === {} ",id);
            return;
        }
        vip4GrouUpData.setComplete(true);

        int allCount = 0;
        int completeCount = 0;
        for (Cfg_VipRebate_Bean bean:CfgManager.getCfg_VipRebate_Container().getValuees()){
            if (player.getVip4Rebate().getCurState()  != bean.getStageType()){
                continue;
            }
            allCount++;
            if (!player.getVip4Rebate().getVip4GrouUpDatas().containsKey(bean.getId())){
                break;
            }
            if (player.getVip4Rebate().getVip4GrouUpDatas().get(bean.getId()).isComplete()){
                completeCount++;
            }
        }
        OpenServerAcMessage.ResV4RebateUpDate.Builder msg = OpenServerAcMessage.ResV4RebateUpDate.newBuilder();
        OpenServerAcMessage.V4Rebate.Builder builder = OpenServerAcMessage.V4Rebate.newBuilder();
        if (completeCount>=allCount){
            //这里要做升级阶段处理
            player.getVip4Rebate().setCurState( player.getVip4Rebate().getCurState()+1);
            List<OpenServerAcMessage.V4Rebate> upDataList = new ArrayList<>();
            upV4RebateState(player,upDataList);
            msg.addAllV4Rebates(upDataList);
        }
        msg.setCurState(player.getVip4Rebate().getCurState());
        builder.setIsComplete(vip4GrouUpData.isComplete());
        builder.setProgress(vip4GrouUpData.getProgress());
        builder.setId(vip4GrouUpData.getId());
        msg.addV4Rebates(builder.build());
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResV4RebateUpDate.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void upV4RebateState(Player player, List<OpenServerAcMessage.V4Rebate> upDataList){
        for (Cfg_VipRebate_Bean bean:CfgManager.getCfg_VipRebate_Container().getValuees()) {
            if (player.getVip4Rebate().getCurState() != bean.getStageType()) {
                continue;
            }
            VIP4GrouUpData newGroupData = new VIP4GrouUpData();
            int progress = getProgress(player,bean.getVariableId());
            newGroupData.setProgress(progress);
            if (bean.getTaskInherit() > 0){
                VIP4GrouUpData vip4GrouUpData = player.getVip4Rebate().getVip4GrouUpDatas().get(bean.getTaskInherit());
                newGroupData.setProgress(vip4GrouUpData.getProgress());
            }

           //VIP4GrouUpData vip4GrouUpData = player.getVip4Rebate().getVip4GrouUpDatas().get(bean.getTaskInherit());
           //VIP4GrouUpData newGroupData = new VIP4GrouUpData();
            //newGroupData.setProgress(vip4GrouUpData.getProgress());
            newGroupData.setComplete(false);
            newGroupData.setId(bean.getId());
            player.getVip4Rebate().getVip4GrouUpDatas().put(bean.getId(),newGroupData);

            OpenServerAcMessage.V4Rebate.Builder builder = OpenServerAcMessage.V4Rebate.newBuilder();
            builder.setIsComplete(newGroupData.isComplete());
            builder.setProgress(newGroupData.getProgress());
            builder.setId(newGroupData.getId());
            upDataList.add(builder.build());
        }
    }

    @Override
    public void onReqV4RebeteReward(Player player, int rewardState) {

        if (player.getVipLv() < Global.VipRebate_NeedVipLevel){
            return;
        }
        if (rewardState < 0){
            log.error("rewardState  === {} ",rewardState);
            return;
        }
        if (rewardState > player.getVip4Rebate().getCurState()-1){
            log.error("领取阶段奖励  rewardState {} >  CurState {}",rewardState,player.getVip4Rebate().getCurState());
            return;
        }
        if ( player.getVip4Rebate().getRewardStates().contains(rewardState)){
            log.error("不能重复领取  rewardState {}",rewardState);
            return;
        }
        ReadArray<Integer> readArray =  Global.VipRebate_Reward.get(rewardState-1);
        Manager.currencyManager.manager().onAddItemCoin(player,readArray.get(0),readArray.get(1), ItemChangeReason.V4RebateGet, IDConfigUtil.getLogId());
        player.getVip4Rebate().getRewardStates().add(rewardState);

        OpenServerAcMessage.ResV4RebateRewardResult.Builder msg = OpenServerAcMessage.ResV4RebateRewardResult.newBuilder();
        msg.addAllRewardState(player.getVip4Rebate().getRewardStates());
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResV4RebateRewardResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private boolean isOpen(){
        int endDay   =  Global.VipRebate_Day_Count.get(1);
        if (  TimeUtils.getOpenServerDay() > endDay){
            return false;
        }
        return true;
    }
    @Override
    public void onReqV4RebeteUpProgress(Player player, int type,int count) {
        if (player.getVip4Rebate().getCurState() > Global.VipRebate_Max_Round){
            return;
        }
        if (!isOpen()){
            return;
        }
        OpenServerAcMessage.ResV4RebateUpDate.Builder msg = OpenServerAcMessage.ResV4RebateUpDate.newBuilder();
        OpenServerAcMessage.V4Rebate.Builder builder;
        for (Cfg_VipRebate_Bean bean :  CfgManager.getCfg_VipRebate_Container().getValuees()){
            if (bean.getStageType() != player.getVip4Rebate().getCurState()){
                continue;
            }
            if (bean.getVariableId().get(0) != type){
                continue;
            }
            VIP4GrouUpData vip4GrouUpData;
            if (!player.getVip4Rebate().getVip4GrouUpDatas().containsKey(bean.getId())){
                vip4GrouUpData = new VIP4GrouUpData();
                vip4GrouUpData.setId(bean.getId());
                vip4GrouUpData.setComplete(false);
                vip4GrouUpData.setProgress(0);
                player.getVip4Rebate().getVip4GrouUpDatas().put(bean.getId(),vip4GrouUpData);
            }else {
                vip4GrouUpData =  player.getVip4Rebate().getVip4GrouUpDatas().get(bean.getId());
            }

            int progress = getProgress(player,bean.getVariableId());
            int oldprogress = vip4GrouUpData.getProgress();
            if (oldprogress >= progress){
                return;//进度没变化 不进行刷新
            }
            vip4GrouUpData.setProgress(progress);
            builder =  OpenServerAcMessage.V4Rebate.newBuilder();
            builder.setProgress(vip4GrouUpData.getProgress());
            builder.setId(vip4GrouUpData.getId());
            builder.setIsComplete(vip4GrouUpData.isComplete());
            msg.addV4Rebates(builder.build());
        }
        if (msg.getV4RebatesList().size()<=0){
            return;
        }
        msg.setCurState(player.getVip4Rebate().getCurState());
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResV4RebateUpDate.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    private int getProgress(Player player, ReadArray<Integer> param) {
        return Manager.controlManager.deal().getFuncProgress(player, param);
    }
}
