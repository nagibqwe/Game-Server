package com.gm.project.gamelog.maillog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.maillog.domain.Maillog;

/**
 * 邮件日志Service接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface IMaillogService 
{

    /**
     * 查询邮件日志列表
     * 
     * @param maillog 邮件日志
     * @return 邮件日志集合
     */
    public List<Maillog> selectMaillogList(Maillog maillog,Map<String, Object> param);

}
