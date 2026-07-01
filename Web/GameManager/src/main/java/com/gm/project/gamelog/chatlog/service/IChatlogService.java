package com.gm.project.gamelog.chatlog.service;

import java.util.List;
import java.util.Map;

import com.gm.project.gamelog.chatlog.domain.Chatlog;

/**
 * 聊天日志Service接口
 * 
 * @author gm
 * @date 2021-06-08
 */
public interface IChatlogService 
{

    /**
     * 查询聊天日志列表
     * 
     * @param chatlog 聊天日志
     * @return 聊天日志集合
     */
    public List<Chatlog> selectChatlogList(Chatlog chatlog,Map<String, Object> param);


}
