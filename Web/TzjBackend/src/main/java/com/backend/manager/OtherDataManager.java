package com.backend.manager;

import com.backend.struct.Questionnaire;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class OtherDataManager {

    private final static Logger log = Logger.getLogger(OtherDataManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        OtherDataManager manager;

        Singleton() {
            this.manager = new OtherDataManager();
        }

        OtherDataManager getProcessor() {
            return manager;
        }
    }

    public static OtherDataManager getInstance() {
        return OtherDataManager.Singleton.INSTANCE.getProcessor();
    }

    private HashMap<Integer, Questionnaire> questionnaireMap = new HashMap<>();

    public HashMap<Integer, Questionnaire> getQuestionnaireMap() {
        return questionnaireMap;
    }

    public void setQuestionnaireMap(HashMap<Integer, Questionnaire> questionnaireMap) {
        this.questionnaireMap = questionnaireMap;
    }
}
