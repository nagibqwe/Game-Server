package common.questionnaire;

import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.questionnaire.script.IQuestionnaireScript;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireScript implements IQuestionnaireScript {

    private static Logger logger = LogManager.getLogger(QuestionnaireScript.class);

    @Override
    public int getId() {
        return ScriptEnum.QuestionnaireScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private static final int canNotGet = 0;
    private static final int canGet = 1;
    private static final int hasGet = 2;

    @Override
    public void checkOpenState(long lastCheckTime, long now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        try {
            long startTime = sdf.parse(Global.Question_Start_Time.get(0)).getTime();
            long endTime = sdf.parse(Global.Question_Start_Time.get(1)).getTime();
            if (lastCheckTime < startTime && now >= startTime) {
                QuestionnaireMessage.ResPanelInfo.Builder builder = QuestionnaireMessage.ResPanelInfo.newBuilder();
                builder.setIsOpen(true);
                builder.setEndTime(getRemainTime());
                builder.setState(0);
                MessageUtils.send_to_all_player(QuestionnaireMessage.ResPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
            if (lastCheckTime < endTime && now >= endTime) {
                QuestionnaireMessage.ResPanelInfo.Builder builder = QuestionnaireMessage.ResPanelInfo.newBuilder();
                builder.setIsOpen(false);
                builder.setEndTime(getRemainTime());
                builder.setState(0);
                MessageUtils.send_to_all_player(QuestionnaireMessage.ResPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        } catch (Exception e) {
            logger.error("有奖问答时间解析错误");
        }
    }

    @Override
    public void onlineDeal(Player player) {
//        QuestionnaireMessage.G2PGetPanelInfo.Builder builder = QuestionnaireMessage.G2PGetPanelInfo.newBuilder();
//        builder.setUserId(player.getUserId());
//        builder.setRoleId(player.getId());
//        MessageUtils.send_to_public(QuestionnaireMessage.G2PGetPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        boolean open = isOpen(TimeUtils.Time());
        logger.info("玩家领取数据：" + ServerParamUtil.questionnaireData.getOrDefault(player.getUserId(), 0L));

        QuestionnaireMessage.ResPanelInfo.Builder builder = QuestionnaireMessage.ResPanelInfo.newBuilder();
        boolean geted = isOpen(ServerParamUtil.questionnaireData.getOrDefault(player.getUserId(), 0L));
        builder.setIsOpen(open && !geted);
        int state = open ? (geted ? hasGet : canGet) : canNotGet;
        builder.setState(state);
        builder.setEndTime(open ? getRemainTime() : 0);
        MessageUtils.send_to_player(player, QuestionnaireMessage.ResPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());


        //上线初始化 是否领取过下载奖励
        QuestionnaireMessage.ResDownloadGetAward.Builder msg = QuestionnaireMessage.ResDownloadGetAward.newBuilder();
        msg.setIsGetDownloadAward( player.isDownLoadOver());
        MessageUtils.send_to_player(player, QuestionnaireMessage.ResDownloadGetAward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqGetReward(Player player) {
//        QuestionnaireMessage.G2PGetReward.Builder builder = QuestionnaireMessage.G2PGetReward.newBuilder();
//        builder.setUserId(player.getUserId());
//        builder.setRoleId(player.getId());
//        MessageUtils.send_to_public(QuestionnaireMessage.G2PGetReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        if (!isOpen(TimeUtils.Time())) {
            return;
        }

        boolean hasGet = isOpen(ServerParamUtil.questionnaireData.getOrDefault(player.getUserId(), 0L));
        if (hasGet) {
            return;
        }

        ServerParamUtil.questionnaireData.put(player.getUserId(), TimeUtils.Time());
        ServerParamUtil.saveQuestionnaire();

        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < Global.Question_Reward.size(); i++) {
            int career = Global.Question_Reward.get(i).get(1);
            if (career != player.getCareer() && career != -1) {
                continue;
            }
            boolean isBind = Global.Question_Reward.get(i).get(4) == 1;
            Item item = Item.createItem(Global.Question_Reward.get(i).get(2), Global.Question_Reward.get(i).get(3), isBind);
            itemList.add(item);
        }
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.QuestionaireReward, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.QuestionaireReward, actionId);
        }
        QuestionnaireMessage.ResPanelInfo.Builder builder = QuestionnaireMessage.ResPanelInfo.newBuilder();
        builder.setIsOpen(false);
        builder.setState(QuestionnaireScript.hasGet);
        builder.setEndTime(getRemainTime());
        MessageUtils.send_to_player(player, QuestionnaireMessage.ResPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //记录bi数据 1710是Question_Reward在global表里面的id
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.QuestionaireReward, BIActiityTypeEnum.QUESTIONNAIRE.getId(), 1710);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.QUESTIONNAIRE, ItemChangeReason.QuestionaireReward);
    }


    @Override
    public void onReqSubmitAnswer(Player player, QuestionnaireMessage.ReqSubmitAnswer messInfo) {
//        QuestionnaireMessage.G2PSubmitAnswer.Builder builder = QuestionnaireMessage.G2PSubmitAnswer.newBuilder();
//        builder.setRoleId(player.getId());
//        builder.setUserId(player.getUserId());
//        builder.addAllAnswers(messInfo.getAnswersList());
//        MessageUtils.send_to_public(QuestionnaireMessage.G2PSubmitAnswer.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onP2GOpenState(QuestionnaireMessage.P2GOpenState messInfo) {
//        QuestionnaireMessage.ResPanelInfo.Builder builder = QuestionnaireMessage.ResPanelInfo.newBuilder();
//        builder.setIsOpen(messInfo.getOpen());
//        builder.setEndTime(messInfo.getRemainTime());
//        builder.setState(0);
//        MessageUtils.send_to_all_player(QuestionnaireMessage.ResPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onP2GGetRewardState(QuestionnaireMessage.P2GGetRewardState messInfo) {
//        if (!messInfo.getCanGet()) {
//            logger.error("不能领取");
//            return;
//        }
//        long roleId = messInfo.getRoleId();
//        Player player = Manager.playerManager.getPlayerCache(roleId);
//        if (player == null) {
//            logger.error("找不到玩家");
//            return;
//        }
//        List<Item> itemList = new ArrayList<>();
//        for (int i = 0; i < Global.Question_Reward.size(); i++) {
//            int career = Global.Question_Reward.get(i).get(1);
//            if (career != player.getCareer() && career != -1) {
//                continue;
//            }
//            boolean isBind = Global.Question_Reward.get(i).get(4) == 1;
//            Item item = Item.createItem(Global.Question_Reward.get(i).get(2), Global.Question_Reward.get(i).get(3), isBind);
//            itemList.add(item);
//        }
//        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.QuestionaireReward, IDConfigUtil.getLogId())) {
//            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, itemList);
//        }
    }

    private boolean isOpen(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        try {
            long startTime = sdf.parse(Global.Question_Start_Time.get(0)).getTime();
            long endTime = sdf.parse(Global.Question_Start_Time.get(1)).getTime();
            return time >= startTime && time <= endTime;
        } catch (Exception e) {
            logger.error("有奖问答时间解析错误");
            return false;
        }
    }

    private int getRemainTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        try {
            long endTime = sdf.parse(Global.Question_Start_Time.get(1)).getTime();
            return Math.max(0, (int) ((endTime - TimeUtils.Time()) / 1000));
        } catch (Exception e) {
            logger.error("有奖问答时间解析错误");
            return 0;
        }
    }


   public  void onDownLoadOver(Player player){

        if (player.isDownLoadOver())
            return;
       List<Item> itemList = new ArrayList<>();
        for (ReadArray<Integer> array : Global.Update_Gift_Reward.getValuees()){
            boolean isBind = array.get(2) == 1;
            Item item = Item.createItem(array.get(0), array.get(1), isBind);
            itemList.add(item);
        }
       long actionId = IDConfigUtil.getLogId();
       if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.DownloadReward, actionId)) {
           Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                   MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.DownloadReward, actionId);
       }
       player.setDownLoadOver(true);
       QuestionnaireMessage.ResDownloadGetAward.Builder msg = QuestionnaireMessage.ResDownloadGetAward.newBuilder();
       msg.setIsGetDownloadAward(true);
       MessageUtils.send_to_player(player, QuestionnaireMessage.ResDownloadGetAward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
       //记录bi数据 1816是Update_Gift_Reward在global表里面的id
//       Manager.biManager.getScript().biActivity(player, ItemChangeReason.DownloadReward, BIActiityTypeEnum.DOWNLOWD.getId(), 1816);
       Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DOWNLOWD, ItemChangeReason.DownloadReward);
   }
}
