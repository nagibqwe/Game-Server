package com.gm.project.gamelog.chatlog.service.impl;

import java.util.List;
import java.util.Map;
import com.gm.project.common.utils.GameLogUtil;
import org.springframework.stereotype.Service;
import com.gm.project.gamelog.chatlog.domain.Chatlog;
import com.gm.project.gamelog.chatlog.service.IChatlogService;

/**
 * 聊天日志Service业务层处理
 * 
 * @author gm
 * @date 2021-06-08
 */
@Service
public class ChatlogServiceImpl implements IChatlogService 
{
    /**
     * 查询聊天日志列表
     * 
     * @param chatlog 聊天日志
     * @return 聊天日志
     */
    @Override
    public List<Chatlog> selectChatlogList(Chatlog chatlog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        if(chatlog.getRoleId() != null){
            wheresql.append(" and roleId = " + chatlog.getRoleId());
        }
        if(chatlog.getLevel() != null){
            wheresql.append(" and level = " + chatlog.getLevel());
        }
        if(chatlog.getChannel() != null){
            wheresql.append(" and channel = " + chatlog.getChannel());
        }
        if(chatlog.getReceRoleId() != null){
            wheresql.append(" and receRoleId = " + chatlog.getReceRoleId());
        }
        //自定义查询条件
        param.put("tableName","chatlog");
        param.put("where",wheresql);
        if(param.containsKey("tableNameList")){
            return GameLogUtil.getChatLogDataList(Chatlog.class,param);
        }else {
            return GameLogUtil.getLogDataList(Chatlog.class,param);
        }


    }




}
