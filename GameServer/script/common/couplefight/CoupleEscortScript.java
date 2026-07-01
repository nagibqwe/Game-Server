package common.couplefight;


import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_ConvoyGirl_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.couplefight.scripts.ICoupleEscort;
import com.game.couplefight.struct.CoupleEscortData;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * cxl
 * 仙女护送
 */
public class CoupleEscortScript implements ICoupleEscort {



    final Logger log = LogManager.getLogger(CoupleEscortScript.class);



    @Override
    public int getId() {
        return ScriptEnum.CoupleEscortScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    @Override
    public void onChangeCoupleEscortState(boolean isStart) {

        log.info("onChangeCoupleEscortState  {} ",isStart);
        Manager.couplefightManager.setStart(isStart);
    }

    @Override
    public void onReqEnterCoupleEscort(Player player, int type) {

        if (!Manager.couplefightManager.isStart() ){
            sendCoupleEscortResult(player,0,type);
            log.error("活动未开启");
            return;
        }
        if( Manager.couplefightManager.getCoupleEscortDatas().containsKey(player.getId())) {
            sendCoupleEscortResult(player,0,type);
            log.error("护送中");
            return;
        }

        Cfg_ConvoyGirl_Bean bean = CfgManager.getCfg_ConvoyGirl_Container().getValueByKey(type);
        if (bean == null){
            log.error("Cfg_ConvoyGirl_Bean == null {}",type);
            return;
        }
        if (bean.getUseItem()!=null && bean.getUseItem().size()>0){
           int num =  Manager.backpackManager.manager().getItemNum(player,bean.getUseItem().get(0));
           if (num < bean.getUseItem().get(1)){
               log.error("所需道具不足  {} ",bean.getUseItem().get(0));
               return;
           }
        }


        CoupleEscortData coupleEscortData = new CoupleEscortData();
        coupleEscortData.setRoleId(player.getId());
        coupleEscortData.setStartTime(TimeUtils.Time());
        coupleEscortData.setType(type);
        Manager.couplefightManager.getCoupleEscortDatas().put(player.getId(),coupleEscortData);

        sendCoupleEscortResult(player,1,type);
    }

    private void sendCoupleEscortResult(Player player,int result,int type){
        CouplefightMessage.ResEnterCoupleEscortResult.Builder msg =
                CouplefightMessage.ResEnterCoupleEscortResult.newBuilder();
        msg.setResult(result);
        msg.setType(type);
        MessageUtils.send_to_player(player, CouplefightMessage.ResEnterCoupleEscortResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    @Override
    public void onReqCoupleEscortOver(Player player) {
        if( !Manager.couplefightManager.getCoupleEscortDatas().containsKey(player.getId())) {
            log.error("掉线或离线");
            return;
        }

        long nowTime = TimeUtils.Time();
        CoupleEscortData coupleEscortData =  Manager.couplefightManager.getCoupleEscortDatas().get(player.getId());
        coupleEscortData.getStartTime();

        Cfg_ConvoyGirl_Bean bean = CfgManager.getCfg_ConvoyGirl_Container().getValueByKey(coupleEscortData.getType());
        if (bean == null){
            log.error("Cfg_ConvoyGirl_Bean == null {}",coupleEscortData.getType());
            Manager.couplefightManager.getCoupleEscortDatas().remove(player.getId());
            return;
        }
        int useTime = (int) ((nowTime - coupleEscortData.getStartTime())/1000);
        if (useTime < bean.getConvoyTime()-5){//误差5秒
            log.error("护送时间没到 useTime  {}  bean.getConvoyTime() {}",useTime,bean.getConvoyTime());
            return;
        }
        if (bean.getUseItem()!=null && bean.getUseItem().size()>0){
            long logId = IDConfigUtil.getLogId();
            boolean isDesc = Manager.backpackManager.manager().onRemoveItem(player,bean.getUseItem().get(0),
                    bean.getUseItem().get(1), ItemChangeReason.CoupleEscortCost,logId);
            if (!isDesc){
                log.error("所需道具不足  {} ",bean.getUseItem().get(0));
                Manager.couplefightManager.getCoupleEscortDatas().remove(player.getId());
                return;
            }
        }

        //扣次数
        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.COUPLE_ESCORT, 1);

        //TODO 某个时间段经验翻倍
        int curMin  = TimeUtils.getDayOfHour(nowTime) * 60 + TimeUtils.getDayOfMin(nowTime);
        int ber = 1;
        for (ReadArray<Integer>readArray : Global.ConvoyDoubleExpTime.getValuees()){
            if (curMin >= readArray.get(0) && curMin <= readArray.get(1)){
                ber = 2;
                break;
            }
        }

        List<Item> items = Item.createItems(player.getCareer(), bean.getReward_ID(), ber);
        long logid = IDConfigUtil.getLogId();
        Manager.backpackManager.manager().addItems(player,items,ItemChangeReason.CoupleEscortGet,logid);

        Cfg_Characters_Bean characters_bean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());


        long addexp = 0;
        for (ReadArray<Long> readArray : characters_bean.getConvoy_Exp().getValuees()){
            if (readArray.get(0) != coupleEscortData.getType()){
                continue;
            }
            addexp = readArray.get(1)*ber;
            Manager.currencyManager.manager().addEXP(player,addexp,ItemChangeReason.CoupleEscortGet,logid);
            break;
        }
        Manager.couplefightManager.getCoupleEscortDatas().remove(player.getId());

        CouplefightMessage.ResCoupleEscortReward.Builder msg = CouplefightMessage.ResCoupleEscortReward.newBuilder();
        CouplefightMessage.EscortReward.Builder builder ;
        builder =  CouplefightMessage.EscortReward.newBuilder();
        builder.setId(ItemCoinType.EXP);
        builder.setNum(addexp);
        msg.addRewards(builder.build());
        for (ReadArray<Integer> readArray :  bean.getReward_ID().getValuees()){
            if (readArray.get(3) !=9 && readArray.get(3) !=player.getCareer()){
                continue;
            }
            builder =  CouplefightMessage.EscortReward.newBuilder();
            builder.setId(readArray.get(0));
            builder.setNum((long)readArray.get(1) * ber);
            msg.addRewards(builder.build());
        }
        MessageUtils.send_to_player(player, CouplefightMessage.ResCoupleEscortReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.controlManager.operate(player, FunctionVariable.Join_Convoy_Girl_Num, 1);

        Manager.retrieveResManager.getScript().count(player, RetrieveType.CoupeEscort);

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.CoupleEscort, 0, "", ItemChangeReason.CoupleEscortGet
                , bean.getId(), "", 0, ber);
    }

    @Override
    public void onLeaveGame(Player player) {
        if( Manager.couplefightManager.getCoupleEscortDatas().containsKey(player.getId())) {
            Manager.couplefightManager.getCoupleEscortDatas().remove(player.getId());
        }
    }
}
