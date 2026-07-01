package com.gm.project.gamelog.feedbacklog.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.feedbacklog.domain.Feedbacklog;
import com.gm.project.gamelog.feedbacklog.service.IFeedbacklogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 反馈日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-10
 */
@Service
public class FeedbacklogServiceImpl implements IFeedbacklogService 
{


    /**
     * 查询反馈日志列表
     * 
     * @param feedbacklog 反馈日志
     * @return 反馈日志
     */
    @Override
    public List<Feedbacklog> selectFeedbacklogList(Feedbacklog feedbacklog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        param.put("tableName","feedbacklog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Feedbacklog.class,param);
    }
}
