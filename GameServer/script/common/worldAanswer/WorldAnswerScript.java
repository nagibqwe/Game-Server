package common.worldAanswer;


import com.data.*;

import com.data.bean.Cfg_Daily_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;

import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import com.game.worldanswer.manager.WorldAnswerManager;
import com.game.worldanswer.script.IWorldAnswer;
import com.game.worldanswer.structs.QuestionDefine;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.worldAnswerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


/**
 * Created by 542 on 2019/7/9.
 */


public class WorldAnswerScript implements IWorldAnswer, IScript {

    private static final Logger log = LogManager.getLogger(WorldAnswerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.AnswerQuestionsBaseScript;
    }
    @Override
    public Object call(Object... objects) {
        int stage = (int) objects[0];
        String method = (String) objects[1];
        switch (method) {
            case "activityChangeCallBack":
                activityChangeCallBack(stage);
                break;
            default:
        }
        return null;
    }

    public  void activityChangeCallBack(int ActivityType){

        switch (ActivityType){
            case QuestionDefine.ActivityPhase_AnswerPlay_1:
                //报名阶段
                WorldAnswerManager.getInstance().setAnswerStage(1);
                break;
            case QuestionDefine.ActivityPhase_AnswerPlay_2:
                WorldAnswerManager.getInstance().setAnswerStage(2);
                // 开始
                break;
            case QuestionDefine.ActivityPhase_AnswerPlay_3:
                WorldAnswerManager.getInstance().setAnswerStage(0);
                break;
        }
    }



    /**
     * 玩家离开答题界面
     */
    public void playerLeaveAnswer(Player player){

        //离开答题界面
        worldAnswerMessage.G2PReqLeaveOutAnswer.Builder msg = worldAnswerMessage.G2PReqLeaveOutAnswer.newBuilder();
        msg.setRoleId(player.getId());
        MessageUtils.send_to_public( worldAnswerMessage.G2PReqLeaveOutAnswer.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 报名 或者说进入答题界面请求
     */
    @Override
    public void applyAnswer(Player player){
        //世界答题开启等级
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.WorldAnser)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_WORLDLEVEL_LEVELBUZU);
            return;
        }
        Cfg_Daily_Bean daily_bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.WORLD_ANSWER_QUESTION.getValue());
        if (daily_bean == null)
            return;
        //是否满足等级开启条件
        if (player.getLevel() < daily_bean.getOpenLevel()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Daily_Level_Not_Enough);
            return;
        }

        if (WorldAnswerManager.getInstance().getAnswerStage()<=0){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldAnswerAppError);
            return;
        }


        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.WORLD_ANSWER_QUESTION, 1);
        //公共服报名
        worldAnswerMessage.G2PReqApplyAnswer.Builder msg = worldAnswerMessage.G2PReqApplyAnswer.newBuilder();
        msg.setRoleId(player.getId());
        msg.setLevel(player.getLevel());
        msg.setRoleName(player.getName());
        MessageUtils.send_to_public( worldAnswerMessage.G2PReqApplyAnswer.MsgID.eMsgID_VALUE, msg.build().toByteArray());
     }

    /**
     * 玩家发答题结果过来
     */
    @Override
    public void playerAnswerResult(Player player,int index){
        if (index>QuestionDefine.ResultIndex_4)
        {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldAnswerOverIndexError,index +"");
            return;
        }
        if (WorldAnswerManager.getInstance().getAnswerStage() != QuestionDefine.ActivityPhase_AnswerPlay_2)
        {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldAnswerNotInError);
            return;
        }
        //把答案发给公共服
        worldAnswerMessage.G2PReqAnswerResult.Builder msg = worldAnswerMessage.G2PReqAnswerResult.newBuilder();
        msg.setRoleId(player.getId());
        msg.setResultIndex(index);
        MessageUtils.send_to_public( worldAnswerMessage.G2PReqAnswerResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    /**
     * 发答题奖励
     */
    @Override
    public void sendAnswerReward( worldAnswerMessage.P2GResQuestionReward messInfo)
    {
        Player player = PlayerManager.getInstance().getPlayerCache(messInfo.getRoleId());
        long actionId = IDConfigUtil.getLogId();
        if (player!=null)
        {
            if (! Manager.backpackManager.manager().addItem(player,messInfo.getItem().getItmeID(),  messInfo.getItem().getItmeNum()
                ,  messInfo.getItem().getIsBind(), 0, ItemChangeReason.WorldAnswerGet, actionId))
            {
                List<Item> rewardItem = Item.createItems(messInfo.getItem().getItmeID(),messInfo.getItem().getItmeNum(),messInfo.getItem().getIsBind());
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                        MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, rewardItem, ItemChangeReason.WorldAnswerGet, actionId);
            }
            Manager.currencyManager.manager().addEXP(player,messInfo.getAddExp(),ItemChangeReason.WorldAnswerGet,actionId);
            //BI
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WorldAnswer, ItemChangeReason.WorldAnswerGet);
        }
        else {
            List<Item> rewardItem = Item.createItems(messInfo.getItem().getItmeID(),messInfo.getItem().getItmeNum(),messInfo.getItem().getIsBind());
            Manager.mailManager.sendMailToPlayer(messInfo.getRoleId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, rewardItem, ItemChangeReason.WorldAnswerGet, actionId);
        }

    }
    /**
     * 发答题结束奖励
     */
    @Override
    public void sendAnswerOverReward( worldAnswerMessage.P2GResWorldAnswerOver messInfo){
        Player player = PlayerManager.getInstance().getPlayerCache(messInfo.getRoleId());
        List<Item> rewardItem = new ArrayList<>();
        for (int i  = 0;i< messInfo.getItemList().size();i++){
            worldAnswerMessage.itemReward mItem = messInfo.getItemList().get(i);
            Item reward = Item.createItem(mItem.getItmeID(), mItem.getItmeNum(), mItem.getIsBind());
            if (reward!=null)
                rewardItem.add(reward);
        }

        Manager.countManager.addVariant(player, VariantType.WorldAnswerNum, 1);
        Manager.controlManager.operate(player, FunctionVariable.WorldQuestionNum, 1);
        Manager.retrieveResManager.getScript().count(player, RetrieveType.WorldAnswer);
        long actionId = IDConfigUtil.getLogId();
        if (player != null &&  player.isOnline())
        {
            if (rewardItem.size()>0)
            {
                if (! Manager.backpackManager.manager().addItems(player,rewardItem,ItemChangeReason.WorldAnswerOverGet, actionId)){
                    Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                            MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, rewardItem,ItemChangeReason.WorldAnswerOverGet, actionId);
                }
            }

            //BI
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WorldAnswer, ItemChangeReason.WorldAnswerOverGet);
        }
        else
        {
            if (rewardItem.size()>0)
            {
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                        MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, rewardItem,ItemChangeReason.WorldAnswerOverGet, actionId);
            }
        }
    }
}
