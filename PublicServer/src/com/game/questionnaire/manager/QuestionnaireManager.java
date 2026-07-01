package com.game.questionnaire.manager;

import com.data.Global;
import com.game.manager.Manager;
import com.game.questionnaire.script.IQuestionnaireScript;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;

public class QuestionnaireManager {

    private static final Logger logger = LogManager.getLogger(QuestionnaireManager.class);

    private long starTime = 0;

    private long endTime = 0;

    public volatile boolean openState = false;

    private ConcurrentHashMap<Long, Byte> questionnaireData = new ConcurrentHashMap<>();

    public long getStarTime() {
        return starTime;
    }

    public void setStarTime(long starTime) {
        this.starTime = starTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public ConcurrentHashMap<Long, Byte> getQuestionnaireData() {
        return questionnaireData;
    }

    public void setQuestionnaireData(ConcurrentHashMap<Long, Byte> questionnaireData) {
        this.questionnaireData = questionnaireData;
    }

    public QuestionnaireManager() {
        initData();
    }

    private enum Singleton {
        INSTANCE;
        QuestionnaireManager manager;

        Singleton() {
            this.manager = new QuestionnaireManager();
        }
        QuestionnaireManager getProcessor() {
            return manager;
        }
    }

    public static QuestionnaireManager getInstance() {
        return QuestionnaireManager.Singleton.INSTANCE.getProcessor();
    }

    public IQuestionnaireScript deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.QuestionnaireScript);
        if (script instanceof  IQuestionnaireScript) {
            return (IQuestionnaireScript) script;
        }
        return null;
    }

    public void initData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        try {
            starTime = sdf.parse(Global.Question_Start_Time.get(0)).getTime();
            endTime = sdf.parse(Global.Question_Start_Time.get(1)).getTime();
        } catch (Exception e) {
            logger.error("有奖问答时间解析错误");
            return;
        }
        logger.info("开始时间：" + starTime);
        logger.info("结束时间：" + endTime);
        questionnaireData.clear();
    }
}
