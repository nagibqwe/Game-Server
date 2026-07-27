package com.gm.project.gamelog.feedbacklog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.feedbacklog.domain.Feedbacklog;

/**
 * 反馈日志Service接口
 * 
 * @author gm
 * @date 2021-09-10
 */
public interface IFeedbacklogService 
{

    /**
     * 查询反馈日志列表
     * 
     * @param feedbacklog 反馈日志
     * @return 反馈日志集合
     */
    public List<Feedbacklog> selectFeedbacklogList(Feedbacklog feedbacklog,Map<String, Object> param);

}
