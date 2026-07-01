package common.questionnaire;

import com.game.manager.Manager;
import com.game.questionnaire.log.QuestionnaireLog;
import com.game.questionnaire.script.IQuestionnaireScript;
import com.game.script.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class QuestionaireScript implements IQuestionnaireScript {

    private static final Logger logger = LogManager.getLogger(QuestionaireScript.class);

    @Override
    public int getId() {
        return ScriptEnum.QuestionnaireScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void checkActivityOpen(long now, long lastCheckTime) {
        if (!Manager.questionnaireManager.openState) {
            if (now >= Manager.questionnaireManager.getStarTime() &&  now < Manager.questionnaireManager.getEndTime()) {
                openActivity();
                return;
            }
        }
        if (lastCheckTime < Manager.questionnaireManager.getStarTime() && now >= Manager.questionnaireManager.getEndTime()) {
            closeActivity();
        }
    }

    @Override
    public void openActivity() {
        logger.info("有奖问答开启");
        Manager.questionnaireManager.openState = true;
        for (ChannelHandlerContext session : Manager.gameServerManager.getGameSessions().values()) {
            if (session != null) {
                sendOpenState(session, true);
            }
        }
    }

    @Override
    public void closeActivity() {
        logger.info("有奖问答结束");
        Manager.questionnaireManager.openState = false;
        for (ChannelHandlerContext session : Manager.gameServerManager.getGameSessions().values()) {
            if (session != null) {
                sendOpenState(session, false);
            }
        }
    }

    private void sendOpenState(ChannelHandlerContext session, boolean open) {
        QuestionnaireMessage.P2GOpenState.Builder builder = QuestionnaireMessage.P2GOpenState.newBuilder();
        builder.setOpen(open);
        int remainTime = (int) Math.max(0, (Manager.questionnaireManager.getEndTime() - TimeUtils.Time()) / 1000);
        builder.setRemainTime(remainTime);
        MessageUtils.send_to_game(session, QuestionnaireMessage.P2GOpenState.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onG2PGetPanelInfo(ChannelHandlerContext session, long userId, long roleId) {
        QuestionnaireMessage.ResPanelInfo.Builder builder = QuestionnaireMessage.ResPanelInfo.newBuilder();
        builder.setIsOpen(Manager.questionnaireManager.openState);
        builder.setState(Manager.questionnaireManager.getQuestionnaireData().getOrDefault(userId, (byte) 0));
        int remainTime = Math.max(0, (int)((Manager.questionnaireManager.getEndTime() - TimeUtils.Time()) / 1000));
        builder.setEndTime(remainTime);
        MessageUtils.send_to_player(session, roleId, QuestionnaireMessage.ResPanelInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onG2PSubmitAnswer(ChannelHandlerContext session, QuestionnaireMessage.G2PSubmitAnswer messInfo) {
        if (!canSubmit(messInfo.getUserId())) {
            return;
        }
        Manager.questionnaireManager.getQuestionnaireData().put(messInfo.getUserId(), (byte) 1);
        onG2PGetPanelInfo(session, messInfo.getUserId(), messInfo.getRoleId());

        //记录问卷填写日志
        QuestionnaireLog log = new QuestionnaireLog();
        log.setRoleId(messInfo.getRoleId());
        log.setUserId(messInfo.getUserId());
        Map<Integer, String> answers = new HashMap<>();
        for (QuestionnaireMessage.answerInfo answerInfo: messInfo.getAnswersList()) {
            answers.put(answerInfo.getId(), answerInfo.getAnswer());
        }
        log.setData(JsonUtils.toJSONString(answers));
        LogService.getInstance().execute(log);
    }

    @Override
    public void onG2PGetReward(ChannelHandlerContext session, QuestionnaireMessage.G2PGetReward messInfo) {
        if (!canGetReward(messInfo.getUserId())) {
            return;
        }
        Manager.questionnaireManager.getQuestionnaireData().put(messInfo.getUserId(), (byte) 2);
        onG2PGetPanelInfo(session, messInfo.getUserId(), messInfo.getRoleId());

        QuestionnaireMessage.P2GGetRewardState.Builder builder = QuestionnaireMessage.P2GGetRewardState.newBuilder();
        builder.setCanGet(true);
        builder.setRoleId(messInfo.getRoleId());
        MessageUtils.send_to_game(session, QuestionnaireMessage.P2GGetRewardState.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private boolean canSubmit(long userId) {
        Byte state = Manager.questionnaireManager.getQuestionnaireData().getOrDefault(userId, (byte) 0);
        return state == 0;
    }

    private boolean canGetReward(long userId) {
        Byte state = Manager.questionnaireManager.getQuestionnaireData().getOrDefault(userId, (byte) 0);
        return state == 1;
    }
}
