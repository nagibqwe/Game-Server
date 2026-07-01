package common.worldanswer;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_World_question_Bean;
import com.data.bean.Cfg_World_question_reward_Bean;
import com.data.struct.ReadArray;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.worldanswer.script.IWorldAnswer;
import com.game.worldanswer.structs.*;
import com.game.worldanswer.timer.WorldAnwerTimer;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by clc on 2019/7/15.
 */
public class WorldAnswerScript implements IWorldAnswer,IScript {

    private static final Logger log = LogManager.getLogger(WorldAnswerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.WorldAnswerScript;
    }
    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 答题阶段
     */
    @Override
    public void worldAnswerStageChange(int stage)
    {
        switch (stage)
        {  case QuestionDefine.ActivityPhase_AnswerPlay_1:
                //报名阶段
                Manager.worldAnswerManager.setAnswerStage(stage);
                initGrouplist();
                break;
            case QuestionDefine.ActivityPhase_AnswerPlay_2:
                // 开始
                Manager.worldAnswerManager.setAnswerStage(stage);
                worldAnswerStart();
                break;
            case QuestionDefine.ActivityPhase_AnswerPlay_3:
                //结束
                Manager.worldAnswerManager.setAnswerStage(stage);
                worldAnswerOver();
                break;
        }
    }

    /**
     * 开始答题
     */
    @Override
    public void  worldAnswerStart()
    {
        initAll();
    }
    /**
     * 结束答题
     */
    @Override
    public void worldAnswerOver(){
        if (Manager.worldAnswerManager.getAnswerStage() ==0)
            return;
        sendAnswerOverReward();
        Manager.worldAnswerManager.clear();
    }

    /**
     * 报名
     */

    public  void onG2PReqApplyAnswer(ChannelHandlerContext context,worldAnswerMessage.G2PReqApplyAnswer messInfo){

        if (Manager.worldAnswerManager.getAnswerStage()<=0){
            MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.WorldAnswerAppError);
            return;
        }

        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
        int serverGroupId  = -1;
        if (Manager.worldAnswerManager.getRoleIDWithGroupID().containsKey( messInfo.getRoleId())) {
            serverGroupId =  Manager.worldAnswerManager.getRoleIDWithGroupID().get( messInfo.getRoleId());}
        else {
            serverGroupId = ServerMatchManager.deal().getGroupIDForCurOpenDay(platServerId,DailyActiveDefine.WORLD_ANSWER_QUESTION);
        }
        if (serverGroupId<0){
            MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.ServerMachtFail);
            return;
        }
        Manager.worldAnswerManager.getRoleIDWithGroupID().put(messInfo.getRoleId(),serverGroupId);
        ConcurrentHashMap<Long, Answer> answerlist =  Manager.worldAnswerManager.getGroupAnswerList().get(serverGroupId);
        Answer a = null;
        if (!answerlist.containsKey(messInfo.getRoleId())) {
            a = new Answer();
            a.setId(messInfo.getRoleId());
            a.setAddIntegral(0);
            a.setIsInPanel(true);
            a.setName(messInfo.getRoleName());
            a.setSyncAnswerCount(0);
            a.setLevel(messInfo.getLevel());
            a.setContext(context);
            answerlist.put(messInfo.getRoleId(),a);
        }
        else {
            a =  answerlist.get(messInfo.getRoleId());
            a.setIsInPanel(true);
        }

        //如果已经开始，直接发题
        worldAnswerMessage.ResApplyAnswerResult.Builder msg = worldAnswerMessage.ResApplyAnswerResult.newBuilder();
        int qeustionID = 0;int round = -1;int lastTime = 0;int curQnum = 0;int mySelect = 0;
        if (Manager.worldAnswerManager.getAnswerStage() == QuestionDefine.ActivityPhase_AnswerPlay_2) {
            Question curQ =  Manager.worldAnswerManager.getCurQuestion();
            if(curQ !=null){
                round = curQ.getRound();
                qeustionID = curQ.getId();
                lastTime = (int)(curQ.getOverTime()-(TimeUtils.Time()/1000));
                curQnum = Manager.worldAnswerManager.getCurQustionNum();
                if ( curQ.getPlayerChooseList().get(serverGroupId).containsKey(messInfo.getRoleId())){
                    PlayerSelectInfo playerSelectInfo =  curQ.getPlayerChooseList().get(serverGroupId).get((messInfo.getRoleId()));
                    mySelect = playerSelectInfo.getAnswerReuslt();
                    if(playerSelectInfo.getAnwerCount() <=0){
                        mySelect +=10;
                    }
                }
                if (round >=QuestionDefine.Round_2) {
                    worldAnswerMessage.totalChooseNum.Builder mAllChoose = worldAnswerMessage.totalChooseNum.newBuilder();
                    ConcurrentHashMap<Integer, Integer> selectList = curQ.getLastRoundResultList().get(serverGroupId);
                    List<Integer> parList =  getQuestionParcent( selectList);
                    mAllChoose.setChooseACount(parList.get(0));
                    mAllChoose.setChooseBCount(parList.get(1));
                    mAllChoose.setChooseCCount(parList.get(2));
                    mAllChoose.setChooseDCount(parList.get(3));
                    msg.setChooseNum(mAllChoose.build());
                }
            }
        }
        //发送报名后的结果给客户端
        worldAnswerMessage.totalIntegral.Builder mIntegral = worldAnswerMessage.totalIntegral.newBuilder();
        msg.setQuestionRound(round);
        mIntegral.setExp(a.getExp());
        mIntegral.setIntegral(a.getIntegral());
        mIntegral.setMoney(a.getGold());
        msg.setIntegral(mIntegral);
        msg.setQuestionID(qeustionID);
        msg.setCurQuestionNum(curQnum);
        msg.setLastTime(lastTime);
        msg.setCurChoose(mySelect);

        MessageUtils.send_to_player(context,messInfo.getRoleId(),
        worldAnswerMessage.ResApplyAnswerResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 答题
     */
    public  void onG2PReqPlayerAnswerResult(ChannelHandlerContext context,worldAnswerMessage.G2PReqAnswerResult messInfo){

        if ( Manager.worldAnswerManager.getAnswerStage()!=QuestionDefine.ActivityPhase_AnswerPlay_2){
            MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.WorldAnswerNotInError);
            return;
        }
        int serverGroupId  = -1;
        if (Manager.worldAnswerManager.getRoleIDWithGroupID().containsKey( messInfo.getRoleId())) {
            serverGroupId =  Manager.worldAnswerManager.getRoleIDWithGroupID().get( messInfo.getRoleId());
        }
        if (serverGroupId<0){
            MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.ServerMachtFail);
            return;
        }

        if (messInfo.getResultIndex()>QuestionDefine.ResultIndex_4) {
            MessageUtils.notify_player(context, messInfo.getRoleId(),
                    MessageString.WorldAnswerOverIndexError,messInfo.getResultIndex() +"");
            return;
        }

        ConcurrentHashMap<Long, Answer> answerlist =  Manager.worldAnswerManager.getGroupAnswerList().get(serverGroupId);
        Answer myAnswer =  answerlist.get(messInfo.getRoleId());
        if (myAnswer == null) {
            log.error("玩家ID 不存在");
            return;
        }
        Question q =  Manager.worldAnswerManager.getCurQuestion();
        if (q == null){
            log.info("答题还未开始");
            return;
        }
        if ( q.getRound()>=QuestionDefine.RoundMax){
            MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.WorldAnswerQuestionOver);
            return;
        }
        Long playerid = messInfo.getRoleId();
        PlayerSelectInfo sl= null;
        if (!q.getPlayerChooseList().get(serverGroupId).containsKey(playerid)){
            sl = new PlayerSelectInfo();
            sl.setId(playerid);
            q.getPlayerChooseList().get(serverGroupId).put(playerid,sl);
        }
        else {
            sl = q.getPlayerChooseList().get(serverGroupId).get(playerid);
            if(sl.getAnwerCount()>0){
                MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.WorldAnswerHasAnswer);
                log.info("本轮已经答过不能再答" );
                return;
            }
            //curQ.getPlayerChooseList().put(sl.getId(),sl);
        }
        sl.setAnwerCount(1);
        sl.setAnswerReuslt(messInfo.getResultIndex());
        Manager.worldAnswerManager.setCurQuestion(q);
        syncToOtherPlayer(playerid,messInfo.getResultIndex(),serverGroupId);
    }
    /**
     * 把答案同步给其他玩家
     */
    private void syncToOtherPlayer(long playerid, int index,int serverGroupId){

        ConcurrentHashMap<Long, Answer> answerlist =  Manager.worldAnswerManager.getGroupAnswerList().get(serverGroupId);
        Answer myAnswer =  answerlist.get(playerid);
        worldAnswerMessage.ResSendOtherPlayerSelect.Builder msg = worldAnswerMessage.ResSendOtherPlayerSelect.newBuilder();
        for (Answer a: answerlist.values()){
            if (!a.getIsInPanel())
                continue;

            if (a.getSyncAnswerCount()< QuestionDefine.SyncResultMax) {
                a.setSyncAnswerCount(a.getSyncAnswerCount()+1);
                msg.setRoleName(myAnswer.getName());
                msg.setQuestionIndex(index);
                MessageUtils.send_to_player(a.getContext(),a.getId(),
                        worldAnswerMessage.ResSendOtherPlayerSelect.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    /**
     * 玩家离开答题界面
     */
    @Override
    public void onG2PReqPlayerLeaveAnswer(ChannelHandlerContext context,Long roleId){

        if (Manager.worldAnswerManager.getAnswerStage()<=0){
            //log.info("活动未开始");
            return;
        }
        if ( Manager.worldAnswerManager.getRoleIDWithGroupID().containsKey(roleId)) {
            int serverGroupId =   Manager.worldAnswerManager.getRoleIDWithGroupID().get(roleId);
            if (serverGroupId <0) {
                return;
            }
            ConcurrentHashMap<Long, Answer> answerlist = Manager.worldAnswerManager.getGroupAnswerList().get(serverGroupId);
            if(answerlist.containsKey(roleId)){
                answerlist.get(roleId).setIsInPanel(false);
            }
        }
    }

    /**
     * 最后发活动结束排名奖励
     */
    private void sendAnswerOverReward(){
        for (ConcurrentHashMap<Long, Answer> list:Manager.worldAnswerManager.getGroupAnswerList().values()){
            List<Answer> answerList = new ArrayList<>(list.values());
            answerList.sort(new AnswerRank());
            int lengh = answerList.size();
            Collections.reverse(answerList);
            int rank = 1;
            for (Answer a:answerList){
                float rankPer = ((float)(lengh -  rank)/lengh)*100;
                for (Cfg_World_question_reward_Bean r: CfgManager.getCfg_World_question_reward_Container().getValuees()){
                    if (a.getIntegral()>=r.getLevel_min() &&a.getIntegral() <=r.getLevel_max()){
                        worldAnswerMessage.ResWorldAnswerOver.Builder msg = worldAnswerMessage.ResWorldAnswerOver.newBuilder();
                        worldAnswerMessage.P2GResWorldAnswerOver.Builder msg_1 = worldAnswerMessage.P2GResWorldAnswerOver.newBuilder();
                        worldAnswerMessage.itemReward.Builder mItem = null;
                        for (ReadArray<Integer> reward:r.getReward().getValuees() ) {
                            mItem  = worldAnswerMessage.itemReward.newBuilder();
                            boolean isBind = reward.get(2)>0?true:false;
                            mItem.setItmeID( reward.get(0));
                            mItem.setItmeNum(reward.get(1));
                            mItem.setIsBind(isBind);
                            msg.addReward(mItem);
                            msg_1.addItem(mItem);
                        }
                        //发送给客户端展示
                        worldAnswerMessage.totalIntegral.Builder mIntegral = worldAnswerMessage.totalIntegral.newBuilder();
                        mIntegral.setExp(a.getExp());mIntegral.setIntegral(a.getIntegral());mIntegral.setMoney(a.getGold());
                        msg.setRankPer(rankPer);
                        msg.setIntegral(mIntegral);
                        MessageUtils.send_to_player(a.getContext(),a.getId(),
                                worldAnswerMessage.ResWorldAnswerOver.MsgID.eMsgID_VALUE, msg.build().toByteArray());

                        //发送给游戏服添加
                        msg_1.setRoleId(a.getId());
                        MessageUtils.send_to_game(a.getContext(),
                                worldAnswerMessage.P2GResWorldAnswerOver.MsgID.eMsgID_VALUE, msg_1.build().toByteArray());
                        break;
                    }
                }
                rank++;
            }
        }
    }
    /**
     * 每题阶段
     */
    @Override
    public void quesiontRound(int round)
    {
        switch (round){
            case QuestionDefine.Round_1:
                questionRound_1();
                break;
            case QuestionDefine.Round_2:
                questionRound_2();
                break;
            case QuestionDefine.Round_3:
                questionRound_3();
                break;
        }
    }

    /**
     * 每组group的容器初始化
     */
    private void initGrouplist(){
        List<Integer> groupList =   ServerMatchManager.deal().getAllReachMatchGroupIDList(DailyActiveDefine.WORLD_ANSWER_QUESTION);
        if (groupList == null){
            log.error("未有达成匹配的服务器");
            return;
        }
        for (Integer groupID :groupList) {
            ConcurrentHashMap<Long, Answer> Answerlist = new  ConcurrentHashMap<>();
            Manager.worldAnswerManager.getGroupAnswerList().put(groupID,Answerlist);
        }
    }

    /**
     * 初始化题目
     */
    private void initAll(){

        if ( Manager.worldAnswerManager.getGroupAnswerList().size()<=0)
            initGrouplist();
        Manager.worldAnswerManager.init();
        worldAnswerTimeLoop();

    }
    public void worldAnswerTimeLoop(){

        if (Manager.worldAnswerManager.questoinIndexList == null)
            return;
        int length = Manager.worldAnswerManager.questoinIndexList.length;
        int round =  Manager.worldAnswerManager.getRound();
        if ( Manager.worldAnswerManager.getCurQustionNum() >=length&& round==QuestionDefine.Round_3)
            return;
        round = round==QuestionDefine.Round_3?0:round;
        int round_Time =  Global.WorldAnswerTime.get(round)*1000;
        round++;
        quesiontRound(round);
        Manager.worldAnswerManager.setRound(round);
        MainServer.getInstance().addTimerEvent(new WorldAnwerTimer(1, round_Time));
    }


    private void sentQuestion(Answer a,Question q,int groupID )
    {
        worldAnswerMessage.ResSendQuestion.Builder msg = worldAnswerMessage.ResSendQuestion.newBuilder();
        worldAnswerMessage.totalIntegral.Builder mIntegral = worldAnswerMessage.totalIntegral.newBuilder();
        worldAnswerMessage.totalChooseNum.Builder mAllChoose = worldAnswerMessage.totalChooseNum.newBuilder();
        ConcurrentHashMap<Integer, Integer> selectList = q.getLastRoundResultList().get(groupID);

        List<Integer> parList =  getQuestionParcent( selectList);
        mAllChoose.setChooseACount(parList.get(0));
        mAllChoose.setChooseBCount(parList.get(1));
        mAllChoose.setChooseCCount(parList.get(2));
        mAllChoose.setChooseDCount(parList.get(3));

        msg.setQuestionID(q.getId());
        msg.setQuestionRound(q.getRound());
        int overTime =(int) (q.getOverTime() - TimeUtils.Time()/1000);
        msg.setLastTime(overTime);
        msg.setCurQuestionNum(Manager.worldAnswerManager.getCurQustionNum());
        mIntegral.setMoney(a.getGold());
        mIntegral.setIntegral(a.getIntegral());
        mIntegral.setExp(a.getExp());
        msg.setIntegral(mIntegral);
        msg.setChooseNum(mAllChoose.build());
        MessageUtils.send_to_player(a.getContext(),a.getId(),
                worldAnswerMessage.ResSendQuestion.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    /**
     * 第一轮答题
     */
    private void questionRound_1(){
        int index= Manager.worldAnswerManager.questoinIndexList[Manager.worldAnswerManager.getCurQustionNum()];
        Cfg_World_question_Bean bean =  CfgManager.getCfg_World_question_Container().getValueByIndex(index);
        Question q =  Manager.worldAnswerManager.getCurQuestion();
        q.init();
        if (bean!=null){
            q.setId(bean.getId());
            q.setSendTime(TimeUtils.Time());
            q.setOverTime(TimeUtils.Time()/1000+ Global.WorldAnswerTime.get(0));
            q.setRound(QuestionDefine.Round_1);
            Manager.worldAnswerManager.setCurQuestion(q);
            Manager.worldAnswerManager.existing.add(bean.getId());
            Manager.worldAnswerManager.setCurQustionNum(Manager.worldAnswerManager.getCurQustionNum()+1);

            //发给 题目给客户端
            Set<Integer> keys=Manager.worldAnswerManager.getGroupAnswerList().keySet();
            Iterator<Integer> iterator1=keys.iterator();
            while (iterator1.hasNext()) {
                Integer groupID =  iterator1.next();
                ConcurrentHashMap<Long, Answer> answers = Manager.worldAnswerManager.getGroupAnswerList().get(groupID);
                for (Answer a:  answers.values()) {
                    a.setSyncAnswerCount(0);
                    if (a.getIsInPanel()){
                        sentQuestion(a,q,groupID);
                    }
                }
            }
        }
    }
    /**
     * 第二轮答题
     */
    private void questionRound_2(){
        Question q =  Manager.worldAnswerManager.getCurQuestion();
        q.setOverTime(TimeUtils.Time()/1000+Global.WorldAnswerTime.get(1));
        q.setRound(QuestionDefine.Round_2);
        q.getId();
        Set<Integer> keys=q.getPlayerChooseList().keySet();
        Iterator<Integer> iterator1=keys.iterator();
        while (iterator1.hasNext()) {
            Integer groupID =  iterator1.next();
            ConcurrentHashMap<Long, PlayerSelectInfo> pValues = q.getPlayerChooseList().get(groupID);
            ConcurrentHashMap<Long, Answer> answers = Manager.worldAnswerManager.getGroupAnswerList().get(groupID);
            calculateVote(q, pValues, groupID);
            if (answers ==null || answers.size() <=0)
                continue;
            //把第一轮答题的 结果发送给玩家
            for (Answer a:  answers.values()) {
                if (a.getIsInPanel()){
                    sentQuestion(a,q,groupID);
                }
            }
        }
    }


    private void calculateVote( Question q,ConcurrentHashMap<Long, PlayerSelectInfo> pValues,int groupID)
    {
        int A = 0,B = 0,C = 0,D = 0;
        for (PlayerSelectInfo p : pValues.values()){
            p.setAnwerCount(0);
            switch (p.getAnswerReuslt()){
                case QuestionDefine.ResultIndex_1:
                    A++;
                    break;
                case QuestionDefine.ResultIndex_2:
                    B++;
                    break;
                case QuestionDefine.ResultIndex_3:
                    C++;
                    break;
                case QuestionDefine.ResultIndex_4:
                    D++;
                    break;
            }
        }
        ConcurrentHashMap<Integer, Integer> selectList = q.getLastRoundResultList().get(groupID);
        selectList.put(QuestionDefine.ResultIndex_1,A);
        selectList.put(QuestionDefine.ResultIndex_2,B);
        selectList.put(QuestionDefine.ResultIndex_3,C);
        selectList.put(QuestionDefine.ResultIndex_4,D);
    }
    /**
     * 本题答题结束发送结果
     */
    public void questionRound_3(){
        Question q =  Manager.worldAnswerManager.getCurQuestion();
        q.setRound(QuestionDefine.Round_3);
        q.setOverTime(TimeUtils.Time()/1000+Global.WorldAnswerTime.get(2));
        //算选择最少的那个答案
        Set<Integer> keys=q.getPlayerChooseList().keySet();
        Iterator<Integer> iterator1=keys.iterator();
        while (iterator1.hasNext()) {
            Integer groupID = iterator1.next();
            sendQuestionReward(calculateChooseLeast(groupID),groupID, q);
        }
    }

    /**
     * 发答题奖励
     */
    private void sendQuestionReward(List<Integer> minIndex,int groupID,Question q){
        //加答题积分 +经验+钱
        Cfg_Characters_Bean character = null;
        long addExp = 0;
        ConcurrentHashMap<Long, Answer> answerList= Manager.worldAnswerManager.getGroupAnswerList().get(groupID);
        for (PlayerSelectInfo p :q.getPlayerChooseList().get(groupID).values()) {
            Answer answer= answerList.get(p.getId());
            if (answer!=null) {
                boolean isChoose =  minIndex.contains(p.getAnswerReuslt());
                int integral =
                        true == isChoose? Global.WorldAnswerCorrectNum.get(0): Global.WorldAnswerCorrectNum.get(1);
                answer.setAddIntegral(integral);
                //加经验
                character = CfgManager.getCfg_Characters_Container().getValueByKey(answer.getLevel());
                if (character!=null) {
                    addExp = character.getWorld_question_exp();
                    answer.setAddExp( addExp);
                }
                //加钱
                for (ReadArray<Integer> reward: Global.World_answer_reward_new.getValuees()){
                    if (answer.getLevel() <= reward.get(0)){
                        boolean isBind = reward.get(3)>0?true:false;
                        answer.setAddGold(reward.get(2));

                        //发送给游戏服添加
                        worldAnswerMessage.P2GResQuestionReward.Builder msg = worldAnswerMessage.P2GResQuestionReward.newBuilder();
                        worldAnswerMessage.itemReward.Builder mItem = worldAnswerMessage.itemReward.newBuilder();
                        mItem.setIsBind(isBind);
                        mItem.setItmeNum(reward.get(2));
                        mItem.setItmeID( reward.get(1));
                        msg.setRoleId(answer.getId());
                        msg.setAddExp(addExp);
                        msg.setItem(mItem);
                        MessageUtils.send_to_game(answer.getContext(),
                                worldAnswerMessage.P2GResQuestionReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                        break;
                    }
                }
            }
        }
        //发送第二轮答题的结果所有玩家
        for (Answer a:  answerList.values()) {
            if (a.getIsInPanel()){
                sentQuestion(a,q,groupID);
            }
        }
    }

    /**
     * 计算最小选择的答案索引
     */
    private  List<Integer>  calculateChooseLeast(int group){
        Question q = Manager.worldAnswerManager.getCurQuestion();
        calculateVote(q, q.getPlayerChooseList().get(group), group);
        ConcurrentHashMap<Integer, Integer> selectList = q.getLastRoundResultList().get(group);
        Manager.worldAnswerManager.setCurQuestion(q);
        List<Integer> parList = getQuestionParcent(  selectList);
        int percentA =parList.get(0);
        int percentB = parList.get(1);
        int percentC = parList.get(2);
        int percentD = parList.get(3);

        int[] indexArr  = {percentA,percentB,percentC,percentD};
        int[] sort = {percentA,percentB,percentC,percentD};
        Arrays.sort(sort);
        int min =  sort[0];
        List<Integer> minIndex = new ArrayList<>();
        for (int i = 0;i<indexArr.length;i++){
            if (indexArr[i] == min){
                minIndex.add(i+1);
            }
        }
        return minIndex;
    }


    //算支持率
    private List<Integer> getQuestionParcent( ConcurrentHashMap<Integer, Integer> selectList)
    {
        List<Integer> percentlist = new ArrayList<>();
        int A = 0,B = 0,C = 0,D = 0;
        A =  selectList.get(QuestionDefine.ResultIndex_1);
        B =  selectList.get(QuestionDefine.ResultIndex_2);
        C =  selectList.get(QuestionDefine.ResultIndex_3);
        D =  selectList.get(QuestionDefine.ResultIndex_4);
        int all = A + B + C + D;
        int percentA = (int)(((float)A/all)*10000);
        percentlist.add(percentA);
        int percentB = (int)(((float)B/all)*10000);
        percentlist.add(percentB);
        int percentC = (int)(((float)C/all)*10000);
        percentlist.add(percentC);
        int percentD = (int)(((float)D/all)*10000);
        percentlist.add(percentD);
        return percentlist;
    }
}
