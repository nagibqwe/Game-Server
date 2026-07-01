package com.game.worldanswer.manager;

import com.data.CfgManager;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.worldanswer.script.IWorldAnswer;
import com.game.worldanswer.structs.Answer;
import com.game.worldanswer.structs.Question;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by 542 on 2019/7/15.
 */
public class WorldAnswerManager {

    private static final Logger log = LogManager.getLogger(WorldAnswerManager.class);

    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, Answer>> groupAnswerList = new ConcurrentHashMap<>();//服务器组ID，对应每组的所有数据

    private Question curQuestion = null;//当前题目

    public int[] questoinIndexList = null;//本次答题的所有题目索引

    private int curQustionNum = 0; //当前答题数

    private int answerStage = 0; ///答题的阶段

    public List<Integer> existing = new ArrayList<>();//已经发送过得题目

    private ConcurrentHashMap<Long,Integer>  roleIDWithGroupID = new ConcurrentHashMap<>();

    private int  round = 0;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        WorldAnswerManager manager;

        Singleton() {
            this.manager = new WorldAnswerManager();
        }

        WorldAnswerManager getProcessor() {
            return manager;
        }
    }

    public IWorldAnswer getIWorldAnswer(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.WorldAnswerScript);
        if (is instanceof IWorldAnswer) {
            return (IWorldAnswer) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public static WorldAnswerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public  ConcurrentHashMap<Integer, ConcurrentHashMap<Long, Answer>> getGroupAnswerList(){return  groupAnswerList;}

    public void setCurQuestion(Question curQuestion){this.curQuestion = curQuestion;}

    public Question getCurQuestion(){return  curQuestion;}

    public void setQuestoinIndexList(int[] questoinIndexList){this.questoinIndexList = questoinIndexList;}

    public int[] getQuestoinIndexList(){return  questoinIndexList;}

    public void  setCurQustionNum(int curQustionNum){this.curQustionNum = curQustionNum;}

    public int getCurQustionNum(){return  this.curQustionNum;}

    public void setAnswerStage(int answerStage){this.answerStage = answerStage;}

    public int getAnswerStage(){return  answerStage;}

    public  ConcurrentHashMap<Long,Integer> getRoleIDWithGroupID(){return  roleIDWithGroupID;}


    public void init()
    {
        Manager.worldAnswerManager.setCurQustionNum(0);
        int questionSize = CfgManager.getCfg_World_question_Container().getValuees().length;
        Manager.worldAnswerManager.questoinIndexList = randomCommon(0,questionSize-1,10);
        Question q = new Question();
        setCurQuestion(q);
        round = 0;
    }

    public void clear()
    {
        answerStage = 0;
        curQuestion.getPlayerChooseList().clear();
        curQuestion.getLastRoundResultList().clear();
        curQuestion  = null;
        questoinIndexList = null;
        groupAnswerList.clear();
        curQustionNum = 0;
        round = 0;
        existing.clear();
        roleIDWithGroupID.clear();
    }
    /**
     * 随机获取N个不重复的数
     */
    public int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}
