package com.backend.module.statistic;

import com.backend.struct.log.entity.questionnaire.QuestionnaireLog;
import com.backend.struct.log.LogRecord;
import com.backend.utils.LogUtil;
import com.backend.manager.OtherDataManager;
import com.backend.struct.Questionnaire;
import com.backend.utils.Toolkit;
import net.sf.json.JSONObject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IocBean
@Ok("json")
@At("/questionnaire")
@Fail("http:500")
public class QuestionnaireStatisticModule {

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @At
    @Ok("jsp:jsp.statistic.questionnaire")
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", sdf.format(new Date()));
    }

    @At
    public Object searchData(@Param("..") JSONObject json) {
        Map<String, Object> params = LogUtil.parseCondition(json);
        LogRecord record = LogUtil.getLogEntityData(QuestionnaireLog.class, params);
        if (record.getDatas().isEmpty()) {
            Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
            return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
        }
        return Toolkit.outResult(true, record);
    }

    @At
    public Object statistics(@Param("..") JSONObject json) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        Map<String, Object> params = LogUtil.parseCondition(json);
        params.remove("roleId");
        params.remove("userId");
        LogRecord record = LogUtil.getLogEntityData(QuestionnaireLog.class, params);

        if (record.getDatas().isEmpty()) {
            return Toolkit.outResult(false, msg.get("jsp.rolelog.nodata"));
        }
        Map<Integer, Map<String, Integer>> map = new HashMap<>();
        for (Questionnaire questionnaire : OtherDataManager.getInstance().getQuestionnaireMap().values()) {
            Map<String, Integer> answer = new HashMap<>();
            for (int i = 0; i < questionnaire.getAnswers().size(); i++) {
                answer.put(questionnaire.getAnswers().get(i), 0);
            }
            map.put(questionnaire.getId(), answer);
        }
        for (Map<String, String> data : record.getDatas()) {

            JSONObject object = JSONObject.fromObject(data.get("data"));
            for (Integer id : map.keySet()) {
                String[] answerArray = object.getString(String.valueOf(id)).split("_");
                for (String answer : answerArray) {
                    Integer count = map.get(id).getOrDefault(answer, 0);
                    map.get(id).put(answer, count + 1);
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("question", OtherDataManager.getInstance().getQuestionnaireMap());
        result.put("data", map);
        result.put("joinNum", record.getDatas().size());
        return Toolkit.outResult(true, result);
    }
}
